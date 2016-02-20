package com.electric3.server.resources.clients;

import com.electric3.dataatoms.Client;
import com.electric3.dataatoms.Department;
import com.electric3.dataatoms.User;
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
        log.info("create new client");
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
        log.info("get client departments");
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
        log.info("get client by owner");
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
        log.info("create client department");
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

    @POST
    @Path("{clientId}/owner")
    public Response setOwner(@PathParam("clientId") String clientId, String json) {
        log.info("set client owner");
        ClientsDBManager clientsDBManager = ClientsDBManager.getInstance();
        try {
            User user = User.deserialize(json, User.class);
            clientsDBManager.setOwner(clientId, user);
            return Response.ok().build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @GET
    @Path("{clientId}/users")
    public Response getUsers(@PathParam("clientId") String clientId) {
        log.info("get client users");
        ClientsDBManager clientsDBManager = ClientsDBManager.getInstance();
        try {
            return Response.ok(clientsDBManager.getClientUsers(clientId), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @POST
    @Path("{clientId}/users")
    public Response createUser(@PathParam("clientId") String clientId, String json) {
        log.info("create client user");
        ClientsDBManager clientsDBManager = ClientsDBManager.getInstance();
        try {
            User user = User.deserialize(json, User.class);
            clientsDBManager.createClientUser(clientId, user);
            return Response.ok().build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }
}
