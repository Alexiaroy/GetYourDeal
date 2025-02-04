package com.isika.ServiceBookingSystem.Configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.isika.ServiceBookingSystem.Service.Jwt.JwtRequestFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

	@Autowired
	private JwtRequestFilter requestFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// Désactive la protection CSRF (Cross-Site Request Forgery) pour cette application car l'API est stateless	    
		return http
	        .csrf(csrf -> csrf.disable()) 

	        .authorizeHttpRequests(authz -> authz
	        	// Spécifie que certaines URL (authentification, inscription et autres) sont accessibles à tout le monde, 
	        	// sans besoin d'authentification.
	            .requestMatchers("/authenticate", "/company/sign-up", "/client/sign-up", "/ads",
	            		"/search/{service}").permitAll()
	            // Spécifie que toutes les URL qui correspondent à `/api/**` nécessitent une authentification pour être 
	        	// accessibles.
	            .requestMatchers("/api/**").authenticated()
	        )
	        // Configure la gestion des sessions pour qu'elle soit "STATELESS", ce qui signifie que l'application ne 
	        // conserve pas l'état de session côté serveur.
	        // Courant dans les API RESTful où chaque requête est autonome (authentifiée via des tokens comme JWT).
	        .sessionManagement(session -> session
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        )
	        // Ajoute un filtre personnalisé (`requestFilter`) avant le filtre par défaut 
	        // `UsernamePasswordAuthenticationFilter`.
	        // Permet de personnaliser le traitement des requêtes entrantes, par exemple pour vérifier un token JWT.
	        .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class)
	        // Construit et retourne l'objet `SecurityFilterChain` configuré.
	        .build();
	}
	
    @Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
    
    @Bean 
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
