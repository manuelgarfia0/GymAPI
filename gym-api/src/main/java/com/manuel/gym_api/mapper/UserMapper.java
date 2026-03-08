package com.manuel.gym_api.mapper;

import com.manuel.gym_api.dto.UserDTO;
import com.manuel.gym_api.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
	public UserDTO toDTO(User user) {
		if (user == null)
			return null;

		UserDTO dto = new UserDTO();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setPremium(user.isPremium());
		dto.setLanguagePreference(user.getLanguagePreference());
		dto.setCreatedAt(user.getCreatedAt());
		return dto;
	}
}