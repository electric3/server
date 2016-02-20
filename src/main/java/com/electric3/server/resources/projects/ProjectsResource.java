package com.electric3.server.resources.projects;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("projects")
public class ProjectsResource {
    private static Logger log = Logger.getLogger(ProjectsResource.class.getName());

    @GET
    @Path("{projectId}/deliveries")
    public Response getDeliveries(@PathParam("projectId") String projectId) {
        return Response.ok().build();
    }

    @POST
    @Path("{projectId}/deliveries")
    public Response createDelivery(@PathParam("projectId") String projectId) {
        return Response.ok().build();
    }
}
