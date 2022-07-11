package org.usth.ict.ulake.admin.resource;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.usth.ict.ulake.common.model.LakeHttpResponse;


@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
public class AdminResource {
    @Inject
    LakeHttpResponse response;

    @GET
    @Path("/stats")
    @RolesAllowed({"User", "Admin"})
    @Operation(summary = "Nothing yet.")
    public Response stats() {
        // get requests from other service
        HashMap<String, Integer> ret = new HashMap<>();

        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        long uptime = bean.getUptime();
        ret.put("uptime", (int) uptime);

        return response.build(200, "", ret);
    }

}
