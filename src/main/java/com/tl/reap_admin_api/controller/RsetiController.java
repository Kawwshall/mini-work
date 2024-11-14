package com.tl.reap_admin_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tl.reap_admin_api.dto.RsetiCourseDto;
import com.tl.reap_admin_api.dto.RsetiDto;
import com.tl.reap_admin_api.dto.RsetiListDto;
import com.tl.reap_admin_api.dto.TraineeRsetiDto;
import com.tl.reap_admin_api.exception.CourseNotFoundException;
import com.tl.reap_admin_api.exception.RsetiNotFoundException;
import com.tl.reap_admin_api.exception.TraineeNotFoundException;
import com.tl.reap_admin_api.service.RsetiCourseService;
import com.tl.reap_admin_api.service.RsetiService;
import com.tl.reap_admin_api.service.TraineeRsetiService;

@RestController
@RequestMapping("/apis/v1/rsetis")
public class RsetiController {

	private final RsetiService rsetiService;
	private final RsetiCourseService rsetiCourseService;
	private final TraineeRsetiService traineeRsetiService;

	@Autowired
	public RsetiController(RsetiService rsetiService, RsetiCourseService rsetiCourseService,
			TraineeRsetiService traineeRsetiService) {
		this.rsetiService = rsetiService;
		this.rsetiCourseService = rsetiCourseService;
		this.traineeRsetiService = traineeRsetiService;
	}

	@GetMapping
    public ResponseEntity<List<RsetiListDto>> getAllRsetis() {
        List<RsetiListDto> rsetis = rsetiService.getAllRsetisWithCourseCount();
        return ResponseEntity.ok(rsetis);
    }

	@GetMapping("/{uuid}")
	public ResponseEntity<RsetiDto> getRsetiByUuid(@PathVariable UUID uuid) {
		RsetiDto rseti = rsetiService.getRsetiByUuid(uuid);
		return rseti != null ? ResponseEntity.ok(rseti) : ResponseEntity.notFound().build();
	}

	@PostMapping
    public ResponseEntity<RsetiDto> createRseti(@RequestBody RsetiDto rsetiDto) {
        try {
            RsetiDto createdRseti = rsetiService.createRseti(rsetiDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRseti);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

	@PutMapping("/{uuid}")
	public ResponseEntity<RsetiDto> updateRseti(@PathVariable UUID uuid, @RequestBody RsetiDto rsetiDto) {
		try {
			RsetiDto updatedRseti = rsetiService.updateRseti(uuid, rsetiDto);
			return ResponseEntity.ok(updatedRseti);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{uuid}")
	public ResponseEntity<Void> deleteRseti(@PathVariable UUID uuid) {
		rsetiService.deleteRseti(uuid);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/state/{stateId}")
	public ResponseEntity<List<RsetiDto>> getRsetisByState(@PathVariable UUID stateId) {
		List<RsetiDto> rsetis = rsetiService.getRsetisByStateId(stateId);
		return ResponseEntity.ok(rsetis);
	}

	@PostMapping("/{uuid}/courses")
	public ResponseEntity<List<RsetiCourseDto>> addCoursesToRseti(@PathVariable UUID uuid,
			@RequestBody List<RsetiCourseDto> courseDtos) {
		try {
			List<RsetiCourseDto> addedCourses = rsetiCourseService.addCoursesToRseti(uuid, courseDtos);
			return ResponseEntity.status(HttpStatus.CREATED).body(addedCourses);
		} catch (RsetiNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/{uuid}/courses")
	public ResponseEntity<List<RsetiCourseDto>> getCoursesInRseti(@PathVariable UUID uuid) {
		try {
			List<RsetiCourseDto> courses = rsetiCourseService.getCoursesInRseti(uuid);
			return ResponseEntity.ok(courses);
		} catch (RsetiNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{uuid}/courses")
	public ResponseEntity<List<RsetiCourseDto>> updateCoursesInRseti(@PathVariable UUID uuid,
			@RequestBody List<RsetiCourseDto> courseDtos) {
		try {
			List<RsetiCourseDto> updatedCourses = rsetiCourseService.updateCoursesInRseti(uuid, courseDtos);
			return ResponseEntity.ok(updatedCourses);
		} catch (RsetiNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{uuid}/courses/{courseUuid}")
	public ResponseEntity<Void> deleteCourseFromRseti(@PathVariable UUID uuid, @PathVariable UUID courseUuid) {
		try {
			rsetiCourseService.deleteCourseFromRseti(uuid, courseUuid);
			return ResponseEntity.noContent().build();
		} catch (RsetiNotFoundException | CourseNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	 @GetMapping("/{rsetiUuid}/courses/{courseUuid}/trainees")
	    public ResponseEntity<List<TraineeRsetiDto>> getTraineesByCourse(
	            @PathVariable UUID rsetiUuid,
	            @PathVariable UUID courseUuid) {
	        try {
	            List<TraineeRsetiDto> trainees = traineeRsetiService.getTraineesByCourse(rsetiUuid, courseUuid);
	            return ResponseEntity.ok(trainees);
	        } catch (RsetiNotFoundException | CourseNotFoundException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    @PostMapping("/{rsetiUuid}/courses/{courseUuid}/trainees")
	    public ResponseEntity<TraineeRsetiDto> addTraineeToCourse(
	            @PathVariable UUID rsetiUuid,
	            @PathVariable UUID courseUuid,
	            @RequestBody TraineeRsetiDto traineeRsetiDto) {
	        try {
	            TraineeRsetiDto addedTrainee = traineeRsetiService.addTraineeToCourse(rsetiUuid, courseUuid, traineeRsetiDto);
	            return ResponseEntity.status(HttpStatus.CREATED).body(addedTrainee);
	        } catch (RsetiNotFoundException | CourseNotFoundException | TraineeNotFoundException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    @PutMapping("/{rsetiUuid}/courses/{courseUuid}/trainees/{traineeUuid}")
	    public ResponseEntity<TraineeRsetiDto> updateTraineeInCourse(
	            @PathVariable UUID rsetiUuid,
	            @PathVariable UUID courseUuid,
	            @PathVariable UUID traineeUuid,
	            @RequestBody TraineeRsetiDto traineeRsetiDto) {
	        try {
	            TraineeRsetiDto updatedTrainee = traineeRsetiService.updateTraineeInCourse(rsetiUuid, courseUuid, traineeUuid, traineeRsetiDto);
	            return ResponseEntity.ok(updatedTrainee);
	        } catch (RsetiNotFoundException | CourseNotFoundException | TraineeNotFoundException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    @DeleteMapping("/{rsetiUuid}/courses/{courseUuid}/trainees/{traineeUuid}")
	    public ResponseEntity<Void> deleteTraineeFromCourse(
	            @PathVariable UUID rsetiUuid,
	            @PathVariable UUID courseUuid,
	            @PathVariable UUID traineeUuid) {
	        try {
	            traineeRsetiService.deleteTraineeFromCourse(rsetiUuid, courseUuid, traineeUuid);
	            return ResponseEntity.noContent().build();
	        } catch (RsetiNotFoundException | CourseNotFoundException | TraineeNotFoundException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }
	

}
