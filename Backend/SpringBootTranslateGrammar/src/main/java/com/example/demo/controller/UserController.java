package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	@PostMapping
	public User createUser(@RequestBody User user) {
		return this.userService.saveUser(user);
	}
	
	@GetMapping("/{username}")
	public User getUser(@PathVariable String username) {
		return this.userService.getUserByUsername(username);
	}
	
	@GetMapping("/id/{id}")
	public User getUserById(@PathVariable Long id) {
		return this.userService.getUserById(id).orElse(null);
	}
}
