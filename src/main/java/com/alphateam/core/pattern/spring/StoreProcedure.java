/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.pattern.spring;

import com.alphateam.convert.ToSql;
import com.alphateam.core.template.Template;

import java.util.List;

import com.alphateam.properties.Global;
import com.alphateam.query.Column;
import com.alphateam.query.Table;
import com.alphateam.utiles.Conversor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Jairleo95
 */


public class StoreProcedure extends Template {

    String params = "";
    String methodParams="";
    String colsParams = "";
    String methods = "";
    String pkParams = "";
    String column ="";
    String tableColumns = "";
    String updateComparission = "";

    private final Logger log = LogManager.getLogger(getClass().getName());
    
    @Override
    public void buildParameters(Table table, Column c) {
        super.buildParameters(table, c);

        column = c.getName();

            methodParams = "sp" + Conversor.firstCharacterToUpper(column);
            params += ToSql.headerParamsProcedure(methodParams, c, database);

            tableColumns += c.getName() + ",";
            updateComparission += c.getName() + "=" + methodParams + ", ";
            colsParams += methodParams + ",";
    }

    @Override
    public void primaryKeys(Table table, Column pk) {
        super.primaryKeys(table, pk);

        pkParams += "sp" +Conversor.firstCharacterToUpper(pk.getName().toLowerCase());
        pkParams = ToSql.headerParamsProcedure(pkParams, pk, database);
    }

    @Override
    public void foreignKeys(Table tcp, Column fk) {
        super.foreignKeys(tcp, fk);
            params += ToSql.headerParamsProcedure(methodParams, fk, database);
            colsParams += methodParams+ ",";
            tableColumns += fk.getName() + ",";
            updateComparission += fk.getName() + "=" + methodParams + ",";
    }


    @Override
    public void buildMethods(Table tnc, List<Column> pks) {
        super.buildMethods(tnc, pks);

        //Remove commas from params
        params = clearLastComma(params);
        pkParams = clearLastComma(pkParams);
        colsParams = clearLastComma(colsParams);
        tableColumns = clearLastComma(tableColumns);
        updateComparission = clearLastComma(updateComparission);

        //Insert record procedure
        methods += ToSql.headerProcedure("spi_" + tnc.getName().toLowerCase(), params, database);
        methods += "INSERT INTO " + tnc.getName() + "(" + tableColumns + ") values (" + colsParams + ")";

        //ruturn id
        log.debug("buildMethods.pks.size():"+pks.size());
            if (pks.size() == 1 & returnId) {
                methods += " returning " + pks.get(0) + " into result ;";
            } else {
                methods += ";";
            }

            methods += ToSql.footerProcedure("spi_" + tnc.getName(), database);
                //END Insert procedure

            //Update procedure
            methods += ToSql.headerProcedure("spu_" + tnc.getName().toLowerCase(), params+", "+pkParams, database);
            //procedure body
            methods += "UPDATE " + tnc.getName() + " set " + updateComparission + " where ";
            log.debug("pks.size():"+pks.size());

            for (int t = 0; t < pks.size(); t++) {
                if (t == 0) {
                    methods += pks.get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(pks.get(t).getName());
                } else {
                    methods += " and " + pks.get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(pks.get(t).getName());
                }
            }
            methods += ";";
            methods += ToSql.footerProcedure("spi_" + tnc.getName(), database);
                //END Update procedure

            //DELETE procedure
            methods += ToSql.headerProcedure("spd_" + tnc.getName().toLowerCase(), pkParams, database);
                //body
            methods += "DELETE FROM " + tnc.getName() + " where ";
            for (int t = 0; t < pks.size(); t++) {
                if (t == 0) {
                    methods += pks.get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(pks.get(t).getName());
                } else {
                    methods += " and " + pks.get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(pks.get(t).getName());
                }
            }
            methods += ";";
            methods += ToSql.footerProcedure("spi_" + tnc.getName(), database);
               //END DELETE procedure

            //Print all
        content = methods;
        generateProject(Global.SQL_SCRIPT_LOCATION, tnc.getName() + ".sql");
        //log.debug(methods);

    }

    @Override
    public void resetValues() {
        super.resetValues();
         params = "";
         methodParams="";
         colsParams = "";
         methods = "";
         pkParams = "";
         column ="";
         tableColumns = "";
         updateComparission = "";
    }
}
