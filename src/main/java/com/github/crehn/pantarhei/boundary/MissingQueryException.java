package com.github.crehn.pantarhei.boundary;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response.Status;

@ApplicationException
@MapToProblem(status = Status.BAD_REQUEST, title = "Query expected. Specity query parameter q.")
public class MissingQueryException extends RuntimeException {
    private static final long serialVersionUID = 1L;

}
