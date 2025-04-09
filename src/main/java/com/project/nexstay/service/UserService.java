package com.project.nexstay.service;

import com.project.nexstay.dto.ProfileUpdateRequestDto;
import com.project.nexstay.dto.UserDto;
import com.project.nexstay.entity.User;

public interface UserService {
    User getUserById(Long id);

    void updateUserProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();
}
