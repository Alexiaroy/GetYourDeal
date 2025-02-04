package com.isika.ServiceBookingSystem.Service.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.isika.ServiceBookingSystem.Dto.SignupRequestDTO;
import com.isika.ServiceBookingSystem.Dto.UserDTO;
import com.isika.ServiceBookingSystem.Entity.User;
import com.isika.ServiceBookingSystem.Enum.UserRole;
import com.isika.ServiceBookingSystem.Repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService{

	@Autowired
	private UserRepository userRepository;
	
	public UserDTO signupClient(SignupRequestDTO signupRequestDTO) {
		User user = new User();
		
		user.setName(signupRequestDTO.getName());
		user.setLastname(signupRequestDTO.getLastname());
		user.setEmail(signupRequestDTO.getEmail());
		user.setPhone(signupRequestDTO.getPhone());
		user.setPassword(new BCryptPasswordEncoder().encode(signupRequestDTO.getPassword()));
		user.setRole(UserRole.CLIENT);
		
		return userRepository.save(user).getDto();
	}
	
	public UserDTO signupCompany(SignupRequestDTO signupRequestDTO) {
		User user = new User();
		
		user.setName(signupRequestDTO.getName());
		user.setAddress(signupRequestDTO.getAddress());
		user.setEmail(signupRequestDTO.getEmail());
		user.setPhone(signupRequestDTO.getPhone());
		user.setPassword(new BCryptPasswordEncoder().encode(signupRequestDTO.getPassword()));
		user.setRole(UserRole.COMPANY);
		
		return userRepository.save(user).getDto();
	}
	
	public Boolean presentByEmail(String email) {
		return userRepository.findFirstByEmail(email) != null;
	}
}

