package com.electric3.server.resources.client;

import com.electric3.dataatoms.*;
import com.electric3.server.resources.clients.ClientsResource;
import com.electric3.server.resources.departments.DepartmentsResource;
import com.electric3.server.resources.projects.ProjectsResource;
import com.electric3.server.resources.users.UsersResource;
import com.electric3.server.resources.utils.Mock;
import com.google.gson.Gson;
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
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ClientResourceTest extends JerseyTest {
    @Override
    protected Application configure() {
        return new ResourceConfig(ClientsResource.class, DepartmentsResource.class, UsersResource.class, ProjectsResource.class);
    }

    @Test
    public void testForDirector() {
        ivanScenario();
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
        createUser(clientId);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int after = getUsers(clientId).getItems().size();
        assertTrue((after - before) == 1);
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
        int before = departments.getItems().size();
        createNewDepartment(getRandomUser());
        int after = getDepartments().getItems().size();
        assertTrue((after - before) == 1);

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
        int before = getProjectsDepartment(departmentId).getItems().size();
        if( before < 6 ) {
            createProjectForDepartment(clientId, departmentId);
            int after = getProjectsDepartment(departmentId).getItems().size();
            assertTrue((after - before) == 1);
        }

        List<Project> items = getProjectsDepartment(departmentId).getItems();
        for(Project project : items) {
            projectScenario((String)project.get_id(), clientId);
        }

    }

    public Holder<Project> getProjectsDepartment(String departmentId) {
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

        if( before < 6 ) {
            createDelivery(projectId, clientId);
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
        Comment comment = new Comment();
        comment.setComment(str_comment);
        comment.setAuthor( author );
        comment.setTimestamp( String.valueOf(System.currentTimeMillis()) );
        Response response = target("deliveries").path(deliveryId).path("comment")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(comment, MediaType.APPLICATION_JSON_TYPE), Response.class );
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

}
