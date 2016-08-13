package com.github.crehn.pantarhei.boundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.crehn.pantarhei.api.*;
import com.github.crehn.pantarhei.data.SipEntity;
import com.github.crehn.pantarhei.data.SipNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class SipGettingTest extends AbstractSipBoundaryTest {

    @Test(expected = SipNotFoundException.class)
    public void shouldFailGettingUnknownSip() {
        givenUnknownSip();

        boundary.getSip(OTHER_GUID);
    }

    private void givenUnknownSip() {
        when(sipStore.findSipByGuid(OTHER_GUID)).thenReturn(null);
    }

    @Test
    public void shouldGetSip() {
        SipEntity sipEntity = createSipEntity();
        givenSipEntity(sipEntity);

        Sip result = boundary.getSip(GUID);

        assertSip(sipEntity, result);
    }

    private void assertSip(SipEntity sipEntity, Sip result) {
        assertEquals(sipEntity.getGuid(), result.getGuid());
        assertEquals(sipEntity.getTitle(), result.getTitle());
        assertEquals(sipEntity.getSummary(), result.getSummary());
        assertEquals(sipEntity.getText(), result.getText());
        assertEquals(sipEntity.getSourceUri(), result.getSourceUri());
        assertThat(result.getTags()).containsExactly(TAG1, TAG2);
    }

    @Test(expected = MissingQueryException.class)
    public void shouldFailQueryingSipsWithoutQuery() {
        boundary.querySips(null);
    }

    @Test(expected = QuerySyntaxException.class)
    public void shouldFailQueryingSipsWithoutPrefix() {
        boundary.querySips(new Query(TAG1));
        assertEquals("SELECT s FROM SipEntity s " //
                + "WHERE 'tag1' NOT MEMBER OF s.tags", captureJpql());
    }

    @Test
    public void shouldQuerySipsByOneTag() {
        boundary.querySips(new Query("+" + TAG1));

        assertEquals("SELECT s FROM SipEntity s " //
                + "WHERE (SELECT t FROM TagEntity t WHERE t.name = 'tag1') MEMBER OF s.tags", captureJpql());
    }

    @Test
    public void shouldQuerySipsByOneTagWithWhiteSpace() {
        boundary.querySips(new Query("  \t\r\n+" + TAG1 + "\t\n\r     "));

        assertEquals("SELECT s FROM SipEntity s " //
                + "WHERE (SELECT t FROM TagEntity t WHERE t.name = 'tag1') MEMBER OF s.tags", captureJpql());
    }

    @Test
    public void shouldQuerySipsByTwoTags() {
        boundary.querySips(new Query("+" + TAG1 + " +" + TAG2));

        assertEquals("SELECT s FROM SipEntity s " //
                + "WHERE (SELECT t FROM TagEntity t WHERE t.name = 'tag1') MEMBER OF s.tags " //
                + "AND (SELECT t FROM TagEntity t WHERE t.name = 'tag2') MEMBER OF s.tags", captureJpql());
    }

    @Test
    public void shouldQuerySipsByTwoTagsAndAdditionalWhiteSpace() {
        boundary.querySips(new Query("+" + TAG1 + "   \r\n\t   +" + TAG2));

        assertEquals("SELECT s FROM SipEntity s " //
                + "WHERE (SELECT t FROM TagEntity t WHERE t.name = 'tag1') MEMBER OF s.tags " //
                + "AND (SELECT t FROM TagEntity t WHERE t.name = 'tag2') MEMBER OF s.tags", captureJpql());
    }

    @Test
    public void shouldQuerySipsByExcludingTag() {
        boundary.querySips(new Query("-" + TAG1));

        assertEquals(
                "SELECT s FROM SipEntity s " //
                        + "WHERE (SELECT t FROM TagEntity t WHERE t.name = 'tag1') NOT MEMBER OF s.tags",
                captureJpql());
    }
}
