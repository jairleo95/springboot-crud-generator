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

        for (int f = 0; f < table.size(); f++) {
            /*one or more ids*/
            List<String> pksCurrentTable = new ArrayList<String>();
            Table tnc = table.get(f);
            // String tableName = Conversor.toJavaFormat(List11[0].substring(6), "_");
            String tableName = Conversor.toJavaFormat(tnc.getName(), "_");
            String tableEntity = Conversor.firstCharacterToUpper(tableName);
            //String beanName = tableEntity + "Bean ";
            System.out.println("/*TABLA :" + tnc.getName() + " */");
            String makeAssociatonColumns = "";
            String makeColumns = "";
            String makeMethods = "";
            String makeParamsMethods = "";
            String paramsPrimaryKey = "";
            for (int h = 0; h < columns.size(); h++) {
              /*table-column-property (TCP)*/
                Table tcp = columns.get(h);
                /*Compare DAO*/
                if (tnc.getName().equals(tcp.getName())) {
                    /*Variables*/
                    String columna = Conversor.toJavaFormat(tcp.getColumn().getName(), "_");
                    Boolean isForean = false;
                    Boolean isPrimaryKey = false;
                    /*Llaves Foraneas*/
                    for (int g = 0; g < listPrimaryKey.size(); g++) {
                       /*primary keys*/
                        Table pk = listPrimaryKey.get(g);
                        if (tnc.getName().equalsIgnoreCase(pk.getName())& tcp.getColumn().getName().equalsIgnoreCase(pk.getColumn().getName())) {
                            makeColumns += ("<id property=\"" + columna + "\" column=\"" + pk.getColumn().getName() + "\" />");
                            pksCurrentTable.add(pk.getColumn().getName());
                            paramsPrimaryKey += "#{" + pk.getColumn().getName() + "},";
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

                            if (tnc.getName().equalsIgnoreCase(fk.getName()) & tcp.getColumn().getName().equalsIgnoreCase(fk.getColumn().getName())) {
                                // String foreignTableEntity = Conversor.firstCharacterToUpper(Conversor.toJavaFormat(ForeignTable.substring(6), "_"));
                                String foreignTableEntity = Conversor.firstCharacterToUpper(Conversor.toJavaFormat(fk.getForeignTable(), "_"));
                                String ForeignColumnBean = Conversor.toJavaFormat(fk.getForeignColumn(), "_");
                                // String ColumnaBean = Conversor.toJavaFormat(column, "_");
                                makeAssociatonColumns += "<association property=\"" + columna + "\" javaType=\"" + foreignTableEntity + "\">";
                                for (int hh = 0; hh < columns.size(); hh++) {
                                    String tableNameFk = columns.get(h).getName();
                                    String columnNameFk = columns.get(h).getColumn().getName();
                                    if (tableNameFk.equals(fk.getForeignColumn())) {
                                        makeAssociatonColumns += "<id column=\"" + columnNameFk + "\" property=\"" + Conversor.toJavaFormat(tcp.getColumn().getName(), "_") + "\"></id>";
                                    } else {
                                        makeAssociatonColumns += "<result column=\"" + columnNameFk + "\" property=\"" + Conversor.toJavaFormat(tcp.getColumn().getName(), "_") + "\"></result>";
                                    }
                                }

                                makeAssociatonColumns += "</association>";
                                isForean = true;
                                makeParamsMethods += "#{" + columna + "" + ForeignColumnBean + "},";
                            }
                        }
                    }
                    if (!isForean & !isPrimaryKey) {
                        makeColumns += ("<result property=\"" + columna + "\" column=\"" + tcp.getColumn().getName() + "\" />");
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

            makeMethods += "select spi_" + tnc.getName() + "(" + makeParamsMethods + ");";
            makeMethods += "</select>";
            //*EDIT METHOD*/
            makeMethods += "<select id=\"edit\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
            makeMethods += "select spu_" + tnc.getName() + "(" + makeParamsMethods + ");";
            makeMethods += "</select>";
            //*DELETE METHOD*/
            makeMethods += "<select id=\"delete\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
            makeMethods += "select spd_" + tnc.getName() + "(" + paramsPrimaryKey + ",usuEli.varUsuario);";
            makeMethods += "</select>";

            //*LIST METHOD*/
            makeMethods += "<select id=\"getAll\" resultMap=\"" + tableEntity + "Map" + "\">";
            makeMethods += "select * from " + tnc.getName() + ";";
            makeMethods += "</select>";

            //*FIND BY ID*/
            makeMethods += "<select id=\"findById\"  parameterType=\"Integer\" resultMap=\"" + tableEntity + "Map" + "\">";
            makeMethods += "select * from " + tnc.getName() + " where ";
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
