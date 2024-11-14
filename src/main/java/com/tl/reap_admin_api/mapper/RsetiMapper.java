package com.tl.reap_admin_api.mapper;

import com.tl.reap_admin_api.dto.RsetiDto;
import com.tl.reap_admin_api.dto.RsetiListDto;
import com.tl.reap_admin_api.dto.RsetiTranslationDto;
import com.tl.reap_admin_api.model.RSETI;
import com.tl.reap_admin_api.model.RsetiTranslation;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RsetiMapper {

    public RsetiDto toDTO(RSETI rseti) {
        if (rseti == null) {
            return null;
        }
        RsetiDto dto = new RsetiDto(
            rseti.getUuid(),
            rseti.getExtId(),
            rseti.getName(),
            rseti.getEmail(),
            rseti.getContactNo(),
            rseti.getDistrictName(),
            rseti.getAddress(),
            rseti.getDirectorName(),
            rseti.getDirectorContactNo(),
            rseti.getStateId(),
            rseti.getBankId()
        );
        
        dto.setTranslations(rseti.getTranslations().stream()
            .map(this::toTranslationDTO)
            .collect(Collectors.toList()));
        return dto;
    }

    public RSETI toEntity(RsetiDto dto) {
        if (dto == null) {
            return null;
        }
        RSETI rseti = new RSETI();
        updateEntityFromDTO(dto, rseti);
        return rseti;
    }

    private RsetiTranslationDto toTranslationDTO(RsetiTranslation translation) {
        return new RsetiTranslationDto(
            translation.getId(),
            translation.getLanguageCode(),
            translation.getName(),
            translation.getDistrictName(),
            translation.getAddress(),
            translation.getDirectorName()
        );
    }

    private RsetiTranslation toTranslationEntity(RsetiTranslationDto dto) {
        RsetiTranslation translation = new RsetiTranslation();
        if (dto.getId() != null) translation.setId(dto.getId());
        if (dto.getLanguageCode() != null) translation.setLanguageCode(dto.getLanguageCode());
        if (dto.getName() != null) translation.setName(dto.getName());
        if (dto.getDistrictName() != null) translation.setDistrictName(dto.getDistrictName());
        if (dto.getAddress() != null) translation.setAddress(dto.getAddress());
        if (dto.getDirectorName() != null) translation.setDirectorName(dto.getDirectorName());
        return translation;
    }

    public void updateEntityFromDTO(RsetiDto dto, RSETI rseti) {
        if (dto == null || rseti == null) {
            return;
        }

        // Update only non-null fields
        if (dto.getExtId() != null) rseti.setExtId(dto.getExtId());
        if (dto.getName() != null) rseti.setName(dto.getName());
        if (dto.getEmail() != null) rseti.setEmail(dto.getEmail());
        if (dto.getContactNo() != null) rseti.setContactNo(dto.getContactNo());
        if (dto.getDistrictName() != null) rseti.setDistrictName(dto.getDistrictName());
        if (dto.getAddress() != null) rseti.setAddress(dto.getAddress());
        if (dto.getDirectorName() != null) rseti.setDirectorName(dto.getDirectorName());
        if (dto.getDirectorContactNo() != null) rseti.setDirectorContactNo(dto.getDirectorContactNo());
        if (dto.getStateId() != null) rseti.setStateId(dto.getStateId());
        if (dto.getBankId() != null) rseti.setBankId(dto.getBankId());

    }

    public void updateTranslationFromDTO(RsetiTranslationDto dto, RsetiTranslation translation) {
        if (dto == null || translation == null) {
            return;
        }

        // Update only non-null fields
        if (dto.getLanguageCode() != null) translation.setLanguageCode(dto.getLanguageCode());
        if (dto.getName() != null) translation.setName(dto.getName());
        if (dto.getDistrictName() != null) translation.setDistrictName(dto.getDistrictName());
        if (dto.getAddress() != null) translation.setAddress(dto.getAddress());
        if (dto.getDirectorName() != null) translation.setDirectorName(dto.getDirectorName());
    }
    
    
    public RsetiListDto toListDTO(RSETI rseti) {
        if (rseti == null) {
            return null;
        }
        return new RsetiListDto(
            rseti.getUuid(),
            rseti.getExtId(),
            rseti.getName(),
            rseti.getEmail(),
            rseti.getContactNo(),
            rseti.getDistrictName(),
            rseti.getAddress(),
            rseti.getDirectorName(),
            rseti.getDirectorContactNo(),
            rseti.getStateId(),
            rseti.getBankId(),
            rseti.getRsetiCourses().size()
        );
    }
}
