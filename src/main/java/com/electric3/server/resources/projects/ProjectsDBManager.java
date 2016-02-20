package com.electric3.server.resources.projects;

import com.electric3.server.database.NoSqlBase;

import java.util.logging.Logger;

public class ProjectsDBManager extends NoSqlBase {
    private static final ProjectsDBManager projectsDBManager = new ProjectsDBManager();

    public static ProjectsDBManager getInstance() {
        return projectsDBManager;
    }

    private static Logger log = Logger.getLogger(ProjectsDBManager.class.getName());

    private ProjectsDBManager() {
    }
}
