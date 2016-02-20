package com.electric3.server.resources.actions;

import com.electric3.server.database.NoSqlBase;

import java.util.logging.Logger;

public class ActionsDBManager extends NoSqlBase {
    private static final ActionsDBManager actionsDBManager = new ActionsDBManager();

    public static ActionsDBManager getInstance() {
        return actionsDBManager;
    }

    private static Logger log = Logger.getLogger(ActionsDBManager.class.getName());

    private ActionsDBManager() {
    }
}
