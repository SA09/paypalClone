package com.paypal.user_service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.paypal.user_service.entity.User;
import com.paypal.user_service.repository.UserRepository;
import com.paypal.user_service.service.UserProducerService;
import com.paypal.user_service.service.UserService;
import com.paypal.user_service.util.JwtUtil;

@Service
public class UserImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserProducerService userProducerService;
	
	@Autowired
    private JwtUtil jwtUtil;
	

	@Override
	public User createUser(User user) {
		Optional<User> exisitingUser = userRepository.findByUserName(user.getUserName());
		if(exisitingUser.isPresent()) {
			 new RuntimeException("User already exist");
		}
		User newUser = new User();
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setUserName(user.getUserName());
		newUser.setEmail(user.getEmail());
		userRepository.save(newUser);
		 Map<String, Object> payload = Map.of(
	        	    "userId", newUser.getId(),
	        	    "email", newUser.getEmail(),
	        	    "userName", newUser.getUserName()

	        	);
		userProducerService.sendMessage(payload);
		return newUser;
	}

	@Override
	public Optional<User> getUserById(Long id) {
		Optional<User> user = userRepository.findById(id);
		return user;
	}

	@Override
	public List<User> getAllUsers() {
		List<User> allUsers = userRepository.findAll();
		return allUsers;
	}

	@Override
	public String genrateJwtToken(String username) {
		User User = userRepository.findByUserName(username).orElseThrow(()-> new RuntimeException("This user does't exist"));
		return jwtUtil.generateToken(username, User.getId().toString());
	}



}
