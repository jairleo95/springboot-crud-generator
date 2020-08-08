package com.alphateam.app.base;

import com.alphateam.app.configurtions.Config;
import com.alphateam.app.configurtions.Configuration;
import com.alphateam.query.Column;
import com.alphateam.query.DAO;
import com.alphateam.query.Table;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ApplicationClass extends Configuration {

    private static ApplicationClass singleton = null;

    private List<Table> tableList;

    private List<Table> xList;

    public List<Table> getxList() {
        return xList;
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
    }

    public void loadData() {
       System.out.println("enter to loadData() function");

       List<Table> tList = new DAO().getWithColumnsNumber();
       List<Column> cList = new DAO().getColumsProperties();

        for (int i = 0; i < tList.size(); i++) {
            LinkedList<Column> columns = new LinkedList<>();

            for(int j = 0; j < cList.size(); j++) {
                if (cList.get(j).getTableName().equals(tList.get(i).getName())){
                    columns.add(cList.get(j));
                }
            }
            tList.get(i).setColumn(columns);
        }
        setTableList(tList);
       // this.xList = new DAO().getWithColumnsNumber();
    }


    @Override
    public void reload() {
        //
    }


}
