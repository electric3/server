package com.electric3.server.resources.users;

import com.electric3.server.database.NoSqlBase;

import java.util.logging.Logger;

public class UsersDBManager extends NoSqlBase {
    private static final UsersDBManager usersDBManager = new UsersDBManager();

    public static UsersDBManager getInstance() {
        return usersDBManager;
    }

    private static Logger log = Logger.getLogger(UsersDBManager.class.getName());

    private UsersDBManager() {
    }
}
