package org.usth.ict.ulake.ingest.resources;

import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usth.ict.ulake.common.model.LakeHttpResponse;
import org.usth.ict.ulake.ingest.model.Policy;
import org.usth.ict.ulake.ingest.model.CrawlRequest;
import org.usth.ict.ulake.ingest.persistence.CrawlRequestRepo;
import org.usth.ict.ulake.ingest.services.CrawlJob;
import org.usth.ict.ulake.ingest.services.CrawlTask;

@Path("/ingest")
@Produces(MediaType.APPLICATION_JSON)
public class CrawlResource {
    private static final Logger log = LoggerFactory.getLogger(CrawlResource.class);

    @Inject
    CrawlTask task;

    @Inject
    CrawlRequestRepo repo;

    @Inject
    JsonWebToken jwt;

    @Inject
    LakeHttpResponse<Object> resp;

    @POST
    @Path("/crawl")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"User", "Admin"})
    @Operation(summary = "run crawl process")
    public Response crawl(
        @HeaderParam("Authorization") String token,
        @Parameter(description = "folder to store crawled files")
        @QueryParam("folderId") Long folderId,
        @Parameter(description = "brief-description of crawl process")
        @QueryParam("desc") String desc,
        @RequestBody(description = "instruction of crawl") Policy policy) {

        var processLog = new CrawlRequest();
        processLog.ownerId = Long.parseLong(jwt.getName());
        processLog.query = policy;
        processLog.folderId = folderId;
        processLog.description = desc;
        processLog.creationTime = new Date().getTime();
        createProcess(processLog);

        try {
            task.start(token, processLog.id, CrawlJob.class);
            return resp.build(200, "", processLog.id);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return resp.build(500, "Could not start task", e);
        }
    }

    @POST
    @Path("/fetch")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"User", "Admin"})
    @Operation(summary = "run fetch process")
    @Transactional
    public Response fetch(
        @RequestBody(description = "instruction of crawl") Policy policy) {
        return resp.build(200, "", task.runFetch(policy));
    }

    @Transactional
    public void createProcess(CrawlRequest log) {
        repo.persist(log);
    }
}
