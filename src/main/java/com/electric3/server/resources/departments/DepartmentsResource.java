package com.electric3.server.resources.departments;

import com.electric3.dataatoms.Department;
import com.electric3.dataatoms.Holder;

import javax.ws.rs.GET;
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

    @GET
    @Path("{clientId}")
    public Response getClientDepartments(@PathParam("clientId") String clientId) {
        List<Department> departments = new ArrayList<>();
        Holder<Department> departmentHolder = new Holder<>();
        departmentHolder.setItems(departments);
        return Response.ok(departmentHolder.serialize(), MediaType.APPLICATION_JSON).build();
    }
}
