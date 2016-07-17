package com.github.crehn.pantarhei.boundary;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.crehn.pantarhei.data.SipEntity;
import com.github.crehn.pantarhei.data.TagEntity;

@RunWith(MockitoJUnitRunner.class)
public class SipCreationTest extends AbstractSipBoundaryTest {
    @Captor
    ArgumentCaptor<SipEntity> sipEntityCaptor;

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailCreatingSipForWrongLocation() throws Exception {
        boundary.putSip(OTHER_GUID, SIP);
    }

    @Test
    public void shouldStoreNewSipWithoutTags() throws Exception {
        boundary.putSip(GUID, generateSip().build());

        verify(sipStore).store(sipEntityCaptor.capture());
        SipEntity sipEntity = sipEntityCaptor.getValue();

        assertSipEntity(sipEntity);
        assertContainsNoTags(sipEntity);
    }

    private void assertContainsNoTags(SipEntity sipEntity) {
        assertTrue(sipEntity.getTags().isEmpty());
    }

    @Test
    public void shouldStoreNewSipWithoutTagsInDb() throws Exception {
        boundary.putSip(GUID, SIP);

        verify(sipStore).store(sipEntityCaptor.capture());
        SipEntity sipEntity = sipEntityCaptor.getValue();

        assertSipEntity(sipEntity);
        assertContainsBothTags(sipEntity);
    }

    @Test
    public void shouldStoreNewSipWithTagInDb() throws Exception {
        givenTagEntity();

        boundary.putSip(GUID, SIP);

        verify(sipStore).store(sipEntityCaptor.capture());
        SipEntity sipEntity = sipEntityCaptor.getValue();

        assertSipEntity(sipEntity);
        assertContainsBothTags(sipEntity);
        assertContainsTagFromDb(sipEntity);
    }

    private void givenTagEntity() {
        when(tagStore.findTagsByNames(asList(TAG1, TAG2))).thenReturn(asList(TAG_ENTITY1));
    }

    private void assertSipEntity(SipEntity sipEntity) {
        assertEquals(SIP.getGuid(), sipEntity.getGuid());
        assertEquals(SIP.getTitle(), sipEntity.getTitle());
        assertEquals(SIP.getSummary(), sipEntity.getSummary());
        assertEquals(SIP.getText(), sipEntity.getText());
        assertEquals(SIP.getSourceUri(), sipEntity.getSourceUri());
    }

    private void assertContainsBothTags(SipEntity sipEntity) {
        assertThat(sipEntity.getTags()) //
                .extracting(TagEntity::getName) //
                .containsExactly(TAG1, TAG2);
    }

    private void assertContainsTagFromDb(SipEntity sipEntity) {
        assertThat(sipEntity.getTags()) //
                .contains(TAG_ENTITY1);
    }
}
