package com.electric3.server.resources.departments;

import com.electric3.dataatoms.*;
import com.electric3.server.database.NoSqlBase;
import com.electric3.server.resources.actions.ActionsDBManager;
import com.electric3.server.utils.StatusCalculator;
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
        List<Project> projectsList = getDepartmentProjects(departmentId, collection);
        projects.setItems(projectsList);

        return projects.serialize();
    }

    public List<Project> getDepartmentProjects(String departmentId, MongoCollection<Document> collection) {
        List<Project> projectsList = new ArrayList<>();

        collection.find(eq("departmentId", departmentId))
                .sort(new Document("modifiedAt", -1))
                .forEach((Block<Document>) doc -> {
                    Project project = Project.deserialize(doc.toJson(), Project.class);
                    project.set_id(convertObjectId(project.get_id()));
                    projectsList.add(project);
                });

        return projectsList;
    }

    public void recalculateStatus(String departmentId, StatusEnum newStatus) {

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DEPARTMENTS);

        if( !newStatus.equals(StatusEnum.RED) ) {
            Document document = collection.find(eq("_id", departmentId)).first();
            if( null != document ) {
                MongoCollection<Document> projectsCollection = database.getCollection(MONGODB_COLLECTION_NAME_PROJECTS);
                List<Project> projects = getDepartmentProjects(departmentId, projectsCollection);
                newStatus = StatusCalculator.ME.selectWorseOnSet(projects);
            }
        }

        collection.updateOne(eq("_id", new ObjectId(departmentId)),
                new Document("$set",
                        new Document("status", newStatus.getValue()).
                                append("modifiedAt", UtilityMethods.getCurrentTimestampAsString())));
    }

    public void recalculateProgress(String departmentId) {
        //TODO calculate

        int newProgress = 0;

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DEPARTMENTS);

        collection.updateOne(eq("_id", new ObjectId(departmentId)),
                new Document("$set",
                        new Document("progress", newProgress).
                                append("modifiedAt", UtilityMethods.getCurrentTimestampAsString())));
    }
}
