/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.pattern.spring;

import com.alphateam.core.template.Template;
import com.alphateam.properties.Global;
import com.alphateam.query.Table;
import com.alphateam.utiles.Conversor;
import com.alphateam.utiles.FileBuilder;

/**
 *
 * @author Jairleo95
 */


public class Mapper extends Template {
    @Override
    public void table(Table table) {
        super.table(table);
        //Table table = tables.get(g);
        String tableName = Conversor.toJavaFormat(table.getName(), "_");

        //one or more IDS
        //String content = "";

        String tableEntity = Conversor.firstCharacterToUpper(tableName);
        String beanName = tableEntity + "Bean ";
        System.out.println("//TABLA :" + table.getName());
        //Print
        content += ("package org.proyecto.mapper." + tableName + ";");
        content += ("import ToJava.util.List;");
        content += ("import org.springframework.stereotype.Component;");
        content += ("import org.proyecto.bean." + tableName + "" + beanName + ";");
        content += ("@Component");
        content += (" public interface " + tableEntity + "Mapper" + " {");
        //Methods
        content += ("public List<" + beanName + "> getAll();");
        content += ("public " + beanName + " findById(Integer id);");
        content += ("public Integer delete(" + beanName + " " + tableName + ");");
        content += ("public Integer save(" + beanName + " " + tableName + ");");
        content += ("public Integer edit(" + beanName + " " + tableName + ");");
        content += ("}");
        //FileBuilder.writeFolderAndFile("org\\proyecto\\mapper\\" + tableName + "\\", tableEntity + "Mapper.ToJava", content);
        generateProject(Global.MAPPER_LOCATION + tableName + "\\", tableEntity + "Mapper.java");
        System.out.println(content);
    }

}
