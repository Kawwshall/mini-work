package com.tl.reap_admin_api.service;

import com.tl.reap_admin_api.dao.UserDao;
import com.tl.reap_admin_api.dao.UserProfileDao;
import com.tl.reap_admin_api.dto.RsetiDto;
import com.tl.reap_admin_api.dto.StateDto;
import com.tl.reap_admin_api.dto.UserDto;
import com.tl.reap_admin_api.model.State;
import com.tl.reap_admin_api.model.RSETI;
import com.tl.reap_admin_api.model.Role;
import com.tl.reap_admin_api.model.User;
import com.tl.reap_admin_api.model.UserProfile;
import com.tl.reap_admin_api.util.SecurityUtils;
import com.tl.reap_admin_api.exception.UserNotFoundException;
import com.tl.reap_admin_api.mapper.RsetiMapper;
import com.tl.reap_admin_api.mapper.StateMapper;
import com.tl.reap_admin_api.mapper.UserMapper;
import com.tl.reap_admin_api.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserDao userDao;
    private final UserProfileDao userProfileDao;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final StateService stateService;
    private final RsetiService rsetiService;
    private final StateMapper stateMapper;
    private final RsetiMapper rsetiMapper;

    @Autowired
    public UserService(UserDao userDao, UserProfileDao userProfileDao, UserMapper userMapper, PasswordEncoder passwordEncoder, StateService stateService, RsetiService rsetiService, StateMapper stateMapper, RsetiMapper rsetiMapper) {
        this.userDao = userDao;
        this.userProfileDao = userProfileDao;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.stateService = stateService;
        this.rsetiService = rsetiService;
        this.stateMapper = stateMapper;
        this.rsetiMapper = rsetiMapper;
    }

    @Transactional  
    public UserDto createUser(UserDto userDto, String password) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.fromNumber(userDto.getRoleId()));
        user = userDao.save(user);

        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setName(userDto.getName());
        profile.setExtId(userDto.getExtId());
        profile.setDesignation(userDto.getDesignation());
        profile.setContactNumber(userDto.getContactNumber());
        profile.setEmail(userDto.getEmail());
        profile.setPermanentAddr(userDto.getPermanentAddr());
        profile.setCurrentAddr(userDto.getCurrentAddr()); 

        profile.setState(getState(userDto));
        profile.setRseti(getRseti(userDto));
        
        profile.setPhotoUrl(userDto.getPhotoUrl());
        profile.setCreatedBy(getCurrentUser().getUsername());
        profile.setUpdatedBy(getCurrentUser().getUsername());
        userProfileDao.save(profile);

        return mapUserToDto(user, profile);
    }

    private State  getState(UserDto userDto) {
        if(userDto.getStateId() != null) {
            StateDto stateDto = stateService.getStateByExtIdAndLanguageCode(userDto.getStateId(),"en");
            return stateMapper.toEntity(stateDto);
        }

        return null;
    }

    private RSETI  getRseti(UserDto userDto) {
        if(userDto.getRsetiId()!= null) {
            RsetiDto rsetiDto = rsetiService.getRsetiByUuid(userDto.getRsetiId());
            return rsetiMapper.toEntity(rsetiDto);
        }

        return null;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN')")
    public UserDto getUserByUuid(UUID uuid) {
        User user = userDao.findByUuid(uuid)
        .orElseThrow(() -> new UserNotFoundException("User not found with UUID: " + uuid));
        UserProfile profile = userProfileDao.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("User profile not found for user with UUID: " + uuid));
        return mapUserToDto(user, profile);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN')")
    public List<UserDto> getAllUsers() {
        List<User> users = userDao.findAll();
        return users.stream()
                .map(user -> {
                    UserProfile profile = userProfileDao.findByUser(user)
                            .orElseThrow(() -> new UserNotFoundException("User profile not found for user with UUID: " + user.getUuid()));
                    return mapUserToDto(user, profile);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN')")
    public UserDto updateUser(UUID uuid, UserDto userDto) {
        User user = userDao.findByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with UUID: " + uuid));
        UserProfile profile = userProfileDao.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("User profile not found for user with UUID: " + uuid));

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getRoleId() != null) {
            user.setRole(Role.fromNumber(userDto.getRoleId()));
        }
        user.setUpdatedAt(ZonedDateTime.now());

        if (userDto.getName() != null) {
            profile.setName(userDto.getName());
        }
        if (userDto.getExtId() != null) {
            profile.setExtId(userDto.getExtId());
        }
        if (userDto.getDesignation() != null) {
            profile.setDesignation(userDto.getDesignation());
        }
        if (userDto.getContactNumber() != null) {
            profile.setContactNumber(userDto.getContactNumber());
        }
        if (userDto.getEmail() != null) {
            profile.setEmail(userDto.getEmail());
        }
        if (userDto.getPermanentAddr() != null) {
            profile.setPermanentAddr(userDto.getPermanentAddr());
        }
        if (userDto.getCurrentAddr() != null) {
            profile.setCurrentAddr(userDto.getCurrentAddr());
        }
        if (userDto.getStateId() != null) {
            profile.setState(getState(userDto));
        }
        if (userDto.getRsetiId() != null) {
            profile.setRseti(getRseti(userDto));
        }
        if (userDto.getPhotoUrl() != null) {
            profile.setPhotoUrl(userDto.getPhotoUrl());
        }
        profile.setUpdatedOn(ZonedDateTime.now());
        profile.setUpdatedBy(getCurrentUser().getUsername());

        user = userDao.save(user);
        profile = userProfileDao.save(profile);

        return mapUserToDto(user, profile);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN')")
    public void deleteUser(UUID uuid) {
        User user = userDao.findByUuid(uuid)
        .orElseThrow(() -> new UserNotFoundException("User not found with UUID: " + uuid));
        UserProfile profile = userProfileDao.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("User profile not found for user with UUID: " + uuid));

        userProfileDao.delete(profile);
        userDao.delete(user);
    }

    public boolean existsByUsername(String username) {
        return userDao.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userDao.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        UserPrincipal userPrincipal = SecurityUtils.getCurrentUser();
        if (userPrincipal == null) {
            throw new RuntimeException("No authenticated user found");
        }

        Optional<User> optUser = userDao.findByUsername(userPrincipal.getUsername());
        if (optUser.isEmpty()) {
            throw new UserNotFoundException("User not found with username: " + userPrincipal.getUsername());
        }
        return optUser.get();
    }

    @Transactional
    public User updateUserProfile(String newEmail) {
        User currentUser = getCurrentUser();
        currentUser.setEmail(newEmail);
        return userDao.save(currentUser);
    }

    @Transactional
    public void resetPassword(UUID uuid, String newPassword) {
        User user = userDao.findByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with UUID: " + uuid));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(ZonedDateTime.now());
        userDao.save(user);
    }

    private UserDto mapUserToDto(User user, UserProfile profile) {
        UserDto dto = new UserDto();
        dto.setUuid(user.getUuid());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoleId(user.getRole().getNumber());
        dto.setName(profile.getName());
        dto.setExtId(profile.getExtId());
        dto.setDesignation(profile.getDesignation());
        dto.setContactNumber(profile.getContactNumber());
        dto.setPermanentAddr(profile.getPermanentAddr());
        dto.setCurrentAddr(profile.getCurrentAddr());
        dto.setStateId(profile.getState() != null ? profile.getState().getExtId() : null);
        dto.setRsetiId(profile.getRseti() != null ? profile.getRseti().getUuid() : null);
        dto.setPhotoUrl(profile.getPhotoUrl());
        return dto;
    }

}