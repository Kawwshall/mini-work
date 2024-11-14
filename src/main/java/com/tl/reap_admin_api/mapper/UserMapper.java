package com.tl.reap_admin_api.mapper;

import com.tl.reap_admin_api.dto.UserDto;
import com.tl.reap_admin_api.model.Role;
import com.tl.reap_admin_api.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setUuid(user.getUuid());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoleId(user.getRole().getNumber());
        return dto;
    }

    public User toEntity(UserDto dto) {
        User user = new User();
        user.setUuid(dto.getUuid());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(Role.fromNumber(dto.getRoleId()));
        return user;
    }
}