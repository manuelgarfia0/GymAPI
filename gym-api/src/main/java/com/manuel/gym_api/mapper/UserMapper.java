package com.manuel.gym_api.mapper;

import org.springframework.stereotype.Component;

import com.manuel.gym_api.dto.UserDTO;
import com.manuel.gym_api.model.User;

@Component
public class UserMapper {

	public UserDTO toDTO(User user) {
		UserDTO result = null;

		if (user != null) {
			result = new UserDTO();
			result.setId(user.getId());
			result.setUsername(user.getUsername());
			result.setEmail(user.getEmail());
			result.setPremium(user.isPremium());
			result.setLanguagePreference(user.getLanguagePreference());
			result.setCreatedAt(user.getCreatedAt());
		}

		return result;
	}
}