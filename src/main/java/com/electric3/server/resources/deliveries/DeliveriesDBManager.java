package com.electric3.server.resources.deliveries;

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
import static java.util.Arrays.asList;

public class DeliveriesDBManager extends NoSqlBase {
    private static final DeliveriesDBManager deliveriesDBManager = new DeliveriesDBManager();

    public static DeliveriesDBManager getInstance() {
        return deliveriesDBManager;
    }

    private static Logger log = Logger.getLogger(DeliveriesDBManager.class.getName());

    private DeliveriesDBManager() {
    }

    public String getDelivery(String deliveryId) throws Exception {
        log.info("getDelivery " + deliveryId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DELIVERIES);

        Document doc = collection.find(eq("_id", new ObjectId(deliveryId))).first();
        if (doc == null) {
            throw new Exception("No delivery with a specified id");
        }

        Delivery delivery = Delivery.deserialize(doc.toJson(), Delivery.class);
        delivery.set_id(convertObjectId(delivery.get_id()));
        return delivery.serialize();
    }

    public String getDeliveryComments(String deliveryId) {
        log.info("getDeliveryComments for " + deliveryId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DELIVERIES);

        Document doc = collection.aggregate(
                asList(new Document("$match", new Document("_id", new ObjectId(deliveryId))),
                        new Document("$unwind", "$comments"), new Document("$sort", new Document("comments.timestamp", -1)),
                        new Document("$group", new Document("_id", "$_id").append("comments", new Document("$push", "$comments"))))).first();

        Holder<Comment> holder = new Holder<>();

        if (doc != null) {
            Delivery delivery = Delivery.deserialize(doc.toJson(), Delivery.class);

            holder.setItems(delivery.getComments());
        }

        return holder.serialize();
    }

    private Delivery getDeliveryById(String deliveryId, MongoCollection<Document> collection) {
        Document document = collection.find(eq("_id", new ObjectId(deliveryId))).first();
        return Delivery.deserialize(document.toJson(), Delivery.class);
    }

    public void setStatus(String deliveryId, int statusId) {
        log.info(String.format("setStatus %d for %s", statusId, deliveryId));

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DELIVERIES);
        ObjectId objectId = new ObjectId(deliveryId);
        collection.updateOne(eq("_id", objectId),
                new Document("$set",
                        new Document("status", statusId).
                                append("modifiedAt", UtilityMethods.getCurrentTimestampAsString())));

        Delivery delivery = getDeliveryById(deliveryId, collection);
        Action action = new Action();
        action.setTimestamp(UtilityMethods.getCurrentTimestampAsString());
        action.setProjectId(delivery.getProjectId());

        String text = String.format("Delivery '%s' status has been changed to %s",
                delivery.getTitle(), StatusEnum.values()[statusId]);

        action.setActionStringRepresentation(text);
        ActionsDBManager actionsDBManager = ActionsDBManager.getInstance();
        actionsDBManager.createAction(action);

        Comment comment = new Comment();
        comment.setTimestamp(UtilityMethods.getCurrentTimestampAsString());
        comment.setComment(text);
        addComment(deliveryId, comment);
    }

    public void setProgress(String deliveryId, int progressValue) {
        log.info(String.format("setProgress %d for %s", progressValue, deliveryId));

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DELIVERIES);
        collection.updateOne(eq("_id", new ObjectId(deliveryId)),
                new Document("$set",
                        new Document("progress", progressValue).
                                append("modifiedAt", UtilityMethods.getCurrentTimestampAsString())));

        Delivery delivery = getDeliveryById(deliveryId, collection);
        Action action = new Action();
        action.setTimestamp(UtilityMethods.getCurrentTimestampAsString());
        action.setProjectId(delivery.getProjectId());

        String text = String.format("Delivery '%s' progress has been changed to %s",
                delivery.getTitle(), progressValue);

        action.setActionStringRepresentation(text);
        ActionsDBManager actionsDBManager = ActionsDBManager.getInstance();
        actionsDBManager.createAction(action);

        Comment comment = new Comment();
        comment.setTimestamp(UtilityMethods.getCurrentTimestampAsString());
        comment.setComment(text);
        addComment(deliveryId, comment);
    }

    public void addComment(String deliveryId, Comment comment) {
        log.info("addComment for " + deliveryId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DELIVERIES);
        ObjectId objectId = new ObjectId(deliveryId);
        comment.setTimestamp(UtilityMethods.getCurrentTimestampAsString());
        collection.updateOne(eq("_id", objectId),
                new Document("$push",
                        new Document("comments", Document.parse(comment.serialize()))));

        collection.updateOne(eq("_id", objectId),
                new Document("$set",
                        new Document("modifiedAt", UtilityMethods.getCurrentTimestampAsString())));

        Delivery delivery = getDeliveryById(deliveryId, collection);
        Action action = new Action();
        action.setTimestamp(UtilityMethods.getCurrentTimestampAsString());
        action.setProjectId(delivery.getProjectId());
        action.setActionStringRepresentation(String.format("Delivery '%s' has a new comment",
                delivery.getTitle()));
        ActionsDBManager actionsDBManager = ActionsDBManager.getInstance();
        actionsDBManager.createAction(action);
    }

    public void setAssignee(String deliveryId, User user) {
        log.info(String.format("set delivery %s new assignee", deliveryId));

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DELIVERIES);
        collection.updateOne(eq("_id", new ObjectId(deliveryId)),
                new Document("$set",
                        new Document("assignee", Document.parse(user.serialize())).
                                append("modifiedAt", UtilityMethods.getCurrentTimestampAsString())));

        Delivery delivery = getDeliveryById(deliveryId, collection);
        Action action = new Action();
        action.setTimestamp(UtilityMethods.getCurrentTimestampAsString());
        action.setProjectId(delivery.getProjectId());

        String text = String.format("Delivery '%s' has a new assignee",
                delivery.getTitle());

        action.setActionStringRepresentation(text);
        ActionsDBManager actionsDBManager = ActionsDBManager.getInstance();
        actionsDBManager.createAction(action);

        Comment comment = new Comment();
        comment.setTimestamp(UtilityMethods.getCurrentTimestampAsString());
        comment.setComment(text);
        addComment(deliveryId, comment);
    }

    public String getUserDeliveries(String userId) {
        log.info("getUserDeliveries for " + userId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DELIVERIES);

        Holder<Delivery> deliveriesHolder = new Holder<>();
        List<Delivery> deliveries = new ArrayList<>();

        collection.find(eq("assignee.user_id", userId))
                .sort(new Document("modifiedAt", -1))
                .forEach((Block<Document>) doc -> {
                    Delivery delivery = Delivery.deserialize(doc.toJson(), Delivery.class);
                    delivery.set_id(convertObjectId(delivery.get_id()));
                    deliveries.add(delivery);
                });

        deliveriesHolder.setItems(deliveries);

        return deliveriesHolder.serialize();
    }
}
