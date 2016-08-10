package com.github.crehn.pantarhei.api;

import javax.ejb.ApplicationException;

@ApplicationException
public class QuerySyntaxException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public QuerySyntaxException(String msg) {
        super(msg);
    }
}
