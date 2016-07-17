package com.github.crehn.pantarhei.api;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class MarshallingTest {

    private ObjectMapper json = new ObjectMapper();

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
                .summary("summary") //
                .text("text") //
                .sourceUri(URI.create("http://example.com/foobar")) //
                .tag("foo") //
                .tag("bar") //
                .build();

        String result = json.writeValueAsString(sip);
        System.out.println(result);

        assertEquals(("{" //
                + "'guid':'9ebb1f74-f5d3-4952-92d9-6a631358b8a0'," //
                + "'title':'title'," //
                + "'summary':'summary'," //
                + "'text':'text'," //
                + "'sourceUri':'http://example.com/foobar'," //
                + "'tags':['foo','bar']" //
                + "}").replace('\'', '"'), result);
    }
}
