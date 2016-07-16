package com.github.crehn.pantarhei.boundary;

import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.*;

import com.github.crehn.pantarhei.api.Sip;
import com.github.crehn.pantarhei.control.SipFacade;
import com.github.t1.log.LogLevel;
import com.github.t1.log.Logged;

@Logged(level = LogLevel.INFO)
@Path("/sips")
public class SipBoundary {

	@Inject
	private SipFacade facade;

	@GET
	@Path("/{guid}")
	public Sip getSip(@PathParam("guid") UUID guid) {
		return facade.getSip(guid);
	}

	@PUT
	@Path("/{guid}")
	public void putSip(@PathParam("guid") UUID guid, Sip sip) {
		if (!guid.equals(sip.getGuid()))
			throw new IllegalArgumentException("guid in path must match guid in sip");

		facade.storeSip(sip);
	}
}