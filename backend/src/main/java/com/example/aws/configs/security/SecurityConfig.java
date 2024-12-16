package com.example.aws.configs.security;

import com.example.aws.configs.security.filters.BasePathFilter;
import com.example.aws.configs.security.filters.FirebaseTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final FirebaseTokenFilter firebaseTokenFilter;
    private final BasePathFilter basePathFilter;

    public SecurityConfig(FirebaseTokenFilter firebaseTokenFilter, BasePathFilter basePathFilter) {
        this.firebaseTokenFilter = firebaseTokenFilter;
        this.basePathFilter = basePathFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**", "/backoffice/**") // Disable CSRF for /api and /backoffice endpoints
                )
                .cors(withDefaults())
                .addFilterBefore(basePathFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(firebaseTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

//                .authorizeHttpRequests(requests -> requests
//                        .requestMatchers("/backoffice/**").permitAll() // Allow all requests to /backoffice/** without authentication
//                        .requestMatchers("/api/**").authenticated()   // Require authentication for /api/** endpoints
//                )
