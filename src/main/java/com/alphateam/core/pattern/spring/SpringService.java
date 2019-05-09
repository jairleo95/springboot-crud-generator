/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.pattern.spring;

import com.alphateam.core.template.Template;
import com.alphateam.query.Table;
import com.alphateam.utiles.Conversor;
import com.alphateam.utiles.FileBuilder;

/**
 *
 * @author Jairleo95
 */
public class SpringService extends Template {

    public void build() {
        init();
        for (int g = 0; g < table.size(); g++) {
            /*table name - columns*/
            Table tnc = table.get(g);
            // String numColumnsTNC = getWithColumnsNumber.get(g).get("NumColumns");
            String tableName = Conversor.toJavaFormat(tnc.getName(), "_");
            /*one or more IDS*/
            String content = "";

            String tableEntity = Conversor.firstCharacterToUpper(tableName);
            String beanName = tableEntity + "Bean ";
            System.out.println("//TABLA :" + tnc.getName());
            /*Print */
            content += ("package org.proyecto.services." + tableName + ";");
            content += ("import ToJava.util.List;");
            content += ("import org.springframework.beans.factory.annotation.Autowired;");
            content += ("import org.springframework.stereotype.Component;");
            content += ("import org.proyecto.bean." + tableName + "" + beanName + ";"
                    + "import org.proyecto.mapper." + tableName + "." + tableEntity + "Mapper;");
            content += ("@Component");
            content += (" public class " + tableEntity + "SpringService" + " {");
            content += ("@Autowired");
            content += (" private " + tableEntity + "Mapper " + tableName + "Mapper;");
            /*Methods*/
            content += ("public List<" + beanName + "> list(){");
            content += ("return " + tableName + "Mapper.getAll();");
            content += ("}");
            content += ("public " + beanName + " findById(Integer id){");
            content += ("return " + tableName + "Mapper.findById(id);");
            content += ("}");
            content += ("public Integer delete(" + beanName + " " + tableName + "){");
            content += ("return " + tableName + "Mapper.delete(" + tableName + ");");
            content += ("}");
            content += ("public Integer save(" + beanName + " " + tableName + "){");
            content += ("return " + tableName + "Mapper.save(" + tableName + ");");
            content += ("}");
            content += ("public Integer edit(" + beanName + " " + tableName + "){");
            content += ("return " + tableName + "Mapper.edit(" + tableName + ");");
            content += ("}");
            content += ("}");
            FileBuilder.writeFolderAndFile("org\\proyecto\\services\\" + tableName + "\\", tableEntity + "SpringService.ToJava", content);
            System.out.println(content);
        }
    }
}
