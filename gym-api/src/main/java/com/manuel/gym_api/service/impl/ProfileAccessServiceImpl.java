package com.manuel.gym_api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manuel.gym_api.exception.ResourceNotFoundException;
import com.manuel.gym_api.model.User;
import com.manuel.gym_api.repository.UserRepository;
import com.manuel.gym_api.service.ProfileAccessService;

@Service
public class ProfileAccessServiceImpl implements ProfileAccessService {

	private final UserRepository userRepository;

	public ProfileAccessServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean canViewRoutines(Long viewerUserId, Long ownerUserId) {
		if (viewerUserId != null && viewerUserId.equals(ownerUserId)) {
			return true;
		}

		User owner = userRepository.findById(ownerUserId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + ownerUserId));

		return owner.isPublicProfile();
	}
}