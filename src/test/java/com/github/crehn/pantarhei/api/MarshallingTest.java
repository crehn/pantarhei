package com.github.crehn.pantarhei.api;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.crehn.pantarhei.boundary.ObjectMapperContextResolver;

@RunWith(MockitoJUnitRunner.class)
public class MarshallingTest {

    private static ObjectMapperContextResolver resolver = new ObjectMapperContextResolver();
    private ObjectMapper json;

    @Before
    public void setup() {
        json = resolver.getContext(ObjectMapper.class);
    }

    @Test
    public void shouldMarshalMinimalSip() throws Exception {
        Sip sip = Sip.builder() //
                .guid(UUID.fromString("9ebb1f74-f5d3-4952-92d9-6a631358b8a0")) //
                .title("title") //
                .build();

        String result = json.writeValueAsString(sip);
        System.out.println(result);

        assertEquals(("{" //
                + "'guid':'9ebb1f74-f5d3-4952-92d9-6a631358b8a0'," //
                + "'title':'title'" //
                + "}").replace('\'', '"'), result);
    }

    @Test
    public void shouldMarshalFullSip() throws Exception {
        Sip sip = Sip.builder() //
                .guid(UUID.fromString("9ebb1f74-f5d3-4952-92d9-6a631358b8a0")) //
                .title("title") //
                .notes("notes") //
                .text("text") //
                .sourceUri(URI.create("http://example.com/foobar")) //
                .tag("foo") //
                .tag("bar") //
                .status("done") //
                .originTimestamp(Instant.parse("2016-08-15T20:21:53.701Z")) //
                .created(Instant.parse("2016-08-15T20:21:53.702Z")) //
                .modified(Instant.parse("2016-08-15T20:21:53.703Z")) //
                .due(Instant.parse("2016-08-15T20:21:53.704Z")) //
                .build();

        String result = json.writeValueAsString(sip);
        System.out.println(result);

        assertEquals(("{" //
                + "'guid':'9ebb1f74-f5d3-4952-92d9-6a631358b8a0'," //
                + "'title':'title'," //
                + "'notes':'notes'," //
                + "'text':'text'," //
                + "'sourceUri':'http://example.com/foobar'," //
                + "'tags':['foo','bar']," //
                + "'status':'done'," //
                + "'originTimestamp':'2016-08-15T20:21:53.701Z'," //
                + "'created':'2016-08-15T20:21:53.702Z'," //
                + "'modified':'2016-08-15T20:21:53.703Z'," //
                + "'due':'2016-08-15T20:21:53.704Z'" //
                + "}").replace('\'', '"'), result);
    }
}
