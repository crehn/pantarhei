package com.github.crehn.pantarhei.data;

import static com.github.crehn.pantarhei.data.SipEntity.DELETE_SIP_BY_GUID;
import static com.github.crehn.pantarhei.data.SipEntity.GET_SIP_BY_GUID;
import static com.github.t1.log.LogLevel.INFO;

import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import com.github.t1.log.Logged;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Logged(level = INFO)
public class SipStore {
    @PersistenceContext
    private EntityManager entityManager;

    public SipEntity findSipByGuid(UUID guid) {
        try {
            TypedQuery<SipEntity> query = entityManager.createNamedQuery(GET_SIP_BY_GUID, SipEntity.class);
            query.setParameter("guid", guid);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void insert(SipEntity sip) {
        entityManager.persist(sip);
    }

    public List<SipEntity> findSipsByJpql(String jpql) {
        TypedQuery<SipEntity> query = entityManager.createQuery(jpql, SipEntity.class);
        return query.getResultList();
    }

    public void deleteSip(UUID guid) {
        Query query = entityManager.createNamedQuery(DELETE_SIP_BY_GUID);
        query.setParameter("guid", guid);
        int numberOfDeletedRows = query.executeUpdate();
        log.info("{} sip(s) deleted", numberOfDeletedRows);
    }
}
