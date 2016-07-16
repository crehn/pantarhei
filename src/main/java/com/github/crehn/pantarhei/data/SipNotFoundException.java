package com.github.crehn.pantarhei.data;

import java.util.UUID;

import javax.ejb.ApplicationException;

@ApplicationException
public class SipNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SipNotFoundException(UUID guid) {
		super("No sip with guid [" + guid + "] found");
	}
}
