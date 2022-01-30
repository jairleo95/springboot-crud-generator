/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.app.core.pattern.spring;

import java.util.List;

import com.alphateam.app.core.builder.Builder;
import com.alphateam.properties.Global;
import com.alphateam.app.bean.Column;
import com.alphateam.app.bean.Table;
import com.alphateam.util.Conversor;
import com.alphateam.util.FileBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Jairleo95
 */

public class HtmlForm extends Builder {

    private String params = "";
    private String paramsMethods = "";
    private String paramsPrimaryKey = "";
    private String tableColumns = "";

    private final Logger log = LogManager.getLogger(getClass().getName());

    String parameterFile = "D:\\Software Development\\crud-generator-project\\template\\parameter.html";
    String formFile = "D:\\Software Development\\crud-generator-project\\template\\form.html";
    String datatableFile = "D:\\Software Development\\crud-generator-project\\template\\datatable.html";

    @Override
    public void buildParameters(Table table, Column column) {
        super.buildParameters(table,column);

        String colName = column.getName();
        params += FileBuilder.readAllBytes(parameterFile);

        params = params
                .replace("{colName}", colName)
                .replace("{input}", buildInput(column));

    }

    String buildInput(Column column){
        String html = "";
        String colName = column.getName();
        String length = "maxlength='"+column.getLength()+"' size='"+column.getLength()+"'";

        if (column.getDataType().equalsIgnoreCase("date")||column.getDataType().toLowerCase().contains("timestamp")){
            html += ("<input name='" + colName + "'  type='date' "+length+" class='" + colName + "'/>");
        } else if (column.getDataType().equalsIgnoreCase("number")){
            html += ("<input name='" + colName + "'  type='number' "+length+" class='" + colName + "'/>");
        }else{
            html += ("<input name='" + colName + "'  type='text' "+length+" class='" + colName + "'/>");
        }
        return html;
    }

    @Override
    public void column(Column column) {
        super.column(column);
        //table headers
        tableColumns +="<th>"+column.getName()+"</th>\n";
    }

    @Override
    public void buildMethods(Table table) {
        super.buildMethods(table);

        String tableName = table.getName();
        String tableEntity = Conversor.firstCharacterToUpper(tableName);

        log.info("Table :" + table.getName());

        if (!paramsMethods.equals("")) {
            paramsMethods = paramsMethods.substring(0, (paramsMethods.length() - 1));
        }
        if (!paramsPrimaryKey.equals("")) {
            paramsPrimaryKey = paramsPrimaryKey.substring(0, (paramsPrimaryKey.length() - 1));
        }

        content += FileBuilder.readAllBytes(formFile);
        String datatable = FileBuilder.readAllBytes(datatableFile);

        content = content.replace("{datatable}", datatable
                .replace("{headers}", tableColumns))
                .replace("{formParameters}", params)
                .replace("{tableEntity}", tableEntity);

        generateProject(Global.VIEW_LOCATION + tableEntity + "\\", "form"+tableEntity + ".html");
    }

    @Override
    public void foreignKeys(Table tcp, Column fk) {
        super.foreignKeys(tcp, fk);

        String colName = fk.getName();

        params += FileBuilder.readAllBytes(parameterFile);
        String select = "<select name='" + colName + "' class='select" + colName + "'>" + "<option></option>" +"</select>";

        params = params.replace("{colName}", colName)
                .replace("{input}", select);

    }

    @Override
    public void resetValues() {
        super.resetValues();
         params = "";
         paramsMethods = "";
         paramsPrimaryKey = "";
         tableColumns = "";
    }

}