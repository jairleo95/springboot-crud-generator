/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.pattern.spring;

import java.util.ArrayList;
import java.util.List;

import com.alphateam.core.template.Template;
import com.alphateam.query.Table;
import com.alphateam.utiles.Conversor;
import com.alphateam.utiles.FileBuilder;

/**
 *
 * @author Jairleo95
 */
public class MapperXml extends Template {

    public void build() {
        init();

        for (int f = 0; f < listTableXNumColumns.size(); f++) {
            /*one or more ids*/
            List<String> pksCurrentTable = new ArrayList<String>();
            Table tnc = listTableXNumColumns.get(f);
            // String tableName = Conversor.toJavaFormat(List11[0].substring(6), "_");
            String tableName = Conversor.toJavaFormat(tnc.getTableName(), "_");
            String tableEntity = Conversor.firstCharacterToUpper(tableName);
            //String beanName = tableEntity + "Bean ";
            System.out.println("/*TABLA :" + tnc.getTableName() + " */");
            String makeAssociatonColumns = "";
            String makeColumns = "";
            String makeMethods = "";
            String makeParamsMethods = "";
            String paramsPrimaryKey = "";
            for (int h = 0; h < listTableXColumsP.size(); h++) {
              /*table-column-property (TCP)*/
                Table tcp = listTableXColumsP.get(h);
                /*Compare DAO*/
                if (tnc.getTableName().equals(tcp.getTableName())) {
                    /*Variables*/
                    String columna = Conversor.toJavaFormat(tcp.getColumnName(), "_");
                    Boolean isForean = false;
                    Boolean isPrimaryKey = false;
                    /*Llaves Foraneas*/
                    for (int g = 0; g < listPrimaryKey.size(); g++) {
                       /*primary keys*/
                        Table pk = listPrimaryKey.get(g);
                        if (tnc.getTableName().equalsIgnoreCase(pk.getTableName())& tcp.getColumnName().equalsIgnoreCase(pk.getColumnName())) {
                            makeColumns += ("<id property=\"" + columna + "\" column=\"" + pk.getColumnName() + "\" />");
                            pksCurrentTable.add(pk.getColumnName());
                            paramsPrimaryKey += "#{" + pk.getColumnName() + "},";
                            isPrimaryKey = true;
                        }
                    }
                    if (!isPrimaryKey) {
                        for (int d = 0; d < listForeignKey.size(); d++) {
                            Table fk = listForeignKey.get(d);

                            /*String table = String.valueOf(listForeignKey.get(d).get("TableName"));
                            String column = String.valueOf(listForeignKey.get(d).get("ColumnName"));
                            String ForeignTable = String.valueOf(listForeignKey.get(d).get("ForeignTable"));
                            String ForeignColumn = String.valueOf(listForeignKey.get(d).get("ForeignColumn"));*/

                            if (tnc.getTableName().equalsIgnoreCase(fk.getTableName()) & tcp.getColumnName().equalsIgnoreCase(fk.getColumnName())) {
                                // String foreignTableEntity = Conversor.firstCharacterToUpper(Conversor.toJavaFormat(ForeignTable.substring(6), "_"));
                                String foreignTableEntity = Conversor.firstCharacterToUpper(Conversor.toJavaFormat(fk.getForeignTable(), "_"));
                                String ForeignColumnBean = Conversor.toJavaFormat(fk.getForeignColumn(), "_");
                                // String ColumnaBean = Conversor.toJavaFormat(columna, "_");
                                makeAssociatonColumns += "<association property=\"" + columna + "\" javaType=\"" + foreignTableEntity + "\">";
                                for (int hh = 0; hh < listTableXColumsP.size(); hh++) {
                                    String tableNameFk = listTableXColumsP.get(h).getTableName();
                                    String columnNameFk = listTableXColumsP.get(h).getColumnName();
                                    if (tableNameFk.equals(fk.getForeignColumn())) {
                                        makeAssociatonColumns += "<id column=\"" + columnNameFk + "\" property=\"" + Conversor.toJavaFormat(tcp.getColumnName(), "_") + "\"></id>";
                                    } else {
                                        makeAssociatonColumns += "<result column=\"" + columnNameFk + "\" property=\"" + Conversor.toJavaFormat(tcp.getColumnName(), "_") + "\"></result>";
                                    }
                                }

                                makeAssociatonColumns += "</association>";
                                isForean = true;
                                makeParamsMethods += "#{" + columna + "" + ForeignColumnBean + "},";
                            }
                        }
                    }
                    if (!isForean & !isPrimaryKey) {
                        makeColumns += ("<result property=\"" + columna + "\" column=\"" + tcp.getColumnName() + "\" />");
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

            makeMethods += "select spi_" + tnc.getTableName() + "(" + makeParamsMethods + ");";
            makeMethods += "</select>";
            //*EDIT METHOD*/
            makeMethods += "<select id=\"edit\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
            makeMethods += "select spu_" + tnc.getTableName() + "(" + makeParamsMethods + ");";
            makeMethods += "</select>";
            //*DELETE METHOD*/
            makeMethods += "<select id=\"delete\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
            makeMethods += "select spd_" + tnc.getTableName() + "(" + paramsPrimaryKey + ",usuEli.varUsuario);";
            makeMethods += "</select>";

            //*LIST METHOD*/
            makeMethods += "<select id=\"getAll\" resultMap=\"" + tableEntity + "Map" + "\">";
            makeMethods += "select * from " + tnc.getTableName() + ";";
            makeMethods += "</select>";

            //*FIND BY ID*/
            makeMethods += "<select id=\"findById\"  parameterType=\"Integer\" resultMap=\"" + tableEntity + "Map" + "\">";
            makeMethods += "select * from " + tnc.getTableName() + " where ";
            for (int ii = 0; ii < pksCurrentTable.size(); ii++) {
                if (ii == 0) {
                    makeMethods += pksCurrentTable.get(ii) + " = " + "#{" + Conversor.toJavaFormat(pksCurrentTable.get(ii), "_") + "} ";
                } else {
                    makeMethods += " and " + pksCurrentTable.get(ii) + " = " + "#{" + Conversor.toJavaFormat(pksCurrentTable.get(ii), "_") + "} ";
                }

            }
            makeMethods += ";";
            makeMethods += "</select>";
            String content = "";

            /*Print */
            System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            content += ("<!DOCTYPE mapper    PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
            content += ("<mapper namespace=\"org.proyecto.mapper." + tableName + "" + Conversor.firstCharacterToUpper(tableName) + "Mapper\">");
            content += ("<resultMap id=\"" + Conversor.firstCharacterToUpper(tableName) + "Map\" type=\"" + Conversor.firstCharacterToUpper(tableName) + "\">");
            content += (makeColumns);
            content += (makeAssociatonColumns);
            content += ("</resultMap>");
            content += (makeMethods);

            content += ("</mapper>");
            FileBuilder.writeFolderAndFile("org\\proyecto\\resources\\xml\\mapper\\" + tableName + "\\", tableName + "-mapper.xml", content);
            System.out.println(content);
        }
    }

}
