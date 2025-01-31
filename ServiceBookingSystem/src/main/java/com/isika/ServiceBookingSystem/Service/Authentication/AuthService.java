package com.isika.ServiceBookingSystem.Service.Authentication;

import com.isika.ServiceBookingSystem.Dto.SignupRequestDTO;
import com.isika.ServiceBookingSystem.Dto.UserDTO;

public interface AuthService {

	UserDTO signupClient(SignupRequestDTO signupRequestDTO);
	
	UserDTO signupCompany(SignupRequestDTO signupRequestDTO);
	
	Boolean presentByEmail(String email);
	
}
