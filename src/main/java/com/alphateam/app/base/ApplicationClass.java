package com.alphateam.app.base;

import com.alphateam.app.configurtions.Config;
import com.alphateam.app.configurtions.Configuration;
import com.alphateam.app.bean.Column;
import com.alphateam.app.core.persistence.EntityDAO;
import com.alphateam.app.bean.Table;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ApplicationClass extends Configuration {

    private static ApplicationClass singleton = null;

    private List<Table> tableList;

//    private List<Table> xList;
//
//    public List<Table> getxList() {
//        return xList;
//    }
//
//    public List<Table> getTableList() {
//        return tableList;
//    }

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
       //System.out.println("enter to loadData() function");

       List<Table> tList = new EntityDAO().getWithColumnsNumber();
       Set<Column> cList = new EntityDAO().getColumsProperties();

        for (Table table : tList) {
            LinkedList<Column> columns = new LinkedList<>();
            for (Column column : cList) {
                if (column.getTableName().equals(table.getName())) {
                    columns.add(column);
                }
            }
            table.setColumn(columns);
        }
        setTableList(tList);
        //System.out.println("list: " +tList.toString());
       // this.xList = new DAO().getWithColumnsNumber();
    }


    @Override
    public void reload() {
        //
    }


}
