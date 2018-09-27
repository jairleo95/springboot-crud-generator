package com.alphateam.query;

/**
 * Created by JairL on 9/24/2018.
 */
public class Table {
    private String tableName;
    private String columnName;
    private String dataType;
    private String attributeNumber;
    private String numColumns;

    private String constraintName;

    private String foreignTable;
    private String foreignColumn;

    public Table() {
        this.tableName = "";
        this.numColumns = "";
        this.dataType = "";
        this.attributeNumber = "";
        this.columnName="";

        this.constraintName="";

        this.foreignTable="";
        this.foreignColumn="";
    }

    public String getForeignTable() {
        return foreignTable;
    }

    public void setForeignTable(String foreignTable) {
        this.foreignTable = foreignTable;
    }

    public String getForeignColumn() {
        return foreignColumn;
    }

    public void setForeignColumn(String foreignColumn) {
        this.foreignColumn = foreignColumn;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public void setConstraintName(String constraintName) {
        this.constraintName = constraintName;
    }

    public String getAttributeNumber() {
        return attributeNumber;
    }

    public void setAttributeNumber(String attributeNumber) {
        this.attributeNumber = attributeNumber;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getNumColumns() {
        return numColumns;
    }

    public void setNumColumns(String numColumns) {
        this.numColumns = numColumns;
    }
}
