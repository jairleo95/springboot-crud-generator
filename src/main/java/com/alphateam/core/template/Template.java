/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.template;

import com.alphateam.connection.Factory;
import com.alphateam.convert.ToJava;
import com.alphateam.query.Column;
import com.alphateam.query.DAO;
import com.alphateam.query.Table;
import com.alphateam.utiles.Conversor;
import com.alphateam.utiles.FileBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author JAIR
 */
interface Methods {
    Table table(Table table);
    void column(Column column);
    void foreignKeys(Table table,Column fk);
    void primaryKeys(Table table,Column pk);

    void buildParameters(Table table,Column column);
    void buildMethods(Table tnc, List<String> pks);
    void resetValues();
    void generateProject(String path,String filename);
}

public class Template extends Core implements Methods{

    @Override
    public void init() {
        database = Factory.getDefaultDatabase();
        returnId = false;
        conn = Factory.open(database);
        dao = new DAO();
        tables = dao.getWithColumnsNumber();
        loadColumns();
    }

    public void loadColumns(){
        //allcolumns
        columns = dao.getColumsProperties("");
        for (int r = 0; r < tables.size(); r++) {
            Table t = tables.get(r);
            LinkedList<Column> column = new LinkedList<>();

            for (int h = 0; h < columns.size(); h++) {
                if (columns.get(h).getTableName().equals(t.getName())){
                    column.add(columns.get(h));
                }
            }

            t.setColumn(column);
            tables.set(r,t);
        }
    }


    //refactoring
    public void build() {
        init();
        for (int r = 0; r < tables.size(); r++) {
            List<String> pks = new ArrayList<>();
            Table table = table(this.tables.get(r));

            System.out.println("Table [" + table.getName()+"]");
            //System.out.println("/*table.getColumn().size() :" + columns.size() + " */");

            /*iterate columnList*/
            for (int h = 0; h < table.getColumn().size(); h++) {
                //make threads
                Column column = table.getColumn().get(h);

                    if (column.isPrimaryKey()){
                        pks.add(column.getName());
                        //listener
                        primaryKeys(table, column);

                   } else if (column.isForeignKey()) {
                        //listener
                        foreignKeys(table,column);
                    }else if (!column.isForeignKey() && !column.isPrimaryKey()) {
                        buildParameters(table,column);
                    }
                    //System.out.println("build().column:"+column.toString());
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
    public Table table(Table table) {
        //check primary keys
        boolean x = false;
        for (Column column: table.getColumn()) {
            if (column.isPrimaryKey()){
                x=true;
            }
        }

        if (!x){
                Column c = new Column();
                c.setTableName(table.getName());
                c.setName("id_temp");
                c.setDataType("char");
                c.setLength("10");
                c.setPrimaryKey(true);
                table.getColumn().addFirst(c);
        }
        return table;
    }

    @Override
    public void column(Column column) {
        //System.out.println("column callback:"+column.toString());
    }

    @Override
    public void foreignKeys(Table tcp, Column fk) {
        //System.out.println("foreignKeys callback TABLE:"+tcp.toString());
        //System.out.println("foreignKeys callback FK:"+fk.toString());
    }

    @Override
    public void primaryKeys(Table table, Column pk) {
        String tableName = Conversor.toJavaFormat(table.getName(), "_");
        String dataType = ToJava.getDataType(pk.getDataType(), Factory.getDefaultDatabase());
        String columnName = Conversor.toJavaFormat(pk.getName(), "_");

      // if (dataType.equalsIgnoreCase("Integer")){
            //pkinput+=  "Integer.parseInt("+columnName+"),";
            //pkSetter+=  tableName +".set"+Conversor.firstCharacterToUpper(columnName)+"("+"Integer.parseInt(Security.decrypt("+columnName+"))"+");";
        //}else {
            pkinput+=  columnName+",";
            pkSetter+=  tableName +".set"+Conversor.firstCharacterToUpper(columnName)+"("+columnName+");";
        //}
        //work with datatype string for crypt
        //todo:refactor this hardcode
        dataType = "String";
        pk.setDataType("char");

        pkParameters+=  tableName +".get"+Conversor.firstCharacterToUpper(columnName)+"(),";
        pkParams+=  columnName+",";
        pkParamsRequest+=  "/{"+Conversor.toJavaFormat(pk.getName(),"_")+"}";
        pkVariables+=  dataType+" "+columnName+",";
        pkMethVarInput +=  "String "+columnName+",";
        pkPathVarInput +=  "@PathVariable String "+columnName+",";

        pkDecrypt+= columnName+" = Security.decrypt("+columnName+"); \n";
        pkMapVarInput +=  "@Param(\""+columnName+"\") String "+columnName+",";
    }

    @Override
    public void buildParameters(Table table, Column column) {
       // System.out.println("Building something - buildParameters callback column:"+column.toString());

    }

    @Override
    public void buildMethods(Table table, List<String> pks) {

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
        //path = "org\\proyecto\\views\\" + filename + "\\";
        FileBuilder.writeFolderAndFile(path, filename, content);
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

   public  String clearLastComma(String string){
        if (!string.equals("")) {
            if (string.contains(",")){
                string = string.substring(0, (string.length() - 1));
            }
        }
        return string;
    }
}
final class Task implements Runnable
{
    private Table table;

    public Task(Table table)
    {
        this.table = table;
    }

    @Override
    public void run()
    {

        System.out.println("Task ID : " + this.table.toString() + " performed by "
                + Thread.currentThread().getName());
    }
}
