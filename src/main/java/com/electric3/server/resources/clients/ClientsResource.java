package com.electric3.server.resources.clients;

import com.electric3.dataatoms.Client;
import com.electric3.dataatoms.Department;
import com.electric3.server.utils.StackTraceUtils;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("clients")
public class ClientsResource {
    private static Logger log = Logger.getLogger(ClientsResource.class.getName());

    @POST
    public Response create(String json) {
        ClientsDBManager clientsDBManager = ClientsDBManager.getInstance();
        try {
            Client client = Client.deserialize(json, Client.class);
            clientsDBManager.createClient(client);
            return Response.ok().build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @GET
    @Path("{clientId}/departments")
    public Response getClientDepartments(@PathParam("clientId") String clientId) {
        ClientsDBManager clientsDBManager = ClientsDBManager.getInstance();
        try {
            return Response.ok(clientsDBManager.getClientDepartments(clientId), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/info/{userId}")
    public Response getClientByOwner(@PathParam("userId") String userId) {
        ClientsDBManager clientsDBManager = ClientsDBManager.getInstance();
        try {
            return Response.ok(clientsDBManager.getClientByOwner(userId), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @POST
    @Path("{clientId}/departments")
    public Response createDepartment(@PathParam("clientId") String clientId, String json) {
        ClientsDBManager clientsDBManager = ClientsDBManager.getInstance();
        try {
            Department department = Department.deserialize(json, Department.class);
            clientsDBManager.createDepartment(clientId, department);
            return Response.ok().build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }
}
