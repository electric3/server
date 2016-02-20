package com.electric3.server.resources.departments;

import com.electric3.dataatoms.Holder;
import com.electric3.dataatoms.Project;
import com.electric3.dataatoms.User;
import com.electric3.server.database.NoSqlBase;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

public class DepartmentsDBManager extends NoSqlBase {
    private static final DepartmentsDBManager departmentsDBManager = new DepartmentsDBManager();

    public static DepartmentsDBManager getInstance() {
        return departmentsDBManager;
    }

    private static Logger log = Logger.getLogger(DepartmentsDBManager.class.getName());

    private DepartmentsDBManager() {
    }


    public void createProject(String departmentId, Project project) {
        log.info("createProject for " + departmentId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_PROJECTS);
        project.setDepartmentId(departmentId);
        collection.insertOne(Document.parse(project.serialize()));
    }

    public String getDepartmentProjects(String departmentId) {
        log.info("getDepartmentProjects for " + departmentId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_PROJECTS);

        Holder<Project> projects = new Holder<>();
        List<Project> projectsList = new ArrayList<>();

        collection.find(eq("departmentId", departmentId)).forEach((Block<Document>) doc -> {
            Project project = Project.deserialize(doc.toJson(), Project.class);
            project.set_id(convertObjectId(project.get_id()));
            projectsList.add(project);
        });

        projects.setItems(projectsList);

        return projects.serialize();
    }

    public String getDepartmentUsers(String departmentId) {
        return null;
    }

    public void createUser(String departmentId, User user) {

    }
}
