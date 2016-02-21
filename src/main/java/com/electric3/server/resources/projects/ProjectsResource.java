package com.electric3.server.resources.projects;

import com.electric3.dataatoms.Delivery;
import com.electric3.dataatoms.User;
import com.electric3.server.utils.StackTraceUtils;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("projects")
public class ProjectsResource {
    private static Logger log = Logger.getLogger(ProjectsResource.class.getName());

    @GET
    @Path("{projectId}/deliveries")
    public Response getDeliveries(@PathParam("projectId") String projectId) {
        log.info("get project deliveries");
        ProjectsDBManager projectsDBManager = ProjectsDBManager.getInstance();
        try {
            return Response.ok(projectsDBManager.getProjectDeliveries(projectId), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @GET
    @Path("user/{userId}")
    public Response getUserProjects(@PathParam("userId") String userId) {
        log.info("get user projects");
        ProjectsDBManager projectsDBManager = ProjectsDBManager.getInstance();
        try {
            return Response.ok(projectsDBManager.getUserProjects(userId), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @POST
    @Path("{projectId}/deliveries")
    public Response createDelivery(@PathParam("projectId") String projectId, String json) {
        log.info("create project delivery");
        ProjectsDBManager projectsDBManager = ProjectsDBManager.getInstance();
        try {
            Delivery delivery = Delivery.deserialize(json, Delivery.class);
            projectsDBManager.createDelivery(projectId, delivery);
            return Response.ok().build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @POST
    @Path("{projectId}/owner")
    public Response setOwner(@PathParam("projectId") String projectId, String json) {
        log.info("set project owner");
        ProjectsDBManager projectsDBManager = ProjectsDBManager.getInstance();
        try {
            User user = User.deserialize(json, User.class);
            projectsDBManager.setOwner(projectId, user);
            return Response.ok().build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }
}
