package com.github.crehn.pantarhei.boundary;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.crehn.pantarhei.api.Sip;
import com.github.crehn.pantarhei.data.SipEntity;
import com.github.crehn.pantarhei.data.TagEntity;

@RunWith(MockitoJUnitRunner.class)
public class SipCreationTest extends AbstractSipBoundaryTest {
    @Captor
    ArgumentCaptor<SipEntity> sipEntityCaptor;
    @Captor
    ArgumentCaptor<TagEntity> tagEntityCaptor;

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailCreatingSipForWrongLocation() {
        boundary.putSip(OTHER_GUID, SIP);
    }

    @Test
    public void shouldStoreNewSipWithoutGuid() {
        boundary.putSip(GUID, SIP.withGuid(null));

        SipEntity sipEntity = captureSipEntity();
        assertNewSipEntity(sipEntity);
    }

    @Test
    public void shouldStoreNewSipWithoutTags() {
        boundary.putSip(GUID, SIP.withTags(null));

        SipEntity sipEntity = captureSipEntity();
        assertNewSipEntity(sipEntity);
        assertContainsNoTags(sipEntity);
    }

    private SipEntity captureSipEntity() {
        verify(sipStore).insert(sipEntityCaptor.capture());
        SipEntity sipEntity = sipEntityCaptor.getValue();
        return sipEntity;
    }

    private void assertNewSipEntity(SipEntity sipEntity) {
        assertEquals(null, sipEntity.getId());
        assertSipEntity(SIP, sipEntity);
    }

    private void assertSipEntity(Sip expected, SipEntity sipEntity) {
        assertEquals(expected.getGuid(), sipEntity.getGuid());
        assertEquals(expected.getTitle(), sipEntity.getTitle());
        assertEquals(expected.getSummary(), sipEntity.getSummary());
        assertEquals(expected.getText(), sipEntity.getText());
        assertEquals(expected.getSourceUri(), sipEntity.getSourceUri());
    }

    private void assertContainsNoTags(SipEntity sipEntity) {
        assertTrue(sipEntity.getTags().isEmpty());
    }

    @Test
    public void shouldStoreNewSipWithoutTagsInDb() {
        boundary.putSip(GUID, SIP);

        SipEntity sipEntity = captureSipEntity();
        assertNewSipEntity(sipEntity);
        assertContainsBothTags(sipEntity);
    }

    @Test
    public void shouldStoreNewSipWithOneTagInDb() {
        givenTagEntity();

        boundary.putSip(GUID, SIP);

        SipEntity sipEntity = captureSipEntity();
        assertNewSipEntity(sipEntity);
        assertContainsBothTags(sipEntity);
        assertContainsTagFromDb(sipEntity);
    }

    private void givenTagEntity() {
        when(tagStore.findTagsByNames(asList(TAG1, TAG2))).thenReturn(asList(TAG_ENTITY1));
    }

    private void assertContainsBothTags(SipEntity sipEntity) {
        assertThat(sipEntity.getTags()) //
                .extracting(TagEntity::getName) //
                .containsExactlyInAnyOrder(TAG1, TAG2);
    }

    private void assertContainsTagFromDb(SipEntity sipEntity) {
        assertThat(sipEntity.getTags()) //
                .contains(TAG_ENTITY1);
    }

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
