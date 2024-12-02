 package com.example.LetsPlay;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;


@Configuration
@EnableWebSecurity
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SecurityConfiguration {

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .cors(cors -> cors.configurationSource(request -> {
            var corsConfig = new org.springframework.web.cors.CorsConfiguration();
            corsConfig.setAllowedOrigins(List.of("http://localhost:3000"));
            corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type"));
            corsConfig.setAllowCredentials(true);
            return corsConfig;
        }))
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(registry -> {
            registry.requestMatchers("/login", "/authenticate", "/registerUser", "/security", "/insertProduct", "/deleteProduct", "/updateOwner").permitAll();
            registry.anyRequest().authenticated();
        })
        .build();
}
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
