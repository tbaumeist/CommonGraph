package com.tbaumeist.common.logging;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingManager {
    public static void initialize(){
        Logger logger = Logger.getLogger("");
        for (Handler h : logger.getHandlers()) {
            h.setFormatter(new ReallySimpleFormatter());
            h.setLevel(Level.SEVERE);
        }
    }
    
    public static void addLogFile(String fileName, Level level) throws Exception{
        if(fileName == null || fileName.isEmpty())
            return;
        
        Logger logger = Logger.getLogger("");
        FileHandler file = new FileHandler(fileName);
        file.setLevel(level);
        file.setFormatter(new ReallySimpleFormatter());
        logger.addHandler(file);
        logger.info("Added file logger " + fileName);
    }
}
