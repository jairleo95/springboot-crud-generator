package com.alphateam.query;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by JairL on 9/24/2018.
 */
public class Table {
    private String name;
    private LinkedList<Column> column;
    private String numColumns;

    public Table() {
        this.name = "";
        this.numColumns = "";
        this.column = new LinkedList<>();
    }

    public LinkedList<Column> getColumn() {
        return column;
    }

    public void setColumn(LinkedList<Column> column) {
        this.column = column;
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
                ", column=" + column +
                ", numColumns='" + numColumns + '\'' +
                '}';
    }
}
