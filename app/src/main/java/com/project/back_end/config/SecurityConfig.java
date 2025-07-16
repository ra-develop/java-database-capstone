package com.project.back_end.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // .csrf(csrf -> csrf
            //     .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            // )
            .csrf(csrf -> csrf.disable()) // Disable CSRF
            .authorizeHttpRequests(auth -> auth
                // .requestMatchers("/", "/css/**", "/js/**", "/assets/**", "/pages/*").permitAll()
                // .requestMatchers("/admin/**").permitAll() //.hasRole("ADMIN")
                // .requestMatchers("/doctor/**").hasRole("DOCTOR")
                // .requestMatchers("/patient/**").hasRole("PATIENT")
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            );
            
        return http.build();
    }
}