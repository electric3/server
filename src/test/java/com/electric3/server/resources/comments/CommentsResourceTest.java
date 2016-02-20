package com.electric3.server.resources.comments;

import com.electric3.dataatoms.Comment;
import com.electric3.dataatoms.Holder;
import com.electric3.server.resources.actions.ActionsResource;
import com.google.gson.Gson;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CommentsResourceTest extends JerseyTest {
    @Override
    protected Application configure() {
        return new ResourceConfig(CommentsResource.class);
    }

//    @Test
//    public void testGetComments() {
//        final String hello = target("comments").path("delivery").path("1").request().get(String.class);
//        Holder holder = new Gson().fromJson(hello, Holder.class);
//        assertNotNull(holder);
//        assertNotNull(holder.getItems());
//    }
//
//    @Test
//    public void testSaveComment() {
//        final Response result =
//                target("comments").path("delivery").path("1").request(MediaType.APPLICATION_JSON_TYPE)
//                .post(Entity.entity(new Comment().serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);
//        assertNotNull(result);
//    }
}
