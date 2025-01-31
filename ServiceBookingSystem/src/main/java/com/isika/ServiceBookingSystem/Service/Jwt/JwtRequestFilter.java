package com.isika.ServiceBookingSystem.Service.Jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.isika.ServiceBookingSystem.Util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Récupère l'en-tête HTTP "Authorization" de la requête.
				String authHeader = request.getHeader("Authorization");

				// Initialise deux variables pour stocker le token JWT et le nom d'utilisateur
				// extrait.
				String token = null;
				String username = null;

				// Vérifie si l'en-tête "Authorization" existe et commence par "Bearer ".
				if (authHeader != null && authHeader.startsWith("Bearer ")) {
					// Extrait le token JWT en supprimant "Bearer " au début de l'en-tête.
					token = authHeader.substring(7);
					// Extrait le nom d'utilisateur (ou subject) du token JWT à l'aide de
					// l'utilitaire "jwtUtil".
					username = jwtUtil.extractUsername(token);
				}

				// Vérifie si un nom d'utilisateur a été extrait et si aucun utilisateur n'est
				// actuellement authentifié.
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					// Charge les détails de l'utilisateur (username, password, authorities) depuis
					// la base de données.
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);

					// Valide le token JWT en vérifiant qu'il correspond à l'utilisateur et qu'il
					// n'a pas expiré.
					if (jwtUtil.validateToken(token, userDetails)) {
						// Crée un objet "UsernamePasswordAuthenticationToken" pour représenter
						// l'utilisateur authentifié.
						UsernamePasswordAuthenticationToken authToken = new 
		UsernamePasswordAuthenticationToken(userDetails,
								null, userDetails.getAuthorities());
						// Associe des détails supplémentaires à l'authentification (comme l'adresse IP
						// ou l'agent utilisateur).
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

						// Définit l'utilisateur comme authentifié dans le contexte de sécurité Spring.
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				}

				// Passe la requête au prochain filtre de la chaîne.
				filterChain.doFilter(request, response);
	}

}
