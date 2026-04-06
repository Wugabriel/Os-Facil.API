package com.oracle.OSfacil.infra.seguranca;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ConfiguracaoSeguranca {

    private final FiltroTokenAcesso filtroTokenAcesso;

    public ConfiguracaoSeguranca(FiltroTokenAcesso filtroTokenAcesso) {
        this.filtroTokenAcesso = filtroTokenAcesso;
    }

    @Bean
    public SecurityFilterChain filtrosSeguranca(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/login", "/atualizar-token", "/register").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/webjars/**").permitAll()
                        .requestMatchers("/clientes/**").hasAnyRole("FUNCIONARIO", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/veiculos/**").hasAnyRole("CLIENTE", "FUNCIONARIO", "ADMIN")
                        .requestMatchers("/veiculos/**").hasAnyRole("FUNCIONARIO", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/ordem-servicos/**").hasAnyRole("CLIENTE", "FUNCIONARIO", "ADMIN")
                        .requestMatchers("/ordem-servicos/**").hasAnyRole("FUNCIONARIO", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/pagamentos/**").hasAnyRole("CLIENTE", "FUNCIONARIO", "ADMIN")
                        .requestMatchers("/pagamentos/**").hasAnyRole("FUNCIONARIO", "ADMIN")
                        .requestMatchers("/produtos/**").hasAnyRole("FUNCIONARIO", "ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(filtroTokenAcesso, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder encriptador() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}