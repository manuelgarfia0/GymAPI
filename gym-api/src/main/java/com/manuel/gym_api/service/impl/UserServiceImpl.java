package com.manuel.gym_api.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder; // NUEVO
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public UserDTO registerUser(UserRegistrationDTO registrationDTO) {
		if (userRepository.existsByEmail(registrationDTO.getEmail())) {
			throw new DuplicateResourceException("Email is already in use");
		}
		if (userRepository.existsByUsername(registrationDTO.getUsername())) {
			throw new DuplicateResourceException("Username is already in use");
		}

		User user = new User();
		user.setUsername(registrationDTO.getUsername());
		user.setEmail(registrationDTO.getEmail());

		user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

		User savedUser = userRepository.save(user);
		return userMapper.toDTO(savedUser);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDTO getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
		return userMapper.toDTO(user);
	}
}