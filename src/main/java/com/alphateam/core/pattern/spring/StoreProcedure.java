/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.pattern.spring;

import com.alphateam.convert.ToSql;
import com.alphateam.core.template.Template;

import java.util.List;

import com.alphateam.query.Column;
import com.alphateam.query.Table;
import com.alphateam.utiles.Conversor;

/**
 *
 * @author Jairleo95
 */


public class StoreProcedure extends Template {

    String params = "";
    String methodParams="";
    String makeColumns = "";
    String methods = "";
    String pkParams = "";
    String column ="";
    String tableColumns = "";
    String updateComparission = "";

    @Override
    public void column(Column tcp) {
        super.column(tcp);
         //Variables
        column = Conversor.toJavaFormat(tcp.getName(), "_");
        //customized

        //updateComparission += tcp.getName() + "=" + methodParams + ",";
        methods = "";
    }

    @Override
    public void buildParameters(Table table, Column c) {
        super.buildParameters(table, c);
            makeColumns += methodParams + ",";
            methodParams = "sp" + Conversor.firstCharacterToUpper(column);
            params += ToSql.headerParamsProcedure(methodParams, c.getDataType(), c.getAttributeNumber(), database);
            tableColumns += c.getName() + ",";
            updateComparission += c.getName() + "=" + methodParams + ", ";
    }

    @Override
    public void primaryKeys(Table table, Column pk) {
        super.primaryKeys(table, pk);
            pkParams += pk.getName() + " " + ToSql.getDataType(pk.getDataType(), database) + ",";
    }

    @Override
    public void foreignKeys(Table tcp, Column fk) {
        super.foreignKeys(tcp, fk);
            params += ToSql.headerParamsProcedure(methodParams, fk.getDataType(), fk.getAttributeNumber(), database);
            makeColumns += methodParams+ ",";
            tableColumns += fk.getName() + ",";
            updateComparission += fk.getName() + "=" + methodParams + ", ";
    }


    @Override
    public void buildMethods(Table tnc, List<String> pks) {
        super.buildMethods(tnc, pks);

        //Remove commas from params
        params = clearLastComma(params);
        pkParams = clearLastComma(pkParams);
        makeColumns = clearLastComma(makeColumns);
        tableColumns = clearLastComma(tableColumns);
        updateComparission = clearLastComma(updateComparission);

        //Insert record procedure
        methods += ToSql.headerProcedure("spi_" + tnc.getName(), params, database);
        methods += "INSERT INTO " + tnc.getName() + "(" + tableColumns + ") values (" + makeColumns + ")";

        //ruturn id
        System.out.println("buildMethods.pks.size():"+pks.size());
            if (pks.size() == 1 & returnId) {
                methods += " returning " + pks.get(0) + " into result ;";
            } else {
                methods += ";";
            }

            methods += ToSql.footerProcedure("spi_" + tnc.getName(), database);
                //END Insert procedure

            //Update procedure
            methods += ToSql.headerProcedure("spu_" + tnc.getName(), params, database);
                //procedure body
            methods += "UPDATE " + tnc.getName() + " set " + updateComparission + " where ";
            System.out.println("pks.size():"+pks.size());

            for (int t = 0; t < pks.size(); t++) {
                if (t == 0) {
                    methods += pks.get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(pks.get(t), "_"));
                } else {
                    methods += " and " + pks.get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(pks.get(t), "_"));
                }
            }
            methods += ";";
            methods += ToSql.footerProcedure("spi_" + tnc.getName(), database);
                //END Update procedure

            //DELETE procedure
            methods += ToSql.headerProcedure("spd_" + tnc.getName(), pkParams, database);
                //body
            methods += "UPDATE " + tnc.getName() + " set recordStatus=0 where ";
            for (int t = 0; t < pks.size(); t++) {
                if (t == 0) {
                    methods += pks.get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(pks.get(t), "_"));
                } else {
                    methods += " and " + pks.get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(pks.get(t), "_"));
                }
            }
            methods += ";";
            methods += ToSql.footerProcedure("spi_" + tnc.getName(), database);
               //END DELETE procedure

            //Print all
        content = methods;
        generateProject("sql\\", tnc.getName() + ".sql");
        System.out.println(methods);

    }

    @Override
    public void resetValues() {
        super.resetValues();
         params = "";
         methodParams="";
         makeColumns = "";
         methods = "";
         pkParams = "";
         column ="";
         tableColumns = "";
         updateComparission = "";
    }
}
