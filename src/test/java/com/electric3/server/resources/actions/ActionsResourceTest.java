package com.electric3.server.resources.actions;

import com.electric3.dataatoms.Holder;
import com.google.gson.Gson;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.swing.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ActionsResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(ActionsResource.class);
    }

    @Test
    public void testClient() {
        final String hello = target("actions").path("client").path("1").request().get(String.class);
        Holder holder = new Gson().fromJson(hello, Holder.class);
        assertNotNull(holder);
        assertNotNull(holder.getItems());
    }

    @Test
    public void testDepartment() {
        final String hello = target("actions").path("department").path("1").request().get(String.class);
        Holder holder = new Gson().fromJson(hello, Holder.class);
        assertNotNull(holder);
        assertNotNull(holder.getItems());
    }

    @Test
    public void testProject() {
        final String hello = target("actions").path("project").path("1").request().get(String.class);
        Holder holder = new Gson().fromJson(hello, Holder.class);
        assertNotNull(holder);
        assertNotNull(holder.getItems());
    }
}
