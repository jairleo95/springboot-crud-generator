package com.alphateam.app.configurtions;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ReadConfig {

    private Logger log = LogManager.getLogger(getClass().getName());

    public Config loadConfigFile(String filename) throws FileNotFoundException {

       log.debug("fileName:" + filename);
       return  new Gson().fromJson(new FileReader(filename), Config.class);
    }
}
