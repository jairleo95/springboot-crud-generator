/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.template;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JAIR
 */
public class Entity {

    private String tableNameTNC;
    private String tableName;
    private String tableEntity;
    private String makeAssociatonColumns;
    private String makeColumns;
    private String makeMethods;
    private String makeParamsMethods;
    private String makeAllParamsMethods;
    private String makeParamsUpdate;
    private String paramsPrimaryKey;
    private String makeColumnsTable;
    private List<String> pksCurrentTable;

    public Entity() {
        this.tableNameTNC = "";
        /*
        tableName = Conversor.toJavaFormat(tableNameTNC, "_");
        tableEntity = Conversor.firstCharacterToUpper(tableName);
         */
        this.tableName = "";
        this.tableEntity = "";
        this.makeAssociatonColumns = "";
        this.makeColumns = "";
        this.makeMethods = "";
        this.makeParamsMethods = "";
        this.makeAllParamsMethods = "";
        this.makeParamsUpdate = "";
        this.paramsPrimaryKey = "";
        this.makeColumnsTable = "";
        this.pksCurrentTable = new ArrayList<>();
    }

    public String getTableNameTNC() {
        return tableNameTNC;
    }

    public void setTableNameTNC(String tableNameTNC) {
        this.tableNameTNC = tableNameTNC;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableEntity() {
        return tableEntity;
    }

    public void setTableEntity(String tableEntity) {
        this.tableEntity = tableEntity;
    }

    public String getMakeAssociatonColumns() {
        return makeAssociatonColumns;
    }

    public void setMakeAssociatonColumns(String makeAssociatonColumns) {
        this.makeAssociatonColumns = makeAssociatonColumns;
    }

    public String getMakeColumns() {
        return makeColumns;
    }

    public void setMakeColumns(String makeColumns) {
        this.makeColumns = makeColumns;
    }

    public String getMakeMethods() {
        return makeMethods;
    }

    public void setMakeMethods(String makeMethods) {
        this.makeMethods = makeMethods;
    }

    public String getMakeParamsMethods() {
        return makeParamsMethods;
    }

    public void setMakeParamsMethods(String makeParamsMethods) {
        this.makeParamsMethods = makeParamsMethods;
    }

    public String getMakeAllParamsMethods() {
        return makeAllParamsMethods;
    }

    public void setMakeAllParamsMethods(String makeAllParamsMethods) {
        this.makeAllParamsMethods = makeAllParamsMethods;
    }

    public String getMakeParamsUpdate() {
        return makeParamsUpdate;
    }

    public void setMakeParamsUpdate(String makeParamsUpdate) {
        this.makeParamsUpdate = makeParamsUpdate;
    }

    public String getParamsPrimaryKey() {
        return paramsPrimaryKey;
    }

    public void setParamsPrimaryKey(String paramsPrimaryKey) {
        this.paramsPrimaryKey = paramsPrimaryKey;
    }

    public String getMakeColumnsTable() {
        return makeColumnsTable;
    }

    public void setMakeColumnsTable(String makeColumnsTable) {
        this.makeColumnsTable = makeColumnsTable;
    }

    public List<String> getPksCurrentTable() {
        return pksCurrentTable;
    }

    public void setPksCurrentTable(List<String> pksCurrentTable) {
        this.pksCurrentTable = pksCurrentTable;
    }

}
