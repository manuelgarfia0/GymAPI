package com.manuel.gym_api.service;

public interface ProfileAccessService {
	boolean canViewRoutines(Long viewerUserId, Long ownerUserId);
}