/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.pattern.spring;

import com.alphateam.connection.Factory;

import com.alphateam.convert.ToJava;
import com.alphateam.core.template.Template;
import com.alphateam.query.Table;
import com.alphateam.utiles.Conversor;

/**
 *
 * @author Jairleo95
 */
public class JavaBeans extends Template {

    public void build() {
        init();
        for (int g = 0; g < listTableXNumColumns.size(); g++) {
            Table tnc = listTableXNumColumns.get(g);
            //   String numColumnsTNC = getWithColumnsNumber.get(g).get("NumColumns");
            String tableName = Conversor.toJavaFormat(tnc.getTableName(), "_");
            String beanName = Conversor.firstCharacterToUpper(tableName + "Bean");
            System.out.println("//TABLA :" + tnc.getTableName());

            String makeInstanceBean = "";
            String makeSettersAndGetters = "";
            String makeImports = "";
            String makeColumns = "";

            for (int h = 0; h < listTableXColumsP.size(); h++) {
               /*table-column-property (TCP)*/
                Table tcp = listTableXColumsP.get(h);
                if (tnc.getTableName().equals(tcp.getTableName())) {
                    /*Variables*/
                    String columna = Conversor.toJavaFormat(tcp.getColumnName(), "_");
                    Boolean isForean = false;

                    /*Llaves Foraneas*/
                    for (int d = 0; d < listForeignKey.size(); d++) {
                        Table fk = listForeignKey.get(d);

                        System.err.println(tnc.getTableName() + ":" + fk.getTableName());
                        if (tnc.getTableName().equalsIgnoreCase(fk.getTableName()) & tcp.getColumnName().equalsIgnoreCase(fk.getColumnName())) {
                            System.err.println("here!");
                            // String TableBean = Conversor.firstCharacterToUpper(Conversor.toJavaFormat(ForeignTable.substring(6), "_")) + "Bean";
                            String TableBean = Conversor.firstCharacterToUpper(Conversor.toJavaFormat(fk.getForeignTable(), "_")) + "Bean";
                            String ColumnaBean = Conversor.toJavaFormat(columna, "_");
                            makeColumns += ("private " + TableBean + " " + ColumnaBean + ";");
                            // s++;
                            isForean = true;
                            makeInstanceBean += ColumnaBean + " = new " + TableBean + "();";
                            makeSettersAndGetters += ("public void set" + Conversor.firstCharacterToUpper(ColumnaBean) + "(" + TableBean + " " + ColumnaBean + "){"
                                    + "this." + ColumnaBean + "=" + ColumnaBean + ";}");
                            makeSettersAndGetters += "public " + TableBean + " get" + Conversor.firstCharacterToUpper(ColumnaBean) + "(){"
                                    + "return " + ColumnaBean + ";}";
                            makeImports += " import org.proyecto.bean." + Conversor.toJavaFormat(fk.getForeignTable(), "_") + "" + TableBean + ";";
                        }
                    }
                    /*columnas*/
                    String dataType = "";
                    if (!isForean) {
                        dataType = ToJava.getDataType(tcp.getDataType(), Factory.getDefaultDatabase());
                        makeColumns += ("private " + dataType + " " + columna + ";");
                        makeSettersAndGetters += ("public void set" + Conversor.firstCharacterToUpper(columna) + "(" + dataType + " " + columna + "){"
                                + "this." + columna + "=" + columna + ";}");
                        makeSettersAndGetters += "public " + dataType + "  get" + Conversor.firstCharacterToUpper(columna) + "(){"
                                + "return " + columna + ";}";
                        makeImports += ToJava.getImportsByDataType(dataType);
                        makeInstanceBean += "this." + columna + "=" + ToJava.getInstanceByDataType(dataType) + ";";
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
            //FileBuilder.writeFolderAndFile("org\\proyecto\\bean\\" + tableName + "\\", beanName + ".ToJava", content);
            System.out.println(content);
        }
    }

}
