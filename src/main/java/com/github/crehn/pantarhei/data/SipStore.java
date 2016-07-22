package com.github.crehn.pantarhei.data;

import static com.github.crehn.pantarhei.data.SipEntity.GET_SIP_BY_GUID;
import static com.github.t1.log.LogLevel.INFO;

import java.util.UUID;

import javax.persistence.*;

import com.github.t1.log.Logged;

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
}
