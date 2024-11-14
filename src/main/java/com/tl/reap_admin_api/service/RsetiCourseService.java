package com.tl.reap_admin_api.service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tl.reap_admin_api.dao.CourseDao;
import com.tl.reap_admin_api.dao.RsetiCourseDao;
import com.tl.reap_admin_api.dao.RsetiDao;
import com.tl.reap_admin_api.dto.RsetiCourseDto;
import com.tl.reap_admin_api.exception.CourseNotFoundException;
import com.tl.reap_admin_api.exception.RsetiNotFoundException;
import com.tl.reap_admin_api.mapper.RsetiCourseMapper;
import com.tl.reap_admin_api.model.Course;
import com.tl.reap_admin_api.model.RSETI;
import com.tl.reap_admin_api.model.RsetiCourse;

@Service
public class RsetiCourseService {

	private final RsetiDao rsetiDao;
	private final RsetiCourseDao rsetiCourseDao;
	private final RsetiCourseMapper rsetiCourseMapper;
	private final UserService userService; 
	private final CourseDao courseDao;

	@Autowired
	public RsetiCourseService(RsetiDao rsetiDao, RsetiCourseDao rsetiCourseDao, RsetiCourseMapper rsetiCourseMapper,
			UserService userService, CourseDao courseDao) {
		this.rsetiDao = rsetiDao;
		this.rsetiCourseDao = rsetiCourseDao;
		this.rsetiCourseMapper = rsetiCourseMapper;
		this.userService = userService;
		this.courseDao = courseDao;
	}

	@Transactional
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'NAR_STAFF')")
	public List<RsetiCourseDto> addCoursesToRseti(UUID rsetiUuid, List<RsetiCourseDto> rsetiCourseDtos) {
		
		List<RsetiCourse> addedCourses = new ArrayList<>();
		 RSETI rseti = rsetiDao.findByUuid(rsetiUuid)
			        .orElseThrow(() -> new RsetiNotFoundException("RSETI not found with UUID: " + rsetiUuid));


		for (RsetiCourseDto dto : rsetiCourseDtos) {
			RsetiCourse rsetiCourse = rsetiCourseMapper.toEntity(dto);
			 Course course = courseDao.findByUuid(dto.getCourseUuid())
			            .orElseThrow(() -> new CourseNotFoundException("Course not found with UUID: " + dto.getCourseUuid()));

			rsetiCourse.setUuid(UUID.randomUUID());
			 rsetiCourse.setRseti(rseti);
			 rsetiCourse.setCourse(course);
            rsetiCourse.setCreatedAt(ZonedDateTime.now());
			rsetiCourse.setCreatedBy(userService.getCurrentUser().getUsername());
            rsetiCourse.setUpdatedAt(ZonedDateTime.now());
			rsetiCourse.setCreatedBy(userService.getCurrentUser().getUsername());
			rsetiCourse= rsetiCourseDao.save(rsetiCourse );
			addedCourses.add(rsetiCourse);
			
		}

		return rsetiCourseMapper.toDtoList(addedCourses);
	}

	@Transactional(readOnly = true)
	public List<RsetiCourseDto> getCoursesInRseti(UUID rsetiUuid) {
		RSETI rseti = rsetiDao.findByUuid(rsetiUuid)
				.orElseThrow(() -> new RsetiNotFoundException("RSETI not found with UUID: " + rsetiUuid));

		List<RsetiCourse> rsetiCourses = rsetiCourseDao.findByRseti(rseti);
		return rsetiCourseMapper.toDtoList(rsetiCourses);
	}

	@Transactional
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'NAR_STAFF')")
	public List<RsetiCourseDto> updateCoursesInRseti(UUID rsetiUuid, List<RsetiCourseDto> courseDtos) {
		
		List<RsetiCourse> updatedCourses = new ArrayList<>();

		for (RsetiCourseDto dto : courseDtos) {
			RsetiCourse rsetiCourse = rsetiCourseDao.findByRsetiUuidAndCourseUuid(rsetiUuid, dto.getCourseUuid()).orElseThrow(
					() -> new CourseNotFoundException("Course not found with UUID: " + dto.getCourseUuid() 
					+ "for rseti with UUID: " + rsetiUuid ));

			if (dto.getStartYear() != 0 && dto.getStartMonth() != 0) {
				rsetiCourse.setStartDate(LocalDate.of(dto.getStartYear(), dto.getStartMonth(), 1));
			}
			if (dto.getEndYear() != 0 && dto.getEndMonth() != 0) {
				rsetiCourse.setEndDate(LocalDate.of(dto.getEndYear(), dto.getEndMonth(), 1));
			}
			updatedCourses.add(rsetiCourseDao.save(rsetiCourse));
		}

		return rsetiCourseMapper.toDtoList(updatedCourses);
	}

	@Transactional
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NAR_ADMIN', 'NAR_STAFF')")
	public void deleteCourseFromRseti(UUID rsetiUuid, UUID courseUuid) {
		RsetiCourse rsetiCourse = rsetiCourseDao.findByRsetiUuidAndCourseUuid(rsetiUuid,courseUuid).orElseThrow(
					() -> new CourseNotFoundException("Course not found with UUID: " +courseUuid 
					+ "for rseti with UUID: " + rsetiUuid ));

		rsetiCourseDao.delete(rsetiCourse);
	}
}
