package com.example.demo.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthentificationFilter extends OncePerRequestFilter {

	private JwtUtil jwtUtil;

	public JwtAuthentificationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authorizationHeader = request.getHeader("Authorization");
		String username = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
			jwt = authorizationHeader.substring(7);
			try {
				username = jwtUtil.extractUsername(jwt);
			} catch (Exception e) {
				response.setStatus(HttpStatus.FORBIDDEN.value());
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if (jwtUtil.validateToken(jwt, username)) {
				Authentication authentification = new UsernamePasswordAuthenticationToken(username, null, null);
				SecurityContextHolder.getContext().setAuthentication(authentification);
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
