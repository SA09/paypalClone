package com.paypal.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.paypal.user_service.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
		 .csrf(csrf -> csrf.disable())
		 .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/users/**").permitAll() 
	                .anyRequest().authenticated()
	            );
		return http.build();
		 
	}

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Provides AuthenticationManager for login

    @Bean
     AuthenticationManager authenticationManager(CustomUserDetailsService userDetailsService) {
        return authentication -> {
            UserDetails user = userDetailsService.loadUserByUsername(authentication.getName());
            if (passwordEncoder().matches(authentication.getCredentials().toString(), user.getPassword())) {
                return new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword(),
                        user.getAuthorities()
                );
            }
            throw new BadCredentialsException("Invalid username or password");
        };
    }


}
