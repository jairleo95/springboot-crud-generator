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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 *
 * @author Jairleo95
 */


public class Mapper extends Template {


    private final Logger log = LogManager.getLogger(getClass().getName());
    @Override
    public void primaryKeys(Table table, Column pk) {
        String tableName = table.getName();
        super.primaryKeys(table, pk);
        String dataType = ToJava.getDataType(pk.getDataType());
    }

    @Override
    public void buildMethods(Table table, List<String> pks) {
        super.buildMethods(table, pks);

        String tableName = table.getName();
        String tableEntity = Conversor.firstCharacterToUpper(tableName);

        String beanName = tableEntity + "Bean";
        //pkParameters = clearLastComma(pkParameters);

        log.info("//TABLA :" + table.getName());
        //Print
        content += ("package "+Global.PACKAGE_NAME +".mapper;");
        content += ("import java.util.List;");
        content += ("import org.apache.ibatis.annotations.Param;");

        content += ("import org.apache.ibatis.annotations.Mapper;");
        content += ("import "+Global.PACKAGE_NAME +".bean." + beanName + ";");
        content += ("\n");
        content += ("@Mapper \n");
        content += ("public interface " + tableEntity + "Mapper" + " { \n");
        //Methods

        content += (" Boolean create(" + beanName + " " + tableName + "); \n");
        content += (" List<" + beanName + "> read(); \n");
        content += (" Boolean update(" + beanName + " " + tableName + "); \n");
        content += (" Boolean delete(" + pkMapVarInput+ "); \n");

        content += (" " + beanName + " getById("+pkMapVarInput+"); \n");
        content += ("} \n");

        generateProject(Global.MAPPER_LOCATION + "\\", tableEntity + "Mapper.java");
        //System.out.println(content);
    }

}
