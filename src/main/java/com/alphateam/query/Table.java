package com.alphateam.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JairL on 9/24/2018.
 */
public class Table {
    private String name;
    private List<Column> column;

    private String numColumns;
    private String constraintName;
    private String foreignColumn;
    private String foreignTable;

    public Table() {
        this.name = "";
        this.numColumns = "";
        this.column = new ArrayList<>();

        this.constraintName="";
        this.foreignTable="";
        this.foreignColumn="";
    }

    public List<Column> getColumn() {
        return column;
    }

    public void setColumn(List<Column> column) {
        this.column = column;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumColumns() {
        return numColumns;
    }

    public void setNumColumns(String numColumns) {
        this.numColumns = numColumns;
    }


    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + '\'' +
                ", numColumns='" + numColumns + '\'' +
                ", constraintName='" + constraintName + '\'' +
                ", foreignColumn='" + foreignColumn + '\'' +
                ", foreignTable='" + foreignTable + '\'' +
                '}';
    }
}
