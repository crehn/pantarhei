package com.github.crehn.pantarhei.data;

import java.util.UUID;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response.Status;

import com.github.crehn.pantarhei.boundary.MapToProblem;

@ApplicationException
@MapToProblem(status = Status.NOT_FOUND, title = "Sip not found")
public class SipNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SipNotFoundException(UUID guid) {
		super("No sip with guid [" + guid + "] found");
	}
}
