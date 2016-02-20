package com.electric3.server.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceUtils {
    public static String getStackTrace(Exception ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}
