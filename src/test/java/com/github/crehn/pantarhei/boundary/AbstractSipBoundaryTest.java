package com.github.crehn.pantarhei.boundary;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
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
    protected static final String SUMMARY = "summary";
    protected static final String TEXT = "text";
    protected static final URI SOURCE_URI = URI.create("http://exampole.com/foobar");
    protected static final String TAG1 = "tag1";
    protected static final String TAG2 = "tag2";
    protected static final String TAG3 = "tag3";
    protected static final TagEntity TAG_ENTITY1 = new TagEntity(TAG1);
    protected static final TagEntity TAG_ENTITY2 = new TagEntity(TAG2);
    protected static final Sip SIP = generateSip() //
            .tag(TAG1) //
            .tag(TAG2) //
            .build();

    protected static SipBuilder generateSip() {
        return Sip.builder() //
                .guid(GUID) //
                .title(TITLE) //
                .summary(SUMMARY) //
                .text(TEXT) //
                .sourceUri(SOURCE_URI);
    }

    protected static SipEntity createSipEntity() {
        return SipEntity.builder() //
                .id(NumberGenerator.nextInt()) //
                .guid(GUID) //
                .title(TITLE) //
                .summary(SUMMARY) //
                .text(TEXT) //
                .sourceUri(SOURCE_URI) //
                .tag(TAG_ENTITY1) //
                .tag(TAG_ENTITY2) //
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
        assertEquals(expected.getSummary(), sipEntity.getSummary());
        assertEquals(expected.getText(), sipEntity.getText());
        assertEquals(expected.getSourceUri(), sipEntity.getSourceUri());
    }
}
