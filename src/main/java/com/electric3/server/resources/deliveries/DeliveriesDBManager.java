package com.electric3.server.resources.deliveries;

import com.electric3.dataatoms.Comment;
import com.electric3.dataatoms.Delivery;
import com.electric3.dataatoms.Holder;
import com.electric3.dataatoms.User;
import com.electric3.server.database.NoSqlBase;
import com.electric3.server.utils.UtilityMethods;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

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

    public void setStatus(String deliveryId, int statusId) {
        log.info(String.format("setStatus %d for %s", statusId, deliveryId));

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DELIVERIES);
        collection.updateOne(eq("_id", new ObjectId(deliveryId)),
                new Document("$set",
                        new Document("status", statusId).
                                append("modifiedAt", UtilityMethods.getCurrentTimestampAsString())));
    }

    public void setProgress(String deliveryId, int progressValue) {
        log.info(String.format("setProgress %d for %s", progressValue, deliveryId));

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DELIVERIES);
        collection.updateOne(eq("_id", new ObjectId(deliveryId)),
                new Document("$set",
                        new Document("progress", progressValue).
                                append("modifiedAt", UtilityMethods.getCurrentTimestampAsString())));
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
    }

    public void setAssignee(String deliveryId, User user) {
        log.info(String.format("set delivery %s new assignee", deliveryId));

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DELIVERIES);
        collection.updateOne(eq("_id", new ObjectId(deliveryId)),
                new Document("$set",
                        new Document("assignee", Document.parse(user.serialize())).
                                append("modifiedAt", UtilityMethods.getCurrentTimestampAsString())));
    }
}
