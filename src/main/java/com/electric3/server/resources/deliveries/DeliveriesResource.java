package com.electric3.server.resources.deliveries;

import com.electric3.dataatoms.Comment;
import com.electric3.dataatoms.Delivery;
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

@Path("deliveries")
public class DeliveriesResource {
    private static Logger log = Logger.getLogger(DeliveriesResource.class.getName());

    @GET
    @Path("{id}")
    public Response getDelivery(@PathParam("id") String deliveryId) {
        Delivery result = new Delivery();
        return Response.ok(result.serialize(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}/comments/")
    public Response getDeliveryComments(@PathParam("id") String deliveryId) {
        List<Comment> list = new ArrayList<>();
        Holder<Comment> holder = new Holder<>();
        holder.setItems(list);
        return Response.ok(holder.serialize(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("{id}/setStatus/")
    public Response setStatus(@PathParam("id") String projectId, String json) {
        return Response.ok().build();
    }

    @POST
    @Path("{id}/setProgress/")
    public Response setProgress(@PathParam("id") String projectId, String json) {
        return Response.ok().build();
    }

    @POST
    @Path("{id}/comment/")
    public Response addComment(@PathParam("id") String projectId, String json) {
        return Response.ok().build();
    }

    @POST
    @Path("{id}/attachment/")
    public Response addAttachment(@PathParam("id") String projectId, String json) {
        return Response.ok().build();
    }
}
