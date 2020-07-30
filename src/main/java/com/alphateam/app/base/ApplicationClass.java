package com.alphateam.app.base;

import com.alphateam.app.configurtions.Config;
import com.alphateam.app.configurtions.Configuration;
import com.alphateam.app.configurtions.ReadConfig;
import com.alphateam.query.Column;
import com.alphateam.query.DAO;
import com.alphateam.query.Table;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class ApplicationClass extends Configuration {

    private static ApplicationClass singleton = null;

    //private Config config;

    private List<Table> tableList;
    private List<Column> columnsList;

    public List<Column> getColumnsList() {
        return columnsList;
    }

    public void setColumnsList(List<Column> columnsList) {
        this.columnsList = columnsList;
    }

    public static ApplicationClass getSingleton() {
        return singleton;
    }

    public static void setSingleton(ApplicationClass singleton) {
        ApplicationClass.singleton = singleton;
    }

    public List<Table> getTableList() {
        return tableList;
    }

    public void setTableList(List<Table> tableList) {
        this.tableList = tableList;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }


    public ApplicationClass(Config config){
        super(config);
        loadConfig(config);
    }

    public static void initialize(Config config){
        if (singleton == null){
            singleton = new ApplicationClass(config);
        }
    }
    public static ApplicationClass instance(){ return singleton;}

    public void loadConfig(Config config){
        setConfig(config);
        //setConfig(new ReadConfig().loadConfigFile(fileName));
        logger.debug(config.toString());
    }
    public void loadData() {
        this.tableList = new DAO().getWithColumnsNumber();
        logger.debug(tableList.toString());
        loadColumns();
    }
    public void loadColumns(){
        //allcolumns
        this.columnsList = new DAO().getColumsProperties();
        for (int r = 0; r < this.tableList.size(); r++) {
            Table t = this.tableList.get(r);
            LinkedList<Column> column = new LinkedList<>();

            for(int h = 0; h < this.columnsList.size(); h++) {
                if (this.columnsList.get(h).getTableName().equals(t.getName())){
                    column.add(this.columnsList.get(h));
                }
            }

            t.setColumn(column);
            this.tableList.set(r,t);
        }
    }

    @Override
    public void reload() {
        //
    }


}
