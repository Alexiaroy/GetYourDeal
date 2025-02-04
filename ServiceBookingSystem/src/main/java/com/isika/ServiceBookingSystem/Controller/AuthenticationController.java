package com.isika.ServiceBookingSystem.Controller;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.isika.ServiceBookingSystem.Dto.AuthenticationRequest;
import com.isika.ServiceBookingSystem.Dto.SignupRequestDTO;
import com.isika.ServiceBookingSystem.Dto.UserDTO;
import com.isika.ServiceBookingSystem.Entity.User;
import com.isika.ServiceBookingSystem.Repository.UserRepository;
import com.isika.ServiceBookingSystem.Service.Authentication.AuthService;
import com.isika.ServiceBookingSystem.Service.Jwt.UserDetailsServiceImpl;
import com.isika.ServiceBookingSystem.Util.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthenticationController {

	@Autowired
	private AuthService authService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	public static final String TOKEN_PREFIX = "Bearer ";
	
	public static final String HEADER_STRING = "Authorization";
	
	
	@PostMapping("/client/sign-up")
	public ResponseEntity<?> signupClient(@RequestBody SignupRequestDTO signupRequestDTO){
		if(authService.presentByEmail(signupRequestDTO.getEmail())) {
			return new ResponseEntity<>("Client already exists with this email !", HttpStatus.NOT_ACCEPTABLE);
		}
		
		UserDTO createdUser = authService.signupClient(signupRequestDTO);
		return new ResponseEntity<>(createdUser, HttpStatus.OK);
	}
	
	@PostMapping("/company/sign-up")
	public ResponseEntity<?> signupCompany(@RequestBody SignupRequestDTO signupRequestDTO){
		if(authService.presentByEmail(signupRequestDTO.getEmail())) {
			return new ResponseEntity<>("Company already exists with this email !", HttpStatus.NOT_ACCEPTABLE);
		}
		
		UserDTO createdUser = authService.signupCompany(signupRequestDTO);
		return new ResponseEntity<>(createdUser, HttpStatus.OK);
	}
	
	@PostMapping("/authenticate")
	public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException, JSONException{
		try {
			// On vérifie que le nom d'utilisateur et le mot de passe fournis correspondent
			// à un utilisateur
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			// Si les informations d'identification sont incorrectes
			throw new BadCredentialsException("Incorrect username or password");
		}
		
		final UserDetails userDetails = 
userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		// Génération du token JWT signé, contenant des informations
		final String jwt = jwtUtil.generateToken(userDetails.getUsername());

		// Récupère l'utilisateur correspondant à l'adresse e-mail depuis la base de
		// données
		User user = userRepository.findFirstByEmail(authenticationRequest.getUsername());

		// Un objet JSON est créé contenant l'ID et le rôle de l'utilisateur
		response.getWriter().write(new JSONObject().put("userId", user.getId()).put("role", user.getRole()).toString());

		// Ajout des en-têtes de réponse
		response.addHeader("Access-Control-Expose-Headers", "Authorization");
		response.addHeader("Access-Control-Allow-Headers",
				"Authorization," + " X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header");
		response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
	}
}
