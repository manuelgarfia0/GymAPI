package com.manuel.gym_api.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.security.crypto.password.PasswordEncoder;
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

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new DuplicateResourceException("El correo electrónico ya está registrado");
        }
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new DuplicateResourceException("El nombre de usuario ya está en uso");
        }

        User user = new User();
        user.setUsername(registrationDTO.getUsername().trim());
        user.setEmail(registrationDTO.getEmail().trim());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return userMapper.toDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO updateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        if (updateDTO.getUsername() != null && !updateDTO.getUsername().isBlank()) {
            String newUsername = updateDTO.getUsername().trim();
            if (!newUsername.equals(user.getUsername())
                    && userRepository.existsByUsername(newUsername)) {
                throw new DuplicateResourceException("El nombre de usuario ya está en uso");
            }
            user.setUsername(newUsername);
        }

        if (updateDTO.getEmail() != null && !updateDTO.getEmail().isBlank()) {
            String newEmail = updateDTO.getEmail().trim();
            if (!newEmail.equals(user.getEmail())
                    && userRepository.existsByEmail(newEmail)) {
                throw new DuplicateResourceException("El correo electrónico ya está registrado");
            }
            user.setEmail(newEmail);
        }

        if (updateDTO.getLanguagePreference() != null) {
            user.setLanguagePreference(updateDTO.getLanguagePreference());
        }

        if (updateDTO.getFirstName() != null) {
            user.setFirstName(updateDTO.getFirstName().trim());
        }

        if (updateDTO.getLastName() != null) {
            user.setLastName(updateDTO.getLastName().trim());
        }

        if (updateDTO.getDateOfBirth() != null) {
            if (updateDTO.getDateOfBirth().isBlank()) {
                user.setDateOfBirth(null);
            } else {
                try {
                    user.setDateOfBirth(LocalDate.parse(updateDTO.getDateOfBirth().trim()));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Formato de fecha inválido. Formato esperado: YYYY-MM-DD", e);
                }
            }
        }

        user.setPublicProfile(updateDTO.isPublicProfile());

        return userMapper.toDTO(userRepository.save(user));
    }
}
