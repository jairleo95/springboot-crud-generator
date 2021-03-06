package com.alphateam.app.bean;

import com.alphateam.properties.Global;
import com.alphateam.util.Conversor;


/**
 * Created by JairL on 9/27/2018.
 */
public class Column {

    private String tableName;
    private String name;
    private String rawName;
    private String dataType;
    private String length;
    private String attributeNumber;

    private String foreignTable;
    private String foreignColumn;

    private boolean foreignKey;
    private boolean primaryKey;

    public Column() {
        this.tableName = "";
        this.name = "";
        this.rawName= "";
        this.dataType = "";
        this.length = "";
        this.attributeNumber ="";
        this.foreignKey =false;
        this.primaryKey =false;

        this.foreignColumn= "";
        this.foreignTable= "";
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getForeignColumn() {
        return foreignColumn;
    }

    public void setForeignColumn(String foreignColumn) {
        this.foreignColumn = foreignColumn;
    }

    public String getForeignTable() {
        return foreignTable;
    }

    public void setForeignTable(String foreignTable) {
        this.foreignTable = foreignTable;
    }

    public boolean isForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(boolean foreignKey) {
        this.foreignKey = foreignKey;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public String getRawName() {
        return rawName;
    }

    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getAttributeNumber() {
        return attributeNumber;
    }

    public void setAttributeNumber(String attributeNumber) {
        this.attributeNumber = attributeNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public Column format(){
        this.name = Conversor.toJavaFormat(name, Global.SPLIT_CRITERIA);
        if (foreignColumn!=null) {
            this.foreignColumn = Conversor.toJavaFormat(foreignColumn, Global.SPLIT_CRITERIA);
        }
        if (foreignTable!= null){
            this.foreignTable = Conversor.toJavaFormat(foreignTable, Global.SPLIT_CRITERIA);
        }
        return this;
    }

    @Override
    public String toString() {
        return "Column{" +
                "tableName='" + tableName + '\'' +
                ", name='" + name + '\'' +
                ", rawName='" + rawName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", length='" + length + '\'' +
                ", attributeNumber='" + attributeNumber + '\'' +
                ", foreignTable='" + foreignTable + '\'' +
                ", foreignColumn='" + foreignColumn + '\'' +
                ", foreignKey=" + foreignKey +
                ", primaryKey=" + primaryKey +
                '}';
    }
}
