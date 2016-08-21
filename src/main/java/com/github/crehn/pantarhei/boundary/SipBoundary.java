package com.github.crehn.pantarhei.boundary;

import static com.github.t1.log.LogLevel.INFO;
import static java.time.Instant.now;

import java.io.StringReader;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.json.*;
import javax.transaction.Transactional;
import javax.ws.rs.*;

import com.github.crehn.pantarhei.api.Query;
import com.github.crehn.pantarhei.api.Sip;
import com.github.crehn.pantarhei.control.SipFacade;
import com.github.t1.log.Logged;

@Path("/sips")
@Transactional
@Logged(level = INFO)
public class SipBoundary {
    private static final String GUID_PARAM = "guid";
    private static final String SINGLE_SIP = "/{" + GUID_PARAM + "}";

    @Inject
    private SipFacade facade;

    @GET
    @Path(SINGLE_SIP)
    public Sip getSip(@PathParam(GUID_PARAM) UUID guid) {
        return facade.getSip(guid);
    }

    @PUT
    @Path(SINGLE_SIP)
    public void putSip(@PathParam(GUID_PARAM) UUID guid, Sip sip) {
        setGuidIfMissing(guid, sip);
        setTimestampsIfMissing(sip);
        setStatusIfMissing(sip);

        facade.putSip(sip);
    }

    private void setGuidIfMissing(UUID guid, Sip sip) {
        if (sip.getGuid() == null)
            sip.setGuid(guid);
        if (!guid.equals(sip.getGuid()))
            throw new IllegalArgumentException(
                    "guid in path must match guid in sip. You don't need to specify it in the sip anyway.");
    }

    private void setTimestampsIfMissing(Sip sip) {
        if (sip.getCreated() == null)
            sip.setCreated(now());
        if (sip.getModified() == null)
            sip.setModified(now());
    }

    private void setStatusIfMissing(Sip sip) {
        if (sip.getStatus() == null)
            sip.setStatus("open");
    }

    @PATCH
    @Path(SINGLE_SIP)
    public void patchSip(@PathParam(GUID_PARAM) UUID guid, String patchString) {
        JsonObject patch = toJsonObject(patchString);
        if (patch.containsKey("guid"))
            throw new PatchNotAllowedException("guid may not be changed");
        if (patch.containsKey("status") && patch.isNull("status"))
            throw new PatchNotAllowedException("status may not be set to null");

        facade.patchSip(guid, patch);
    }

    private JsonObject toJsonObject(String string) {
        try (StringReader sReader = new StringReader(string); //
                JsonReader reader = Json.createReader(sReader)) {
            return reader.readObject();
        }
    }

    @DELETE
    @Path(SINGLE_SIP)
    public void deleteSip(@PathParam(GUID_PARAM) UUID guid) {
        facade.deleteSip(guid);
    }


    @GET
    public List<Sip> querySips(@QueryParam("q") Query query) {
        if (query == null)
            throw new MissingQueryException();

        return facade.querySips(query);
    }
}