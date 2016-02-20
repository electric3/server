package com.electric3.server.resources.clients;

import com.electric3.dataatoms.Department;
import com.electric3.dataatoms.Holder;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Path("clients")
public class ClientsResource {
    private static Logger log = Logger.getLogger(ClientsResource.class.getName());

    @POST
    public Response create(String json) {
        return Response.ok().build();
    }

    @GET
    @Path("{clientId}/departments")
    public Response getClientDepartments(@PathParam("clientId") String clientId) {
        List<Department> departments = new ArrayList<>();
        Holder<Department> departmentHolder = new Holder<>();
        departmentHolder.setItems(departments);
        return Response.ok(departmentHolder.serialize(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("{clientId}/departments")
    public Response createDepartment(@PathParam("clientId") String clientId, String json) {
        return Response.ok().build();
    }
}
