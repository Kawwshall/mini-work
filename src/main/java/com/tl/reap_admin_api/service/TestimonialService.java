package com.tl.reap_admin_api.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tl.reap_admin_api.dao.TestimonialDao;
import com.tl.reap_admin_api.dto.TestimonialDTO;
import com.tl.reap_admin_api.dto.TestimonialTranslationDTO;
import com.tl.reap_admin_api.mapper.TestimonialMapper;
import com.tl.reap_admin_api.model.Language;
import com.tl.reap_admin_api.model.Testimonial;
import com.tl.reap_admin_api.model.TestimonialTranslation;
import com.tl.reap_admin_api.model.User;

import jakarta.transaction.Transactional;

@Service
public class TestimonialService {

	@Autowired
	private TestimonialDao testimonialDAO;

	@Autowired
	private TestimonialMapper mapper;
	
    @Autowired
    private UserService userService;

	@Transactional
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'NAR_STAFF')")
    public TestimonialDTO createTestimonial(TestimonialDTO dto) {
        Testimonial testimonial = new Testimonial();
        testimonial.setImage(dto.getImage());
        User currentUser = userService.getCurrentUser();
        testimonial.setCreatedBy(currentUser.getUsername());
        testimonial.setUpdatedBy(currentUser.getUsername());
        updateTranslations(testimonial, dto.getTranslations());
        testimonial = testimonialDAO.save(testimonial);
        return mapper.toDTO(testimonial);
    }

	@Transactional
	public TestimonialDTO getTestimonialByUuid(UUID uuid) {
		Testimonial testimonial = testimonialDAO.findByUuid(uuid)
				.orElseThrow(() -> new RuntimeException("Testimonial not found"));
		return mapper.toDTO(testimonial);
	}

	@Transactional
	public List<TestimonialDTO> getAllTestimonials() {
		return testimonialDAO.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
	}

	@Transactional
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'NAR_STAFF')")
    public TestimonialDTO updateTestimonial(UUID uuid, TestimonialDTO dto) {
        Testimonial testimonial = testimonialDAO.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Testimonial not found"));

        testimonial.setImage(dto.getImage());
        testimonial.setUpdatedAt(ZonedDateTime.now());
        User currentUser = userService.getCurrentUser();
        testimonial.setUpdatedBy(currentUser.getUsername());

        updateTranslations(testimonial, dto.getTranslations());

        testimonial = testimonialDAO.save(testimonial);
        return mapper.toDTO(testimonial);
    }

	private void updateTranslations(Testimonial testimonial, Set<TestimonialTranslationDTO> translationDTOs) {
		testimonial.clearTranslations();

		if (translationDTOs != null) {
			for (TestimonialTranslationDTO translationDTO : translationDTOs) {
				TestimonialTranslation translation = new TestimonialTranslation();
				translation.setName(translationDTO.getName());
				translation.setDesignation(translationDTO.getDesignation());
				translation.setTestimonialText(translationDTO.getTestimonialText());
				Language language = testimonialDAO.findLanguageByCode(translationDTO.getLanguageCode()).orElseThrow(
						() -> new RuntimeException("Language not found: " + translationDTO.getLanguageCode()));
				translation.setLanguage(language);
				testimonial.addTranslation(translation);
			}
		}
	}

	@Transactional
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'NAR_STAFF')")
	public void deleteTestimonial(UUID uuid) {
		testimonialDAO.deleteByUuid(uuid);
	}
}