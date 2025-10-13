package com.paypal.api_gateway.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.paypal.api_gateway.util.JwtUtil;

import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret}")
    private String secret;  // field injection

    private Key key;
    

    // Spring calls this after bean creation
    @PostConstruct
    public void init() {
        System.out.println("AuthenticationFilter initialized with secret: " + secret);
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    	  System.out.println("filter invoked for request: " + exchange.getRequest().getURI());
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("Incoming request: " + path);

        if (path.startsWith("/users") || path.startsWith("/public")) {
        	System.out.println("Aiiims " + path);
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        System.out.println(" Authorization Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Missing or invalid Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("Token valid.rerer Subject: " + claims.getSubject());


     
            String userId = (String) claims.get("userId");
            System.out.println(userId);
            exchange = exchange.mutate()
                    .request(r -> r.headers(h -> {
                        h.add("X-User", claims.getSubject());
                        h.add("X-UserId", userId);             
                    }))
                    .build();

            return chain.filter(exchange);
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1; // Run before other filters
    }
}