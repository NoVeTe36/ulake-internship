package org.usth.ict.ulake.user.resource;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usth.ict.ulake.common.misc.Utils;
import org.usth.ict.ulake.common.model.LakeHttpResponse;
import org.usth.ict.ulake.user.model.Institution;
import org.usth.ict.ulake.user.persistence.InstitutionRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/institution")
@Tag(name = "Institutions")
@Produces(MediaType.APPLICATION_JSON)
public class InstitutionResource {
    private static final Logger log = LoggerFactory.getLogger(InstitutionResource.class);

    @Inject
    LakeHttpResponse response;

    @Inject
    InstitutionRepository repo;

    @GET
    @Operation(summary = "List all institutions")
    public Response all() {
        return response.build(200, "", repo.listAll());
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get one institution info")
    public Response one(@PathParam("id") @Parameter(description = "Institution id to search") Long id) {
        return response.build(200, null, repo.findById(id));
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new institution")
    public Response post(@RequestBody(description = "New institution info to save") Institution entity) {
        repo.persist(entity);
        return response.build(200, "", entity);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update an existing institution")
    public Response update(@PathParam("id") @Parameter(description = "Institution id to update") Long id,
                           @RequestBody(description = "New institution info to update") Institution newEntity) {
        Institution entity = repo.findById(id);
        if (entity == null) {
            return response.build(404);
        }
        if (!Utils.isEmpty(newEntity.name)) entity.name = newEntity.name;
        // TODO: allow update department
        repo.persist(entity);
        return response.build(200);
    }
}
