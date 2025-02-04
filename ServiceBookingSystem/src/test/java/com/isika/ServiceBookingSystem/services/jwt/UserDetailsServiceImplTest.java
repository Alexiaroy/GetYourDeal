package com.isika.ServiceBookingSystem.services.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.isika.ServiceBookingSystem.Entity.User;
import com.isika.ServiceBookingSystem.Repository.UserRepository;
import com.isika.ServiceBookingSystem.Service.Jwt.UserDetailsServiceImpl;


class UserDetailsServiceImplTest {

	@InjectMocks
	private UserDetailsServiceImpl userDetailsService;

	@Mock
	private UserRepository userRepository;

	@BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
	
	@Test
	void testLoadUserByUsername_UserExists() {
		// Arrange
		String email = "test@example.com";
		User user = new User();
		user.setId(1L);
		user.setEmail(email);
		user.setPassword("password");

		when(userRepository.findFirstByEmail(email)).thenReturn(user);

		// Act
		UserDetails userDetails = userDetailsService.loadUserByUsername(email);

		// Assert
		assertNotNull(userDetails);
		assertEquals(email, userDetails.getUsername());
		assertEquals("password", userDetails.getPassword());
	}

	@Test
	void testLoadUserByUsername_UserNotFound() {
		// Arrange
		String email = "nonexistent@example.com";
		when(userRepository.findFirstByEmail(email)).thenReturn(null);

		// Act & Assert
		assertThrows(UsernameNotFoundException.class, () -> {
			userDetailsService.loadUserByUsername(email);
		});
	}
}
