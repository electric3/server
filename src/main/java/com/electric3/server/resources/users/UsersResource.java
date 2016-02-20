package com.electric3.server.resources.users;

import com.electric3.dataatoms.User;
import com.electric3.server.utils.StackTraceUtils;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("users")
public class UsersResource {
    private static Logger log = Logger.getLogger(UsersResource.class.getName());

    @GET
    public Response getUsers() {
        log.info("get all users");
        UsersDBManager usersDBManager = UsersDBManager.getInstance();
        try {
            return Response.ok(usersDBManager.getUsers(), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @POST
    public Response createUser(String json) {
        log.info("create user");
        UsersDBManager usersDBManager = UsersDBManager.getInstance();
        try {
            User user = User.deserialize(json, User.class);
            usersDBManager.createUser(user);
            return Response.ok().build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }
}
