/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.app.core.builder;

import com.alphateam.app.core.persistence.EntityDAO;
import com.alphateam.connection.Factory;



import com.alphateam.app.bean.Column;

import com.alphateam.app.bean.Table;
import com.alphateam.util.Conversor;
import com.alphateam.util.FileBuilder;
import com.alphateam.util.ToJava;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Builder extends Core implements Methods{

    public String pkParameters ="";
    public String pkVariables="";
    public String pkDecrypt="";
    public String pkinput="";
    public String pkParamDecrypt="";
    public String pkMethVarInput ="";
    public String pkPathVarInput ="";
    public String pkParams ="";
    public String pkParamsRequest ="";
    public String pkSetter ="";
    public String pkMapVarInput ="";

    private final Logger log = LogManager.getLogger(getClass().getName());

    private List<Table> tables;

    @Override
    public void init() {
        log.debug("Enter to init() method.");

        database = Factory.getDefaultDB();
        returnId = false;

        //app = ApplicationClass.instance();
        //tables = app.getTableList();

        loadData();
        log.debug("Tablesx [" + tables.toString()+"]");
    }

    public void loadData() {
        System.out.println("enter to loadData() function");

        List<Table> tList = new EntityDAO().getWithColumnsNumber();
        List<Column> cList = new EntityDAO().getColumsProperties();

        for (int i = 0; i < tList.size(); i++) {
            LinkedList<Column> columns = new LinkedList<>();

            for(int j = 0; j < cList.size(); j++) {
                if (cList.get(j).getTableName().equals(tList.get(i).getName())){
                    columns.add(cList.get(j));
                }
            }
            tList.get(i).setColumn(columns);
        }
        tables = tList;

    }
    //refactoring
    @Override
    public void build() {
        init();
        //List<Table> tables = app.getTableList();

        for (int r = 0; r < tables.size(); r++) {
            List<Column> pks = new ArrayList<>();

            Table table = processTable(tables.get(r));

            /*iterate columnList*/
            for (int h = 0; h < table.getColumn().size(); h++) {
                //make threads
                Column column = table.getColumn().get(h).format();

                    if (column.isPrimaryKey()){
                        pks.add(column);
                        //listener
                        primaryKeys(table, column);

                   } else if (column.isForeignKey()) {
                        //listener
                        foreignKeys(table,column);
                    }else if (!column.isForeignKey() && !column.isPrimaryKey()) {
                        buildParameters(table,column);
                    }
                    //setting new data
                     column(column);
                    //table.getColumn().set(h, column);
            }
            buildMethods(table, pks);
            //generateProject("","");
            //setting new data
            resetValues();
        }

    }

    @Override
    public Table processTable(Table t) {

        //check primary keys
        boolean x = false;
        for (Column column: t.getColumn()) {
            if (column.isPrimaryKey()){
                x=true;
            }
        }

        if (!x){
                Column c = new Column();
                c.setTableName(t.getName());
                c.setName("id_temp");
                c.setDataType("char");
                c.setLength("10");
                c.setPrimaryKey(true);
                t.getColumn().addFirst(c);
        }

        return t.format();
    }

    @Override
    public void column(Column column) {
        //System.out.println("column callback:"+column.toString());
    }

    @Override
    public void foreignKeys(Table fkTable, Column fk) {
        //System.out.println("foreignKeys callback TABLE:"+tcp.toString());
        //System.out.println("foreignKeys callback FK:"+fk.toString());
    }

    @Override
    public void primaryKeys(Table table, Column pk) {

        String tableName = table.getName();
        String dataType = ToJava.getDataType(pk.getDataType());
        String columnName = pk.getName();

        pkinput+=  columnName+",";


        //todo:refactor this hardcode
        dataType = "String";
        pk.setDataType("char");

        pkParameters+=  tableName +".get"+Conversor.firstCharacterToUpper(columnName)+"(),";
        pkParams+=  columnName+",";
        pkParamsRequest+=  "/{"+pk.getName()+"}";
        pkVariables+=  dataType+" "+columnName+",";
        pkMethVarInput +=  "String "+columnName+",";
        pkPathVarInput +=  "@PathVariable String "+columnName+",";

        pkDecrypt+= "String " + columnName+"Decrypt" + " = Security.decrypt("+columnName+"); \n";
        pkParamDecrypt+= columnName+"Decrypt,";
        pkSetter+=  tableName +".set"+Conversor.firstCharacterToUpper(columnName)+"("+columnName+"Decrypt);";
        pkMapVarInput +=  "@Param(\""+columnName+"\") String "+columnName+",";
    }

    @Override
    public void buildParameters(Table table, Column column) {
       // System.out.println("Building something - buildParameters callback column:"+column.toString());

    }

    @Override
    public void buildMethods(Table table, List<Column> pks) {

        if (pks.size()==0){
            //todo: refator
/*            String tableName = Conversor.toJavaFormat(table.getName(), "_");
            pkSetter+=  tableName +".setId(\"id\");";
            pkinput+=  "id";
            pkParams+=  "id";
            pkParamsRequest+=  "/{id}";
            pkParameters+=  tableName +".getId()";
            pkVariables+=  "String id";
            pkMethVarInput +=  "String id";
            pkPathVarInput +=  "@PathVariable String id";

            pkDecrypt+= "id = Security.decrypt(id); \n";*/
        }

        pkParameters = clearLastComma(pkParameters);
        pkVariables = clearLastComma(pkVariables);
        pkinput = clearLastComma(pkinput);
        pkParamDecrypt = clearLastComma(pkParamDecrypt);
        pkMethVarInput = clearLastComma(pkMethVarInput);
        pkPathVarInput = clearLastComma(pkPathVarInput);
        pkParams = clearLastComma(pkParams);
        pkMapVarInput = clearLastComma(pkMapVarInput);

      // System.out.println("Building something - primaryKeys callback PK:"+pks.toString());
    }
    @Override
    public void resetValues() {
        content = "";

        pkParameters="";
        pkVariables="";
        pkDecrypt="";
        pkinput="";
        pkParamDecrypt="";
        pkMethVarInput ="";
        pkPathVarInput ="";
        pkParams ="";
        pkParamsRequest ="";
        pkSetter ="";
        pkMapVarInput ="";
        //System.out.println("Building something - resetValues callback ");
    }

    @Override
    public void generateProject(String path,String filename) {
        new FileBuilder().writeFolderAndFile(path, filename, content);
    }

    public String header(SyntaxType opc) {
        String c;
        switch (opc) {
            case SP_MYSQL:

                break;
        }
        return null;
    }

    public enum SyntaxType {
        SP_MYSQL,
        FORM_VIEW
    }

   public static String clearLastComma(String string){
        if (!string.equals("")) {
            if (string.contains(",")){
                string = string.substring(0, (string.length() - 1));
            }
        }
        return string;
    }
}

//final class Task implements Runnable {
//    private Table table;
//
//    public Task(Table table)
//    {
//        this.table = table;
//    }
//
//    @Override
//    public void run(){
//        System.out.println("Task ID : " + this.table.toString() + " performed by " + Thread.currentThread().getName());
//    }
//}
