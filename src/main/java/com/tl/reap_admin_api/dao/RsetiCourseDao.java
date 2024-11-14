package com.tl.reap_admin_api.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tl.reap_admin_api.model.Course;
import com.tl.reap_admin_api.model.RSETI;
import com.tl.reap_admin_api.model.RsetiCourse;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class RsetiCourseDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
    public RsetiCourse save(RsetiCourse rsetiCourse) {
        if (rsetiCourse.getId() == null) {
            entityManager.persist(rsetiCourse);
        } else {
            rsetiCourse = entityManager.merge(rsetiCourse);
        }
        return rsetiCourse;
    }
	
	
	public List<RsetiCourse> findByRseti(RSETI rseti) {
		TypedQuery<RsetiCourse> query = entityManager
				.createQuery("SELECT rc FROM RsetiCourse rc WHERE rc.rseti = :rseti", RsetiCourse.class);
		query.setParameter("rseti", rseti);
		return query.getResultList();
	}

	public Optional<RsetiCourse> findByRsetiAndCourseCode(RSETI rseti, String courseCode) {
		TypedQuery<RsetiCourse> query = entityManager
				.createQuery("SELECT rc FROM RsetiCourse rc JOIN rc.rseti r JOIN Course c ON rc.id = c.id "
						+ "WHERE r = :rseti AND c.courseCode = :courseCode", RsetiCourse.class);
		query.setParameter("rseti", rseti);
		query.setParameter("courseCode", courseCode);
		List<RsetiCourse> results = query.getResultList();
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	public Optional<RsetiCourse> findByRsetiAndCourse(RSETI rseti, Course course) {
		TypedQuery<RsetiCourse> query = entityManager.createQuery(
				"SELECT rc FROM RsetiCourse rc WHERE rc.rseti = :rseti AND rc.id = :courseId", RsetiCourse.class);
		query.setParameter("rseti", rseti);
		query.setParameter("courseId", course.getId());
		List<RsetiCourse> results = query.getResultList();
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	public Optional<RsetiCourse> findByRsetiUuidAndCourseUuid(UUID rsetiUuid, UUID courseUuid) {
        TypedQuery<RsetiCourse> query = entityManager.createQuery(
                "SELECT rc FROM RsetiCourse rc " +
                "JOIN rc.rseti r " +
                "JOIN rc.course c " +
                "WHERE r.uuid = :rsetiUuid AND c.uuid = :courseUuid", RsetiCourse.class);
        query.setParameter("rsetiUuid", rsetiUuid);
        query.setParameter("courseUuid", courseUuid);
        List<RsetiCourse> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

	public List<RsetiCourse> findByCourse(Course course) {
        TypedQuery<RsetiCourse> query = entityManager.createQuery(
            "SELECT rc FROM RsetiCourse rc WHERE rc.course = :course", RsetiCourse.class);
        query.setParameter("course", course);
        return query.getResultList();
    }

	@Transactional
	public void delete(RsetiCourse rsetiCourse) {
		entityManager.remove(rsetiCourse);
	}

}