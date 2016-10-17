package com.github.crehn.pantarhei.boundary;

import static org.junit.Assert.*;

import java.net.URI;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.spi.DefaultOptionsMethodException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.crehn.pantarhei.boundary.AnnotatedExceptionMapper.Problem;

@RunWith(MockitoJUnitRunner.class)
public class ExceptionMapperTest {

    @InjectMocks
    AnnotatedExceptionMapper mapper;

    @MapToProblem(status = Status.BAD_REQUEST, title = "title")
    static class AnnotatedException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public AnnotatedException(String msg) {
            super(msg);
        }
    }

    @Test
    public void shouldMapAnnotatedException() {
        Response response = mapper.toResponse(new AnnotatedException("message"));

        assertEquals(400, response.getStatus());
        assertEquals("application/problem+json", response.getMediaType().toString());

        Problem problem = (Problem) response.getEntity();
        assertEquals(
                URI.create("urn:problem:com.github.crehn.pantarhei.boundary.ExceptionMapperTest$AnnotatedException"),
                problem.getType());
        assertEquals("title", problem.getTitle());
        assertEquals(Status.BAD_REQUEST, problem.getStatus());
        assertEquals("message", problem.getDetail());
        assertNotNull(problem.getInstance());
    }

    @Test
    public void shouldMapRuntimeException() {
        Response response = mapper.toResponse(new RuntimeException("message"));

        assertEquals(500, response.getStatus());
        assertEquals("application/problem+json", response.getMediaType().toString());

        Problem problem = (Problem) response.getEntity();
        assertEquals(URI.create("urn:problem:java.lang.RuntimeException"), problem.getType());
        assertEquals("a problem occured", problem.getTitle());
        assertEquals(Status.INTERNAL_SERVER_ERROR, problem.getStatus());
        assertEquals("message", problem.getDetail());
        assertNotNull(problem.getInstance());
    }

    @Test
    public void shouldMapWebException() {
        Response response = mapper.toResponse(new WebApplicationException("message",
                Response //
                        .status(Status.METHOD_NOT_ALLOWED) //
                        .header("Allow", "GET") //
                        .build()));

        assertEquals(405, response.getStatus());
        assertEquals("GET", response.getHeaderString("Allow"));
        assertEquals("application/problem+json", response.getMediaType().toString());

        Problem problem = (Problem) response.getEntity();
        assertEquals(URI.create("urn:problem:javax.ws.rs.WebApplicationException:405"), problem.getType());
        assertEquals("Method Not Allowed", problem.getTitle());
        assertEquals(Status.METHOD_NOT_ALLOWED, problem.getStatus());
        assertEquals("message", problem.getDetail());
        assertNotNull(problem.getInstance());
    }

    @Test
    public void shouldNotMapDefaultOptionsMethodException() {
        Response response = Response.ok().build();
        Response result = mapper.toResponse(new DefaultOptionsMethodException("message", response));

        assertTrue(response == result);
    }
}
