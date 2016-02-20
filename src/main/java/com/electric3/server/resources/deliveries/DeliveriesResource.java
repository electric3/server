package com.electric3.server.resources.deliveries;

import com.electric3.dataatoms.Comment;
import com.electric3.server.utils.StackTraceUtils;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("deliveries")
public class DeliveriesResource {
    private static Logger log = Logger.getLogger(DeliveriesResource.class.getName());

    @GET
    @Path("{id}")
    public Response getDelivery(@PathParam("id") String deliveryId) {
        DeliveriesDBManager deliveriesDBManager = DeliveriesDBManager.getInstance();
        try {
            return Response.ok(deliveriesDBManager.getDelivery(deliveryId), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/{id}/comments/")
    public Response getDeliveryComments(@PathParam("id") String deliveryId) {
        DeliveriesDBManager deliveriesDBManager = DeliveriesDBManager.getInstance();
        try {
            return Response.ok(deliveriesDBManager.getDeliveryComments(deliveryId), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @GET
    @Path("{id}/setStatus/{statusId}")
    public Response setStatus(@PathParam("id") String deliveryId, @PathParam("statusId") String statusId) {
        DeliveriesDBManager deliveriesDBManager = DeliveriesDBManager.getInstance();
        try {
            deliveriesDBManager.setStatus(deliveryId, Integer.parseInt(statusId));
            return Response.ok().build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @GET
    @Path("{id}/setProgress/{progressValue}")
    public Response setProgress(@PathParam("id") String deliveryId, @PathParam("progressValue") String progressValue) {
        DeliveriesDBManager deliveriesDBManager = DeliveriesDBManager.getInstance();
        try {
            deliveriesDBManager.setProgress(deliveryId, Integer.parseInt(progressValue));
            return Response.ok().build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @POST
    @Path("{id}/comment/")
    public Response addComment(@PathParam("id") String deliveryId, String json) {
        DeliveriesDBManager deliveriesDBManager = DeliveriesDBManager.getInstance();
        try {
            Comment comment = Comment.deserialize(json, Comment.class);
            deliveriesDBManager.addComment(deliveryId, comment);
            return Response.ok().build();
        } catch (Exception e) {
            log.severe(StackTraceUtils.getStackTrace(e));
            return Response.serverError().build();
        }
    }

    @POST
    @Path("{id}/attachment/")
    public Response addAttachment(@PathParam("id") String deliveryId, String json) {
        return Response.ok().build();
    }
}
