package com.github.crehn.pantarhei.boundary;

import java.net.URI;
import java.util.UUID;

import org.mockito.*;

import com.github.crehn.pantarhei.api.Sip;
import com.github.crehn.pantarhei.api.Sip.SipBuilder;
import com.github.crehn.pantarhei.control.SipFacade;
import com.github.crehn.pantarhei.data.*;

public class AbstractSipBoundaryTest {
    protected static final UUID GUID = UUID.randomUUID();
    protected static final UUID OTHER_GUID = UUID.randomUUID();
    private static final String TITLE = "title";
    private static final String SUMMARY = "summary";
    private static final String TEXT = "text";
    private static final URI SOURCE_URI = URI.create("http://exampole.com/foobar");
    protected static final String TAG1 = "tag1";
    protected static final String TAG2 = "tag2";
    protected static final TagEntity TAG_ENTITY1 = new TagEntity(TAG1);
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

    protected static final SipEntity SIP_ENTITY = SipEntity.builder() //
            .guid(GUID) //
            .title(TITLE) //
            .summary(SUMMARY) //
            .text(TEXT) //
            .sourceUri(SOURCE_URI) //
            .tag(TAG_ENTITY1) //
            .tag(new TagEntity(TAG2)) //
            .build();

    @InjectMocks
    protected SipBoundary boundary;

    @Spy
    @InjectMocks
    SipFacade facade = new SipFacade();

    @Mock
    protected SipStore sipStore;
    @Mock
    protected TagStore tagStore;
}
