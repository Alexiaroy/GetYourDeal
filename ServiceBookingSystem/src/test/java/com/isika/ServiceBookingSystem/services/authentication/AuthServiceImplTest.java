package com.isika.ServiceBookingSystem.services.authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.isika.ServiceBookingSystem.Dto.SignupRequestDTO;
import com.isika.ServiceBookingSystem.Dto.UserDTO;
import com.isika.ServiceBookingSystem.Entity.User;
import com.isika.ServiceBookingSystem.Enum.UserRole;
import com.isika.ServiceBookingSystem.Repository.UserRepository;
import com.isika.ServiceBookingSystem.Service.Authentication.AuthServiceImpl;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignupClient_Success() {
        // Arrange
        SignupRequestDTO signupRequestDTO = new SignupRequestDTO();
        signupRequestDTO.setName("John");
        signupRequestDTO.setLastname("Doe");
        signupRequestDTO.setEmail("john.doe@example.com");
        signupRequestDTO.setPhone("1234567890");
        signupRequestDTO.setPassword("password123");

        UserDTO userDto = new UserDTO();
        userDto.setName("John");
        userDto.setLastname("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPhone("1234567890");
        userDto.setRole(UserRole.CLIENT);
        
        User savedUser = mock(User.class);
        
        when(savedUser.getDto()).thenReturn(userDto);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserDTO result = authService.signupClient(signupRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals(UserRole.CLIENT, result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSignupCompany_Success() {
        // Arrange
        SignupRequestDTO signupRequestDTO = new SignupRequestDTO();
        signupRequestDTO.setName("Acme Corp");
        signupRequestDTO.setEmail("contact@acme.com");
        signupRequestDTO.setPhone("0987654321");
        signupRequestDTO.setPassword("securepassword");

        UserDTO userDto = new UserDTO();
        userDto.setName("Acme Corp");
        userDto.setEmail("contact@acme.com");
        userDto.setPhone("0987654321");
        userDto.setRole(UserRole.COMPANY);
        
        User savedUser = mock(User.class);
        
        when(savedUser.getDto()).thenReturn(userDto);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserDTO result = authService.signupCompany(signupRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Acme Corp", result.getName());
        assertEquals("contact@acme.com", result.getEmail());
        assertEquals(UserRole.COMPANY, result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testPresentByEmail_EmailExists() {
        // Arrange
        String email = "existing@example.com";
        when(userRepository.findFirstByEmail(email)).thenReturn(new User());

        // Act
        Boolean result = authService.presentByEmail(email);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).findFirstByEmail(email);
    }

    @Test
    void testPresentByEmail_EmailDoesNotExist() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findFirstByEmail(email)).thenReturn(null);

        // Act
        Boolean result = authService.presentByEmail(email);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findFirstByEmail(email);
    }
}
