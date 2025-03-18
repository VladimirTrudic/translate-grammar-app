package com.example.demo.security;

import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static String SECRET_KEY = "37826786328762842kjwh2947+ewkfgwhkefvkwehvf328";
//	private static final byte[] SECRET_KEY_BYTES = SECRET_KEY.getBytes();
//	private static final SecretKey SECRET_KEY_GENERATED = Keys.hmacShaKeyFor(SECRET_KEY_BYTES);
//	
	public static String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) //ten hours
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//				.signWith(SignatureAlgorithm.HS256, SECRET_KEY_GENERATED)
				.compact();
	}
	
	public static String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public static Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(SECRET_KEY)
				.build()
				.parseClaimsJws(token)
				.getBody();
		
		
//		String[] parts = token.split("\\.");
//		
//		String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
//		
//		return Jwts.parserBuilder()
//				.setSigningKey(SECRET_KEY)
//				.build()
//				.parseClaimsJws(payload)
//				.getBody();
	}

	public static boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public static Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public static boolean validateToken(String token, String username) {
		return (username.equals(extractUsername(token)) && !isTokenExpired(token));
	}
}
