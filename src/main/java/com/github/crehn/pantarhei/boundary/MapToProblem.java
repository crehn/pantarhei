package com.github.crehn.pantarhei.boundary;

import java.lang.annotation.*;

import javax.ws.rs.core.Response.Status;

/**
 * @see <a href="https://tools.ietf.org/html/draft-ietf-appsawg-http-problem-01">IETF: Problem Details for HTTP APIs</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MapToProblem {
	/**
	 * HTTP status code to map to
	 */
	Status status();

	/**
	 * Human readable description of the problem type; SHOULD NOT change between invocations.
	 */
	String title();
}
