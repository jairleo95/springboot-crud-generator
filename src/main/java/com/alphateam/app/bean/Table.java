package com.alphateam.app.bean;

import com.alphateam.properties.Global;
import com.alphateam.util.Conversor;

import java.util.LinkedList;

/**
 * Created by JairL on 9/24/2018.
 */
public class Table {
    private String name;
    private String rawName;
    private LinkedList<Column> column;
    private int numColumns;

    public Table() {
        this.name = "";
        this.rawName = "";
        this.numColumns = 0;
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

    public String getRawName() {
        return rawName;
    }

    public void setRawName(String rawName) {
        this.rawName = rawName;
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
                ", rawName='" + rawName + '\'' +
                ", column=" + column +
                ", numColumns=" + numColumns +
                '}';
    }

    public Table format() {
        setName(Conversor.toJavaFormat(name, Global.SPLIT_CRITERIA));
        return this;
    }
}
