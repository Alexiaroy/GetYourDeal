package com.isika.ServiceBookingSystem.Configs;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter {

	@Value("${app.client.url}")
	private String clientAppUrl;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		// On transforme les Servlet en Http
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;

		// Origine autorisée pour accéder à l'API
		response.setHeader("Access-Control-Allow-Origin", clientAppUrl);
		// Méthodes HTTP autorisées pour les requêtes cross-origin
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
		// Durée (en secondes) pendant laquelle le navigateur peut mettre en cache la
		// réponse des pré-vérifications CORS
		response.setHeader("Access-Control-Max-Age", "3600");
		// En-têtes personnalisés que le client peut inclure dans les requêtes (par
		// exemple, Authorization pour les tokens).
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

		// Si la méthode HTTP est OPTIONS (requête de pré-vérification), elle répond
		// avec le statut 200 OK sans transmettre la requête au reste de l'application.
		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
			// Sinon, elle passe la requête au prochain filtre ou composant via
			// chain.doFilter.
		} else {
			chain.doFilter(req, res);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) {
		// Pas besoin de configuration spécifique
	}

	@Override
	public void destroy() {
		// Pas d'action spécifique à la destruction
	}
}