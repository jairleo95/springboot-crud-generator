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

import java.util.List;

/**
 *
 * @author Jairleo95
 */



public class MapperXml extends Template {

    String associatonColumns = "";
    String parameters = "";
    String methods = "";
    String paramsMethods = "";
    String paramsPrimaryKey = "";


    @Override
    public void primaryKeys(Table table, Column pk) {
        super.primaryKeys(table, pk);
        String columna = pk.getName();
        parameters += ("       <id property=\"" + columna + "\" column=\"" + pk.getRawName() + "\" />\n");
        paramsPrimaryKey += "#{" +columna + "},";
    }

    @Override
    public void foreignKeys(Table tcp, Column fk) {
        super.foreignKeys(tcp, fk);

        String columna = fk.getName();
        String tableBean = Conversor.firstCharacterToUpper((fk.getForeignTable())+"Bean");
            String ForeignColumnBean = fk.getForeignColumn();

           associatonColumns += "<association property=\"" + columna + "\" javaType=\"" + tableBean + "\">\n";

           //todo: refactor this
//
//            List<Column> foreignColumns =  dao.getColumsProperties(fk.getForeignTable());
//
//            for (int hh = 0; hh < foreignColumns.size(); hh++) {
//
//                String columnNameFk = foreignColumns.get(hh).getRawName();
//                if (columnNameFk.equals(fk.getForeignColumn())) {
//                    associatonColumns += "          <id column=\"" + columnNameFk + "\" property=\"" + columnNameFk + "\"></id>\n";
//                } else {
//                    //associatonColumns += "          <result column=\"" + columnNameFk + "\" property=\"" + Conversor.toJavaFormat(columnNameFk, "_") + "\"></result>\n";
//                }
//            }

            associatonColumns += "</association>\n";
            paramsMethods += "#{" + columna + "" + ForeignColumnBean + "},";

    }

    @Override
    public void buildParameters(Table table, Column column) {
        super.buildParameters(table, column);
        String columna = column.getName();

        parameters += ("       <result property=\"" + columna + "\" column=\"" + column.getRawName() + "\" />\n");
        paramsMethods += "#{" + columna + "},";
    }

    @Override
    public void buildMethods(Table table, List<String> pks) {
        super.buildMethods(table, pks);

        String tableName = table.getName();
        String tableBean = Conversor.firstCharacterToUpper(tableName)+"Bean";


        if (!paramsMethods.equals("")) {
            paramsMethods = paramsMethods.substring(0, (paramsMethods.length() - 1));
        }
        if (!paramsPrimaryKey.equals("")) {
            paramsPrimaryKey = paramsPrimaryKey.substring(0, (paramsPrimaryKey.length() - 1));
        }

        //Save Method
        methods += "<select id=\"create\" parameterType=\""+tableBean+"\" resultMap=\"" + tableName + "Map\" statementType=\"CALLABLE\">\n";

        methods += "call spi_" + table.getName() + "(" + paramsMethods + ")";
        methods += "</select>\n";
        //getAll
        methods += "<select id=\"read\" resultMap=\"" + tableName + "Map\" >\n";
        methods += "select * from " + table.getRawName() + "";
        methods += "</select>\n";

        //*EDIT METHOD
        methods += "<select id=\"update\" resultType=\"Integer\" parameterType=\"" + tableBean + "\">\n";
        methods += "call spu_" + table.getName() + "(" + paramsMethods + ")";
        methods += "</select>\n";

        //*DELETE METHOD
        methods += "<select id=\"delete\" resultType=\"Integer\" parameterType=\"" + tableBean + "\">\n";
        methods += "call spd_" + table.getName() + "(" + paramsPrimaryKey + ")";
        methods += "</select>\n";

        //*FIND BY ID
        methods += "<select id=\"getById\"  parameterType=\""+tableBean+"\" resultMap=\"" + tableName + "Map" + "\">\n";
        methods += "select * from " + table.getName() + " where ";
        for (int ii = 0; ii < pks.size(); ii++) {
            if (ii == 0) {
                methods += pks.get(ii) + " = " + "#{" + pks.get(ii) + "} ";
            } else {
                methods += " and " + pks.get(ii) + " = " + "#{" + pks.get(ii) + "} ";
            }

        }
        methods += "</select>\n";
       // String content = "";

        //Print
        System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        content += ("<!DOCTYPE mapper    PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        content += ("<mapper namespace=\""+Global.PACKAGE_NAME+".mapper."  + Conversor.firstCharacterToUpper(tableName) + "Mapper\">\n");
        content += ("   <resultMap id=\"" +tableName + "Map\" type=\"" + tableBean + "\">\n");
        content += (parameters);
        content += (associatonColumns);
        content += ("   </resultMap>\n");
        content += (methods);

        content += ("</mapper>\n");
        generateProject(Global.MAPPER_XML_LOCATION +  "\\", tableName + "-mapper.xml");
        //System.out.println(content);
    }
    @Override
    public void resetValues() {
        super.resetValues();
         associatonColumns = "";
         parameters = "";
         methods = "";
         paramsMethods = "";
         paramsPrimaryKey = "";
    }
}
