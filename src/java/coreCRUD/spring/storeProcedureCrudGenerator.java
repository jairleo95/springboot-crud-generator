/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreCRUD.spring;

import convert.sql;
import coreCRUD.template.Template;
import java.util.ArrayList;
import java.util.List;
import utiles.conversor;

/**
 *
 * @author Jairleo95
 */
public class storeProcedureCrudGenerator extends Template {

    public Boolean makeStoreProcedureCrud() {
        for (int r = 0; r < listTableXNumColumns.size(); r++) {
            /*one or more ids*/
            List<String> pksCurrentTable = new ArrayList<String>();
            String tableNameTNC = listTableXNumColumns.get(r).get("TableName");
            String tableName = conversor.toJavaFormat(tableNameTNC, "_");
            String tableEntity = conversor.firstCharacterToUpper(tableName);
            String makeAssociatonColumns = "";
            String makeColumns = "";
            String makeMethods = "";
            String makeParamsMethods = "";
            String makeAllParamsMethods = "";
            String makeParamsUpdate = "";
            String paramsPrimaryKey = "";
            String makeColumnsTable = "";
            System.out.println("/*TABLA :" + tableNameTNC + " */");

            for (int h = 0; h < listTableXColumsP.size(); h++) {
                String tableNameTCP = listTableXColumsP.get(h).get("TableName");
                String columnNameTCP = listTableXColumsP.get(h).get("ColumnName");
                String dataTypeColumnTCP = listTableXColumsP.get(h).get("DataType");
                String bytes = listTableXColumsP.get(h).get("NumAtributo");
                /*Compare Tables*/
                if (tableNameTNC.equals(tableNameTCP)) {
                    /*Variables*/
                    String columna = conversor.toJavaFormat(columnNameTCP, "_");
                    String dataType = "";
                    Boolean isForean = false;
                    Boolean isPrimaryKey = false;
                    dataType = dataTypeColumnTCP;
                    /*Llaves Primarias*/
                    String parametersProcedure = "sp" + conversor.firstCharacterToUpper(columna);
                    for (int g = 0; g < listPrimaryKey.size(); g++) {
                        String pkTableName = String.valueOf(listPrimaryKey.get(g).get("TableName"));
                        String pkColumnName = String.valueOf(listPrimaryKey.get(g).get("ColumnName"));
                        String pkConstraintName = String.valueOf(listPrimaryKey.get(g).get("ConstraintName"));
                        // System.err.println(tableNameTNC + " : " + pkTableName);
                        if (tableNameTNC.equalsIgnoreCase(pkTableName)
                                & columnNameTCP.equalsIgnoreCase(pkColumnName)) {
                            pksCurrentTable.add(pkColumnName);
                            paramsPrimaryKey += pkColumnName + " " + sql.getDataType(dataType, database) + ",";
                            isPrimaryKey = true;
                        }
                    }
                    if (!isPrimaryKey) {
                        /*Llaves Foraneas*/
                        for (int d = 0; d < listForeignKey.size(); d++) {
                            String table = String.valueOf(listForeignKey.get(d).get("TableName"));
                            String column = String.valueOf(listForeignKey.get(d).get("ColumnName"));
                            String ForeignTable = String.valueOf(listForeignKey.get(d).get("ForeignTable"));
                            String ForeignColumn = String.valueOf(listForeignKey.get(d).get("ForeignColumn"));
                            if (tableNameTNC.equalsIgnoreCase(table) & columnNameTCP.equalsIgnoreCase(column)) {
                                // String ColumnaBean = conversor.toJavaFormat(columna, "_");
                                isForean = true;
                                /*MAKE HEADER PARAMETERS*/
                                makeParamsMethods += sql.headerParamsProcedure(parametersProcedure, sql.getDataType(dataType, database), bytes, database);
                                makeColumns += "sp" + conversor.firstCharacterToUpper(columna) + ",";
                                makeColumnsTable += columnNameTCP + ",";
                                makeParamsUpdate += columnNameTCP + "=" + parametersProcedure + ", ";
                            }
                        }
                    }
                    if (!isForean & !isPrimaryKey) {
                        makeColumns += parametersProcedure + ",";
                        makeParamsMethods += sql.headerParamsProcedure(parametersProcedure, sql.getDataType(dataType, database), bytes, database);
                        makeColumnsTable += columnNameTCP + ",";
                        makeParamsUpdate += columnNameTCP + "=" + parametersProcedure + ", ";
                    }
                    makeAllParamsMethods += "sp" + conversor.firstCharacterToUpper(columna) + " " + sql.getDataType(dataType, database);
                    makeParamsUpdate += columnNameTCP + "=" + "sp" + conversor.firstCharacterToUpper(columna) + ",";
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
            /*Insert record procedure */
            makeMethods += sql.headerProcedure("spi_" + tableNameTNC, makeParamsMethods, database);
            makeMethods += "INSERT INTO " + tableNameTNC + "(" + makeColumnsTable + ") values (" + makeColumns + ")";

            /*ruturn id*/
            if (pksCurrentTable.size() == 1 & returnId) {
                makeMethods += " returning " + pksCurrentTable.get(0) + " into result ;";
            } else {
                makeMethods += ";";
            }
            makeMethods += sql.footerProcedure("spi_" + tableNameTNC, database);
            //*EDIT procedure*/
            makeMethods += sql.headerProcedure("spu_" + tableNameTNC, makeParamsMethods, database);
            /*procedure body*/
            makeMethods += "UPDATE " + tableNameTNC + " set " + makeParamsUpdate + " where ";

            for (int t = 0; t < pksCurrentTable.size(); t++) {
                if (t == 0) {
                    makeMethods += pksCurrentTable.get(t) + " = " + "sp" + conversor.firstCharacterToUpper(conversor.toJavaFormat(pksCurrentTable.get(t), "_"));
                } else {
                    makeMethods += " and " + pksCurrentTable.get(t) + " = " + "sp" + conversor.firstCharacterToUpper(conversor.toJavaFormat(pksCurrentTable.get(t), "_"));
                }
            }
            makeMethods += ";";
            makeMethods += sql.footerProcedure("spi_" + tableNameTNC, database);
            /*DELETE procedure*/
            makeMethods += sql.headerProcedure("spd_" + tableNameTNC, paramsPrimaryKey, database);
            /*DELETE body*/
            makeMethods += "UPDATE " + tableNameTNC + " set recordStatus=0 where ";
            for (int t = 0; t < pksCurrentTable.size(); t++) {
                if (t == 0) {
                    makeMethods += pksCurrentTable.get(t) + " = " + "sp" + conversor.firstCharacterToUpper(conversor.toJavaFormat(pksCurrentTable.get(t), "_"));
                } else {
                    makeMethods += " and " + pksCurrentTable.get(t) + " = " + "sp" + conversor.firstCharacterToUpper(conversor.toJavaFormat(pksCurrentTable.get(t), "_"));
                }
            }
            makeMethods += ";";
            makeMethods += sql.footerProcedure("spi_" + tableNameTNC, database);

            /*Print */
            System.out.println(makeMethods);
            /*Contructor*/
        }
        return true;
    }

}
