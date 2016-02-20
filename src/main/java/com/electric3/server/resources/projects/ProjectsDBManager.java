package com.electric3.server.resources.projects;

import com.electric3.dataatoms.Delivery;
import com.electric3.dataatoms.Holder;
import com.electric3.dataatoms.User;
import com.electric3.server.database.NoSqlBase;
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
    }

    public void setOwner(String projectId, User user) {
        log.info(String.format("set project %s new owner", projectId));

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_PROJECTS);
        collection.updateOne(eq("_id", new ObjectId(projectId)),
                new Document("$set",
                        new Document("owner", Document.parse(user.serialize())).
                                append("modifiedAt", UtilityMethods.getCurrentTimestampAsString())));
    }
}
