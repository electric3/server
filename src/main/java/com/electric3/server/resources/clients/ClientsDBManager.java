package com.electric3.server.resources.clients;

import com.electric3.dataatoms.Client;
import com.electric3.dataatoms.Department;
import com.electric3.dataatoms.Holder;
import com.electric3.server.database.NoSqlBase;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

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

    public void createClient(Client client) {
        log.info("createClient");

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_CLIENTS);
        collection.insertOne(Document.parse(client.serialize()));
    }

    public String getClientDepartments(String clientId) {
        log.info("getClientDepartments for " + clientId);

        MongoDatabase database = ConnectionFactory.CONNECTION.getClientDatabase();
        MongoCollection<Document> collection = database.getCollection(MONGODB_COLLECTION_NAME_DEPARTMENTS);

        Holder<Department> departments = new Holder<>();
        List<Department> departmentsList = new ArrayList<>();

        collection.find(eq("clientId", clientId)).forEach((Block<Document>) doc -> {
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
        collection.insertOne(Document.parse(department.serialize()));
    }
}