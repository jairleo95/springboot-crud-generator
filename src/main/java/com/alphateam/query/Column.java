package com.alphateam.query;

/**
 * Created by JairL on 9/27/2018.
 */
public class Column {

    private String name;
    private String dataType;
    private String length;
    private String attributeNumber;
    private boolean foreignKey;

    private String foreignTable;
    private String foreignColumn;

    private boolean primaryKey;

    public Column() {
        this.name = "";
        this.dataType = "";
        this.length = "";
        this.attributeNumber ="";
        this.foreignKey =false;
        this.primaryKey =false;

        this.foreignColumn= "";
        this.foreignTable= "";
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

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", dataType='" + dataType + '\'' +
                ", length='" + length + '\'' +
                ", attributeNumber='" + attributeNumber + '\'' +
                ", foreignKey=" + foreignKey +
                ", foreignTable='" + foreignTable + '\'' +
                ", foreignColumn='" + foreignColumn + '\'' +
                ", primaryKey=" + primaryKey +
                '}';
    }
}
