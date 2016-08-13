package com.github.crehn.pantarhei.boundary;

import static com.github.t1.log.LogLevel.INFO;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.transaction.Transactional;
import javax.ws.rs.*;

import com.github.crehn.pantarhei.api.Query;
import com.github.crehn.pantarhei.api.Sip;
import com.github.crehn.pantarhei.control.SipFacade;
import com.github.crehn.pantarhei.control.UnknownPropertyException;
import com.github.t1.log.Logged;

@Path("/sips")
@Transactional
@Logged(level = INFO)
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
        if (sip.getGuid() == null)
            sip.setGuid(guid);
        if (!guid.equals(sip.getGuid()))
            throw new IllegalArgumentException(
                    "guid in path must match guid in sip. You don't need to specify it in the sip anyway.");

        facade.putSip(sip);
    }

    @PATCH
    @Path("/{guid}")
    public void patchSip(@PathParam("guid") UUID guid, JsonObject patch) {
        if (patch.containsKey("guid"))
            throw new UnknownPropertyException("guid may not be changed");

        facade.patchSip(guid, patch);
    }

    @DELETE
    @Path("/{guid}")
    public void deleteSip(@PathParam("guid") UUID guid) {
        facade.deleteSip(guid);
    }


    @GET
    public List<Sip> querySips(@QueryParam("q") Query query) {
        if (query == null)
            throw new MissingQueryException();

        return facade.querySips(query);
    }
}