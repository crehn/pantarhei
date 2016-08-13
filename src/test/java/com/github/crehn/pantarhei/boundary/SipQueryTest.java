package com.github.crehn.pantarhei.boundary;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.crehn.pantarhei.api.Query;
import com.github.crehn.pantarhei.api.QuerySyntaxException;

@RunWith(MockitoJUnitRunner.class)
public class SipQueryTest extends AbstractSipBoundaryTest {

    @Test(expected = MissingQueryException.class)
    public void shouldFailQueryingSipsWithoutQuery() {
        boundary.querySips(null);
    }

    @Test(expected = MissingQueryException.class)
    public void shouldFailQueryingSipsWithEmptyQuery() {
        boundary.querySips(new Query(""));
    }

    @Test(expected = MissingQueryException.class)
    public void shouldFailQueryingSipsWithEmptyQuery2() {
        boundary.querySips(new Query("    \t\t\r\n   "));
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
