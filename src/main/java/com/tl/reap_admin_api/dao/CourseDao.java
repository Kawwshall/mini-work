package com.tl.reap_admin_api.dao;

import com.tl.reap_admin_api.model.Course;
import com.tl.reap_admin_api.model.RSETI;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CourseDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Course> findAll() {
        TypedQuery<Course> query = entityManager.createQuery("SELECT c FROM Course c", Course.class);
        return query.getResultList();
    }

    public Optional<Course> findByUuid(UUID uuid) {
        TypedQuery<Course> query = entityManager.createQuery(
            "SELECT c FROM Course c WHERE c.uuid = :uuid", Course.class);
        query.setParameter("uuid", uuid);
        List<Course> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    public Optional<Course> findById(Long id) {
        Course course = entityManager.find(Course.class, id);
        return Optional.ofNullable(course);
    }

    public Course save(Course course) {
        if (course.getId() == null) {
            entityManager.persist(course);
        } else {
            course = entityManager.merge(course);
        }
        return course;
    }

    public void deleteById(Long id) {
        Course course = entityManager.find(Course.class, id);
        if (course != null) {
            entityManager.remove(course);
        }
    }

    public List<Course> findByCategoryId(Long categoryId) {
        return entityManager.createQuery("SELECT c FROM Course c WHERE c.category.id = :categoryId", Course.class)
                .setParameter("categoryId", categoryId)
                .getResultList();
    }


    public Course findLatestByCourseCode(String courseCode) {
        TypedQuery<Course> query = entityManager.createQuery(
            "SELECT c FROM Course c WHERE c.courseCode = :courseCode order by c.updatedAt DESC", Course.class);
        query.setParameter("courseCode", courseCode);
        query.setMaxResults(1);
        Course result = query.getSingleResult();
        return result;
    }
  
	  public Optional<Course> findByCourseCode(String courseCode) {
        TypedQuery<Course> query = entityManager.createQuery(
            "SELECT c FROM Course c WHERE c.courseCode = :courseCode", Course.class);
        query.setParameter("courseCode", courseCode);
        List<Course> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}
