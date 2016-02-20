package com.electric3.server.resources.comments;

import com.electric3.dataatoms.Comment;
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

@Path("comments")
public class CommentsResource {
    private static Logger log = Logger.getLogger(CommentsResource.class.getName());

    @GET
    @Path("/delivery/{id}")
    public Response getDeliveryComments(@PathParam("id") String deliveryId) {
        List<Comment> list = new ArrayList<>();
        Holder<Comment> holder = new Holder<>();
        holder.setItems(list);
        return Response.ok(holder.serialize(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/delivery/{id}")
    public Response addDeliveryComment(@PathParam("id") String deliveryId, String json) {
        return Response.ok().build();
    }
}
