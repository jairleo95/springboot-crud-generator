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
        makeColumns += ("       <id property=\"" + columna + "\" column=\"" + pk.getName() + "\" />\n");
        paramsPrimaryKey += "#{" +columna + "},";
    }

    @Override
    public void foreignKeys(Table tcp, Column fk) {
        super.foreignKeys(tcp, fk);

        String columna = Conversor.toJavaFormat(fk.getName(), "_");
        String tableBean = Conversor.firstCharacterToUpper(Conversor.toJavaFormat(fk.getForeignTable(), "_"))+"Bean";
            String ForeignColumnBean = Conversor.toJavaFormat(fk.getForeignColumn(), "_");

           makeAssociatonColumns += "<association property=\"" + columna + "\" javaType=\"" + tableBean + "\">\n";

            List<Column> foreignColumns =  dao.getColumsProperties(fk.getForeignTable());

            for (int hh = 0; hh < foreignColumns.size(); hh++) {

                String columnNameFk = foreignColumns.get(hh).getName();
                if (columnNameFk.equals(fk.getForeignColumn())) {
                    makeAssociatonColumns += "          <id column=\"" + columnNameFk + "\" property=\"" + Conversor.toJavaFormat(columnNameFk, "_") + "\"></id>\n";
                } else {
                    //makeAssociatonColumns += "          <result column=\"" + columnNameFk + "\" property=\"" + Conversor.toJavaFormat(columnNameFk, "_") + "\"></result>\n";
                }
            }

            makeAssociatonColumns += "</association>\n";
            makeParamsMethods += "#{" + columna + "" + ForeignColumnBean + "},";

    }

    @Override
    public void buildParameters(Table table, Column column) {
        super.buildParameters(table, column);
        String columna = Conversor.toJavaFormat(column.getName(), "_");

        makeColumns += ("       <result property=\"" + columna + "\" column=\"" + column.getName() + "\" />\n");
        makeParamsMethods += "#{" + columna + "},";
    }

    @Override
    public void buildMethods(Table table, List<String> pks) {
        super.buildMethods(table, pks);

        String tableName = Conversor.toJavaFormat(table.getName(), "_");
        String tableBean = Conversor.firstCharacterToUpper(tableName)+"Bean";


        if (!makeParamsMethods.equals("")) {
            makeParamsMethods = makeParamsMethods.substring(0, (makeParamsMethods.length() - 1));
        }
        if (!paramsPrimaryKey.equals("")) {
            paramsPrimaryKey = paramsPrimaryKey.substring(0, (paramsPrimaryKey.length() - 1));
        }

        //Save Method
        makeMethods += "<select id=\"create\" parameterType=\""+tableBean+"\" resultMap=\"" + tableName + "Map\" statementType=\"CALLABLE\">\n";

        makeMethods += "call spi_" + table.getName() + "(" + makeParamsMethods + ")";
        makeMethods += "</select>\n";
        //getAll
        makeMethods += "<select id=\"read\" resultMap=\"" + tableName + "Map\" >\n";
        makeMethods += "select * from " + table.getName() + "";
        makeMethods += "</select>\n";

        //*EDIT METHOD
        makeMethods += "<select id=\"update\" resultType=\"Integer\" parameterType=\"" + tableBean + "\">\n";
        makeMethods += "call spu_" + table.getName() + "(" + makeParamsMethods + ")";
        makeMethods += "</select>\n";

        //*DELETE METHOD
        makeMethods += "<select id=\"delete\" resultType=\"Integer\" parameterType=\"" + tableBean + "\">\n";
        makeMethods += "call spd_" + table.getName() + "(" + paramsPrimaryKey + ")";
        makeMethods += "</select>\n";

        //*FIND BY ID
        makeMethods += "<select id=\"getById\"  parameterType=\""+tableBean+"\" resultMap=\"" + tableName + "Map" + "\">\n";
        makeMethods += "select * from " + table.getName() + " where ";
        for (int ii = 0; ii < pks.size(); ii++) {
            if (ii == 0) {
                makeMethods += pks.get(ii) + " = " + "#{" + Conversor.toJavaFormat(pks.get(ii), "_") + "} ";
            } else {
                makeMethods += " and " + pks.get(ii) + " = " + "#{" + Conversor.toJavaFormat(pks.get(ii), "_") + "} ";
            }

        }
        makeMethods += "</select>\n";
       // String content = "";

        //Print
        System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        content += ("<!DOCTYPE mapper    PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        content += ("<mapper namespace=\""+Global.PACKAGE_NAME+".mapper."  + Conversor.firstCharacterToUpper(tableName) + "Mapper\">\n");
        content += ("   <resultMap id=\"" +tableName + "Map\" type=\"" + tableBean + "\">\n");
        content += (makeColumns);
        content += (makeAssociatonColumns);
        content += ("   </resultMap>\n");
        content += (makeMethods);

        content += ("</mapper>\n");
        generateProject(Global.MAPPER_XML_LOCATION +  "\\", tableName + "-mapper.xml");
        //System.out.println(content);
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
