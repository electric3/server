package com.electric3.server.resources.deliveries;

import com.electric3.server.database.NoSqlBase;

import java.util.logging.Logger;

public class DeliveriesDBManager extends NoSqlBase {
    private static final DeliveriesDBManager deliveriesDBManager = new DeliveriesDBManager();

    public static DeliveriesDBManager getInstance() {
        return deliveriesDBManager;
    }

    private static Logger log = Logger.getLogger(DeliveriesDBManager.class.getName());

    private DeliveriesDBManager() {
    }
}
