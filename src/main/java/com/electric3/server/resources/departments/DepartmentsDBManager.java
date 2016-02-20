package com.electric3.server.resources.departments;

import com.electric3.dataatoms.*;
import com.electric3.server.database.NoSqlBase;
import com.electric3.server.resources.actions.ActionsDBManager;
import com.electric3.server.utils.UtilityMethods;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

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
        project.setCreatedAt(UtilityMethods.getCurrentTimestampAsString());
        project.setModifiedAt(UtilityMethods.getCurrentTimestampAsString());
        collection.insertOne(Document.parse(project.serialize()));

        collection = database.getCollection(MONGODB_COLLECTION_NAME_DEPARTMENTS);
        Document departmentDoc = collection.find(eq("_id", new ObjectId(departmentId))).first();
        Department department = Project.deserialize(departmentDoc.toJson(), Department.class);

        Action action = new Action();
        action.setTimestamp(UtilityMethods.getCurrentTimestampAsString());
        action.setDepartmentId(departmentId);

        action.setActionStringRepresentation(String.format("A new project '%s' has been created in department '%s",
                project.getTitle(), department.getTitle()));
        ActionsDBManager actionsDBManager = ActionsDBManager.getInstance();
        actionsDBManager.createAction(action);
    }

    public String getDepartmentProjects(String departmentId) {
        log.info("getDepartmentProjects for " + departmentId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_PROJECTS);

        Holder<Project> projects = new Holder<>();
        List<Project> projectsList = new ArrayList<>();

        collection.find(eq("departmentId", departmentId))
                .sort(new Document("modifiedAt", -1))
                .forEach((Block<Document>) doc -> {
                    Project project = Project.deserialize(doc.toJson(), Project.class);
                    project.set_id(convertObjectId(project.get_id()));
                    projectsList.add(project);
                });

        projects.setItems(projectsList);

        return projects.serialize();
    }
}
