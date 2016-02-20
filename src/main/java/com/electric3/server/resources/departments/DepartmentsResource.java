package com.electric3.server.resources.departments;

import com.electric3.dataatoms.Department;
import com.electric3.dataatoms.Holder;
import com.electric3.dataatoms.User;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Path("departments")
public class DepartmentsResource {
    private static Logger log = Logger.getLogger(DepartmentsResource.class.getName());

    @POST
    @Path("{departmentId}/projects")
    public Response createProject(@PathParam("departmentId") String departmentId, String json) {
        return Response.ok().build();
    }

    @GET
    @Path("{departmentId}/projects")
    public Response getProjects(@PathParam("departmentId") String departmentId) {
        return Response.ok().build();
    }

    @GET
    @Path("{departmentId}/users")
    public Response getUsers(@PathParam("departmentId") String departmentId) {
        List<User> users = new ArrayList<>();
        Holder<User> usersHolder = new Holder<>();
        usersHolder.setItems(users);
        return Response.ok(usersHolder.serialize(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("{departmentId}/users")
    public Response createUser(@PathParam("departmentId") String departmentId, String json) {
        return Response.ok().build();
    }
}
