package com.alphateam.query;

import com.alphateam.properties.Global;
import com.alphateam.utiles.Conversor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by JairL on 9/24/2018.
 */
public class Table {
    private String name;
    private LinkedList<Column> column;
    private int numColumns;

    public Table() {
        this.name = "";
        numColumns = 0;
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

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumColumns(int numColumns) {
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

    public Table format(){
        this.name = Conversor.toJavaFormat(name, Global.SPLIT_CRITERIA);
        return this;
    }
}
