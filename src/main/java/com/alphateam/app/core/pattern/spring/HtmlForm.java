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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Jairleo95
 */

public class HtmlForm extends Builder {

    String parameters = "";
    String associatonColumns = "";
    String methods = "";
    String paramsMethods = "";
    String paramsPrimaryKey = "";
    String tableColumns = "";

    private final Logger log = LogManager.getLogger(getClass().getName());

    @Override
    public void buildParameters(Table table, Column column) {
        super.buildParameters(table,column);

        String columna = column.getName();

        parameters += ("<div class=\"form-group\">");
        parameters += ("<label class=\"col-md-3 control-label\">" + columna + "</label>");
        parameters += ("<div class=\"col-md-4\">");


        String length = "maxlength='"+column.getLength()+"' size='"+column.getLength()+"'";

        if (column.getDataType().equalsIgnoreCase("date")||column.getDataType().toLowerCase().contains("timestamp")){
            parameters += ("<input name='" + columna + "'  type='date' "+length+" class='" + columna + "'/>");
        }else if (column.getDataType().equalsIgnoreCase("number")){
            parameters += ("<input name='" + columna + "'  type='number' "+length+" class='" + columna + "'/>");
        }else{
            parameters += ("<input name='" + columna + "'  type='text' "+length+" class='" + columna + "'/>");
        }
        parameters += ("</div>");
        parameters += ("</div>");

    }

    @Override
    public void column(Column column) {
        super.column(column);
        String columna = column.getName();
        //table headers
        tableColumns +="<th>"+columna+"</th>";
    }

    @Override
    public void buildMethods(Table table, List<Column> pks) {
        super.buildMethods(table, pks);

        String tableName = table.getName();
        String tableEntity = Conversor.firstCharacterToUpper(tableName);

        log.info("/*Table :" + table.getName() + " ");

        if (!paramsMethods.equals("")) {
            paramsMethods = paramsMethods.substring(0, (paramsMethods.length() - 1));
        }
        if (!paramsPrimaryKey.equals("")) {
            paramsPrimaryKey = paramsPrimaryKey.substring(0, (paramsPrimaryKey.length() - 1));
        }

        content += ("<div class=\"row\">");
        content += ("<div class=\"col-sm-12\">");
        content += ("<div class=\"well\">");

        content += ("<div class=\"row\">");
        content += ("<div class=\"col col-sm-12\"><h1>" + tableEntity + "</h1></div>");
        content += ("</div>");


        content += ("\n");
        content += ("<div class=\"row\">\n"
                + "<div class=\"col col-sm-12\">");
        content += ("<form class=\"form-horizontal form"+tableEntity+"\" >");
        content += (parameters);

        content += ("\n");
        content += ("</form>");

        content += ("</div>");
        content += ("</div>");
        content += ("</div>");

        //buttons
        content += ("<section class=\"col col-sm-3\">");
        content += ("<button type=\"button\" id=\"btn-registrar\"\n"
                + "						class=\"btn btn-default btn-circle btn-lg btnSave\"\n"
                + "						style=\"float: right; display: none\" rel=\"tooltip\"\n"
                + "						data-placement=\"top\" data-original-title=\"Guardar\">\n"
                + "						<i class=\"glyphicon glyphicon-floppy-disk\"></i>\n"
                + "					</button>\n"
                + "					<button type=\"button\"\n"
                + "						class=\"btn btn-primary btn-circle btn-lg btnAdd\"\n"
                + "						style=\"float: right; display: none\" rel=\"tooltip\"\n"
                + "						data-placement=\"top\" data-original-title=\"Agregar\">\n"
                + "						<i class=\"glyphicon glyphicon-plus\"></i>\n"
                + "					</button>\n"
                + "					<button type=\"button\"\n"
                + "						class=\"btn btn-danger btn-circle btn-lg btnCancel\"\n"
                + "						style=\"float: right; display: none\" rel=\"tooltip\"\n"
                + "						data-placement=\"top\" data-original-title=\"Cancelar\">\n"
                + "						<i class=\"glyphicon glyphicon-remove\"></i>\n"
                + "					</button>");
        content += ("</section>");

        content += ("</div>");
        content += ("</div>");

        content += ("\n");

        content = ""
                + content
                + buildDataTable(tableEntity,tableColumns)
                + "\n"
                + "<script src=\"../../js/business-logic/"+tableEntity+"/"+tableEntity+".js\"></script>"
                ;

        generateProject(Global.VIEW_LOCATION + tableEntity + "\\", "form"+tableEntity + ".html");
    }

    @Override
    public void foreignKeys(Table tcp, Column fk) {
        super.foreignKeys(tcp, fk);

        String columna = fk.getName();
        String column = Conversor.firstCharacterToUpper(fk.getName());

        parameters += ("<div class=\"form-group\">");
        parameters += ("<label class=\"col-md-3 control-label\">" + column + "</label>");
        parameters += ("<div class=\"col-md-4\">");
        parameters += ("<select name='" + columna + "' class='select" + column + "'>");
        parameters += ("<option></option>");
        parameters += ("</select>");
        parameters += ("</br>");

        parameters += ("</div>");
        parameters += ("</div>");

        //tableColumns +="<th>"+fk.getForeignColumn()+"</th>";
    }

    @Override
    public void primaryKeys(Table table, Column pk) {
        super.primaryKeys(table, pk);

        String columna = pk.getName();
       // parameters += ("<input name='" + columna + "' type='hidden' />");
        //tableColumns +="<th>"+columna+"</th>";
    }

    @Override
    public void resetValues() {
        super.resetValues();
         parameters = "";
         associatonColumns = "";
         methods = "";
         paramsMethods = "";
         paramsPrimaryKey = "";
         tableColumns = "";
    }

    String buildDataTable(String tableEntity,String headers){
     String section =  "<section id=\"widget-grid\">\n" +
                "<div class=\"row\">\n" +
                "<div class=\"col-sm-12\">\n" +
                "<div class=\"well well-light\">\n"
                 +"<div class='table-responsive'>" +
                    "<table class=\"table table-bordered table-hover "+tableEntity+"-datatable\" width=\"100%\">"
                        +"<thead>"
                                +"<tr>"
                                 +"<th>Actions</th>"
                                 +"<th>Nro</th>"
                                + headers
                                +"</tr>"
                            +"</thead>"
                        +"</table>"
                 +" </div>\n"
                 +"</div>\n"
                 +"</div>\n"
                 +"</div>\n"
                 +"</section>";
          return section;
    }
}