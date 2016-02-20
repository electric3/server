package com.electric3.server.database;

import com.electric3.server.utils.StackTraceUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mongodb.MongoClient;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;
import java.util.logging.Logger;

public class NoSqlBase {

    protected final String DBNAME = "projectrack";

    public enum ConnectionFactory {
        CONNECTION;
        private Logger log = null;
        private MongoClient client = null;

        ConnectionFactory() {
            try {
                client = new MongoClient();
            } catch (Exception e) {
                log = Logger.getLogger(ConnectionFactory.class.getName());
                log.severe(StackTraceUtils.getStackTrace(e));
            }
        }

        public MongoClient getClient() {
            if (client == null)
                throw new RuntimeException();
            return client;
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
