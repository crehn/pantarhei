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
		super(createMessage(guid));
	}

	public SipNotFoundException(UUID guid, Throwable cause) {
		super(createMessage(guid), cause);
	}

	private static String createMessage(UUID guid) {
		return "No sip with guid [" + guid + "] found";
	}
}
