/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.app.core.pattern.spring;

import com.alphateam.app.core.builder.Builder;
import com.alphateam.properties.Global;
import com.alphateam.app.bean.Table;
import com.alphateam.util.Conversor;
import com.alphateam.util.FileBuilder;

/**
 *
 * @author Jairleo95
 */


public class MyBatisConf extends Builder {

    String aliases ="";
    String mappers ="";
    String content ="";
    @Override
    public Table processTable(Table table) {
        super.processTable(table);

            String tableName = table.getName();
            String tableEntity = Conversor.firstCharacterToUpper(tableName);
            String beanName = tableEntity + "Bean";

            aliases += ("   <typeAlias alias=\""+beanName+"\" type=\"com.app.bean."+beanName+"\"></typeAlias>\n");
            mappers += ("   <mapper resource=\"com/app/mapper/"+tableName+"-mapper.xml\" /> \n");
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

        new FileBuilder().writeFolderAndFile(Global.RESOURCES_LOCATION  + "\\", "mybatis-config.xml", content);
    }

}
