package com.electric3.server.resources.actions;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

public class ActionsResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(ActionsResource.class);
    }

    @Test
    public void test() {
//        final String hello = target("actions").request().get(String.class);
//        assertEquals("Hello Worldsdfasdf!", hello);
    }
}
