package com.github.crehn.pantarhei.boundary;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response.Status;

@MapToProblem(status = Status.BAD_REQUEST, title = "Patch not allowed")
@ApplicationException
public class PatchNotAllowedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PatchNotAllowedException(String message) {
        super(message);
    }
}
