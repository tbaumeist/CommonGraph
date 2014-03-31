package com.tbaumeist.common.logging;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingManager {
    public static void initialize(){
        Logger logger = Logger.getLogger("");
        for (Handler h : logger.getHandlers()) {
            h.setFormatter(new ReallySimpleFormatter());
            h.setLevel(Level.ALL);
        }
    }
}
