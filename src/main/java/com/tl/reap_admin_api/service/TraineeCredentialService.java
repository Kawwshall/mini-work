package com.tl.reap_admin_api.service;

import com.tl.reap_admin_api.dao.TraineeCredentialDao;
import com.tl.reap_admin_api.dto.TraineeCredentialDto;
import com.tl.reap_admin_api.model.TraineeCredential;
import com.tl.reap_admin_api.exception.UserNotFoundException;
import com.tl.reap_admin_api.mapper.TraineeCredentialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TraineeCredentialService {

    private final TraineeCredentialDao traineeCredentialDao;
    private final TraineeCredentialMapper traineeCredentialMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TraineeCredentialService(TraineeCredentialDao traineeCredentialDao, TraineeCredentialMapper traineeCredentialMapper, PasswordEncoder passwordEncoder) {
        this.traineeCredentialDao = traineeCredentialDao;
        this.traineeCredentialMapper = traineeCredentialMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'STATE_ADMIN', 'RSETI_ADMIN', 'TRAINER','NAR_STAFF','STATE_STAFF','RSETI_STAFF')")
    public TraineeCredentialDto createTrainee(TraineeCredentialDto traineeCredentialDto, String password) {
        TraineeCredential traineeCredential = traineeCredentialMapper.toEntity(traineeCredentialDto);
        traineeCredential.setUuid(UUID.randomUUID());
        traineeCredential.setPassword(passwordEncoder.encode(password));
        traineeCredential.setCreatedAt(ZonedDateTime.now());
        traineeCredential.setUpdatedAt(ZonedDateTime.now());

        TraineeCredential savedTraineeCredential = traineeCredentialDao.save(traineeCredential);
        return traineeCredentialMapper.toDto(savedTraineeCredential);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'STATE_ADMIN', 'RSETI_ADMIN', 'TRAINER','NAR_STAFF','STATE_STAFF','RSETI_STAFF','TRAINEE')")
    public TraineeCredentialDto getTraineeCredentialByUuid(UUID uuid) {
        TraineeCredential traineeCredential = traineeCredentialDao.findByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with uuid: " + uuid));
        return traineeCredentialMapper.toDto(traineeCredential);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'STATE_ADMIN', 'RSETI_ADMIN', 'TRAINER','NAR_STAFF','STATE_STAFF','RSETI_STAFF')")
    public List<TraineeCredentialDto> getAllTrainees() {
        return traineeCredentialDao.findAll().stream()
                .map(traineeCredentialMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'STATE_ADMIN', 'RSETI_ADMIN', 'TRAINER','NAR_STAFF','STATE_STAFF','RSETI_STAFF')")
    public TraineeCredentialDto updateTrainee(UUID uuid, TraineeCredentialDto traineeCredentialDto) {
        TraineeCredential existingTraineeCredential = traineeCredentialDao.findByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("Trainee not found with uuid: " + uuid));

        existingTraineeCredential.setUsername(traineeCredentialDto.getUsername());
        existingTraineeCredential.setEmail(traineeCredentialDto.getEmail());
        existingTraineeCredential.setUpdatedAt(ZonedDateTime.now());

        TraineeCredential updatedTraineeCredential = traineeCredentialDao.save(existingTraineeCredential);
        return traineeCredentialMapper.toDto(updatedTraineeCredential);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'STATE_ADMIN', 'RSETI_ADMIN')")
    public void deleteTrainee(UUID uuid) {
        TraineeCredential trainee = traineeCredentialDao.findByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("Trainee not found with uuid: " + uuid));
        traineeCredentialDao.delete(trainee);
    }
}