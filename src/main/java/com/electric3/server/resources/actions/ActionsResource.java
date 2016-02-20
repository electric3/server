package com.electric3.server.resources.actions;

import com.electric3.dataatoms.Action;
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

@Path("actions")
public class ActionsResource {
    private static Logger log = Logger.getLogger(ActionsResource.class.getName());

    @GET
    @Path("/client/{id}")
    public Response getClientActions(@PathParam("{id}") String projectId) {
        List<Action> list = new ArrayList<>();
        Holder<Action> holder = new Holder<>();
        holder.setItems(list);
        return Response.ok(holder.serialize(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/department/{id}")
    public Response getDepartmentActions(@PathParam("{id}") String projectId) {
        List<Action> list = new ArrayList<>();
        Holder<Action> holder = new Holder<>();
        holder.setItems(list);
        return Response.ok(holder.serialize(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/project/{id}")
    public Response getProjectActions(@PathParam("{id}") String projectId) {
        List<Action> list = new ArrayList<>();
        Holder<Action> holder = new Holder<>();
        holder.setItems(list);
        return Response.ok(holder.serialize(), MediaType.APPLICATION_JSON).build();
    }
}
