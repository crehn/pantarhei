package com.github.crehn.pantarhei.boundary;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.crehn.pantarhei.api.Sip;
import com.github.crehn.pantarhei.data.SipEntity;
import com.github.crehn.pantarhei.data.TagEntity;

@RunWith(MockitoJUnitRunner.class)
public class SipUpdateTest extends AbstractSipBoundaryTest {

    @Test
    public void shouldReplaceSipInDb() {
        SipEntity sipEntity = createSipEntity();
        givenSipEntity(sipEntity);

        Sip sip = Sip.builder() //
                .guid(GUID) //
                .title(TITLE + "2") //
                .summary(SUMMARY + "2") //
                .text(TEXT + "2") //
                .sourceUri(URI.create(SOURCE_URI.toString() + "2")) //
                .build();
        boundary.putSip(GUID, sip);

        assertSipEntity(sip, sipEntity);
    }

    @Test
    public void shouldNotDeleteGuidWhenReplacingSipInDb() {
        SipEntity sipEntity = createSipEntity();
        givenSipEntity(sipEntity);

        Sip sip = Sip.builder() //
                .guid(null) //
                .title(TITLE + "2") //
                .build();
        boundary.putSip(GUID, sip);

        assertEquals(GUID, sipEntity.getGuid());
        assertEquals(TITLE + "2", sipEntity.getTitle());
        assertEquals(null, sipEntity.getSourceUri());
        assertEquals(null, sipEntity.getSummary());
        assertEquals(emptyList(), sipEntity.getTags());
        assertEquals(null, sipEntity.getText());
    }

    @Test
    public void shouldReplaceSipTagInDb() {
        SipEntity sipEntity = createSipEntity();
        givenSipEntity(sipEntity);

        Sip sip = SIP.withTags(asList(TAG3, TAG2));
        boundary.putSip(GUID, sip);

        assertThat(sipEntity.getTags()) //
                .extracting(TagEntity::getName) //
                .containsExactlyInAnyOrder(TAG2, TAG3);
    }
}
