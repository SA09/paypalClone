package com.paypal.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.user_service.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

		@Autowired    
		private AuthenticationManager authenticationManager;
		
		@Autowired
	    private JwtUtil jwtUtil;



	    @PostMapping("/login")
	    public String login(@RequestParam String username, @RequestParam String password) {
	        authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(username, password)
	        );
	        return jwtUtil.generateToken(username);
	    }
	}

