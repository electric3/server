package com.electric3.server.resources.actions;

import com.electric3.dataatoms.Action;
import com.electric3.dataatoms.Holder;
import com.electric3.server.database.NoSqlBase;
import com.electric3.server.utils.UtilityMethods;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

public class ActionsDBManager extends NoSqlBase {
    private static final ActionsDBManager actionsDBManager = new ActionsDBManager();

    public static ActionsDBManager getInstance() {
        return actionsDBManager;
    }

    private static Logger log = Logger.getLogger(ActionsDBManager.class.getName());

    private ActionsDBManager() {
    }

    public String getClientActions(String clientId) {
        log.info("getClientActions for " + clientId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_ACTIONS);

        Holder<Action> actionsHolder = new Holder<>();
        List<Action> actions = new ArrayList<>();

        collection.find(eq("clientId", clientId))
                .sort(new Document("timestamp", -1))
                .forEach((Block<Document>) doc -> {
                    Action action = Action.deserialize(doc.toJson(), Action.class);
                    action.set_id(convertObjectId(action.get_id()));
                    actions.add(action);
                });

        actionsHolder.setItems(actions);

        return actionsHolder.serialize();
    }

    public String getDepartmentActions(String departmentId) {
        log.info("getDepartmentActions for " + departmentId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_ACTIONS);

        Holder<Action> actionsHolder = new Holder<>();
        List<Action> actions = new ArrayList<>();

        collection.find(eq("departmentId", departmentId))
                .sort(new Document("timestamp", -1))
                .forEach((Block<Document>) doc -> {
                    Action action = Action.deserialize(doc.toJson(), Action.class);
                    action.set_id(convertObjectId(action.get_id()));
                    actions.add(action);
                });

        actionsHolder.setItems(actions);

        return actionsHolder.serialize();
    }

    public String getProjectActions(String projectId) {
        log.info("getProjectActions for " + projectId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_ACTIONS);

        Holder<Action> actionsHolder = new Holder<>();
        List<Action> actions = new ArrayList<>();

        collection.find(eq("projectId", projectId))
                .sort(new Document("timestamp", -1))
                .forEach((Block<Document>) doc -> {
                    Action action = Action.deserialize(doc.toJson(), Action.class);
                    action.set_id(convertObjectId(action.get_id()));
                    actions.add(action);
                });

        actionsHolder.setItems(actions);

        return actionsHolder.serialize();
    }

    public void createAction(Action action) {
        log.info("create action");

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_ACTIONS);
        action.setTimestamp(UtilityMethods.getCurrentTimestampAsString());
        collection.insertOne(Document.parse(action.serialize()));

    }
}
