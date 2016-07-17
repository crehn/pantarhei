package com.github.crehn.pantarhei.boundary;

import java.net.URI;
import java.util.UUID;

import org.mockito.*;

import com.github.crehn.pantarhei.api.Sip;
import com.github.crehn.pantarhei.control.SipFacade;
import com.github.crehn.pantarhei.data.SipEntity;
import com.github.crehn.pantarhei.data.SipStore;

public class AbstractSipBoundaryTest {
    protected static final UUID GUID = UUID.randomUUID();
    protected static final UUID OTHER_GUID = UUID.randomUUID();
    private static final String TITLE = "title";
    private static final String SUMMARY = "summary";
    private static final String TEXT = "text";
    private static final URI SOURCE_URI = URI.create("http://exampole.com/foobar");
    protected static final Sip SIP = Sip.builder() //
            .guid(GUID) //
            .title(TITLE) //
            .summary(SUMMARY) //
            .text(TEXT) //
            .sourceUri(SOURCE_URI) //
            .build();
    protected static final SipEntity SIP_ENTITY = SipEntity.builder() //
            .guid(GUID) //
            .title(TITLE) //
            .summary(SUMMARY) //
            .text(TEXT) //
            .sourceUri(SOURCE_URI) //
            .build();

    @InjectMocks
    protected SipBoundary boundary;

    @Spy
    @InjectMocks
    SipFacade facade = new SipFacade();

    @Mock
    protected SipStore store;
}
