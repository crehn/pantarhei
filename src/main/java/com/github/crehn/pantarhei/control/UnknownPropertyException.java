package com.github.crehn.pantarhei.control;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response.Status;

import com.github.crehn.pantarhei.boundary.MapToProblem;

@ApplicationException
@MapToProblem(status = Status.BAD_REQUEST, title = "Unknown property")
public class UnknownPropertyException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnknownPropertyException(String msg) {
        super(msg);
    }
}
