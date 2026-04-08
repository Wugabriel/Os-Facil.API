package com.oracle.OSfacil.infra.seguranca;

import com.oracle.OSfacil.repository.ClienteRepository;
import com.oracle.OSfacil.repository.FuncionarioRepository;
import com.oracle.OSfacil.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
public class FiltroTokenAcesso extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;

    public FiltroTokenAcesso(TokenService tokenService,
                             @Lazy ClienteRepository clienteRepository,
                             @Lazy FuncionarioRepository funcionarioRepository) {
        this.tokenService = tokenService;
        this.clienteRepository = clienteRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recuperarTokenRequisicao(request);

        if (token != null) {
            try {
                String email = tokenService.verificarToken(token);


                UserDetails usuario = clienteRepository.findByEmailIgnoreCase(email)
                        .map(u -> (UserDetails) u)
                        .or(() -> funcionarioRepository.findByEmailIgnoreCase(email).map(f -> (UserDetails) f))
                        .orElse(null);

                if (usuario != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(
                            usuario, null, usuario.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarTokenRequisicao(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}