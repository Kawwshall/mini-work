package com.tl.reap_admin_api.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.tl.reap_admin_api.dao.FAQDao;
import com.tl.reap_admin_api.dto.FAQDto;
import com.tl.reap_admin_api.dto.FAQTranslationDto;
import com.tl.reap_admin_api.mapper.FAQMapper;
import com.tl.reap_admin_api.model.FAQ;
import com.tl.reap_admin_api.model.FAQCategory;
import com.tl.reap_admin_api.model.FAQTranslation;
import com.tl.reap_admin_api.model.Language;
import com.tl.reap_admin_api.model.User;

import jakarta.transaction.Transactional;

@Service
public class FAQService {
    
    @Autowired
    private FAQDao faqDao;
   
    @Autowired
    private FAQMapper mapper;
    
    @Autowired
    private UserService userService;
   
    @Transactional
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'NAR_STAFF')")
    public FAQDto createFAQ(FAQDto dto) {
        FAQ faq = new FAQ();
        FAQCategory category = faqDao.findCategoryById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));
        faq.setCategory(category);
        User currentUser = userService.getCurrentUser();
        faq.setCreatedBy(currentUser.getUsername());
        faq.setUpdatedBy(currentUser.getUsername());
        updateTranslations(faq, dto.getTranslations());
        faq = faqDao.save(faq);
        return mapper.toDto(faq);
    }
   
    @Transactional
    public FAQDto getFAQByUuid(UUID uuid) {
        FAQ faq = faqDao.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("FAQ not found"));
        return mapper.toDto(faq);
    }

    @Transactional
    public List<FAQDto> getAllFAQs() {
        return faqDao.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'NAR_STAFF')")
    public FAQDto updateFAQ(UUID uuid, FAQDto dto) {
        FAQ faq = faqDao.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("FAQ not found"));
        
        FAQCategory category = faqDao.findCategoryById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));
        faq.setCategory(category);
        faq.setUpdatedAt(ZonedDateTime.now());
        User currentUser = userService.getCurrentUser();
        faq.setUpdatedBy(currentUser.getUsername());
        updateTranslations(faq, dto.getTranslations());

        faq = faqDao.save(faq);
        return mapper.toDto(faq);
    }

    private void updateTranslations(FAQ faq, Set<FAQTranslationDto> translationDtos) {
        faq.clearTranslations();

        if (translationDtos != null) {
            for (FAQTranslationDto translationDto : translationDtos) {
                FAQTranslation translation = new FAQTranslation();
                translation.setQuestion(translationDto.getQuestion());
                translation.setAnswer(translationDto.getAnswer());
                Language language = faqDao.findLanguageByCode(translationDto.getLanguageCode())
                        .orElseThrow(() -> new RuntimeException("Language not found: " + translationDto.getLanguageCode()));
                translation.setLanguage(language);
                faq.addTranslation(translation);
            }
        }
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'NAR_STAFF')")
    public void deleteFAQ(UUID uuid) {
        faqDao.deleteByUuid(uuid);
    }
}