package com.infinitysolutions.applicationservice.infra.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableMethodSecurity
public class ResourceServerConfig {

    @Value("${jwt.public-key}")
    private RSAPublicKey rsaPublicKey;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public ResourceServerConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/produtos", "/api/produtos/*").permitAll()
                        .requestMatchers("/api/usuarios/cpf").permitAll()
                        .requestMatchers("/api/usuarios/cnpj").permitAll()
                        .requestMatchers("/api/usuarios/rg").permitAll()
                        .requestMatchers("/api/emails/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/swagger-ui/index.html").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "auth/login", "auth/cadastrar", "auth/test", "auth/reset-senha/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "auth/email/verificar", "auth/credenciais/*/email").permitAll()
                        .anyRequest().authenticated()

                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder()))
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }
}
