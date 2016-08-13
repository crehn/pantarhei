package com.github.crehn.pantarhei.control;

import static com.github.crehn.listquery.Just.map;
import static com.github.crehn.listquery.ListQuery.from;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

import javax.inject.Inject;
import javax.json.*;
import javax.json.JsonValue.ValueType;

import com.github.crehn.pantarhei.api.Query;
import com.github.crehn.pantarhei.api.Sip;
import com.github.crehn.pantarhei.data.*;

import lombok.SneakyThrows;
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

    public void putSip(Sip sip) {
        SipEntity sipFromDb = sipStore.findSipByGuid(sip.getGuid());
        if (sipFromDb == null) {
            sipStore.insert(toEntity(sip));
        } else {
            replaceData(sipFromDb, sip);
        }
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

    private void replaceData(SipEntity entity, Sip sip) {
        entity.setTitle(sip.getTitle());
        entity.setSummary(sip.getSummary());
        entity.setText(sip.getText());
        entity.setSourceUri(sip.getSourceUri());
        entity.setTags(getTagEntities(sip.getTags()));
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
        List<SipEntity> sipEntities = sipStore.findSipsByJpql(query.toJpql());
        return map(sipEntities, SipEntity::toApi);
    }

    public void deleteSip(UUID guid) {
        sipStore.deleteSip(guid);
    }

    public void patchSip(UUID guid, JsonObject patch) {
        SipEntity sipEntity = sipStore.findSipByGuid(guid);
        if (sipEntity == null)
            throw new SipNotFoundException(guid);
        Sip sip = sipEntity.toApi();

        patch.forEach((property, value) -> {
            log.debug("patching property [{}] to new value [{}] in [{}]", property, value, sip);
            invoke(getSetterFor(property), sip, value);
        });
        replaceData(sipEntity, sip);
    }

    private Method getSetterFor(String key) {
        String setterName = "set" + Character.toUpperCase(key.charAt(0)) + key.substring(1);
        return from(Sip.class.getDeclaredMethods()) //
                .where(m -> m.getName().equals(setterName)) //
                .selectFirst() //
                .orElseThrow(() -> new UnknownPropertyException("Unknown property " + key));
    }

    @SneakyThrows
    private void invoke(Method setter, Sip sip, JsonValue value) {
        if (JsonValue.NULL.equals(value))
            setter.invoke(sip, new Object[] { null });
        else if (value.getValueType() == ValueType.ARRAY) {
            List<JsonString> JsonStrings = ((JsonArray) value).getValuesAs(JsonString.class);
            setter.invoke(sip, map(JsonStrings, JsonString::getString));
        } else {
            setter.invoke(sip, createNewValue(value, setter.getParameterTypes()[0]));
        }
    }

    @SneakyThrows
    private Object createNewValue(JsonValue value, Class<?> type) {
        Constructor<?> constructor = type.getConstructor(String.class);
        return constructor.newInstance(((JsonString) value).getString());
    }
}
