package com.paypal.user_service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.user_service.entity.User;
import com.paypal.user_service.repository.UserRepository;
import com.paypal.user_service.service.UserService;

@Service
public class UserImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	

	@Override
	public User createUser(User user) {
		User savedUser = userRepository.save(user);
		return savedUser;
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

}
