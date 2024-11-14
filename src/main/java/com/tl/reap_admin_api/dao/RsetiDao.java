package com.tl.reap_admin_api.dao;

import com.tl.reap_admin_api.model.RSETI;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RsetiDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<RSETI> findAll() {
        TypedQuery<RSETI> query = entityManager.createQuery("SELECT r FROM RSETI r LEFT JOIN FETCH r.translations", RSETI.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public Optional<RSETI> findByUuid(UUID uuid) {
        TypedQuery<RSETI> query = entityManager.createQuery(
            "SELECT r FROM RSETI r LEFT JOIN FETCH r.translations WHERE r.uuid = :uuid", RSETI.class);
        query.setParameter("uuid", uuid);
        List<RSETI> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Transactional
    public RSETI save(RSETI rseti) {
        if (rseti.getUuid() == null) {
            rseti.setUuid(UUID.randomUUID());
            entityManager.persist(rseti);
        } else {
            rseti = entityManager.merge(rseti);
        }
        return rseti;
    }

    @Transactional
    public void deleteByUuid(UUID uuid) {
        RSETI rseti = findByUuid(uuid).orElse(null);
        if (rseti != null) {
            entityManager.remove(rseti);
        }
    }
    
    @Transactional(readOnly = true)
    public List<RSETI> findByStateId(UUID stateId) {
        TypedQuery<RSETI> query = entityManager.createQuery(
            "SELECT r FROM RSETI r LEFT JOIN FETCH r.translations WHERE r.stateId = :stateId", RSETI.class);
        query.setParameter("stateId", stateId);
        return query.getResultList();
    }

	@Transactional(readOnly = true)
    public List<RSETI> findAllWithCourses() {
        TypedQuery<RSETI> query = entityManager.createQuery(
            "SELECT DISTINCT r FROM RSETI r LEFT JOIN FETCH r.rsetiCourses", RSETI.class);
        return query.getResultList();
    }
}