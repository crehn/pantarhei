package com.github.crehn.pantarhei.boundary;

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.*;
import java.util.UUID;

import org.mockito.*;

import com.github.crehn.pantarhei.api.Sip;
import com.github.crehn.pantarhei.api.Sip.SipBuilder;
import com.github.crehn.pantarhei.control.SipFacade;
import com.github.crehn.pantarhei.data.*;
import com.github.crehn.pantarhei.tools.NumberGenerator;

public class AbstractSipBoundaryTest {
    protected static final UUID GUID = UUID.randomUUID();
    protected static final UUID OTHER_GUID = UUID.randomUUID();
    protected static final String TITLE = "title";
    protected static final String NOTES = "notes";
    protected static final String TEXT = "text";
    protected static final URI SOURCE_URI = URI.create("http://example.com/foobar");
    protected static final String TAG1 = "tag1";
    protected static final String TAG2 = "tag2";
    protected static final String TAG3 = "tag3";
    protected static final TagEntity TAG_ENTITY1 = new TagEntity(TAG1);
    protected static final TagEntity TAG_ENTITY2 = new TagEntity(TAG2);
    protected static final String STATUS = "done";
    protected static final Instant ORIGIN_TIMESTAMP = Instant.now().minus(Period.ofDays(1));
    protected static final Instant CREATED = ORIGIN_TIMESTAMP.plusSeconds(1);
    protected static final Instant MODIFIED = ORIGIN_TIMESTAMP.plusSeconds(2);
    protected static final Instant DUE = ORIGIN_TIMESTAMP.plusSeconds(3);
    protected static final Sip SIP = generateSip() //
            .tag(TAG1) //
            .tag(TAG2) //
            .build();
    protected static final Duration ONE_SECOND = Duration.ofSeconds(1);

    protected static SipBuilder generateSip() {
        return Sip.builder() //
                .guid(GUID) //
                .title(TITLE) //
                .notes(NOTES) //
                .text(TEXT) //
                .status(STATUS) //
                .originTimestamp(ORIGIN_TIMESTAMP) //
                .created(CREATED) //
                .modified(MODIFIED) //
                .due(DUE) //
                .sourceUri(SOURCE_URI);
    }

    protected static SipEntity createSipEntity() {
        return SipEntity.builder() //
                .id(NumberGenerator.nextInt()) //
                .guid(GUID) //
                .title(TITLE) //
                .notes(NOTES) //
                .text(TEXT) //
                .sourceUri(SOURCE_URI) //
                .tag(TAG_ENTITY1) //
                .tag(TAG_ENTITY2) //
                .status(STATUS) //
                .originTimestamp(ORIGIN_TIMESTAMP) //
                .created(CREATED) //
                .modified(MODIFIED) //
                .due(DUE) //
                .build();
    }

    @InjectMocks
    protected SipBoundary boundary;

    @Spy
    @InjectMocks
    SipFacade facade = new SipFacade();

    @Mock
    protected SipStore sipStore;
    @Mock
    protected TagStore tagStore;
    @Captor
    protected ArgumentCaptor<String> jpqlCaptor;

    protected void givenSipEntity(SipEntity sipEntity) {
        when(sipStore.findSipByGuid(GUID)).thenReturn(sipEntity);
    }

    protected String captureJpql() {
        verify(sipStore).findSipsByJpql(jpqlCaptor.capture());
        return jpqlCaptor.getValue();
    }

    protected void assertSipEntity(Sip expected, SipEntity sipEntity) {
        assertEquals(expected.getGuid(), sipEntity.getGuid());
        assertEquals(expected.getTitle(), sipEntity.getTitle());
        assertEquals(expected.getNotes(), sipEntity.getNotes());
        assertEquals(expected.getText(), sipEntity.getText());
        assertEquals(expected.getSourceUri(), sipEntity.getSourceUri());
        assertEquals(expected.getStatus(), sipEntity.getStatus());
        assertEquals(expected.getOriginTimestamp(), sipEntity.getOriginTimestamp());
        assertEquals(expected.getCreated(), sipEntity.getCreated());
        assertEquals(expected.getDue(), sipEntity.getDue());
    }

    protected void assertModificationTimestampSet(SipEntity sipEntity) {
        assertThat(Duration.between(sipEntity.getModified(), now())).isLessThan(ONE_SECOND);
    }
}
