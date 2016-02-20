package com.electric3.server.resources.actions;

import com.electric3.server.utils.StackTraceUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("actions")
public class ActionsResource {
    private static Logger log = Logger.getLogger(ActionsResource.class.getName());

    @GET
    @Path("/client/{id}")
    public Response getClientActions(@PathParam("id") String clientId) {
        log.info("get actions for client");
        ActionsDBManager actionsDBManager = ActionsDBManager.getInstance();
        try {
            return Response.ok(actionsDBManager.getClientActions(clientId), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/department/{id}")
    public Response getDepartmentActions(@PathParam("id") String departmentId) {
        log.info("get actions for department");
        ActionsDBManager actionsDBManager = ActionsDBManager.getInstance();
        try {
            return Response.ok(actionsDBManager.getDepartmentActions(departmentId), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/project/{id}")
    public Response getProjectActions(@PathParam("id") String projectId) {
        log.info("get actions for department");
        ActionsDBManager actionsDBManager = ActionsDBManager.getInstance();
        try {
            return Response.ok(actionsDBManager.getProjectActions(projectId), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }
}
