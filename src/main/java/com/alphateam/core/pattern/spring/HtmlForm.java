/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.pattern.spring;

import java.util.ArrayList;
import java.util.List;

import com.alphateam.core.template.Template;
import com.alphateam.query.Column;
import com.alphateam.query.Table;
import com.alphateam.utiles.Conversor;
import com.alphateam.utiles.FileBuilder;

/**
 *
 * @author Jairleo95
 */

public class HtmlForm extends Template {

    String makeColumns = "";
    String makeAssociatonColumns = "";
    String makeMethods = "";
    String makeParamsMethods = "";
    String paramsPrimaryKey = "";


    @Override
    public void buildParameters(Table table, Column column) {
        super.buildParameters(table, column);
        String columna = Conversor.toJavaFormat(column.getName(), "_");
        makeColumns += ("<label>" + columna + "</label>");
        makeColumns += ("</br>");
        makeColumns += ("<input name='" + columna + "'  type='text' class='" + columna + "'/>");
        makeColumns += ("</br>");
        // makeParamsMethods += "#{" + column + "},";
    }

    @Override
    public void buildMethods(Table tnc, List<String> pks) {
        super.buildMethods(tnc, pks);
        String tableName = Conversor.toJavaFormat(tnc.getName(), "_");
        String tableEntity = Conversor.firstCharacterToUpper(tableName);
        System.out.println("/*TABLA :" + tnc.getName() + " ");

        if (!makeParamsMethods.equals("")) {
            makeParamsMethods = makeParamsMethods.substring(0, (makeParamsMethods.length() - 1));
        }
        if (!paramsPrimaryKey.equals("")) {
            paramsPrimaryKey = paramsPrimaryKey.substring(0, (paramsPrimaryKey.length() - 1));
        }
        //Save Method
        makeMethods += "<select id=\"save\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
        makeMethods += "select spi_" + tnc.getName() + "(" + makeParamsMethods + ");";
        makeMethods += "</select>";
        ///EDIT METHOD
        makeMethods += "<select id=\"edit\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
        makeMethods += "select spu_" + tnc.getName() + "(" + makeParamsMethods + ");";
        makeMethods += "</select>";
        //DELETE METHOD
        makeMethods += "<select id=\"delete\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
        makeMethods += "select spd_" + tnc.getName() + "(" + paramsPrimaryKey + ",usuEli.varUsuario);";
        makeMethods += "</select>";
        //LIST METHOD
        makeMethods += "<select id=\"getAll\" resultMap=\"" + tableEntity + "Map" + "\">";
        makeMethods += "select * from " + tnc.getName() + ";";
        makeMethods += "</select>";
        //FIND BY ID
        makeMethods += "<select id=\"findById\"  parameterType=\"Integer\" resultMap=\"" + tableEntity + "Map" + "\">";
        makeMethods += "select * from " + tnc.getName() + " where ";
        for (int ii = 0; ii < pks.size(); ii++) {
            if (ii == 0) {
                makeMethods += pks.get(ii) + " = " + "#{" + Conversor.toJavaFormat(pks.get(ii), "_") + "} ";
            } else {
                makeMethods += " and " + pks.get(ii) + " = " + "#{" + Conversor.toJavaFormat(pks.get(ii), "_") + "} ";
            }

        }
        makeMethods += ";";
        makeMethods += "</select>";

        //String content = "";

        content += ("<div class=\"row\">");
        content += ("<div class=\"col-sm-12\">");
        content += ("<div class=\"well\">");

        content += ("<div class=\"row\">");
        content += ("<div class=\"col col-sm-9\"><h1>" + tableEntity + "</h1></div>");
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
        content += ("<div class=\"row\">\n"
                + "<div class=\"col col-sm-12\">");
        content += ("<form id=\"form-submit\" class=\"smart-form formSubmit\" style=\"display: \">");
        content += (makeColumns);
        content += ("\n");
        content += ("</form>");

         content = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Title of the document</title>\n"
                + "</head>\n"
                + "\n"
                + "<body>\n"
                + content
                + "</body>\n"
                + "\n"
                + "</html>";
        generateProject("org\\proyecto\\views\\" + tableName + "\\", tableName + ".html");
       System.out.println(content);
    }

    @Override
    public void foreignKeys(Table tcp, Column fk) {
        super.foreignKeys(tcp, fk);
        String columna = Conversor.toJavaFormat(fk.getName(), "_");

            String foreignTableEntity = Conversor.firstCharacterToUpper(Conversor.toJavaFormat(fk.getForeignTable().substring(6), "_"));
            String ForeignColumnBean = Conversor.toJavaFormat(fk.getForeignColumn(), "_");
            // String ColumnaBean = Conversor.toJavaFormat(column, "_");
            makeAssociatonColumns += "<select name='" + columna + "' javaType=\"" + foreignTableEntity + "\">";
            /*for (int hh = 0; hh < columnList.size(); hh++) {
                String tableNameFk = columnList.get(h).getName();
                String columnNameFk = columnList.get(h).getColumn().getName();
                if (tableNameFk.equals(fk.getForeignTable())) {
                    if (columnNameFk.equals(fk.getForeignColumn())) {
                        makeAssociatonColumns += "<id column=\"" + columnNameFk + "\" property=\"" + Conversor.toJavaFormat(columnNameFk, "_") + "\"></id>";
                    } else {
                        makeAssociatonColumns += "<result column=\"" + columnNameFk + "\" property=\"" + Conversor.toJavaFormat(columnNameFk, "_") + "\"></result>";
                    }
                }
            }*/
            makeAssociatonColumns += "</select>";
    }

    @Override
    public void primaryKeys(Table table, Column pk) {
        super.primaryKeys(table, pk);
        String columna = Conversor.toJavaFormat(pk.getName(), "_");
        makeColumns += ("<input name='" + columna + "' type='hidden' />");
    }


    @Override
    public void resetValues() {
        super.resetValues();
         makeColumns = "";
         makeAssociatonColumns = "";
         makeMethods = "";
         makeParamsMethods = "";
         paramsPrimaryKey = "";
    }
}