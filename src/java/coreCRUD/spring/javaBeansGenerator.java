/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreCRUD.spring;

import conexionDB.ConexionBD;
import conexionDB.FactoryConnectionDB;
import convert.java;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import query.DAO;
import query.makeQuery;
import utiles.conversor;
import utiles.makeFile;

/**
 *
 * @author Jairleo95
 */
public class javaBeansGenerator {

    ConexionBD conn;

    public Boolean makeJavaBeans() throws SQLException {
        this.conn = FactoryConnectionDB.open(FactoryConnectionDB.getDefaultDatabase());
        DAO dao = new DAO();
        List<Map<String, String>> listTableXNumColumns = dao.listTableXNumColumns(FactoryConnectionDB.getDefaultDatabase(),
                makeQuery.getSQLTableXNumColums(FactoryConnectionDB.getDefaultDatabase()));
        List<Map<String, String>> listTableXColumsP = dao.listTableXColumsP(FactoryConnectionDB.getDefaultDatabase(),
                makeQuery.getTableXColumsProperties(FactoryConnectionDB.getDefaultDatabase()));
        List<Map<String, String>> listForeignKey = dao.listForeignKey(FactoryConnectionDB.getDefaultDatabase(),
                makeQuery.getTableColumnsForeignProperties(FactoryConnectionDB.getDefaultDatabase()));

        for (int g = 0; g < listTableXNumColumns.size(); g++) {
            String tableNameTNC = listTableXNumColumns.get(g).get("TableName");
            //   String numColumnsTNC = listTableXNumColumns.get(g).get("NumColumns");
            String tableName = conversor.toJavaFormat(tableNameTNC, "_");
            String beanName = conversor.firstCharacterToUpper(tableName + "Bean");
            System.out.println("//TABLA :" + tableNameTNC);

            String makeInstanceBean = "";
            String makeSettersAndGetters = "";
            String makeImports = "";
            String makeColumns = "";
            // for (String[] List21 : tableXColumsProperties) {
            for (int h = 0; h < listTableXColumsP.size(); h++) {
                String tableNameTCP = listTableXColumsP.get(h).get("TableName");
                String columnNameTCP = listTableXColumsP.get(h).get("ColumnName");
                String dataTypeColumnTCP = listTableXColumsP.get(h).get("DataType");
                /*  }
                String tableNameTCP = List21[0];
                String columnNameTCP = List21[1];
                String dataTypeColumnTCP = List21[2];*/

                if (tableNameTNC.equals(tableNameTCP)) {
                    /*Variables*/
                    String columna = conversor.toJavaFormat(columnNameTCP, "_");
                    Boolean isForean = false;

                    /*Llaves Foraneas*/
                    for (int d = 0; d < listForeignKey.size(); d++) {
                        String table = listForeignKey.get(d).get("TableName");
                        String column = listForeignKey.get(d).get("ColumnName");
                        String ForeignTable = listForeignKey.get(d).get("ForeignTable");
                        //  String ForeignColumn = listForeignKey.get(d).get("ForeignColumn");
                        System.err.println(tableNameTNC + ":" + table);
                        if (tableNameTNC.equalsIgnoreCase(table) & columnNameTCP.equalsIgnoreCase(column)) {
                            System.err.println("here!");
                            // String TableBean = conversor.firstCharacterToUpper(conversor.toJavaFormat(ForeignTable.substring(6), "_")) + "Bean";
                            String TableBean = conversor.firstCharacterToUpper(conversor.toJavaFormat(ForeignTable, "_")) + "Bean";
                            String ColumnaBean = conversor.toJavaFormat(columna, "_");
                            makeColumns += ("private " + TableBean + " " + ColumnaBean + ";");
                            // s++;
                            isForean = true;
                            makeInstanceBean += ColumnaBean + " = new " + TableBean + "();";
                            makeSettersAndGetters += ("public void set" + conversor.firstCharacterToUpper(ColumnaBean) + "(" + TableBean + " " + ColumnaBean + "){"
                                    + "this." + ColumnaBean + "=" + ColumnaBean + ";}");
                            makeSettersAndGetters += "public " + TableBean + " get" + conversor.firstCharacterToUpper(ColumnaBean) + "(){"
                                    + "return " + ColumnaBean + ";}";
                            makeImports += " import org.proyecto.bean." + conversor.toJavaFormat(ForeignTable, "_") + "." + TableBean + ";";
                        }
                    }
                    /*columnas*/
                    String dataType = "";
                    if (!isForean) {
                        dataType = java.getDataType(dataTypeColumnTCP, FactoryConnectionDB.getDefaultDatabase());
                        makeColumns += ("private " + dataType + " " + columna + ";");
                        makeSettersAndGetters += ("public void set" + conversor.firstCharacterToUpper(columna) + "(" + dataType + " " + columna + "){"
                                + "this." + columna + "=" + columna + ";}");
                        makeSettersAndGetters += "public " + dataType + "  get" + conversor.firstCharacterToUpper(columna) + "(){"
                                + "return " + columna + ";}";
                        makeImports += java.getImportsByDataType(dataType);
                        makeInstanceBean += "this." + columna + "=" + java.getInstanceByDataType(dataType) + ";";
                    }
                }
            }

            String content = "";
            content += ("package org.proyecto.bean." + tableName + ";");
            content += (makeImports);
            content += ("public class " + beanName + " {");
            content += (makeColumns);
            /*Contructor*/
            content += ("public " + beanName + "(){");
            content += (makeInstanceBean);
            content += ("}");
            content += (makeSettersAndGetters);
            content += ("}");
            makeFile.writeFolderAndFile("org\\proyecto\\bean\\" + tableName + "\\", beanName + ".java", content);
            System.out.println(content);
        }
        return true;
    }

}
