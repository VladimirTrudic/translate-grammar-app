package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtResponse;
import com.example.demo.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
//	@Autowired
//	private BCryptPasswordEncoder passwordEncoder;
	
	private final PasswordEncoder passwordEncoder2;
	
	@Autowired
	public AuthController(PasswordEncoder passwordEncoder) {
		this.passwordEncoder2 = passwordEncoder;
	}
	
//	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/register")
	public String registerUser(@RequestBody User user) {
		user.setPassword(passwordEncoder2.encode(user.getPassword()));
		userRepository.save(user);
		return "Registration successful!";
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody User user) {
		User foundUser = userRepository.findByEmail(user.getEmail());
		if(foundUser == null) {
			throw new RuntimeException("User not found");
		}
		if(passwordEncoder2.matches(user.getPassword(), foundUser.getPassword())) {
			String token = JwtUtil.generateToken(foundUser.getUsername());
			System.out.println(token);
			return ResponseEntity.ok(new JwtResponse(token));
		} else {
			throw new RuntimeException("Invalid credentials");
		}
	}

}
