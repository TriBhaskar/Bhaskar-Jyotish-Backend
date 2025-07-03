package com.anterka.bjyotish.security;

import com.anterka.bjyotish.controller.constants.ApiPaths;
import com.anterka.bjyotish.security.jwt.AuthEntryPointJwt;
import com.anterka.bjyotish.security.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthTokenFilter authTokenFilter;
    private final AuthenticationManager authenticationManager;

    private final String[] skipAuthorizationForRequests = {
            ApiPaths.API_PREFIX+ApiPaths.LOGIN,
            ApiPaths.API_PREFIX+ApiPaths.REGISTER,
            ApiPaths.API_PREFIX+ApiPaths.VERIFY_OTP,
            ApiPaths.API_PREFIX+ApiPaths.FORGOT_PASSWORD,
            ApiPaths.API_PREFIX+ApiPaths.VALIDATE_TOKEN,
            ApiPaths.API_PREFIX+ApiPaths.RESET_PASSWORD,
            ApiPaths.API_PREFIX+ApiPaths.RESEND_OTP,
            "/api/v1/testredis"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(skipAuthorizationForRequests).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new AuthEntryPointJwt())
                )
                .authenticationManager(authenticationManager)
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}