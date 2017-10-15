/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreCRUD.spring;

import conexionDB.ConexionBD;
import conexionDB.FactoryConnectionDB;
import java.util.ArrayList;
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
public class mapperXmlGenerator {

    ConexionBD conn;

    public Boolean makeMapperXml() throws Exception {
        int database = FactoryConnectionDB.getDefaultDatabase();
        //   boolean returnId = false;
        this.conn = FactoryConnectionDB.open(database);
        DAO dao = new DAO();
        List<Map<String, String>> listTableXNumColumns = dao.listTableXNumColumns(database,
                makeQuery.getSQLTableXNumColums(database));

        List<Map<String, String>> listTableXColumsP = dao.listTableXColumsP(database,
                makeQuery.getTableXColumsProperties(database));
        List<Map<String, String>> listForeignKey = dao.listForeignKey(database,
                makeQuery.getTableColumnsForeignProperties(database));

        List<Map<String, String>> listPrimaryKey = dao.listPrimaryKey(database,
                makeQuery.getTableColumnsPrimaryKeyProperties(database));

        for (int f = 0; f < listTableXNumColumns.size(); f++) {
            /*one or more ids*/
            List<String> pksCurrentTable = new ArrayList<String>();
            String tableNameTNC = listTableXNumColumns.get(f).get("TableName");
            // String tableName = conversor.toJavaFormat(List11[0].substring(6), "_");
            String tableName = conversor.toJavaFormat(tableNameTNC, "_");
            String tableEntity = conversor.firstCharacterToUpper(tableName);
            //String beanName = tableEntity + "Bean ";
            System.out.println("/*TABLA :" + tableNameTNC + " */");
            String makeAssociatonColumns = "";
            String makeColumns = "";
            String makeMethods = "";
            String makeParamsMethods = "";
            String paramsPrimaryKey = "";
            for (int h = 0; h < listTableXColumsP.size(); h++) {
                String tableNameTCP = listTableXColumsP.get(h).get("TableName");
                String columnNameTCP = listTableXColumsP.get(h).get("ColumnName");
                // String dataTypeColumnTCP = listTableXColumsP.get(h).get("DataType");
                //String bytes = listTableXColumsP.get(h).get("NumAtributo");
                /*Compare Tables*/
                if (tableNameTNC.equals(tableNameTCP)) {
                    /*Variables*/
                    String columna = conversor.toJavaFormat(columnNameTCP, "_");
                    Boolean isForean = false;
                    Boolean isPrimaryKey = false;
                    /*Llaves Foraneas*/
                    for (int g = 0; g < listPrimaryKey.size(); g++) {
                        String pkTableName = String.valueOf(listPrimaryKey.get(g).get("TableName"));
                        String pkColumnName = String.valueOf(listPrimaryKey.get(g).get("ColumnName"));
                        String pkConstraintName = String.valueOf(listPrimaryKey.get(g).get("ConstraintName"));
                        if (tableNameTNC.equalsIgnoreCase(pkTableName)
                                & columnNameTCP.equalsIgnoreCase(pkColumnName)) {
                            makeColumns += ("<id property=\"" + columna + "\" column=\"" + pkColumnName + "\" />");
                            pksCurrentTable.add(pkColumnName);
                            paramsPrimaryKey += "#{" + pkColumnName + "},";
                            isPrimaryKey = true;
                        }
                    }
                    if (!isPrimaryKey) {
                        for (int d = 0; d < listForeignKey.size(); d++) {
                            String table = String.valueOf(listForeignKey.get(d).get("TableName"));
                            String column = String.valueOf(listForeignKey.get(d).get("ColumnName"));
                            String ForeignTable = String.valueOf(listForeignKey.get(d).get("ForeignTable"));
                            String ForeignColumn = String.valueOf(listForeignKey.get(d).get("ForeignColumn"));
                            if (tableNameTNC.equalsIgnoreCase(table) & columnNameTCP.equalsIgnoreCase(column)) {
                                // String foreignTableEntity = conversor.firstCharacterToUpper(conversor.toJavaFormat(ForeignTable.substring(6), "_"));
                                String foreignTableEntity = conversor.firstCharacterToUpper(conversor.toJavaFormat(ForeignTable, "_"));
                                String ForeignColumnBean = conversor.toJavaFormat(ForeignColumn, "_");
                                // String ColumnaBean = conversor.toJavaFormat(columna, "_");
                                makeAssociatonColumns += "<association property=\"" + columna + "\" javaType=\"" + foreignTableEntity + "\">";
                                for (int hh = 0; hh < listTableXColumsP.size(); hh++) {
                                    String tableNameFk = listTableXColumsP.get(h).get("TableName");
                                    String columnNameFk = listTableXColumsP.get(h).get("ColumnName");
                                    if (tableNameFk.equals(ForeignColumn)) {
                                        makeAssociatonColumns += "<id column=\"" + columnNameFk + "\" property=\"" + conversor.toJavaFormat(columnNameTCP, "_") + "\"></id>";
                                    } else {
                                        makeAssociatonColumns += "<result column=\"" + columnNameFk + "\" property=\"" + conversor.toJavaFormat(columnNameTCP, "_") + "\"></result>";
                                    }
                                }

                                makeAssociatonColumns += "</association>";
                                isForean = true;
                                makeParamsMethods += "#{" + columna + "." + ForeignColumnBean + "},";
                            }
                        }
                    }
                    if (!isForean & !isPrimaryKey) {
                        makeColumns += ("<result property=\"" + columna + "\" column=\"" + columnNameTCP + "\" />");
                        makeParamsMethods += "#{" + columna + "},";
                    }
                }
            }

            if (!makeParamsMethods.equals("")) {
                makeParamsMethods = makeParamsMethods.substring(0, (makeParamsMethods.length() - 1));
            }
            if (!paramsPrimaryKey.equals("")) {
                paramsPrimaryKey = paramsPrimaryKey.substring(0, (paramsPrimaryKey.length() - 1));
            }

            /*Save Method */
            makeMethods += "<select id=\"save\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";

            makeMethods += "select spi_" + tableNameTNC + "(" + makeParamsMethods + ");";
            makeMethods += "</select>";
            //*EDIT METHOD*/
            makeMethods += "<select id=\"edit\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
            makeMethods += "select spu_" + tableNameTNC + "(" + makeParamsMethods + ");";
            makeMethods += "</select>";
            //*DELETE METHOD*/
            makeMethods += "<select id=\"delete\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
            makeMethods += "select spd_" + tableNameTNC + "(" + paramsPrimaryKey + ",usuEli.varUsuario);";
            makeMethods += "</select>";

            //*LIST METHOD*/
            makeMethods += "<select id=\"getAll\" resultMap=\"" + tableEntity + "Map" + "\">";
            makeMethods += "select * from " + tableNameTNC + ";";
            makeMethods += "</select>";

            //*FIND BY ID*/
            makeMethods += "<select id=\"findById\"  parameterType=\"Integer\" resultMap=\"" + tableEntity + "Map" + "\">";
            makeMethods += "select * from " + tableNameTNC + " where ";
            for (int ii = 0; ii < pksCurrentTable.size(); ii++) {
                if (ii == 0) {
                    makeMethods += pksCurrentTable.get(ii) + " = " + "#{" + conversor.toJavaFormat(pksCurrentTable.get(ii), "_") + "} ";
                } else {
                    makeMethods += " and " + pksCurrentTable.get(ii) + " = " + "#{" + conversor.toJavaFormat(pksCurrentTable.get(ii), "_") + "} ";
                }

            }
            makeMethods += ";";
            makeMethods += "</select>";
            String content = "";

            /*Print */
            System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            content += ("<!DOCTYPE mapper    PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
            content += ("<mapper namespace=\"org.proyecto.mapper." + tableName + "." + conversor.firstCharacterToUpper(tableName) + "Mapper\">");
            content += ("<resultMap id=\"" + conversor.firstCharacterToUpper(tableName) + "Map\" type=\"" + conversor.firstCharacterToUpper(tableName) + "\">");
            content += (makeColumns);
            content += (makeAssociatonColumns);
            content += ("</resultMap>");
            content += (makeMethods);

            content += ("</mapper>");
            makeFile.writeFolderAndFile("org\\proyecto\\resources\\xml\\mapper\\" + tableName + "\\", tableName + "-mapper.xml", content);
            System.out.println(content);
        }
        return true;
    }

}
