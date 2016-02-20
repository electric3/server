package com.electric3.server.resources.departments;

import com.electric3.dataatoms.Project;
import com.electric3.dataatoms.User;
import com.electric3.server.utils.StackTraceUtils;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("departments")
public class DepartmentsResource {
    private static Logger log = Logger.getLogger(DepartmentsResource.class.getName());

    @POST
    @Path("{departmentId}/projects")
    public Response createProject(@PathParam("departmentId") String departmentId, String json) {
        log.info("create department project");
        DepartmentsDBManager departmentsDBManager = DepartmentsDBManager.getInstance();
        try {
            Project project = Project.deserialize(json, Project.class);
            departmentsDBManager.createProject(departmentId, project);
            return Response.ok().build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @GET
    @Path("{departmentId}/projects")
    public Response getProjects(@PathParam("departmentId") String departmentId) {
        log.info("get department projects");
        DepartmentsDBManager departmentsDBManager = DepartmentsDBManager.getInstance();
        try {
            return Response.ok(departmentsDBManager.getDepartmentProjects(departmentId), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }
}
