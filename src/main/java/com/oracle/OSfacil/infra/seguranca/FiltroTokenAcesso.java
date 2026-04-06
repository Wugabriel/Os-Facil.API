package com.oracle.OSfacil.infra.seguranca;

import com.oracle.OSfacil.model.Cliente;
import com.oracle.OSfacil.repository.ClienteRepository;
import com.oracle.OSfacil.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
public class FiltroTokenAcesso extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final ClienteRepository clienteRepository;

    public FiltroTokenAcesso(TokenService tokenService, @Lazy ClienteRepository clienteRepository) {
        this.tokenService = tokenService;
        this.clienteRepository = clienteRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recuperarTokenRequisicao(request);

        if (token != null) {
            String email = tokenService.verificarToken(token);

            var cliente = clienteRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            var authentication = new UsernamePasswordAuthenticationToken(
                    cliente, null, cliente.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarTokenRequisicao(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        return (authHeader != null) ? authHeader.replace("Bearer ", "") : null;
    }
}