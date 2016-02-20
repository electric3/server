package com.electric3.server.resources.client;

import com.electric3.dataatoms.*;
import com.electric3.server.resources.clients.ClientsResource;
import com.electric3.server.resources.departments.DepartmentsResource;
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
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ClientResourceTest extends JerseyTest {
    @Override
    protected Application configure() {
        return new ResourceConfig(ClientsResource.class, DepartmentsResource.class);
    }

    @Test
    public void testForDirector() {
        final String client_response_str =
                target("clients")
                        .path("info")
                        .path(Mock.getUserDirector().getUser_id())
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
        userMetadata.setName("F: " + new Date(System.currentTimeMillis()).toLocaleString() );
        userMetadata.setSkypeName("skype");
        userMetadata.setPhotoUrl("https://pbs.twimg.com/profile_images/679653172244262912/q8D6AbS4.jpg");
        userMetadata.setClientId(clientId);
        userMetadata.setPhone("+" + System.currentTimeMillis());
        userMetadata.setRole( UserRole.EMPLOYEE );
        user.setUser_metadata(userMetadata);
        user.setPassword("xyi");
        user.setEmail(System.currentTimeMillis() + "@" + System.currentTimeMillis() + ".com");

        Response user_resp =
                target("clients")
                        .path(clientId).path("/users")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(user.serialize(),MediaType.APPLICATION_JSON_TYPE), Response.class);

        assertNotNull(user_resp);
    }

    public Holder<User> getUsers(String clientId) {
        String users_str =
                target("clients")
                        .path(clientId).path("/users")
                        .request()
                        .get(String.class);

        Holder<User> usersHolder = new Gson().fromJson(users_str, Holder.class);
        return usersHolder;
    }
    /******************************************************************************************************************/

    public void departmentsScript(String clientId) {
        Holder<Department> departments = getDepartments();
        int before = departments.getItems().size();
        createNewDepartment();
        int after = getDepartments().getItems().size();
        assertTrue((after - before) == 1);

        Holder<Department> forDepP = getDepartments();
        List<Department> deps = forDepP.getItems();
        for(Department department : deps ) {
            projectScript(clientId, (String) department.get_id());
        }
    }

    public void createNewDepartment() {
        Department newDep = new Department();
        newDep.setDescription("this is description " + new Date(System.currentTimeMillis()).toLocaleString());
        newDep.setOwner(Mock.getUserManager());
        newDep.setClientId((String) Mock.ME.getClient().get_id());
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

        Holder<Department> departmentsHolder = new Gson().fromJson(deps_str, Holder.class);

        return departmentsHolder;
    }

    /******************************************************************************************************************/

    public void projectScript(String clientId, String departmentId) {
        int before = getProjectsDepartment(departmentId).getItems().size();
        createProjectForDepartment(clientId, departmentId);
        int after = getProjectsDepartment(departmentId).getItems().size();
        assertTrue( (after - before) == 0 );
    }

    public Holder<Project> getProjectsDepartment(String departmentId) {
        String result = target("departments").path(departmentId).path("projects")
                .request().get(String.class);
        Holder<Project> departmentsHolder = new Gson().fromJson(result, Holder.class);
        return departmentsHolder;
    }

    public void createProjectForDepartment(String clientId, String departmentId) {
        Project project = new Project();
        project.setTitle("Project " + getTSS());
        project.setDescription("Description " + getTSS());
        project.setDeadline( System.currentTimeMillis() + 60 * 60 * 1000 * 12 );
        project.setDepartmentId(departmentId);

        Holder<User> users = getUsers(clientId);
        int size = users.getItems().size();
        int ownerId = ThreadLocalRandom.current().nextInt(0, size);
        project.setOwner(users.getItems().get(ownerId));

        Response response = target("departments").path(departmentId).path("projects")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post( Entity.entity(project.serialize(), MediaType.APPLICATION_JSON_TYPE), Response.class);
        assertNotNull(response);
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

}
