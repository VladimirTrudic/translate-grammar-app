package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.security.JwtAuthentificationFilter;
import com.example.demo.security.JwtUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig  implements WebMvcConfigurer{

	private final JwtUtil jwtUtil;
	
	public SecurityConfig(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.csrf().disable()
			.authorizeRequests(
					authorizeRequests ->
					authorizeRequests.requestMatchers("api/auth/register", "/api/auth/login")
					.permitAll()
					.anyRequest().authenticated()
					//).addFilter(new JwtAuthentificationFilter(jwtUtil));
					).addFilterAfter(new JwtAuthentificationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
		
		http.cors().configurationSource(request -> new org.springframework.web.cors.CorsConfiguration().applyPermitDefaultValues());
		
		return http.build();
	}
	
	
	private void addCorsMapping(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://localhost:4200")
				.allowedMethods("GET", "POST", "PUT", "DELETE")
				.allowedHeaders("*")
				.allowCredentials(true);

	}
}
