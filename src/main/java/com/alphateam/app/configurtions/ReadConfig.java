package com.alphateam.app.configurtions;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadConfig {

    private Logger log = LogManager.getLogger(getClass().getName());

    public Config loadConfigFile(String filename) {
        Reader reader = null;
        try {
            log.debug("fileName:" + filename);
            reader = Files.newBufferedReader(Paths.get(filename));
            System.out.println("fileName:"+reader.toString());
        } catch (Exception ex) {
             ex.printStackTrace();
         }

       return new Gson().fromJson(reader, Config.class);
    }
}
