package com.isika.ServiceBookingSystem.Util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

	// Clé secrète pour signer les tokens JWT, injectée depuis le fichier de
	// configuration (application.properties)
	@Value("${jwt.secret}")
	private String secret;

	// Variable statique pour stocker la clé secrète afin qu'elle soit accessible
	// globalement
	public static String SECRET;

	// Méthode appelée après la construction du bean pour initialiser la variable
	// statique `SECRET`
	@PostConstruct
	public void init() {
		SECRET = secret;
	}

	// Crée un token JWT avec des données spécifiques (claims) et un nom
	// d'utilisateur : expiration de 30 minutes
	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder().setClaims(claims).setSubject(userName).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
				.signWith(SignatureAlgorithm.HS256, getSignKey()).compact();
	}

	// Génère la clé pour signer les tokens en utilisant la clé secrète configurée
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
	}

	// Génère un token JWT pour un utilisateur donné sans claims supplémentaires
	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName);
	}

	// Extrait tous les claims contenus dans un token JWT donné
	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
	}

	// Extrait une donnée spécifique (claim) d'un token JWT en utilisant une
	// fonction de résolution
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// Extrait la date d'expiration d'un token JWT
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// Extrait le nom d'utilisateur (subject) d'un token JWT
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// Vérifie si un token JWT est expiré
	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// Valide un token JWT en vérifiant qu'il correspond à l'utilisateur et qu'il
	// n'est pas expiré
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}