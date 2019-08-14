/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.pattern.spring;

import com.alphateam.connection.Factory;

import com.alphateam.convert.ToJava;
import com.alphateam.core.template.Template;
import com.alphateam.query.Column;
import com.alphateam.query.Table;
import com.alphateam.utiles.Conversor;

import java.util.List;

/**
 *
 * @author Jairleo95
 */
public class JavaBeans extends Template {


    String makeColumns = "";
    String makeInstanceBean = "";
    String makeSettersAndGetters = "";
    String makeImports = "";
   //String content = "";

    @Override
    public void foreignKeys(Table tcp, Column column) {
        super.foreignKeys(tcp, column);

        String columna = Conversor.toJavaFormat(column.getName(), "_");
        System.out.println("Enter to foreignKeys method");
        String TableBean = Conversor.firstCharacterToUpper(Conversor.toJavaFormat(column.getForeignTable(), "_")) + "Bean";
        String ColumnaBean = Conversor.toJavaFormat(columna, "_");
        makeColumns += ("private " + TableBean + " " + ColumnaBean + ";");
        // s++;
        //isForean = true;
        makeInstanceBean += ColumnaBean + " = new " + TableBean + "();";
        makeSettersAndGetters += ("public void set" + Conversor.firstCharacterToUpper(ColumnaBean) + "(" + TableBean + " " + ColumnaBean + "){"
                + "this." + ColumnaBean + "=" + ColumnaBean + ";}");
        makeSettersAndGetters += "public " + TableBean + " get" + Conversor.firstCharacterToUpper(ColumnaBean) + "(){"
                + "return " + ColumnaBean + ";}";
        makeImports += " import org.proyecto.bean." + Conversor.toJavaFormat(column.getForeignTable(), "_") + "" + TableBean + ";";
    }


    @Override
    public void buildParameters(Table table, Column column) {
        super.buildParameters(table, column);
        String dataType = "";
        String columna = Conversor.toJavaFormat(column.getName(), "_");
        dataType = ToJava.getDataType(column.getDataType(), Factory.getDefaultDatabase());
        makeColumns += ("private " + dataType + " " + columna + ";");
        makeSettersAndGetters += ("public void set" + Conversor.firstCharacterToUpper(columna) + "(" + dataType + " " + columna + "){"
                + "this." + columna + "=" + columna + ";}");
        makeSettersAndGetters += "public " + dataType + "  get" + Conversor.firstCharacterToUpper(columna) + "(){"
                + "return " + columna + ";}";
        makeImports += ToJava.getImportsByDataType(dataType);
        makeInstanceBean += "this." + columna + "=" + ToJava.getInstanceByDataType(dataType) + ";";
    }

    @Override
    public void buildMethods(Table table, List<String> pks) {
        super.buildMethods(table, pks);
        String tableName = Conversor.toJavaFormat(table.getName(), "_");
        String beanName = Conversor.firstCharacterToUpper(tableName + "Bean");

        content += ("package org.proyecto.bean." + tableName + ";");
        content += (makeImports);
        content += ("public class " + beanName + " {");
        content += (makeColumns);
        //Contructor
        content += ("public " + beanName + "(){");
        content += (makeInstanceBean);
        content += ("}");
        content += (makeSettersAndGetters);
        content += ("}");
        generateProject("org\\proyecto\\bean\\" + tableName + "\\", beanName  + ".java");
        System.out.println(content);

    }

    @Override
    public void resetValues() {
        super.resetValues();
         makeColumns = "";
         makeInstanceBean = "";
         makeSettersAndGetters = "";
         makeImports = "";
         content = "";
    }
}
