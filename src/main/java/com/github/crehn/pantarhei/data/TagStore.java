package com.github.crehn.pantarhei.data;

import static com.github.crehn.pantarhei.data.TagEntity.FIND_TAGS_BY_NAMES;
import static com.github.t1.log.LogLevel.INFO;

import java.util.List;

import javax.persistence.*;

import com.github.t1.log.Logged;

@Logged(level = INFO)
public class TagStore {

    @PersistenceContext
    private EntityManager entityManager;

    public List<TagEntity> findTagsByNames(List<String> tags) {
        TypedQuery<TagEntity> query = entityManager.createNamedQuery(FIND_TAGS_BY_NAMES, TagEntity.class);
        query.setParameter("names", tags);
        return query.getResultList();
    }

}
