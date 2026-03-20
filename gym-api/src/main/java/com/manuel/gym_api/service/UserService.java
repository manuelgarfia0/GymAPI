package com.manuel.gym_api.service;

import com.manuel.gym_api.dto.UserDTO;
import com.manuel.gym_api.dto.UserRegistrationDTO;
import com.manuel.gym_api.model.User;

public interface UserService {
	User registerUser(UserRegistrationDTO registrationDTO);

	UserDTO getUserById(Long id);

	UserDTO updateUser(Long id, UserDTO updateDTO);
}