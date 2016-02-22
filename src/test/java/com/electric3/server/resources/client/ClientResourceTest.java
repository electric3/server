package com.electric3.server.resources.client;

import com.electric3.dataatoms.*;
import com.electric3.server.database.NoSqlBase;
import com.electric3.server.resources.clients.ClientsResource;
import com.electric3.server.resources.deliveries.DeliveriesResource;
import com.electric3.server.resources.departments.DepartmentsResource;
import com.electric3.server.resources.projects.ProjectsResource;
import com.electric3.server.resources.users.UsersResource;
import com.electric3.server.resources.utils.Mock;
import com.electric3.server.utils.StatusCalculator;
import com.google.gson.Gson;
import com.mongodb.client.MongoDatabase;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ClientResourceTest extends JerseyTest {
    @Override
    protected Application configure() {
        return new ResourceConfig(ClientsResource.class, DepartmentsResource.class, UsersResource.class, ProjectsResource.class, DeliveriesResource.class);
    }

    @Test
    public void testForDirector() {
        //testStatusCalculator();
        clearDB();
        //ivanScenario();
        //playWithStatusScenario(getClientId());
        //deliveryScenario("56c8e80dc4fa9065ab9376d9", "56c8e340c4fa9063806e0115");
        electric3Scenario();
    }

    HashMap<String, User> usersMap = new HashMap<String, User>();
    List<User> usersList = new LinkedList<User>();

    public void electric3Scenario() {
        replics = toSplit.split(" ");
        System.out.println(" replics.length " + replics.length);
        Holder<User> users = getUsers();
        List<User> items = users.getItems();
        User targetUser = null;

        for( User user : items ) {
            if( user.getEmail().equals("ivan@handyassist.com") ) {
                usersMap.put("ivan", user);
                usersList.add(user);
            } else if (  user.getEmail().equals("tanya@handyassist.com") ) {
                usersMap.put("tanya", user);
                usersList.add(user);
            } else if ( user.getEmail().equals("stas@handyassist.com") ) {
                usersMap.put("stas", user);
                usersList.add(user);
            }
        }

        createClient(usersMap.get("ivan"));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        final String client_response_str = target("clients").path("info").path((String) usersMap.get("ivan").getUser_id()).request(MediaType.APPLICATION_JSON_TYPE).get(String.class);
        assertNotNull(client_response_str);
        System.out.println("client: " + client_response_str);
        Client client = Client.deserialize(client_response_str, Client.class);
        System.out.println("client: " + client_response_str);
        assertNotNull(client);
        Mock.ME.setClient(client);

        while (true) {
            Department dep = new Department();
            dep.setDescription("Department related to server side development");
            dep.setOwner(usersMap.get("ivan"));
            dep.setClientId((String) Mock.ME.getClient().get_id());
            dep.setProgress(randomProgress());
            dep.setStatus(randomStatus());
            dep.setTitle("Server dev");

            electricNewDepartment(dep);
            List<Department> items1 = getDepartments().getItems();

            Department targetDepartment = null;

            for (Department department : items1) {
                if (department.getTitle().equals("Server dev")) {
                    targetDepartment = department;
                }
            }

            if (null == targetDepartment) {
                assertTrue(false);
            }

            {

                Project p1 = new Project();
                p1.setTitle("Protocol development");
                p1.setDescription("Necessary develop protocol");
                p1.setOwner(usersMap.get("ivan"));
                p1.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));

                electricNewProject1(getClientId(), (String) targetDepartment.get_id(), p1);

                List<Project> projectItems = getProjects((String) targetDepartment.get_id()).getItems();

                Project targetProject = null;

                for (Project project : projectItems) {
                    if (project.getTitle().equals("Protocol development")) {
                        targetProject = project;
                    }
                }

                if (null == targetProject) {
                    assertTrue(false);
                }
                electricCreateDeliveriesForProject(getClientId(), (String) targetDepartment.get_id(), targetProject, StatusEnum.RED, usersMap.get("ivan"));
            }

            {
                Project p = new Project();
                p.setTitle("Server development");
                p.setDescription("Necessary develop server");
                p.setOwner(usersMap.get("ivan"));
                p.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));

                electricNewProject1(getClientId(), (String) targetDepartment.get_id(), p);

                List<Project> pi = getProjects((String) targetDepartment.get_id()).getItems();

                Project tp = null;

                for (Project project : pi) {
                    if (project.getTitle().equals("Server development")) {
                        tp = project;
                    }
                }

                if (null == tp) {
                    assertTrue(false);
                }
                electricCreateDeliveriesForProject(getClientId(), (String) targetDepartment.get_id(), tp, StatusEnum.ORANGE, usersMap.get("ivan"));
            }

            {
                Project p = new Project();
                p.setTitle("Tests development");
                p.setDescription("Necessary develop tests for server");
                p.setOwner(usersMap.get("ivan"));
                p.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));

                electricNewProject1(getClientId(), (String) targetDepartment.get_id(), p);

                List<Project> pi = getProjects((String) targetDepartment.get_id()).getItems();

                Project tp = null;

                for (Project project : pi) {
                    if (project.getTitle().equals("Tests development")) {
                        tp = project;
                    }
                }

                if (null == tp) {
                    assertTrue(false);
                }
                electricCreateDeliveriesForProject(getClientId(), (String) targetDepartment.get_id(), tp, StatusEnum.GREEN, usersMap.get("ivan"));
            }
            break;
        }


        while (true) {
            Department dep = new Department();
            dep.setDescription("Department related to web development");
            dep.setOwner(usersMap.get("stas"));
            dep.setClientId((String) Mock.ME.getClient().get_id());
            dep.setProgress(randomProgress());
            dep.setStatus(randomStatus());
            dep.setTitle("Web dev");

            electricNewDepartment(dep);
            List<Department> items1 = getDepartments().getItems();

            Department targetDepartment = null;

            for (Department department : items1) {
                if (department.getTitle().equals("Web dev")) {
                    targetDepartment = department;
                }
            }

            if (null == targetDepartment) {
                assertTrue(false);
            }

            {

                Project p1 = new Project();
                p1.setTitle("UI stubs");
                p1.setDescription("Necessary develop UI stubs using material light");
                p1.setOwner(usersMap.get("stas"));
                p1.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));

                electricNewProject1(getClientId(), (String) targetDepartment.get_id(), p1);

                List<Project> projectItems = getProjects((String) targetDepartment.get_id()).getItems();

                Project targetProject = null;

                for (Project project : projectItems) {
                    if (project.getTitle().equals("UI stubs")) {
                        targetProject = project;
                    }
                }

                if (null == targetProject) {
                    assertTrue(false);
                }
                electricCreateDeliveriesForProject(getClientId(), (String) targetDepartment.get_id(), targetProject, StatusEnum.ORANGE, usersMap.get("stas"));
            }

            {
                Project p = new Project();
                p.setTitle("Angular switch");
                p.setDescription("Necessary develop web ui using Angular framework");
                p.setOwner(usersMap.get("stas"));
                p.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));

                electricNewProject1(getClientId(), (String) targetDepartment.get_id(), p);

                List<Project> pi = getProjects((String) targetDepartment.get_id()).getItems();

                Project tp = null;

                for (Project project : pi) {
                    if (project.getTitle().equals("Angular switch")) {
                        tp = project;
                    }
                }

                if (null == tp) {
                    assertTrue(false);
                }
                electricCreateDeliveriesForProject(getClientId(), (String) targetDepartment.get_id(), tp, StatusEnum.ORANGE, usersMap.get("stas"));
            }

            {
                Project p = new Project();
                p.setTitle("Tests development");
                p.setDescription("Necessary develop tests for angular");
                p.setOwner(usersMap.get("stas"));
                p.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));

                electricNewProject1(getClientId(), (String) targetDepartment.get_id(), p);

                List<Project> pi = getProjects((String) targetDepartment.get_id()).getItems();

                Project tp = null;

                for (Project project : pi) {
                    if (project.getTitle().equals("Tests development")) {
                        tp = project;
                    }
                }

                if (null == tp) {
                    assertTrue(false);
                }
                electricCreateDeliveriesForProject(getClientId(), (String) targetDepartment.get_id(), tp, StatusEnum.GREEN, usersMap.get("stas"));
            }
            break;
        }

        while (true) {
            Department dep = new Department();
            dep.setDescription("Department related to UX & JS DEV");
            dep.setOwner(usersMap.get("tanya"));
            dep.setClientId((String) Mock.ME.getClient().get_id());
            dep.setProgress(randomProgress());
            dep.setStatus(randomStatus());
            dep.setTitle("UX & UI JS");

            electricNewDepartment(dep);
            List<Department> items1 = getDepartments().getItems();

            Department targetDepartment = null;

            for (Department department : items1) {
                if (department.getTitle().equals("UX & UI JS")) {
                    targetDepartment = department;
                }
            }

            if (null == targetDepartment) {
                assertTrue(false);
            }

            {

                Project p1 = new Project();
                p1.setTitle("Prototype dev");
                p1.setDescription("Necessary develop prototype");
                p1.setOwner(usersMap.get("tanya"));
                p1.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));

                electricNewProject1(getClientId(), (String) targetDepartment.get_id(), p1);

                List<Project> projectItems = getProjects((String) targetDepartment.get_id()).getItems();

                Project targetProject = null;

                for (Project project : projectItems) {
                    if (project.getTitle().equals("Prototype dev")) {
                        targetProject = project;
                    }
                }

                if (null == targetProject) {
                    assertTrue(false);
                }
                electricCreateDeliveriesForProject(getClientId(), (String) targetDepartment.get_id(), targetProject, StatusEnum.GREEN, usersMap.get("tanya"));
            }

            {
                Project p = new Project();
                p.setTitle("Sketches dev");
                p.setDescription("Necessary develop sketches");
                p.setOwner(usersMap.get("tanya"));
                p.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));

                electricNewProject1(getClientId(), (String) targetDepartment.get_id(), p);

                List<Project> pi = getProjects((String) targetDepartment.get_id()).getItems();

                Project tp = null;

                for (Project project : pi) {
                    if (project.getTitle().equals("Sketches dev")) {
                        tp = project;
                    }
                }

                if (null == tp) {
                    assertTrue(false);
                }
                electricCreateDeliveriesForProject(getClientId(), (String) targetDepartment.get_id(), tp, StatusEnum.GREEN, usersMap.get("tanya"));
            }

            {
                Project p = new Project();
                p.setTitle("Tests development");
                p.setDescription("Necessary develop tests for UI & UX");
                p.setOwner(usersMap.get("tanya"));
                p.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));

                electricNewProject1(getClientId(), (String) targetDepartment.get_id(), p);

                List<Project> pi = getProjects((String) targetDepartment.get_id()).getItems();

                Project tp = null;

                for (Project project : pi) {
                    if (project.getTitle().equals("Tests development")) {
                        tp = project;
                    }
                }

                if (null == tp) {
                    assertTrue(false);
                }
                electricCreateDeliveriesForProject(getClientId(), (String) targetDepartment.get_id(), tp, StatusEnum.GREEN, usersMap.get("tanya"));
            }
            break;
        }

//        {
//            Department dep = new Department();
//            dep.setDescription("Department related to angular development");
//            dep.setOwner(usersMap.get("tanya"));
//            dep.setClientId((String) Mock.ME.getClient().get_id());
//            dep.setProgress(randomProgress());
//            dep.setStatus(randomStatus());
//            dep.setTitle("Product dev");
//
//            electricNewDepartment(dep);
//        }
//
//        {
//            Department dep = new Department();
//            dep.setDescription("Department related to angular development");
//            dep.setOwner(usersMap.get("stas"));
//            dep.setClientId((String) Mock.ME.getClient().get_id());
//            dep.setProgress(randomProgress());
//            dep.setStatus(randomStatus());
//            dep.setTitle("Agular dev");
//
//            electricNewDepartment(dep);
//        }
    }

    public void electricCreateDeliveriesForProject(String clientId, String id, Project targetProject, StatusEnum col, User assignee) {
        if( col.equals(StatusEnum.RED) ) {
            {
                Delivery delivery = new Delivery();
                delivery.setAssignee(assignee);
                delivery.setTitle("Delivery 1 ");
                delivery.setDescription("Description 1 " + getTSS());
                delivery.setProgress(randomProgress());
                delivery.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));
                delivery.setStatus(StatusEnum.RED);
                Response response = target("projects").path((String) targetProject.get_id()).path("deliveries")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(delivery.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);
            }
            {
                Delivery delivery = new Delivery();
                delivery.setAssignee(assignee);
                delivery.setTitle("Delivery 2 ");
                delivery.setDescription("Description 2 " + getTSS());
                delivery.setProgress(randomProgress());
                delivery.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));
                delivery.setStatus(StatusEnum.ORANGE);
                Response response = target("projects").path((String) targetProject.get_id()).path("deliveries")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(delivery.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);

                List<Delivery> items = getDeliveries((String) targetProject.get_id()).getItems();
            }

            {
                Delivery delivery = new Delivery();
                delivery.setAssignee(assignee);
                delivery.setTitle("Delivery 3 ");
                delivery.setDescription("Description 3 " + getTSS());
                delivery.setProgress(randomProgress());
                delivery.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));
                delivery.setStatus(StatusEnum.GREEN);
                Response response = target("projects").path((String) targetProject.get_id()).path("deliveries")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(delivery.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);

                List<Delivery> items = getDeliveries((String) targetProject.get_id()).getItems();
            }
        } else if ( col.equals(StatusEnum.ORANGE ) ) {
            {
                Delivery delivery = new Delivery();
                delivery.setAssignee(assignee);
                delivery.setTitle("Delivery 1 ");
                delivery.setDescription("Description 1 " + getTSS());
                delivery.setProgress(randomProgress());
                delivery.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));
                delivery.setStatus(StatusEnum.GREEN);
                Response response = target("projects").path((String) targetProject.get_id()).path("deliveries")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(delivery.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);
            }
            {
                Delivery delivery = new Delivery();
                delivery.setAssignee(assignee);
                delivery.setTitle("Delivery 2 ");
                delivery.setDescription("Description 2 " + getTSS());
                delivery.setProgress(randomProgress());
                delivery.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));
                delivery.setStatus(StatusEnum.ORANGE);
                Response response = target("projects").path((String) targetProject.get_id()).path("deliveries")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(delivery.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);
            }

            {
                Delivery delivery = new Delivery();
                delivery.setAssignee(assignee);
                delivery.setTitle("Delivery 3 ");
                delivery.setDescription("Description 3 " + getTSS());
                delivery.setProgress(randomProgress());
                delivery.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));
                delivery.setStatus(StatusEnum.GREEN);
                Response response = target("projects").path((String) targetProject.get_id()).path("deliveries")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(delivery.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);
            }
        } else {
            {
                Delivery delivery = new Delivery();
                delivery.setAssignee(assignee);
                delivery.setTitle("Delivery 1 ");
                delivery.setDescription("Description 1 " + getTSS());
                delivery.setProgress(randomProgress());
                delivery.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));
                delivery.setStatus(StatusEnum.GREEN);
                Response response = target("projects").path((String) targetProject.get_id()).path("deliveries")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(delivery.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);
            }
            {
                Delivery delivery = new Delivery();
                delivery.setAssignee(assignee);
                delivery.setTitle("Delivery 2 ");
                delivery.setDescription("Description 2 " + getTSS());
                delivery.setProgress(randomProgress());
                delivery.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));
                delivery.setStatus(StatusEnum.GREEN);
                Response response = target("projects").path((String) targetProject.get_id()).path("deliveries")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(delivery.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);
            }

            {
                Delivery delivery = new Delivery();
                delivery.setAssignee(assignee);
                delivery.setTitle("Delivery 3 ");
                delivery.setDescription("Description 3 " + getTSS());
                delivery.setProgress(randomProgress());
                delivery.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 12));
                delivery.setStatus(StatusEnum.GREEN);
                Response response = target("projects").path((String) targetProject.get_id()).path("deliveries")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(delivery.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);
            }
        }

        List<Delivery> items = getDeliveries((String) targetProject.get_id()).getItems();
        for( Delivery d : items ) {
            electricChatForDelivery((String)d.get_id());
        }
    }

    public void electricNewProject1(String clientId, String departmentId, Project p1) {
        Response response = target("departments").path(departmentId).path("projects")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(p1.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);
        assertNotNull(response);
    }

    public void electricNewDepartment(Department newDep) {
        Response newDepartmentResponse = target("clients")
                .path((String) Mock.ME.getClient().get_id()).path("/departments")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(newDep.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);
    }

    public void electricChatForDelivery( String deliveryId ) {
        for( int i = 0; i < 5; i++) {
            String randomReplic = replics[ThreadLocalRandom.current().nextInt(0, replics.length)];
            User usr = usersList.get(ThreadLocalRandom.current().nextInt(0, usersList.size() ));
            deliveryAddComment(randomReplic, deliveryId, usr);
        }
    }


    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///
    ///

    public void testStatusCalculator( ) {
        assertTrue(StatusCalculator.ME.selectWorse(StatusEnum.GREEN, StatusEnum.ORANGE).equals(StatusEnum.ORANGE));
        assertTrue(StatusCalculator.ME.selectWorse(StatusEnum.ORANGE, StatusEnum.RED).equals(StatusEnum.RED));
        assertTrue(StatusCalculator.ME.selectWorse(StatusEnum.GREEN, StatusEnum.RED).equals(StatusEnum.RED));
        assertTrue(StatusCalculator.ME.selectWorse(StatusEnum.ORANGE, StatusEnum.GREEN).equals(StatusEnum.ORANGE));
        assertTrue(StatusCalculator.ME.selectWorse(StatusEnum.RED, StatusEnum.GREEN).equals(StatusEnum.RED));
        assertTrue(StatusCalculator.ME.selectWorse(StatusEnum.RED, StatusEnum.ORANGE).equals(StatusEnum.RED));

        List<Delivery> stubDeliveries = getStubDeliveries(StatusEnum.GREEN);

        StatusEnum worse = StatusCalculator.ME.selectWorseOnSet(stubDeliveries);
        System.out.println("worse " + worse);

        setStubColor(stubDeliveries, 0, StatusEnum.RED);
        worse = StatusCalculator.ME.selectWorseOnSet(stubDeliveries);
        System.out.println("worse " + worse);
        assertTrue(worse.equals(StatusEnum.RED));

        setStubColor(stubDeliveries, 2, StatusEnum.ORANGE);
        worse = StatusCalculator.ME.selectWorseOnSet(stubDeliveries);
        System.out.println("worse " + worse);
        assertTrue(worse.equals(StatusEnum.RED));

        setStubColor(stubDeliveries, 0, StatusEnum.ORANGE);
        worse = StatusCalculator.ME.selectWorseOnSet(stubDeliveries);
        System.out.println("worse " + worse);
        assertTrue(worse.equals(StatusEnum.ORANGE));

        setStubColor(stubDeliveries, 1, StatusEnum.ORANGE);
        worse = StatusCalculator.ME.selectWorseOnSet(stubDeliveries);
        System.out.println("worse " + worse);
        assertTrue(worse.equals(StatusEnum.ORANGE));

        setStubColor(stubDeliveries, 6, StatusEnum.RED);
        worse = StatusCalculator.ME.selectWorseOnSet(stubDeliveries);
        System.out.println("worse " + worse);
        assertTrue(worse.equals(StatusEnum.RED));

        setStubColor(stubDeliveries, 0, StatusEnum.GREEN);
        worse = StatusCalculator.ME.selectWorseOnSet(stubDeliveries);
        System.out.println("worse " + worse);
        assertTrue(worse.equals(StatusEnum.RED));

        setStubColor(stubDeliveries, 1, StatusEnum.GREEN);
        worse = StatusCalculator.ME.selectWorseOnSet(stubDeliveries);
        System.out.println("worse " + worse);
        assertTrue(worse.equals(StatusEnum.RED));

        setStubColor(stubDeliveries, 6, StatusEnum.GREEN);
        worse = StatusCalculator.ME.selectWorseOnSet(stubDeliveries);
        System.out.println("worse " + worse);
        assertTrue(worse.equals(StatusEnum.ORANGE));

        setStubColor(stubDeliveries, 2, StatusEnum.GREEN);
        worse = StatusCalculator.ME.selectWorseOnSet(stubDeliveries);
        System.out.println("worse " + worse);
        assertTrue(worse.equals(StatusEnum.GREEN));
    }

    public void setStubColor( List<? extends IStatusProvider> statusProviders, int index, StatusEnum color) {
        statusProviders.get(index).setStatus(color);
    }

    public List<Delivery> getStubDeliveries(StatusEnum color) {
        List<Delivery> deliveries = new LinkedList<Delivery>();
        for( int i = 0; i < 7; i++) {
            Delivery delivery = new Delivery();
            delivery.setStatus(color);
            deliveries.add(delivery);
        }
        return deliveries;
    }

    public void testSetStatusCalculator(List<IStatusProvider> statusProviders) {

    }

    public void createTestOutput(String clientId) {
        System.out.println(">>>>");
        System.out.println(">>>>");
        System.out.println(">>>>");
        System.out.println(">>>>");

        Holder<Department> departments = getDepartments(clientId);
        System.out.println("departments");
        System.out.println(departments.serialize());

        System.out.println("project");
        Holder<Project> projects = getProjects((String) departments.getItems().get(0).get_id());
        System.out.println(projects.serialize());

        System.out.println("deliveries");
        Holder<Delivery> deliveries = getDeliveries((String) projects.getItems().get(0).get_id());
        System.out.println(deliveries.serialize());

        System.out.println("comments");
        Holder<Comment> comments = getComments((String) deliveries.getItems().get(0).get_id());
        System.out.println(comments.serialize());
    }

    public void clearDB() {
        MongoDatabase clientDatabase = NoSqlBase.ConnectionFactory.CONNECTION.getClientDatabase();
        clientDatabase.drop();
    }

    public void ivanScenario() {
        Holder<User> users = getUsers();
        List<User> items = users.getItems();
        User targetUser = null;
        for( User user : items ) {
            if( user.getEmail().equals("ivan@handyassist.com") ) {
                targetUser = user;
                break;
            }
        }

        createClient(targetUser);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        final String client_response_str =
                target("clients")
                        .path("info")
                        .path(targetUser.getUser_id())
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .get(String.class);
        assertNotNull(client_response_str);
        System.out.println(" " + client_response_str);
        Client client = Client.deserialize(client_response_str, Client.class);
        assertNotNull(client);
        Mock.ME.setClient(client);

        usersScenario(getClientId());
        departmentsScript(getClientId());

        createTestOutput(getClientId());
    }

    public void fullScenario() {
        createUser();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        User testingUser = getRandomUser();
        createClient(testingUser);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        final String client_response_str =
                target("clients")
                        .path("info")
                        .path(testingUser.getUser_id())
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .get(String.class);
        assertNotNull(client_response_str);
        System.out.println(" " + client_response_str);
        Client client = Client.deserialize(client_response_str, Client.class);
        assertNotNull(client);
        Mock.ME.setClient(client);

        usersScenario(getClientId());
        departmentsScript(getClientId());
    }

    public void createClient(User user) {
        Client client = new Client();
        client.setEmail(user.getEmail());
        client.setTitle("Cli t " + getTSS());
        client.setDescription("Cli d " + getTSS());
        client.setOwner(user);
        client.setPhone("+7 981 772 74 49");
        target("clients").request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(client.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);
    }

    public Holder<User> getUsers() {
        String users = target("users").request().get(String.class);
        Type fooType = new TypeToken<Holder<User>>() {}.getType();
        Holder<User> usersHolder = new Gson().fromJson(users, fooType);
        return usersHolder;
    }

    public void createUser() {
        User user = new User();
        User.UserMetadata userMetadata = user.new UserMetadata();
        userMetadata.setName("F: " + getTSS());
        userMetadata.setSkypeName("skype");
        userMetadata.setPhotoUrl("https://pbs.twimg.com/profile_images/679653172244262912/q8D6AbS4.jpg");
        userMetadata.setPhone("+" + System.currentTimeMillis());
        userMetadata.setRole(UserRole.DIRECTOR);
        user.setUser_metadata(userMetadata);
        user.setPassword("xyi");
        user.setEmail(System.currentTimeMillis() + "@" + System.currentTimeMillis() + ".com");

        Response user_resp =
                target("users")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(user.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);
    }

    /******************************************************************************************************************/

    public void usersScenario(String clientId) {
        Holder<User> users = getUsers(clientId);
        int before = users.getItems().size();
        if( before < 5 ) {
            for( int i = 0; i < 6; i++ ) {
                createUser(clientId);
            }
        }
        createUser(clientId);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int after = getUsers(clientId).getItems().size();
        assertTrue((after - before) > 0);
    }


    public void createUser(String clientId) {
        User user = new User();
        User.UserMetadata userMetadata = user.new UserMetadata();
        userMetadata.setName("F: " + new Date(System.currentTimeMillis()).toLocaleString());
        userMetadata.setSkypeName("skype");
        userMetadata.setPhotoUrl("https://pbs.twimg.com/profile_images/679653172244262912/q8D6AbS4.jpg");
        userMetadata.setClientId(clientId);
        userMetadata.setPhone("+" + System.currentTimeMillis());
        userMetadata.setRole(UserRole.EMPLOYEE);
        user.setUser_metadata(userMetadata);
        user.setPassword("xyi");
        user.setEmail(System.currentTimeMillis() + "@" + System.currentTimeMillis() + ".com");

        Response user_resp =
                target("clients")
                        .path(clientId).path("/users")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(user.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);

        assertNotNull(user_resp);
    }

    public Holder<User> getUsers(String clientId) {
        String users_str =
                target("clients")
                        .path(clientId).path("/users")
                        .request()
                        .get(String.class);

        Type fooType = new TypeToken<Holder<User>>() {}.getType();
        Holder<User> usersHolder = new Gson().fromJson(users_str, fooType);
        return usersHolder;
    }

    /******************************************************************************************************************/

    public void departmentsScript(String clientId) {
        Holder<Department> departments = getDepartments();
        Holder<User> users = getUsers(clientId);
        if( departments.getItems().size() < 2 ) {
            for( int i = 0; i < 2; i++ ) {
                createNewDepartment(getRandomUser(users));
            }
        }

        Holder<Department> forDepP = getDepartments();
        List<Department> deps = forDepP.getItems();
        for (Department department : deps) {
            departmentScenario(clientId, (String) department.get_id());
        }
    }

    public void createNewDepartment(User owner) {
        Department newDep = new Department();
        newDep.setDescription("this is description " + new Date(System.currentTimeMillis()).toLocaleString());
        newDep.setOwner(owner);
        newDep.setClientId((String) Mock.ME.getClient().get_id());
        newDep.setProgress(randomProgress());
        newDep.setStatus(randomStatus());
        newDep.setTitle("this is title " + new Date(System.currentTimeMillis()).toLocaleString());

        Response newDepartmentResponse = target("clients")
                .path((String) Mock.ME.getClient().get_id()).path("/departments")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(newDep.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);
        assertNotNull(newDepartmentResponse);
    }

    public Holder<Department> getDepartments(String clientId) {
        String deps_str =
                target("clients")
                        .path(clientId).path("/departments")
                        .request().get(String.class);
        System.out.println(deps_str);

        Type fooType = new TypeToken<Holder<Department>>() {}.getType();
        Holder<Department> departmentsHolder = new Gson().fromJson(deps_str, fooType);

        return departmentsHolder;
    }

    public Holder<Department> getDepartments() {
        String deps_str =
                target("clients")
                        .path((String) Mock.ME.getClient().get_id()).path("/departments")
                        .request().get(String.class);
        System.out.println(deps_str);

        Type fooType = new TypeToken<Holder<Department>>() {}.getType();
        Holder<Department> departmentsHolder = new Gson().fromJson(deps_str, fooType);

        return departmentsHolder;
    }

    /******************************************************************************************************************/

    public void departmentScenario(String clientId, String departmentId) {
        int before = getProjects(departmentId).getItems().size();
        if( before < 2 ) {
            for( int i = 0; i < 2; i++ ) {
                createProjectForDepartment(clientId, departmentId);
            }
        }

        List<Project> items = getProjects(departmentId).getItems();
        for(Project project : items) {
            projectScenario((String)project.get_id(), clientId);
        }

    }

    public Holder<Project> getProjects(String departmentId) {
        String result = target("departments").path(departmentId).path("projects")
                .request().get(String.class);
        Type fooType = new TypeToken<Holder<Project>>() {}.getType();
        Holder<Project> departmentsHolder = new Gson().fromJson(result, fooType);
        return departmentsHolder;
    }

    public void createProjectForDepartment(String clientId, String departmentId) {
        Project project = new Project();
        project.setTitle("Project " + getTSS());
        project.setDescription("Description " + getTSS());
        project.setDeadline(String.valueOf(System.currentTimeMillis() + 60 * 60 * 1000 * 12));
        project.setDepartmentId(departmentId);
        project.setProgress(randomProgress());
        project.setStatus(randomStatus());

        Holder<User> users = getUsers(clientId);
        int size = users.getItems().size();
        int ownerId = ThreadLocalRandom.current().nextInt(0, size);
        project.setOwner(users.getItems().get(ownerId));

        Response response = target("departments").path(departmentId).path("projects")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(project.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);
        assertNotNull(response);
    }

    /******************************************************************************************************************/

    public void projectScenario(String projectId, String clientId) {
        Holder<Delivery> deliveries = getDeliveries(projectId);
        int before = deliveries.getItems().size();

        if( before < 3 ) {
            for( int i = 0; i < 3; i++ ) {
                createDelivery(projectId, clientId);
            }
        }

        deliveries = getDeliveries(projectId);
        List<Delivery> items = deliveries.getItems();
        for( Delivery delivery : items ) {
            deliveryScenario((String)delivery.get_id(), clientId);
        }
    }

    public Holder<Delivery> getDeliveries(String projectId) {
        // projects                                               deliveries
        String dlv_str = target("projects").path(projectId).path("deliveries").request().get(String.class);
        Type fooType = new TypeToken<Holder<Delivery>>() {}.getType();
        Holder<Delivery> usersHolder = new Gson().fromJson(dlv_str, fooType);
        return usersHolder;
    }

    public void createDelivery(String projectId, String clientId) {
        Holder<User> users = getUsers(clientId);
        User randomUser = getRandomUser(users);
        Delivery delivery = new Delivery();
        delivery.setAssignee(randomUser);
        delivery.setTitle( "D t " + getTSS() );
        delivery.setDescription( "Description " + getTSS() );
        delivery.setAssignee( randomUser );
        delivery.setProgress( randomProgress() );
        delivery.setStatus( randomStatus() );
        Response response = target("projects").path(projectId).path("deliveries")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(delivery.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);
        assertNotNull(response);
    }

    /******************************************************************************************************************/

    public void deliveryScenario(String deliveryId, String clientId) {
        Holder<User> users = getUsers(clientId);
        User randomUser = getRandomUser(users);
        deliveryAddComment("Comment " + getTSS(), deliveryId, randomUser );
    }

    public Delivery getDelivery(String deliveryId) {
        String str_delivery = target("deliveries").path(deliveryId).request().get(String.class);
        Delivery delivery = Delivery.deserialize(str_delivery, Delivery.class);
        return delivery;
    }

    public void setStatus( ) {

    }

    public void setProgress( ) {

    }

    public void deliveryAddComment(String str_comment, String deliveryId, User author) {
        System.out.println("delivery add comment ");
        Comment comment = new Comment();
        comment.setComment(str_comment);
        comment.setAuthor( author );
        comment.setTimestamp( String.valueOf(System.currentTimeMillis()) );
        Response response = target("deliveries").path(deliveryId).path("comment")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(comment.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class );
        assertNotNull(response);
    }

    public Holder<Comment> getComments(String deliveryId) {
        String comments_str = target("deliveries").path(deliveryId).path("comments").request().get(String.class);
        Type fooType = new TypeToken<Holder<Comment>>() {}.getType();
        Holder<Comment> holder = new Gson().fromJson(comments_str, fooType);
        return holder;
    }

    /******************************************************************************************************************/

    public void progressScenario() {

    }

    /******************************************************************************************************************/

    public String getClientId() {
        return (String) Mock.ME.getClient().get_id();
    }

    public String getTSS() {
        return new Date(System.currentTimeMillis()).toLocaleString();
    }

    public User getRandomUser(Holder<User> users) {
        int ownerId = ThreadLocalRandom.current().nextInt(0, users.getItems().size());
        return users.getItems().get(ownerId);
    }

    public StatusEnum randomStatus( ) {
        int status = ThreadLocalRandom.current().nextInt(0, 2);
        switch (status) {
            case 0:
                return StatusEnum.RED;
            case 1:
                return StatusEnum.ORANGE;
            default:
                return StatusEnum.GREEN;
        }
    }

    public int randomProgress( ) {
        return ThreadLocalRandom.current().nextInt(0, 100);
    }

    public User getRandomUser() {
        Holder<User> users = getUsers();
        int ownerId = ThreadLocalRandom.current().nextInt(0, users.getItems().size());
        return users.getItems().get(ownerId);
    }


    /******************************************************************************************************************/

    public void playWithStatusScenario( String clientId ) {
        Holder<Department> departments = getDepartments(clientId);
        playWithStatus(clientId, StatusEnum.GREEN);
        playWithStatus(clientId, StatusEnum.ORANGE);
        playWithStatus(clientId, StatusEnum.RED);
        playWithStatus(clientId, StatusEnum.GREEN);
        playWithStatus(clientId, StatusEnum.ORANGE);
    }

    public void playWithStatus(String clientId, StatusEnum newColor) {
        Holder<Department> departments = getDepartments(clientId);
        List<Department> items = departments.getItems();
        for( Department department : items ) {
            String departmentId = (String)department.get_id();
            colorifyDepartment(departmentId, newColor);
        }
        departments = getDepartments(clientId);
        checkDepartmentColor(departments.getItems(), newColor);
    }

    public void checkDepartmentColor(List<Department> items, StatusEnum newColor) {
        for( Department department : items) {
            assertTrue(department.getStatus().equals(newColor));
            checkProjects(getProjects((String) department.get_id()).getItems(), newColor);
        }
    }

    public void checkProjects(List<Project> items, StatusEnum newColor) {
        for( Project project : items) {
            assertTrue(project.getStatus().equals(newColor));
        }
    }

    public void colorifyDepartment(String departmentId, StatusEnum newStatus) {
        Holder<Project> projectsDepartment = getProjects(departmentId);
        List<Project> items = projectsDepartment.getItems();
        for(Project project : items) {
            colorifyProject((String)project.get_id(), newStatus);
        }

    }

    public void colorifyProject(String projectId, StatusEnum newStatus) {
        Holder<Delivery> deliveries = getDeliveries(projectId);
        List<Delivery> items = deliveries.getItems();

        for(Delivery delivery : items) {
            putDeliveryToColor((String)delivery.get_id(), newStatus);
        }
        deliveries = getDeliveries(projectId);
        items = deliveries.getItems();
        for(Delivery delivery : items) {
            assertTrue(delivery.getStatus().equals(newStatus));
        }
    }


    public void putDeliveryToColor(String deliveryId, StatusEnum newStatus) {
        //{id}/setStatus/{statusId}
        Response response = target("deliveries").path(deliveryId).path("setStatus").path(newStatus.getValue())
        .request().get(Response.class);
        assertNotNull(response);
    }

    public void playWithColorsChanges(String clientId) {
        Holder<Department> departments = getDepartments(clientId);
        Department department = departments.getItems().get(0);
        playColorChangesDepartment((String)department.get_id());
    }

    public void playColorChangesDepartment(String departmentId) {

    }

    /******************************************************************************************************************/

    public void playWithProgress(String clientId) {

    }

    public String[] replics;

    String toSplit = "Sed ut perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas sit, aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos, qui ratione voluptatem sequi nesciunt, neque porro quisquam est, qui dolorem ipsum, quia dolor sit, amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt, ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit, qui in ea voluptate velit esse, quam nihil molestiae consequatur, vel illum, qui dolorem eum fugiat, quo voluptas nulla pariatur? At vero eos et accusamus et iusto odio dignissimos ducimus, qui blanditiis praesentium voluptatum deleniti atque corrupti, quos dolores et quas molestias excepturi sint, obcaecati cupiditate non provident, similique sunt in culpa, qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio, cumque nihil impedit, quo minus id, quod maxime placeat, facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet, ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat.";
}
