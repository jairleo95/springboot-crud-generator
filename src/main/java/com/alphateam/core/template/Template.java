/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.template;

import com.alphateam.connection.Factory;
import com.alphateam.convert.ToSql;
import com.alphateam.query.DAO;
import com.alphateam.query.Table;
import com.alphateam.utiles.Conversor;

/**
 *
 * @author JAIR
 */
public class Template extends Core {

    @Override
    public void init() {
        database = Factory.getDefaultDatabase();
        returnId = false;
        conn = Factory.open(database);
        dao = new DAO();
        listTableXNumColumns = dao.getWithColumnsNumber();
        listTableXColumsP = dao.getColumsProperties();
        listPrimaryKey = dao.getPrimaryKeys();
        listForeignKey = dao.getForeignKeys();
    }

    public void build() {

        for (int r = 0; r < listTableXNumColumns.size(); r++) {
            /*one or more IDs*/
            Entity e = new Entity();
            /*table name - columns*/
            Table tnc = listTableXNumColumns.get(r);
            e.setTableNameTNC(tnc.getTableName());
            e.setTableName(Conversor.toJavaFormat(e.getTableNameTNC(), "_"));
            e.setTableEntity(Conversor.firstCharacterToUpper(e.getTableName()));
            String content = "";
            /*
            String makeAssociatonColumns = "";
            String content = "";
            List<String> pksCurrentTable = new ArrayList<String>();*/
            String makeColumns = "", makeAllParamsMethods = "", makeParamsUpdate = "", makeParamsMethods = "", paramsPrimaryKey = "", makeColumnsTable = "";

            System.out.println("/*TABLA :" + e.getTableNameTNC() + " */");

            for (int h = 0; h < listTableXColumsP.size(); h++) {
                /*table-column-property (TCP)*/
                Table tcp = listTableXColumsP.get(h);
                /*Compare DAO*/
                if (e.getTableNameTNC().equals(tcp.getTableName())) {
                    /*Variables*/
                    String columna = Conversor.toJavaFormat(tcp.getColumnName(), "_");
                    String dataType = "";
                    Boolean isForean = false;
                    Boolean isPrimaryKey = false;
                    dataType = tcp.getDataType();
                    /*Llaves Primarias*/
                    String parametersProcedure = "sp" + Conversor.firstCharacterToUpper(columna);
                    for (int g = 0; g < listPrimaryKey.size(); g++) {
                        /*primary keys*/
                        Table pk = listPrimaryKey.get(g);

                        // System.err.println(e.getTableNameTNC() + " : " + pkTableName);
                        if (e.getTableNameTNC().equalsIgnoreCase(pk.getTableName()) & tcp.getColumnName().equalsIgnoreCase(pk.getColumnName())) {
                            e.getPksCurrentTable().add(pk.getColumnName());
                            paramsPrimaryKey += pk.getColumnName() + " " + ToSql.getDataType(dataType, database) + ",";
                            isPrimaryKey = true;
                        }
                    }

                    if (!isPrimaryKey) {
                        /*Llaves Foraneas*/
                        for (int d = 0; d < listForeignKey.size(); d++) {
                            Table fk = listForeignKey.get(d);
                            if (e.getTableNameTNC().equalsIgnoreCase(fk.getTableName()) & tcp.getColumnName().equalsIgnoreCase(fk.getColumnName())) {
                                // String ColumnaBean = Conversor.toJavaFormat(columna, "_");
                                isForean = true;

                                /*MAKE HEADER PARAMETERS*/
                                makeParamsMethods += ToSql.headerParamsProcedure(parametersProcedure, ToSql.getDataType(dataType, database), tcp.getAttributeNumber(), database);

                                makeColumns += "sp" + Conversor.firstCharacterToUpper(columna) + ",";
                                makeColumnsTable += tcp.getColumnName() + ",";
                                makeParamsUpdate += tcp.getColumnName() + "=" + parametersProcedure + ", ";
                            }
                        }
                    }
                    if (!isForean & !isPrimaryKey) {
                        makeColumns += parametersProcedure + ",";

                        makeParamsMethods += ToSql.headerParamsProcedure(parametersProcedure, ToSql.getDataType(dataType, database), tcp.getAttributeNumber(), database);

                        makeColumnsTable += tcp.getColumnName() + ",";
                        makeParamsUpdate += tcp.getColumnName() + "=" + parametersProcedure + ", ";
                    }

                    makeAllParamsMethods += "sp" + Conversor.firstCharacterToUpper(columna) + " " + ToSql.getDataType(dataType, database);

                    makeParamsUpdate += tcp.getColumnName() + "=" + "sp" + Conversor.firstCharacterToUpper(columna) + ",";
                }
            }

            /*PARAMS SEQUENCE*/
            if (!makeParamsMethods.equals("")) {
                makeParamsMethods = makeParamsMethods.substring(0, (makeParamsMethods.length() - 1));
            }
            if (!paramsPrimaryKey.equals("")) {
                paramsPrimaryKey = paramsPrimaryKey.substring(0, (paramsPrimaryKey.length() - 1));
            }
            if (!makeColumns.equals("")) {
                makeColumns = makeColumns.substring(0, (makeColumns.length() - 1));
            }
            if (!makeColumnsTable.equals("")) {
                makeColumnsTable = makeColumnsTable.substring(0, (makeColumnsTable.length() - 1));
            }
            if (!makeParamsUpdate.equals("")) {
                makeParamsUpdate = makeParamsUpdate.substring(0, (makeParamsUpdate.length() - 1));
            }

            /*Insert record procedure */
            content += ToSql.headerProcedure("spi_" + e.getTableNameTNC(), makeParamsMethods, database);
            content += "INSERT INTO " + e.getTableNameTNC() + "(" + makeColumnsTable + ") values (" + makeColumns + ")";

            /*ruturn id*/
            if (e.getPksCurrentTable().size() == 1 & returnId) {
                content += " returning " + e.getPksCurrentTable().get(0) + " into result ;";
            } else {
                content += ";";
            }
            content += ToSql.footerProcedure("spi_" + e.getTableNameTNC(), database);

            //*EDIT procedure*/
            content += ToSql.headerProcedure("spu_" + e.getTableNameTNC(), makeParamsMethods, database);
            /*procedure body*/
            content += "UPDATE " + e.getTableNameTNC() + " set " + makeParamsUpdate + " where ";
            for (int t = 0; t < e.getPksCurrentTable().size(); t++) {
                if (t == 0) {
                    content += e.getPksCurrentTable().get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(e.getPksCurrentTable().get(t), "_"));
                } else {
                    content += " and " + e.getPksCurrentTable().get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(e.getPksCurrentTable().get(t), "_"));
                }
            }
            content += ";";
            content += ToSql.footerProcedure("spi_" + e.getTableNameTNC(), database);
            /*DELETE procedure*/
            content += ToSql.headerProcedure("spd_" + e.getTableNameTNC(), paramsPrimaryKey, database);
            /*DELETE body*/
            content += "UPDATE " + e.getTableNameTNC() + " set recordStatus=0 where ";
            for (int t = 0; t < e.getPksCurrentTable().size(); t++) {
                if (t == 0) {
                    content += e.getPksCurrentTable().get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(e.getPksCurrentTable().get(t), "_"));
                } else {
                    content += " and " + e.getPksCurrentTable().get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(e.getPksCurrentTable().get(t), "_"));
                }
            }
            content += ";";
            content += ToSql.footerProcedure("spi_" + e.getTableNameTNC(), database);
            /*Print */
            System.out.println(content);
            /*Contructor*/
        }
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
}
