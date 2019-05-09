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
public class Mapper extends Template {

    public void build() {
        init();
        for (int g = 0; g < table.size(); g++) {
            Table tnc = table.get(g);
           // String tableNameTNC = table.get(g).getName();
            // String numColumnsTNC = getWithColumnsNumber.get(g).get("NumColumns");
            String tableName = Conversor.toJavaFormat(tnc.getName(), "_");

            /*one or more IDS*/
            String content = "";
            // String tableName = Conversor.toJavaFormat(List11[0].substring(6), "_");

            String tableEntity = Conversor.firstCharacterToUpper(tableName);
            String beanName = tableEntity + "Bean ";
            System.out.println("//TABLA :" + tnc.getName());
            /*Print */
            content += ("package org.proyecto.mapper." + tableName + ";");
            content += ("import ToJava.util.List;");
            content += ("import org.springframework.stereotype.Component;");
            content += ("import org.proyecto.bean." + tableName + "" + beanName + ";");
            content += ("@Component");
            content += (" public interface " + tableEntity + "Mapper" + " {");
            /*Methods*/
            content += ("public List<" + beanName + "> getAll();");
            content += ("public " + beanName + " findById(Integer id);");
            content += ("public Integer delete(" + beanName + " " + tableName + ");");
            content += ("public Integer save(" + beanName + " " + tableName + ");");
            content += ("public Integer edit(" + beanName + " " + tableName + ");");
            content += ("}");
            FileBuilder.writeFolderAndFile("org\\proyecto\\mapper\\" + tableName + "\\", tableEntity + "Mapper.ToJava", content);
            System.out.println(content);
        }
    }
}
