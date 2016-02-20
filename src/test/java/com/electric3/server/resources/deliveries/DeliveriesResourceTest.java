package com.electric3.server.resources.deliveries;

import com.electric3.dataatoms.Comment;
import com.electric3.dataatoms.Delivery;
import com.electric3.dataatoms.Holder;
import com.electric3.server.resources.comments.CommentsResource;
import com.google.gson.Gson;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class DeliveriesResourceTest extends JerseyTest {
    @Override
    protected Application configure() {
        return new ResourceConfig(DeliveriesResource.class);
    }

//    @POST
//    @Path("{id}/setStatus/")
//    public Response setStatus(@PathParam("id") String projectId, String json) {
//        return Response.ok().build();
//    }
//
//    @POST
//    @Path("{id}/setProgress/")
//    public Response setProgress(@PathParam("id") String projectId, String json) {
//        return Response.ok().build();
//    }
//
//    @POST
//    @Path("{id}/comment/")
//    public Response addComment(@PathParam("id") String projectId, String json) {
//        return Response.ok().build();
//    }
//
//    @POST
//    @Path("{id}/attachment/")
//    public Response addAttachment(@PathParam("id") String projectId, String json) {
//        return Response.ok().build();
//    }

    @Test
    public void testGetDelivery() {
        final Delivery result = target("deliveries").path("1").request().get(Delivery.class);
        assertNotNull( result );
    }

    @Test
    public void testGetDeliveryComments() {
        final String response = target("deliveries").path("1").path("comments").request().get(String.class);
        Holder holder = new Gson().fromJson(response, Holder.class);
        assertNotNull(holder);
        assertNotNull(holder.getItems());
    }
}
