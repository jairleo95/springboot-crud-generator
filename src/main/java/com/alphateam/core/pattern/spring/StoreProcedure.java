/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.pattern.spring;

import com.alphateam.convert.ToSql;
import com.alphateam.core.template.Template;
import java.util.ArrayList;
import java.util.List;
import com.alphateam.query.Table;
import com.alphateam.utiles.Conversor;

/**
 *
 * @author Jairleo95
 */
public class StoreProcedure extends Template {

    public void build() {
        init();
        for (int r = 0; r < listTableXNumColumns.size(); r++) {
            /*one or more ids*/
            List<String> pksCurrentTable = new ArrayList<>();

            Table tnc = listTableXNumColumns.get(r);
           // String tableNameTNC = listTableXNumColumns.get(r).getTableName();
            String tableName = Conversor.toJavaFormat(tnc.getTableName(), "_");
            String tableEntity = Conversor.firstCharacterToUpper(tableName);
            String makeAssociatonColumns = "";
            String makeColumns = "";
            String makeMethods = "";
            String makeParamsMethods = "";
            String makeAllParamsMethods = "";
            String makeParamsUpdate = "";
            String paramsPrimaryKey = "";
            String makeColumnsTable = "";
            System.out.println("/*TABLE :" + tnc.getTableName() + " */");

            for (int h = 0; h < listTableXColumsP.size(); h++) {
                /*table-column-property (TCP)*/
                Table tcp = listTableXColumsP.get(h);
                /*Compare DAO*/
                if (tnc.getTableName().equals(tcp.getTableName())) {
                    /*Variables*/
                    String columna = Conversor.toJavaFormat(tcp.getColumnName(), "_");
                    String dataType = "";
                    Boolean isForean = false;
                    Boolean isPrimaryKey = false;
                    dataType = tcp.getDataType();
                    /*Llaves Primarias*/

                    //customized
                    String parametersProcedure = "sp" + Conversor.firstCharacterToUpper(columna);

                    for (int g = 0; g < listPrimaryKey.size(); g++) {
                     /*primary keys*/
                        Table pk = listPrimaryKey.get(g);
                        // System.err.println(tableNameTNC + " : " + pkTableName);
                        if (tnc.getTableName().equalsIgnoreCase(pk.getTableName()) & tcp.getColumnName().equalsIgnoreCase(pk.getColumnName())) {
                            pksCurrentTable.add(pk.getColumnName());
                            //customized
                            paramsPrimaryKey += pk.getColumnName() + " " + ToSql.getDataType(dataType, database) + ",";

                            isPrimaryKey = true;
                        }
                    }
                    if (!isPrimaryKey) {
                        /*Llaves Foraneas*/
                        for (int d = 0; d < listForeignKey.size(); d++) {
                            Table fk = listForeignKey.get(d);
                            if (tnc.getTableName().equalsIgnoreCase(fk.getTableName()) & tcp.getColumnName().equalsIgnoreCase(fk.getColumnName())) {
                                // String ColumnaBean = Conversor.toJavaFormat(columna, "_");
                                isForean = true;
                                //customized
                                /*MAKE HEADER PARAMETERS*/
                                makeParamsMethods += ToSql.headerParamsProcedure(parametersProcedure, ToSql.getDataType(dataType, database), tcp.getAttributeNumber(), database);
                                makeColumns += "sp" + Conversor.firstCharacterToUpper(columna) + ",";
                                makeColumnsTable += tcp.getColumnName() + ",";
                                makeParamsUpdate += tcp.getColumnName() + "=" + parametersProcedure + ", ";
                                //end customized
                            }
                        }
                    }
                    if (!isForean & !isPrimaryKey) {
                        //customized
                        makeColumns += parametersProcedure + ",";
                        makeParamsMethods += ToSql.headerParamsProcedure(parametersProcedure, ToSql.getDataType(dataType, database), tcp.getAttributeNumber(), database);
                        makeColumnsTable += tcp.getColumnName() + ",";
                        makeParamsUpdate += tcp.getColumnName() + "=" + parametersProcedure + ", ";

                        //end customized
                    }
                    makeAllParamsMethods += "sp" + Conversor.firstCharacterToUpper(columna) + " " + ToSql.getDataType(dataType, database);
                    makeParamsUpdate += tcp.getColumnName() + "=" + "sp" + Conversor.firstCharacterToUpper(columna) + ",";
                }
            }

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
            //end customized
            /*Insert record procedure */
            makeMethods += ToSql.headerProcedure("spi_" + tnc.getTableName(), makeParamsMethods, database);
            makeMethods += "INSERT INTO " + tnc.getTableName() + "(" + makeColumnsTable + ") values (" + makeColumns + ")";

            /*ruturn id*/
            if (pksCurrentTable.size() == 1 & returnId) {
                makeMethods += " returning " + pksCurrentTable.get(0) + " into result ;";
            } else {
                makeMethods += ";";
            }
            makeMethods += ToSql.footerProcedure("spi_" + tnc.getTableName(), database);
            //*EDIT procedure*/
            makeMethods += ToSql.headerProcedure("spu_" + tnc.getTableName(), makeParamsMethods, database);
            /*procedure body*/
            makeMethods += "UPDATE " + tnc.getTableName() + " set " + makeParamsUpdate + " where ";

            for (int t = 0; t < pksCurrentTable.size(); t++) {
                if (t == 0) {
                    makeMethods += pksCurrentTable.get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(pksCurrentTable.get(t), "_"));
                } else {
                    makeMethods += " and " + pksCurrentTable.get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(pksCurrentTable.get(t), "_"));
                }
            }
            makeMethods += ";";
            makeMethods += ToSql.footerProcedure("spi_" + tnc.getTableName(), database);
            /*DELETE procedure*/
            makeMethods += ToSql.headerProcedure("spd_" + tnc.getTableName(), paramsPrimaryKey, database);
            /*DELETE body*/
            makeMethods += "UPDATE " + tnc.getTableName() + " set recordStatus=0 where ";
            for (int t = 0; t < pksCurrentTable.size(); t++) {
                if (t == 0) {
                    makeMethods += pksCurrentTable.get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(pksCurrentTable.get(t), "_"));
                } else {
                    makeMethods += " and " + pksCurrentTable.get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(pksCurrentTable.get(t), "_"));
                }
            }
            makeMethods += ";";
            makeMethods += ToSql.footerProcedure("spi_" + tnc.getTableName(), database);
            //end customized
            /*Print */
            System.out.println(makeMethods);
            /*Contructor*/
        }
    }

}
