package com.alphateam.app.configurtions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;

abstract public class Configuration {
    protected Logger logger = LogManager.getLogger(getClass().getName());
    protected String fileName;

    public Configuration(String fileName){
        this.fileName = fileName;
    }

    abstract public void reload();

}
