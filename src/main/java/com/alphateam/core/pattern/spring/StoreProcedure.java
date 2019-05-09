/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.pattern.spring;

import com.alphateam.convert.ToSql;
import com.alphateam.core.template.Template;

import java.util.List;
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
    public void column(Table tcp) {
        super.column(tcp);
         /*Variables*/
         column = Conversor.toJavaFormat(tcp.getColumn().getName(), "_");
        //customized
        methodParams = "sp" + Conversor.firstCharacterToUpper(column);
        updateComparission += tcp.getColumn().getName() + "=" + methodParams + ",";
        methods = "";
    }

    @Override
    public void buildParameters(Table tcp) {
        super.buildParameters(tcp);
            makeColumns += methodParams + ",";
            params += ToSql.headerParamsProcedure(methodParams, tcp.getColumn().getDataType(), tcp.getColumn().getAttributeNumber(), database);
            tableColumns += tcp.getColumn().getName() + ",";
            updateComparission += tcp.getColumn().getName() + "=" + methodParams + ", ";
    }

    @Override
    public void primaryKeys(Table tcp, Table pk) {
        super.primaryKeys(tcp, pk);
            pkParams += pk.getColumn().getName() + " " + ToSql.getDataType(tcp.getColumn().getDataType(), database) + ",";

    }

    @Override
    public void foreignKeys(Table tcp, Table fk) {
        super.foreignKeys(tcp, fk);
            params += ToSql.headerParamsProcedure(methodParams, tcp.getColumn().getDataType(), tcp.getColumn().getAttributeNumber(), database);
            makeColumns += methodParams+ ",";
            tableColumns += tcp.getColumn().getName() + ",";
            updateComparission += tcp.getColumn().getName() + "=" + methodParams + ", ";
    }

    @Override
    public void build() {
        super.build();

            /*Remove commas from params*/
            clearLastComma(params);
            clearLastComma(pkParams);
            clearLastComma(makeColumns);
            clearLastComma(tableColumns);
            clearLastComma(updateComparission);
    }

    @Override
    public void buildMethods(Table tnc, List<String> pks) {
        super.buildMethods(tnc, pks);
              /*Insert record procedure */
            methods += ToSql.headerProcedure("spi_" + tnc.getName(), params, database);
            methods += "INSERT INTO " + tnc.getName() + "(" + tableColumns + ") values (" + makeColumns + ")";

                /*ruturn id*/
            if (pks.size() == 1 & returnId) {
                methods += " returning " + pks.get(0) + " into result ;";
            } else {
                methods += ";";
            }

            methods += ToSql.footerProcedure("spi_" + tnc.getName(), database);
                /*END Insert procedure*/

                /*Update procedure*/
            methods += ToSql.headerProcedure("spu_" + tnc.getName(), params, database);
                /*procedure body*/
            methods += "UPDATE " + tnc.getName() + " set " + updateComparission + " where ";

            for (int t = 0; t < pks.size(); t++) {
                if (t == 0) {
                    methods += pks.get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(pks.get(t), "_"));
                } else {
                    methods += " and " + pks.get(t) + " = " + "sp" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(pks.get(t), "_"));
                }
            }
            methods += ";";
            methods += ToSql.footerProcedure("spi_" + tnc.getName(), database);
                /*END Update procedure*/

                /*DELETE procedure*/
            methods += ToSql.headerProcedure("spd_" + tnc.getName(), pkParams, database);
                /*body*/
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
               /*END DELETE procedure*/

                /*Print all*/
            System.out.println(methods);

    }
}
