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

    public SipEntity getSipByGuid(UUID guid) {
        try {
            TypedQuery<SipEntity> query = entityManager.createNamedQuery(GET_SIP_BY_GUID, SipEntity.class);
            query.setParameter("guid", guid);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new SipNotFoundException(guid, e);
        }
    }

    public void store(SipEntity sip) {
        entityManager.merge(sip);
    }
}
