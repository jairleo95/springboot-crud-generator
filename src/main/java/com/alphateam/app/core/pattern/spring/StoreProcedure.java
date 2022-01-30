/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.app.core.pattern.spring;

import com.alphateam.util.ToSql;
import com.alphateam.app.core.builder.Builder;

import java.util.List;

import com.alphateam.properties.Global;
import com.alphateam.app.bean.Column;
import com.alphateam.app.bean.Table;
import com.alphateam.util.Conversor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Jairleo95
 */


public class StoreProcedure extends Builder {

    String params = "";
    String methodParams="";
    String colsParams = "";
    String methods = "";
    String pkParams = "";
    String pkParamsCreate = "";
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

        tableColumns += c.getRawName() + ",";
        updateComparission += c.getRawName() + "=" + methodParams + ",";
        colsParams += methodParams + ",";
    }

    @Override
    public void primaryKeys(Table table, Column pk) {
        super.primaryKeys(table, pk);

        pkParams += "sp" + Conversor.firstCharacterToUpper(pk.getName().toLowerCase());
        pkParams = ToSql.headerParamsProcedure(pkParams, pk, database);

        pkParamsCreate += ToSql.headerParamsProcedure("sp" + Conversor.firstCharacterToUpper(pk.getName().toLowerCase())+ "_OUT OUT", pk, database);
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
    public void buildMethods(Table tnc) {
        super.buildMethods(tnc);
/*
        //Remove commas from params
        params = clearLastComma(params);
        pkParams = clearLastComma(pkParams);
        pkParamsCreate = clearLastComma(pkParamsCreate);
        colsParams = clearLastComma(colsParams);
        tableColumns = clearLastComma(tableColumns);
        updateComparission = clearLastComma(updateComparission);

        //Insert record procedure
        methods += ToSql.headerProcedure("spi_" + tnc.getName().toLowerCase(),pkParamsCreate +", "+ params, database);
        methods += "INSERT INTO " + tnc.getRawName() + "(" + tableColumns + ") values (" + colsParams + ")";

        //ruturn id
        methods += ToSql.addReturnID(pks, true, database);
        methods += "COMMIT;";
        methods += ToSql.footerProcedure("spi_" + tnc.getName(), database);
        //END Insert procedure

            //Update procedure
            methods += ToSql.headerProcedure("spu_" + tnc.getName().toLowerCase(), pkParams +", "+params, database);
            //procedure body
            methods += "UPDATE " + tnc.getRawName() + " set " + updateComparission + " where ";
            log.debug("pks.size():"+pks.size());

            for (int t = 0; t < pks.size(); t++) {
                if (t == 0) {
                    methods += pks.get(t).getRawName() + " = " + "sp" + Conversor.firstCharacterToUpper(pks.get(t).getName());
                } else {
                    methods += " and " + pks.get(t).getRawName() + " = " + "sp" + Conversor.firstCharacterToUpper(pks.get(t).getName());
                }
            }
            methods += ";";
            methods += "COMMIT;";
            methods += ToSql.footerProcedure("spi_" + tnc.getName(), database);
            //END Update procedure

            //DELETE procedure
            methods += ToSql.headerProcedure("spd_" + tnc.getName().toLowerCase(), pkParams, database);
                //body
            methods += "DELETE FROM " + tnc.getRawName() + " where ";
            for (int t = 0; t < pks.size(); t++) {
                if (t == 0) {
                    methods += pks.get(t).getRawName() + " = " + "sp" + Conversor.firstCharacterToUpper(pks.get(t).getName());
                } else {
                    methods += " and " + pks.get(t).getRawName() + " = " + "sp" + Conversor.firstCharacterToUpper(pks.get(t).getName());
                }
            }
            methods += ";";
            methods += ToSql.footerProcedure("spi_" + tnc.getName(), database);
               //END DELETE procedure

            //Print all
        content = methods;
        generateProject(Global.SQL_SCRIPT_LOCATION, tnc.getName() + ".sql");
        //log.debug(methods);
*/
    }

    @Override
    public void resetValues() {
        super.resetValues();
         params = "";
         methodParams="";
         colsParams = "";
         methods = "";
         pkParams = "";
        pkParamsCreate = "";
         column ="";
         tableColumns = "";
         updateComparission = "";
    }
}
