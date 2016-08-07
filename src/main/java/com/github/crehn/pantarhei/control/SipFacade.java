package com.github.crehn.pantarhei.control;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.util.*;

import javax.inject.Inject;

import com.github.crehn.pantarhei.api.Query;
import com.github.crehn.pantarhei.api.Sip;
import com.github.crehn.pantarhei.data.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SipFacade {

    @Inject
    private SipStore sipStore;
    @Inject
    private TagStore tagStore;

    public Sip getSip(UUID guid) {
        SipEntity sipEntity = sipStore.findSipByGuid(guid);
        if (sipEntity == null)
            throw new SipNotFoundException(guid);
        return sipEntity.toApi();
    }

    public void storeSip(Sip sip) {
        SipEntity sipFromDb = sipStore.findSipByGuid(sip.getGuid());
        if (sipFromDb == null) {
            sipStore.insert(toEntity(sip));
        } else {
            replaceData(sipFromDb, sip);
        }
    }

    private void replaceData(SipEntity entity, Sip sip) {
        entity.setTitle(sip.getTitle());
        entity.setSummary(sip.getSummary());
        entity.setText(sip.getText());
        entity.setSourceUri(sip.getSourceUri());
        entity.setTags(getTagEntities(sip.getTags()));
    }

    private SipEntity toEntity(Sip sip) {
        return SipEntity.builder() //
                .guid(sip.getGuid()) //
                .title(sip.getTitle()) //
                .summary(sip.getSummary()) //
                .text(sip.getText()) //
                .sourceUri(sip.getSourceUri()) //
                .tags(getTagEntities(sip.getTags())) //
                .build();
    }

    private List<TagEntity> getTagEntities(List<String> tags) {
        if (tags == null)
            return emptyList();

        List<TagEntity> tagsInDb = tagStore.findTagsByNames(tags);
        List<String> alreadyFoundEntities = tagsInDb.stream() //
                .map(TagEntity::getName) //
                .collect(toList());
        log.debug("found tags in db: {}", alreadyFoundEntities);

        List<TagEntity> newlyCreatedTags = tags.stream() //
                .filter(tag -> !alreadyFoundEntities.contains(tag)) //
                .map(TagEntity::new) //
                .collect(toList());
        log.debug("create new tags: {}", newlyCreatedTags);

        List<TagEntity> result = new ArrayList<>();
        result.addAll(tagsInDb);
        result.addAll(newlyCreatedTags);
        return result;
    }

    public List<Sip> querySips(Query query) {
        return sipStore.findSipsByJpql(query.toJpql());
    }
}
