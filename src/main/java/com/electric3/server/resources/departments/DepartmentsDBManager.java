package com.electric3.server.resources.departments;

import com.electric3.server.database.NoSqlBase;

import java.util.logging.Logger;

public class DepartmentsDBManager extends NoSqlBase {
    private static final DepartmentsDBManager departmentsDBManager = new DepartmentsDBManager();

    public static DepartmentsDBManager getInstance() {
        return departmentsDBManager;
    }

    private static Logger log = Logger.getLogger(DepartmentsDBManager.class.getName());

    private DepartmentsDBManager() {
    }
}
