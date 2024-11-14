package com.tl.reap_admin_api.controller;

import com.tl.reap_admin_api.dto.ChapterDto;
import com.tl.reap_admin_api.dto.CourseDto;
import com.tl.reap_admin_api.dto.CourseRsetisDto;
import com.tl.reap_admin_api.dto.VideoDto;
import com.tl.reap_admin_api.dto.VideoResponse;
import com.tl.reap_admin_api.model.CourseTranslation;
import com.tl.reap_admin_api.model.Language;
import com.tl.reap_admin_api.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.UUID;

import java.io.IOException;

@RestController
@RequestMapping("/apis/v1/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody CourseDto courseDto) {

        try {
            CourseDto createdCourseDto = courseService.createCourse(courseDto);           
            return new ResponseEntity<>(createdCourseDto, HttpStatus.CREATED);
        }  catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch(AccessDeniedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error creating course: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{uuid}/image")
    public ResponseEntity<CourseDto> uploadCourseImage(@PathVariable UUID uuid, @RequestParam("file") MultipartFile file) {
        try {
            CourseDto updatedCourse = courseService.uploadCourseImage(uuid, file);
            return ResponseEntity.ok(updatedCourse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/batch-update")
    public ResponseEntity<String> batchUpdateCourses(@RequestParam("file") MultipartFile file) {
        try {
            courseService.processBatchUpdate(file.getInputStream());
            return ResponseEntity.ok("Batch update completed successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error processing file: " + e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        try {
            
            List<CourseDto> courses = courseService.getAllCourses();
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch(AccessDeniedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<CourseDto> getCourseByUuid(@PathVariable UUID uuid) {
        CourseDto courseDto = courseService.getCourseByUuid(uuid);
        return courseDto != null
                ? new ResponseEntity<>(courseDto, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{uuid}/rsetis")
    public ResponseEntity<CourseRsetisDto> getCourseRsetis(@PathVariable UUID uuid) {
        CourseRsetisDto response = courseService.getCourseRsetis(uuid);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{uuid}")
    public  ResponseEntity<CourseDto> updateCourse(@PathVariable UUID uuid, @RequestBody CourseDto courseDetails) {
        try {
            CourseDto updatedCourseDto = courseService.updateCourse(uuid, courseDetails);
            return new ResponseEntity<>(updatedCourseDto, HttpStatus.OK);
        } catch(AccessDeniedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteCourse(@PathVariable UUID uuid) {
        courseService.deleteCourse(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{uuid}/translations")
    public ResponseEntity<CourseDto> addCourseTranslation(
            @PathVariable UUID uuid,
            @RequestBody CourseTranslation translation,
            @RequestParam Long languageId) {
        Language language = new Language(); // In a real application, you would fetch this from a language service
        language.setId(languageId);
        CourseDto updatedCourseDto = courseService.addCourseTranslation(uuid, translation, languageId);
        return new ResponseEntity<>(updatedCourseDto, HttpStatus.CREATED);
    }

    @PostMapping("/{uuid}/chapters")
    public ResponseEntity<CourseDto> addChapterToCourse(@PathVariable UUID uuid, 
            @RequestBody ChapterDto chapterDto) {
        CourseDto updatedCourse = courseService.addChapterToCourse(uuid, chapterDto);
        return new ResponseEntity<>(updatedCourse, HttpStatus.CREATED);
    }

    @PutMapping("/{courseUuid}/chapters/{chapterUuid}")
    public ResponseEntity<CourseDto> updateChapterInCourse(
            @PathVariable UUID courseUuid,
            @PathVariable UUID chapterUuid,
            @RequestBody ChapterDto chapterDto) {
        CourseDto updatedCourse = courseService.updateChapterInCourse(courseUuid, chapterUuid, chapterDto);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    @DeleteMapping("/{courseUuid}/chapters/{chapterUuid}")
    public ResponseEntity<CourseDto> deleteChapterFromCourse(
            @PathVariable UUID courseUuid,
            @PathVariable UUID chapterUuid) {
        CourseDto updatedCourse = courseService.deleteChapterFromCourse(courseUuid, chapterUuid);
        return new ResponseEntity<>(updatedCourse, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{courseUuid}/chapters/{chapterUuid}/videos")
    public ResponseEntity<?> addVideoToChapter(
            @PathVariable UUID courseUuid,
            @PathVariable UUID chapterUuid,
            @RequestBody VideoDto videoDto) {
        try {
            VideoResponse response = courseService.addVideoToChapter(courseUuid, chapterUuid, videoDto);
            VideoDto video = response.getVideo();
            return new ResponseEntity<>(video, response.getStatus());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();          
            System.out.println("Error adding video to chapter: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{courseUuid}/chapters/{chapterUuid}/videos/{videoUuid}")
    public ResponseEntity<?> deleteVideoFromChapter(
            @PathVariable UUID courseUuid,
            @PathVariable UUID chapterUuid,
            @PathVariable UUID videoUuid) {
        try {
            CourseDto updatedCourse = courseService.deleteVideoFromChapter(courseUuid, chapterUuid, videoUuid);
            return new ResponseEntity<>(updatedCourse, HttpStatus.NO_CONTENT);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}