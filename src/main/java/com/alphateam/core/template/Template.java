/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.template;

import ch.qos.logback.classic.db.names.ColumnName;
import com.alphateam.connection.Factory;
import com.alphateam.query.Column;
import com.alphateam.query.DAO;
import com.alphateam.query.Table;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JAIR
 */
interface Methods {
    void column(Table table);
    void foreignKeys(Table table,Table fk);
    void primaryKeys(Table table,Table pk);

    void buildParameters(Table table);
    void buildMethods(Table tnc, List<String> pks);
}
public class Template extends Core implements Methods{

    @Override
    public void init() {
        database = Factory.getDefaultDatabase();
        returnId = false;
        conn = Factory.open(database);
        dao = new DAO();
        tables = dao.getWithColumnsNumber();
        //columns = dao.getColumsProperties();
        listPrimaryKey = dao.getPrimaryKeys();
        listForeignKey = dao.getForeignKeys();
    }


    public void build2() {
        init();
        //System.out.println("table: "+ table.toString());
        for (int r = 0; r < tables.size(); r++) {
            /*one or more ids (pk for current table)*/
            List<String> pks = new ArrayList<>();
            Table table = this.tables.get(r);

            table.setColumn(dao.getColumsProperties(table.getName()));


            System.out.println("/*TABLE :" + table.getName() + " */");
            /*iterate columns*/
            for (int h = 0; h < table.getColumn().size(); h++) {
                /*table-column-property (TCP)*/
                Column c = table.getColumn().get(h);

                /*Compare DAO*/
                //if (table.getName().equals(tcp.getName())) {
                 table.setColumn(columns.get(h).getColumn());
                    /*Llaves Primarias*/
                    Boolean isForean = false;
                    Boolean isPrimaryKey = false;
                    /*do a listener for column*/
                    column(c);

                   List<Column> foreign =  dao.getForeignKeys(table.getName(),c.getName());
                     isPrimaryKey =  dao.getPrimaryKeys(table.getName(),c.getName());


                    /*Do something with primary keys*/
                    //for (int g = 0; g < listPrimaryKey.size(); g++) {
                     /*primary keys*/
                        //Table pk = listPrimaryKey.get(g);
                     //   if (table.getName().equalsIgnoreCase(pk.getName()) & c.getName().equalsIgnoreCase(pk.getColumn().getName())) {
                            pks.add(c.getName());
                            //customized
                            primaryKeys(table, pk);
                            //isPrimaryKey = true;
                            c.setPrimaryKey(isPrimaryKey);
                        //}
                    //}
                    /*Do something with foreign keys*/
                    if (!isPrimaryKey) {
                        /*Llaves Foraneas*/
                        for (int d = 0; d < foreign.size(); d++) {
                            Table fk = listForeignKey.get(d);
                            if (table.getName().equalsIgnoreCase(fk.getName()) & tcp.getColumn().getName().equalsIgnoreCase(fk.getColumn().getName())) {
                                isForean = true;
                                c.setForeignKey(isForean);
                                foreignKeys(tcp,fk);
                            }
                        }
                    }
                      /*Do something with parameters*/
                    if (!isForean & !isPrimaryKey) {
                        buildParameters(tcp);
                    }
               // }
            }
            buildMethods(table, pks);
        }

    }


    public void build() {
        init();
        //System.out.println("table: "+ table.toString());
        for (int r = 0; r < tables.size(); r++) {
            /*one or more ids (pk for current table)*/
            List<String> pks = new ArrayList<>();
            Table table = this.tables.get(r);
            //table.setColumn(dao.getColumsProperties(table.getName()));



            System.out.println("/*TABLE :" + table.getName() + " */");
            /*iterate columns*/
            for (int h = 0; h < columns.size(); h++) {
                /*table-column-property (TCP)*/
                Table tcp = columns.get(h);

                /*Compare DAO*/
                if (table.getName().equals(tcp.getName())) {
                    table.setColumn(columns.get(h).getColumn());
                    /*Llaves Primarias*/
                    Boolean isForean = false;
                    Boolean isPrimaryKey = false;
                    /*do a listener for column*/
                    column(tcp);

                    /*Do something with primary keys*/
                    for (int g = 0; g < listPrimaryKey.size(); g++) {
                     /*primary keys*/
                        Table pk = listPrimaryKey.get(g);
                        if (table.getName().equalsIgnoreCase(pk.getName()) & tcp.getColumn().getName().equalsIgnoreCase(pk.getColumn().getName())) {
                            pks.add(pk.getColumn().getName());
                            //customized
                            primaryKeys(table, pk);
                            isPrimaryKey = true;
                            table.getColumn().setPrimaryKey(isPrimaryKey);
                        }
                    }
                    /*Do something with foreign keys*/
                    if (!isPrimaryKey) {
                        /*Llaves Foraneas*/
                        for (int d = 0; d < listForeignKey.size(); d++) {
                            Table fk = listForeignKey.get(d);
                            if (table.getName().equalsIgnoreCase(fk.getName()) & tcp.getColumn().getName().equalsIgnoreCase(fk.getColumn().getName())) {
                                isForean = true;
                                table.getColumn().setForeignKey(isForean);
                                foreignKeys(tcp,fk);
                            }
                        }
                    }
                      /*Do something with parameters*/
                    if (!isForean & !isPrimaryKey) {
                        buildParameters(tcp);
                    }
                }
            }
            buildMethods(table, pks);
        }

    }

    @Override
    public void column(Table table) {
       // System.out.println("column callback:"+table.toString());
    }

    @Override
    public void foreignKeys(Table tcp, Table fk) {
        //System.out.println("foreignKeys callback TABLE:"+tcp.toString());
        //System.out.println("foreignKeys callback FK:"+fk.toString());
    }

    @Override
    public void primaryKeys(Table table, Table pk) {
        //System.out.println("foreignKeys callback PK:"+pk.toString());
    }

    @Override
    public void buildParameters(Table table) {

    }

    @Override
    public void buildMethods(Table tnc, List<String> pks) {

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
