package com.alphateam.core.pattern.spring;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 
package com.alphateam.core.pattern.spring;

import java.util.ArrayList;
import java.util.List;
*/
import com.alphateam.core.template.Template;
import com.alphateam.properties.Global;
import com.alphateam.query.Column;
import com.alphateam.query.Table;
import com.alphateam.utiles.Conversor;
import com.alphateam.utiles.FileBuilder;

import java.util.List;

/**
 *
 * @author Jairleo95
 */



public class MapperXml extends Template {

    String makeAssociatonColumns = "";
    String makeColumns = "";
    String makeMethods = "";
    String makeParamsMethods = "";
    String paramsPrimaryKey = "";

    @Override
    public void primaryKeys(Table table, Column pk) {
        super.primaryKeys(table, pk);
        String columna = Conversor.toJavaFormat(pk.getName(), "_");
        makeColumns += ("<id property=\"" + columna + "\" column=\"" + pk.getName() + "\" />");
        paramsPrimaryKey += "#{" + pk.getName() + "},";
    }

    @Override
    public void foreignKeys(Table tcp, Column fk) {
        super.foreignKeys(tcp, fk);

        String columna = Conversor.toJavaFormat(fk.getName(), "_");
        String foreignTableEntity = Conversor.firstCharacterToUpper(Conversor.toJavaFormat(fk.getForeignTable(), "_"));
            String ForeignColumnBean = Conversor.toJavaFormat(fk.getForeignColumn(), "_");
            makeAssociatonColumns += "<association property=\"" + columna + "\" javaType=\"" + foreignTableEntity + "\">";
            List<Column> foreignColumns =  dao.getColumsProperties(fk.getForeignTable());
            for (int hh = 0; hh < foreignColumns.size(); hh++) {
                //String tableNameFk = foreignColumns.get(h).getName();
                String columnNameFk = foreignColumns.get(hh).getName();
                if (columnNameFk.equals(fk.getForeignColumn())) {
                    makeAssociatonColumns += "<id column=\"" + columnNameFk + "\" property=\"" + Conversor.toJavaFormat(columnNameFk, "_") + "\"></id>";
                } else {
                    makeAssociatonColumns += "<result column=\"" + columnNameFk + "\" property=\"" + Conversor.toJavaFormat(columnNameFk, "_") + "\"></result>";
                }
            }

            makeAssociatonColumns += "</association>";
            makeParamsMethods += "#{" + columna + "" + ForeignColumnBean + "},";

    }

    @Override
    public void buildParameters(Table table, Column column) {
        super.buildParameters(table, column);
        String columna = Conversor.toJavaFormat(column.getName(), "_");

        makeColumns += ("<result property=\"" + columna + "\" column=\"" + table.getName() + "\" />");
        makeParamsMethods += "#{" + columna + "},";
    }

    @Override
    public void buildMethods(Table tnc, List<String> pks) {
        super.buildMethods(tnc, pks);

        String tableName = Conversor.toJavaFormat(tnc.getName(), "_");
        String tableEntity = Conversor.firstCharacterToUpper(tableName);


        if (!makeParamsMethods.equals("")) {
            makeParamsMethods = makeParamsMethods.substring(0, (makeParamsMethods.length() - 1));
        }
        if (!paramsPrimaryKey.equals("")) {
            paramsPrimaryKey = paramsPrimaryKey.substring(0, (paramsPrimaryKey.length() - 1));
        }

        //Save Method
        makeMethods += "<select id=\"save\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";

        makeMethods += "select spi_" + tnc.getName() + "(" + makeParamsMethods + ");";
        makeMethods += "</select>";
        //*EDIT METHOD
        makeMethods += "<select id=\"edit\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
        makeMethods += "select spu_" + tnc.getName() + "(" + makeParamsMethods + ");";
        makeMethods += "</select>";
        //*DELETE METHOD
        makeMethods += "<select id=\"delete\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
        makeMethods += "select spd_" + tnc.getName() + "(" + paramsPrimaryKey + ",usuEli.varUsuario);";
        makeMethods += "</select>";

        //*LIST METHOD
        makeMethods += "<select id=\"getAll\" resultMap=\"" + tableEntity + "Map" + "\">";
        makeMethods += "select * from " + tnc.getName() + ";";
        makeMethods += "</select>";

        //*FIND BY ID
        makeMethods += "<select id=\"findById\"  parameterType=\"Integer\" resultMap=\"" + tableEntity + "Map" + "\">";
        makeMethods += "select * from " + tnc.getName() + " where ";
        for (int ii = 0; ii < pks.size(); ii++) {
            if (ii == 0) {
                makeMethods += pks.get(ii) + " = " + "#{" + Conversor.toJavaFormat(pks.get(ii), "_") + "} ";
            } else {
                makeMethods += " and " + pks.get(ii) + " = " + "#{" + Conversor.toJavaFormat(pks.get(ii), "_") + "} ";
            }

        }
        makeMethods += ";";
        makeMethods += "</select>";
       // String content = "";

        //Print
        System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        content += ("<!DOCTYPE mapper    PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
        content += ("<mapper namespace=\"org.proyecto.mapper." + tableName + "" + Conversor.firstCharacterToUpper(tableName) + "Mapper\">");
        content += ("<resultMap id=\"" + Conversor.firstCharacterToUpper(tableName) + "Map\" type=\"" + Conversor.firstCharacterToUpper(tableName) + "\">");
        content += (makeColumns);
        content += (makeAssociatonColumns);
        content += ("</resultMap>");
        content += (makeMethods);

        content += ("</mapper>");
        generateProject(Global.MAPPER_XML_LOCATION + tableName + "\\", tableName + "-mapper.xml");
        System.out.println(content);
    }
    @Override
    public void resetValues() {
        super.resetValues();
         makeAssociatonColumns = "";
         makeColumns = "";
         makeMethods = "";
         makeParamsMethods = "";
         paramsPrimaryKey = "";
    }
}
