package com.github.crehn.pantarhei.boundary;

import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.github.crehn.pantarhei.api.Sip;
import com.github.crehn.pantarhei.control.SipFacade;

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