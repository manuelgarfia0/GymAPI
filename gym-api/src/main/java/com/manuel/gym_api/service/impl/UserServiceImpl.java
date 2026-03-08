package com.manuel.gym_api.service.impl;

import org.springframework.stereotype.Service;

import com.manuel.gym_api.dto.UserDTO;
import com.manuel.gym_api.dto.UserRegistrationDTO;
import com.manuel.gym_api.exception.DuplicateResourceException;
import com.manuel.gym_api.exception.ResourceNotFoundException;
import com.manuel.gym_api.mapper.UserMapper;
import com.manuel.gym_api.model.User;
import com.manuel.gym_api.repository.UserRepository;
import com.manuel.gym_api.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	@Override
	public UserDTO registerUser(UserRegistrationDTO registrationDTO) {
		if (userRepository.existsByEmail(registrationDTO.getEmail())) {
			throw new DuplicateResourceException("El email ya está en uso");
		}
		if (userRepository.existsByUsername(registrationDTO.getUsername())) {
			throw new DuplicateResourceException("El nombre de usuario ya está en uso");
		}

		User user = new User();
		user.setUsername(registrationDTO.getUsername());
		user.setEmail(registrationDTO.getEmail());
		user.setPassword(registrationDTO.getPassword());

		User savedUser = userRepository.save(user);
		return userMapper.toDTO(savedUser);
	}

	@Override
	public UserDTO getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));
		return userMapper.toDTO(user);
	}
}