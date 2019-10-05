/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.pattern.spring;

import com.alphateam.connection.Factory;
import com.alphateam.convert.ToJava;
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


public class MyBatisConf extends Template {

    String aliases ="";
    String mappers ="";
    String content ="";
    @Override
    public Table table(Table table) {
        super.table(table);

            String tableName = Conversor.toJavaFormat(table.getName(), "_");
            String tableEntity = Conversor.firstCharacterToUpper(tableName);
            String beanName = tableEntity + "Bean";

            aliases += ("   <typeAlias alias=\""+beanName+"\" type=\"com.hpe.bean."+beanName+"\"></typeAlias>\n");
            mappers += ("   <mapper resource=\"com/hpe/mapper/"+tableName+"-mapper.xml\" /> \n");
            return table;
         }

    @Override
    public void build() {
        super.build();
        content += ("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE configuration  PUBLIC \"-//mybatis.org//DTD Config 3.1//EN\"   \"http://mybatis.org/dtd/mybatis-3-config.dtd\">\n" +
                "\n" +
                "<configuration>\n" +
                "   <typeAliases> \n");
        content +=aliases;
        content += ("  </typeAliases>\n" +
                "    \n" +
                "    <mappers>\n");
        content +=mappers;

        content += ("    </mappers>\n" +
                "</configuration> \n");

        FileBuilder.writeFolderAndFile(Global.RESOURCES_LOCATION  + "\\", "mybatis-config.xml", content);
    }

}
