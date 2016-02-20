package com.electric3.server.resources.users;

import com.electric3.server.utils.StackTraceUtils;

import javax.ws.rs.GET;
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
}
