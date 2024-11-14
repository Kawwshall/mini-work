package com.tl.reap_admin_api.mapper;

import com.tl.reap_admin_api.dto.TraineeCredentialDto;
import com.tl.reap_admin_api.model.TraineeCredential;
import org.springframework.stereotype.Component;

@Component
public class TraineeCredentialMapper {

    public TraineeCredentialDto toDto(TraineeCredential trainee) {
        TraineeCredentialDto dto = new TraineeCredentialDto();
        dto.setUuid(trainee.getUuid());
        dto.setUsername(trainee.getUsername());
        dto.setEmail(trainee.getEmail());
        return dto;
    }

    public TraineeCredential toEntity(TraineeCredentialDto dto) {
        TraineeCredential trainee = new TraineeCredential();
        trainee.setUuid(dto.getUuid());
        trainee.setUsername(dto.getUsername());
        trainee.setEmail(dto.getEmail());
        return trainee;
    }
}