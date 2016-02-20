package com.electric3.server.database;

import com.electric3.server.utils.StackTraceUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;
import java.util.logging.Logger;

public class NoSqlBase {

    private static final String DBNAME = "projectrack";

    protected static final String MONGODB_COLLECTION_NAME_DELIVERIES = "deliveries";

    public enum ConnectionFactory {
        CONNECTION;
        private Logger log = null;
        private MongoClient client = null;
        private MongoDatabase database = null;

        ConnectionFactory() {
            try {
                client = new MongoClient(new MongoClientURI("mongodb://casper1149.koding.io:27017"));
                database = client.getDatabase(DBNAME);
            } catch (Exception e) {
                log = Logger.getLogger(ConnectionFactory.class.getName());
                log.severe(StackTraceUtils.getStackTrace(e));
            }
        }

        public MongoDatabase getClientDatabase() {
            if (client == null || database == null)
                throw new RuntimeException();
            return database;
        }
    }

    public String convertObjectId(Object id) {
        if (id == null) {
            return "";
        }
        return id.toString().replaceAll("\\{\\$oid=(.*?)\\}", "$1");
    }

    public class ObjectIdSerializer implements JsonSerializer<ObjectId> {
        @Override
        public JsonElement serialize(ObjectId objectId, Type type, JsonSerializationContext jsonSerializationContext) {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("$oid", objectId.toString());
            return jsonObject;
        }
    }
}
