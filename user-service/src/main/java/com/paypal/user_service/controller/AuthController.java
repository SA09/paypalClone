package com.paypal.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.user_service.entity.User;
import com.paypal.user_service.service.UserService;
import com.paypal.user_service.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

		@Autowired    
		private AuthenticationManager authenticationManager;
		
		@Autowired
		private UserService userService;
		
		@Autowired
	    private JwtUtil jwtUtil;



	    @PostMapping("/login")
	    public String login(@RequestParam String username, @RequestParam String password) {
	        authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(username, password)
	        );
	        return jwtUtil.generateToken(username);
	    }
	    
	    
	    @PostMapping("/register")
	    public ResponseEntity<?> register(@RequestBody User user) {
	        try {
	        	User newuser = userService.createUser(user);
	        	return ResponseEntity.ok("Registered Sussesfully");
			} catch (Exception e) {
			    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
			}
	        
	    }
	    
	    
	}

