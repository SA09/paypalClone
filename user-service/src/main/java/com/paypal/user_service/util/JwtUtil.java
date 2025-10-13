package com.paypal.user_service.util;



import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // Use a long enough secret key (at least 32 bytes for HS256, 48 bytes for HS384)
	 private static final String SECRET = "ReplaceWithASecureKeyAtLeast32BytesLong_ForHS256_or_48+ForHS384";
    
	 private Key getKey() {
	        return Keys.hmacShaKeyFor(SECRET.getBytes());
	    }

	    public String generateToken(String username, String userId){
	        return Jwts.builder()
	                .setSubject(username)
	                .claim("userId", userId)
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
	                .signWith(getKey(), SignatureAlgorithm.HS256)
	                .compact();
	    }

	    public boolean validateToken(String token) {
	        try {
	            Jwts.parserBuilder()
	                    .setSigningKey(getKey())
	                    .build()
	                    .parseClaimsJws(token);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    public String extractUsername(String token) {
	        return Jwts.parserBuilder()
	                .setSigningKey(getKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody()
	                .getSubject();
	    }
}