package com.electric3.server.resources.comments;

import com.electric3.server.database.NoSqlBase;

import java.util.logging.Logger;

public class CommentsDBManager extends NoSqlBase {
    private static final CommentsDBManager commentsDBManager = new CommentsDBManager();

    public static CommentsDBManager getInstance() {
        return commentsDBManager;
    }

    private static Logger log = Logger.getLogger(CommentsDBManager.class.getName());

    private CommentsDBManager() {
    }
}
