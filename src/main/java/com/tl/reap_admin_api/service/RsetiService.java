package com.tl.reap_admin_api.service;

import com.tl.reap_admin_api.dao.RsetiDao;
import com.tl.reap_admin_api.dao.LanguageDao;
import com.tl.reap_admin_api.dto.RsetiDto;
import com.tl.reap_admin_api.dto.RsetiListDto;
import com.tl.reap_admin_api.dto.RsetiTranslationDto;
import com.tl.reap_admin_api.mapper.RsetiMapper;
import com.tl.reap_admin_api.model.RSETI;
import com.tl.reap_admin_api.model.Language;
import com.tl.reap_admin_api.model.RsetiTranslation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RsetiService {

    private final RsetiDao rsetiDao;
    private final LanguageDao languageDao;
    private final RsetiMapper rsetiMapper;

    @Autowired
    public RsetiService(RsetiDao rsetiDao, LanguageDao languageDao, RsetiMapper rsetiMapper) {
        this.rsetiDao = rsetiDao;
        this.languageDao = languageDao;
        this.rsetiMapper = rsetiMapper;
    }

    @Transactional(readOnly = true)
    public List<RsetiListDto> getAllRsetisWithCourseCount() {
        List<RSETI> rsetis = rsetiDao.findAllWithCourses();
        return rsetis.stream().map(rsetiMapper::toListDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RsetiDto getRsetiByUuid(UUID uuid) {
        return rsetiDao.findByUuid(uuid)
                .map(rsetiMapper::toDTO)
                .orElse(null);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'NAR_STAFF')")
    public RsetiDto createRseti(RsetiDto rsetiDto) {
        RSETI rseti = rsetiMapper.toEntity(rsetiDto);
        rseti.setUuid(UUID.randomUUID()); 

        // Create translations only if provided in the input
        if (rsetiDto.getTranslations() != null && !rsetiDto.getTranslations().isEmpty()) {
            createTranslations(rseti, rsetiDto.getTranslations());
        }

        setLanguagesForTranslations(rseti);
        RSETI savedRseti = rsetiDao.save(rseti);
        return rsetiMapper.toDTO(savedRseti);
    }

    private void createTranslations(RSETI rseti, List<RsetiTranslationDto> translationDtos) {
        for (RsetiTranslationDto translationDto : translationDtos) {
            RsetiTranslation translation = new RsetiTranslation();
            translation.setRseti(rseti);
            translation.setLanguageCode(translationDto.getLanguageCode());
            translation.setName(translationDto.getName());
            translation.setDistrictName(translationDto.getDistrictName());
            translation.setAddress(translationDto.getAddress());
            translation.setDirectorName(translationDto.getDirectorName());
            rseti.addTranslation(translation);
        }
    }


    @Transactional
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'NAR_STAFF')")
    public RsetiDto updateRseti(UUID uuid, RsetiDto rsetiDto) {
        RSETI rseti = rsetiDao.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("RSETI not found"));
        
        // Update main RSETI fields
        rsetiMapper.updateEntityFromDTO(rsetiDto, rseti);
        
        // Handle translations
        if (rsetiDto.getTranslations() != null) {
            updateRsetiTranslations(rseti, rsetiDto.getTranslations());
        }
        
        setLanguagesForTranslations(rseti);
        
        RSETI updatedRseti = rsetiDao.save(rseti);
        return rsetiMapper.toDTO(updatedRseti);
    }

    private void updateRsetiTranslations(RSETI rseti, List<RsetiTranslationDto> translationDtos) {
        Map<String, RsetiTranslation> existingTranslations = rseti.getTranslations().stream()
                .collect(Collectors.toMap(RsetiTranslation::getLanguageCode, t -> t));

        for (RsetiTranslationDto translationDto : translationDtos) {
            RsetiTranslation translation = existingTranslations.get(translationDto.getLanguageCode());
            if (translation == null) {
                translation = new RsetiTranslation();
                translation.setRseti(rseti);
                rseti.addTranslation(translation);
            }
            rsetiMapper.updateTranslationFromDTO(translationDto, translation);
        }
    }

    private void setLanguagesForTranslations(RSETI rseti) {
        for (RsetiTranslation translation : rseti.getTranslations()) {
            String languageCode = translation.getLanguageCode();
            if (languageCode == null || languageCode.isEmpty()) {
                throw new IllegalArgumentException("Language code is missing for a translation");
            }
            Language language = languageDao.findByCode(languageCode)
                .orElseThrow(() -> new RuntimeException("Language not found: " + languageCode));
            translation.setLanguage(language);
        }
    }


    @Transactional
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'NAR_STAFF')")
    public void deleteRseti(UUID uuid) {
        rsetiDao.deleteByUuid(uuid);
    }

    
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'NAR_STAFF')")
    public List<RsetiDto> getRsetisByStateId(UUID stateId) {
        List<RSETI> rsetis = rsetiDao.findByStateId(stateId);
        return rsetis.stream().map(rsetiMapper::toDTO).collect(Collectors.toList());
    }
}