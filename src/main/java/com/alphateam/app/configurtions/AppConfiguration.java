package com.alphateam.app.configurtions;

import java.io.FileNotFoundException;

public class AppConfiguration extends Configuration {

    static AppConfiguration theInstance = null;
    private Config config;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public AppConfiguration(String filename){
        super(filename);
        read(fileName);
    }

    public static void initialize(String filename){
        if (theInstance == null){
            theInstance = new AppConfiguration(filename);
        }
    }
    public static AppConfiguration instance(){ return theInstance;}

    public void read(String fileName){

        try {
            setConfig(new ReadConfig().loadConfigFile(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        logger.debug(config.toString());

    }
    @Override
    public void reload() {
        //
    }
}
