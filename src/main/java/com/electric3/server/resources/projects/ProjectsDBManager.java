package com.electric3.server.resources.projects;

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

public class ProjectsDBManager extends NoSqlBase {
    private static final ProjectsDBManager projectsDBManager = new ProjectsDBManager();

    public static ProjectsDBManager getInstance() {
        return projectsDBManager;
    }

    private static Logger log = Logger.getLogger(ProjectsDBManager.class.getName());

    private ProjectsDBManager() {
    }

    public String getProjectDeliveries(String projectId) {
        log.info("getProjectDeliveries for " + projectId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DELIVERIES);

        Holder<Delivery> deliveriesHolder = new Holder<>();
        List<Delivery> deliveries = new ArrayList<>();

        collection.find(eq("projectId", projectId))
                .sort(new Document("modifiedAt", -1))
                .forEach((Block<Document>) doc -> {
                    Delivery delivery = Delivery.deserialize(doc.toJson(), Delivery.class);
                    delivery.set_id(convertObjectId(delivery.get_id()));
                    deliveries.add(delivery);
                });

        deliveriesHolder.setItems(deliveries);

        return deliveriesHolder.serialize();
    }

    public void createDelivery(String projectId, Delivery delivery) {
        log.info("createProject for " + projectId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DELIVERIES);
        delivery.setProjectId(projectId);
        delivery.setCreatedAt(UtilityMethods.getCurrentTimestampAsString());
        delivery.setModifiedAt(UtilityMethods.getCurrentTimestampAsString());
        collection.insertOne(Document.parse(delivery.serialize()));

        collection = database.getCollection(MONGODB_COLLECTION_NAME_PROJECTS);
        Document projectDoc = collection.find(eq("_id", new ObjectId(projectId))).first();
        Project project = Project.deserialize(projectDoc.toJson(), Project.class);

        Action action = new Action();
        action.setTimestamp(UtilityMethods.getCurrentTimestampAsString());
        action.setProjectId(delivery.getProjectId());

        action.setActionStringRepresentation(String.format("A new delivery '%s' has been created in project '%s",
                delivery.getTitle(), project.getTitle()));
        ActionsDBManager actionsDBManager = ActionsDBManager.getInstance();
        actionsDBManager.createAction(action);
    }

    public void setOwner(String projectId, User user) {
        log.info(String.format("set project %s new owner", projectId));

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_PROJECTS);
        collection.updateOne(eq("_id", new ObjectId(projectId)),
                new Document("$set",
                        new Document("owner", Document.parse(user.serialize())).
                                append("modifiedAt", UtilityMethods.getCurrentTimestampAsString())));

        Document projectDoc = collection.find(eq("_id", new ObjectId(projectId))).first();
        Project project = Project.deserialize(projectDoc.toJson(), Project.class);

        Action action = new Action();
        action.setTimestamp(UtilityMethods.getCurrentTimestampAsString());
        action.setProjectId(projectId);

        action.setActionStringRepresentation(String.format("A new owner '%s' has been set for project '%s",
                user.getUser_metadata().getName(), project.getTitle()));
        ActionsDBManager actionsDBManager = ActionsDBManager.getInstance();
        actionsDBManager.createAction(action);
    }

    public String getUserProjects(String userId) {
        log.info("getUserProjects for " + userId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_PROJECTS);

        Holder<Project> projectsHolder = new Holder<>();
        List<Project> projects = new ArrayList<>();

        collection.find(eq("owner.user_id", userId))
                .sort(new Document("modifiedAt", -1))
                .forEach((Block<Document>) doc -> {
                    Project project = Project.deserialize(doc.toJson(), Project.class);
                    project.set_id(convertObjectId(project.get_id()));
                    projects.add(project);
                });

        projectsHolder.setItems(projects);

        return projectsHolder.serialize();
    }

    public void setStatusRecalculation(String projectId) {
        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_PROJECTS);

        collection.updateOne(eq("_id", new ObjectId(projectId)),
                new Document("$set",
                        new Document("isStatusRecalculationRequired", 1).
                                append("modifiedAt", UtilityMethods.getCurrentTimestampAsString())));

        Document projectDoc = collection.find(eq("_id", new ObjectId(projectId))).first();
        Project project = Project.deserialize(projectDoc.toJson(), Project.class);

        collection = database.getCollection(MONGODB_COLLECTION_NAME_DEPARTMENTS);
        collection.updateOne(eq("_id", new ObjectId(project.getDepartmentId())),
                new Document("$set",
                        new Document("isStatusRecalculationRequired", 1).
                                append("modifiedAt", UtilityMethods.getCurrentTimestampAsString())));
    }

    public void setProgressRecalculation(String projectId) {
        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_PROJECTS);

        collection.updateOne(eq("_id", new ObjectId(projectId)),
                new Document("$set",
                        new Document("isProgressRecalculationRequired", 1).
                                append("modifiedAt", UtilityMethods.getCurrentTimestampAsString())));

        Document projectDoc = collection.find(eq("_id", new ObjectId(projectId))).first();
        Project project = Project.deserialize(projectDoc.toJson(), Project.class);

        collection = database.getCollection(MONGODB_COLLECTION_NAME_DEPARTMENTS);
        collection.updateOne(eq("_id", new ObjectId(project.getDepartmentId())),
                new Document("$set",
                        new Document("isProgressRecalculationRequired", 1).
                                append("modifiedAt", UtilityMethods.getCurrentTimestampAsString())));
    }
}
