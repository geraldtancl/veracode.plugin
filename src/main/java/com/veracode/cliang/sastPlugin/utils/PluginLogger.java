package com.veracode.cliang.sastPlugin.utils;

import com.intellij.openapi.diagnostic.Logger;

public class PluginLogger {

    private static final String PREFIX = "VC_GERALD_PLUGIN - ";

    public static void info(Class c, String message) {
        Logger.getInstance(c).info(PREFIX + message);
    }

    public static void error(Class c, String message) {
        Logger.getInstance(c).error(PREFIX + message);
    }

    public static void error(Class c, String message, Exception e) {
        Logger.getInstance(c).error(PREFIX + message, e);
    }

}
