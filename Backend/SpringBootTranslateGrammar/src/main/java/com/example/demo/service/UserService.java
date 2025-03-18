package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User saveUser(User user) {
		return this.userRepository.save(user);
	}
	
	public Optional<User> getUserById(Long id){
		return this.userRepository.findById(id);
	}
	
	public User getUserByUsername(String username) {
		return this.userRepository.findByUsername(username);
	}
}
