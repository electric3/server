package com.electric3.server.resources.clients;

import com.electric3.dataatoms.*;
import com.electric3.server.database.NoSqlBase;
import com.electric3.server.resources.actions.ActionsDBManager;
import com.electric3.server.resources.users.UsersDBManager;
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

public class ClientsDBManager extends NoSqlBase {
    private static final ClientsDBManager clientsDBManager = new ClientsDBManager();

    public static ClientsDBManager getInstance() {
        return clientsDBManager;
    }

    private static Logger log = Logger.getLogger(ClientsDBManager.class.getName());

    private ClientsDBManager() {
    }

    public void createClient(Client client) throws Exception {
        log.info("createClient");

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_CLIENTS);
        client.setCreatedAt(UtilityMethods.getCurrentTimestampAsString());
        client.setModifiedAt(UtilityMethods.getCurrentTimestampAsString());
        collection.insertOne(Document.parse(client.serialize()));

        Document doc = collection.find(eq("owner.user_id", client.getOwner().getUser_id())).first();
        if (doc == null) {
            throw new Exception("no client found for user");
        }
        Client createdClient = Client.deserialize(doc.toJson(), Client.class);
        UsersDBManager usersDBManager = UsersDBManager.getInstance();
        usersDBManager.setUserClientId(createdClient.getOwner(), convertObjectId(createdClient.get_id()));
    }

    public String getClientDepartments(String clientId) {
        log.info("getClientDepartments for " + clientId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DEPARTMENTS);

        Holder<Department> departments = new Holder<>();
        List<Department> departmentsList = new ArrayList<>();

        collection.find(eq("clientId", clientId))
                .sort(new Document("modifiedAt", -1))
                .forEach((Block<Document>) doc -> {
                    Department department = Department.deserialize(doc.toJson(), Department.class);
                    department.set_id(convertObjectId(department.get_id()));
                    departmentsList.add(department);
                });

        departments.setItems(departmentsList);

        return departments.serialize();
    }

    public void createDepartment(String clientId, Department department) {
        log.info("createDepartment for " + clientId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DEPARTMENTS);
        department.setClientId(clientId);
        department.setCreatedAt(UtilityMethods.getCurrentTimestampAsString());
        department.setModifiedAt(UtilityMethods.getCurrentTimestampAsString());
        collection.insertOne(Document.parse(department.serialize()));

        collection = database.getCollection(MONGODB_COLLECTION_NAME_CLIENTS);
        Document clientDoc = collection.find(eq("_id", new ObjectId(clientId))).first();
        Client client = Client.deserialize(clientDoc.toJson(), Client.class);

        Action action = new Action();
        action.setTimestamp(UtilityMethods.getCurrentTimestampAsString());
        action.setClientId(clientId);

        action.setActionStringRepresentation(String.format("A new department '%s' has been created for client '%s",
                department.getTitle(), client.getTitle()));
        ActionsDBManager actionsDBManager = ActionsDBManager.getInstance();
        actionsDBManager.createAction(action);
    }

    public String getClientByOwner(String userId) throws Exception {
        log.info("getClientByOwner " + userId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_CLIENTS);

        Document doc = collection.find(eq("owner.user_id", userId)).first();
        if (doc == null) {
            throw new Exception("No client with such owner");
        }

        Client client = Client.deserialize(doc.toJson(), Client.class);
        client.set_id(convertObjectId(client.get_id()));
        return client.serialize();
    }

    public void setOwner(String clientId, User user) {
        log.info(String.format("set client %s new owner", clientId));

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_CLIENTS);
        collection.updateOne(eq("_id", new ObjectId(clientId)),
                new Document("$set",
                        new Document("owner", Document.parse(user.serialize())).
                                append("modifiedAt", UtilityMethods.getCurrentTimestampAsString())));

        Document clientDoc = collection.find(eq("_id", new ObjectId(clientId))).first();
        Client client = Client.deserialize(clientDoc.toJson(), Client.class);

        Action action = new Action();
        action.setTimestamp(UtilityMethods.getCurrentTimestampAsString());
        action.setClientId(clientId);

        action.setActionStringRepresentation(String.format("A new owner '%s' has been set for client '%s",
                user.getUser_metadata().getName(), client.getTitle()));
        ActionsDBManager actionsDBManager = ActionsDBManager.getInstance();
        actionsDBManager.createAction(action);
    }
}
