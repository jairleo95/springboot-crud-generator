/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.template;

import com.alphateam.connection.Factory;
import com.alphateam.query.Column;
import com.alphateam.query.DAO;
import com.alphateam.query.Table;
import com.alphateam.utiles.FileBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author JAIR
 */
interface Methods {
    void table(Table table);
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
    }

    //refactoring
    public void build() {
        init();
        List<Column> columns = dao.getColumsProperties("");
        for (int r = 0; r < tables.size(); r++) {
            /*one or more ids (pk for current table)*/
            List<String> pks = new ArrayList<>();
            Table table = this.tables.get(r);

            ///table.setColumn(dao.getColumsProperties(table.getName()));

            System.out.println("/*Table :" + table.getName() + " */");
            System.out.println("/*table.getColumn().size() :" + columns.size() + " */");
            table(table);
            /*iterate columnList*/
            for (int h = 0; h < columns.size(); h++) {
                if (columns.get(h).getTableName().equals(table.getName())){

                //threads
               /* ExecutorService service = Executors.newFixedThreadPool(10);
                service.submit(new Task(table));*/

                /*table-column-property (TCP)*/
                Column column = columns.get(h);

                /*do a listener for column iteration*/
                //todo:refactor this methods for ORACLE

                //Boolean isForeignKey =  dao.isForeignKey(table.getName(),column.getName());
               /*if (database!= Factory.ORACLE) {  dao.isPrimaryKey(table.getName(),column.getName());}
                else{
                   isPrimaryKey = column.isPrimaryKey();
               }*/
                // System.out.println("isForeignKey:"+isForeignKey+", isPrimaryKey:"+isPrimaryKey);
                    /*Do something with primary keys*/
                    if (column.isPrimaryKey()){
                        pks.add(column.getName());
                        //column.setPrimaryKey(isPrimaryKey);
                        //listener
                        primaryKeys(table, column);

                   } else if (column.isForeignKey()) {

                           // column.setForeignKey(isForeignKey);
                           // Column fkColumn =  dao.getForeignKey(table.getName(),column.getName());
                           // column.setForeignColumn(fkColumn.getForeignColumn());
                           // column.setForeignTable(fkColumn.getForeignTable());

                        //listener
                        foreignKeys(table,column);
                    }else if (!column.isForeignKey() && !column.isPrimaryKey()) {
                        buildParameters(table,column);
                    }
                    //System.out.println("build().column:"+column.toString());

                    //setting new data
                column(column);
               // table.getColumn().set(h, column);
            }
            }
            buildMethods(table, pks);
            //generateProject("","");
            //setting new data
            resetValues();
        }

    }

    @Override
    public void table(Table table) {

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
        //System.out.println("foreignKeys callback PK:"+pk.toString());
    }

    @Override
    public void buildParameters(Table table, Column column) {
       // System.out.println("Building something - buildParameters callback column:"+column.toString());

    }

    @Override
    public void buildMethods(Table tnc, List<String> pks) {
      // System.out.println("Building something - primaryKeys callback PK:"+pks.toString());
    }
    @Override
    public void resetValues() {
        content = "";
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
            string = string.substring(0, (string.length() - 1));
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
