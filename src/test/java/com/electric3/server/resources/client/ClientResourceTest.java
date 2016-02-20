package com.electric3.server.resources.client;

import com.electric3.dataatoms.Client;
import com.electric3.dataatoms.Comment;
import com.electric3.dataatoms.Holder;
import com.electric3.dataatoms.User;
import com.electric3.server.resources.clients.ClientsResource;
import com.electric3.server.resources.comments.CommentsResource;
import com.electric3.server.resources.utils.Mock;
import com.google.gson.Gson;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class ClientResourceTest extends JerseyTest {
    @Override
    protected Application configure() {
        return new ResourceConfig(ClientsResource.class);
    }

    @Test
    public void testCreateClient() {
        Client newClient = new Client();
        newClient.setTitle("title " + new Date(System.currentTimeMillis()).toLocaleString() );
        newClient.setDescription("description " + new Date(System.currentTimeMillis()).toLocaleString() );
        newClient.setEmail("ivan@handyassist.com");
        newClient.setOwner(Mock.getUserIvan() );

        final Response response = target("clients").request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(newClient.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);

        assertNotNull(response);
    }
}
