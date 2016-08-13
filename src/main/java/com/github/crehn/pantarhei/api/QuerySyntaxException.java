package com.github.crehn.pantarhei.api;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response.Status;

import com.github.crehn.pantarhei.boundary.MapToProblem;

@ApplicationException
@MapToProblem(status = Status.BAD_REQUEST, title = "syntax error in query")
public class QuerySyntaxException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public QuerySyntaxException(String msg) {
        super(msg);
    }
}
