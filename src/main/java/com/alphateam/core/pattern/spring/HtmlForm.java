/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.pattern.spring;

import java.util.ArrayList;
import java.util.List;

import com.alphateam.core.template.Template;
import com.alphateam.query.Table;
import com.alphateam.utiles.Conversor;
import com.alphateam.utiles.FileBuilder;

/**
 *
 * @author Jairleo95
 */
/*
public class HtmlForm extends Template {

  public void build() {
        init();

        for (int f = 0; f < tables.size(); f++) {
            /*one or more ids
            List<String> pksCurrentTable = new ArrayList<>();
            Table tnc = tables.get(f);

            String tableName = Conversor.toJavaFormat(tnc.getName(), "_");
            String tableEntity = Conversor.firstCharacterToUpper(tableName);

            //String beanName = tableEntity + "Bean ";
            System.out.println("/*TABLA :" + tnc.getName() + " ");

            String makeAssociatonColumns = "";
            String makeColumns = "";
            String makeMethods = "";
            String makeParamsMethods = "";
            String paramsPrimaryKey = "";

            for (int h = 0; h < columnList.size(); h++) {
                 /*table-column-property (TCP)
                Table tcp = columnList.get(h);
                /*Compare DAO
                if (tnc.getName().equals(tcp.getName())) {
                    /*Variables
                    String columna = Conversor.toJavaFormat(tcp.getColumn().getName(), "_");
                    String dataType = "";
                    Boolean isForean = false;
                    Boolean isPrimaryKey = false;
                    dataType = tcp.getColumn().getDataType();
                    /*Llaves Primarias
                    for (int g = 0; g < listPrimaryKey.size(); g++) {
                        /*primary keys
                        Table pk = listPrimaryKey.get(g);
                        if (tnc.getName().equals(pk.getColumn().getName()) & tcp.getColumn().getName().equals(pk.getColumn().getName())) {
                            makeColumns += ("<input name='" + columna + "' type='hidden' />");
                            pksCurrentTable.add(pk.getColumn().getName());
                            // pkParams += "#{" + pkColumnName + "},";
                            isPrimaryKey = true;
                            isForean = false;
                        }
                    }
                    /*llaves foraneas
                    if (!isPrimaryKey) {
                        for (int d = 0; d < listForeignKey.size(); d++) {

                            Table fk = listForeignKey.get(d);

                            if (tnc.getName().equals(fk.getName()) & tcp.getColumn().getName().equals(fk.getColumn().getName())) {
                                String foreignTableEntity = Conversor.firstCharacterToUpper(Conversor.toJavaFormat(fk.getForeignTable().substring(6), "_"));
                                String ForeignColumnBean = Conversor.toJavaFormat(fk.getForeignColumn(), "_");
                                // String ColumnaBean = Conversor.toJavaFormat(column, "_");
                                makeAssociatonColumns += "<select name='" + columna + "' javaType=\"" + foreignTableEntity + "\">";
                                for (int hh = 0; hh < columnList.size(); hh++) {
                                    String tableNameFk = columnList.get(h).getName();
                                    String columnNameFk = columnList.get(h).getColumn().getName();
                                    if (tableNameFk.equals(fk.getForeignTable())) {
                                        if (columnNameFk.equals(fk.getForeignColumn())) {
                                            makeAssociatonColumns += "<id column=\"" + columnNameFk + "\" property=\"" + Conversor.toJavaFormat(columnNameFk, "_") + "\"></id>";
                                        } else {
                                            makeAssociatonColumns += "<result column=\"" + columnNameFk + "\" property=\"" + Conversor.toJavaFormat(columnNameFk, "_") + "\"></result>";
                                        }
                                    }
                                }
                                makeAssociatonColumns += "</select>";
                                isForean = true;
                                isPrimaryKey = false;
                                //makeParamsMethods += "#{" + column + "." + ForeignColumnBean + "},";
                            }
                        }
                    }
                    if (!isForean & !isPrimaryKey) {
                        makeColumns += ("<label>" + columna + "</label>");
                        makeColumns += ("</br>");
                        makeColumns += ("<input name='" + columna + "'  type='text' class='" + columna + "'/>");
                        makeColumns += ("</br>");
                        // makeParamsMethods += "#{" + column + "},";
                    }
                }
            }
            if (!makeParamsMethods.equals("")) {
                makeParamsMethods = makeParamsMethods.substring(0, (makeParamsMethods.length() - 1));
            }
            if (!paramsPrimaryKey.equals("")) {
                paramsPrimaryKey = paramsPrimaryKey.substring(0, (paramsPrimaryKey.length() - 1));
            }
            /*Save Method 
            makeMethods += "<select id=\"save\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
            makeMethods += "select spi_" + tnc.getName() + "(" + makeParamsMethods + ");";
            makeMethods += "</select>";
            //*EDIT METHOD
            makeMethods += "<select id=\"edit\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
            makeMethods += "select spu_" + tnc.getName() + "(" + makeParamsMethods + ");";
            makeMethods += "</select>";
            //*DELETE METHOD
            makeMethods += "<select id=\"delete\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
            makeMethods += "select spd_" + tnc.getName() + "(" + paramsPrimaryKey + ",usuEli.varUsuario);";
            makeMethods += "</select>";
            //*LIST METHOD
            makeMethods += "<select id=\"getAll\" resultMap=\"" + tableEntity + "Map" + "\">";
            makeMethods += "select * from " + tnc.getName() + ";";
            makeMethods += "</select>";
            //*FIND BY ID
            makeMethods += "<select id=\"findById\"  parameterType=\"Integer\" resultMap=\"" + tableEntity + "Map" + "\">";
            makeMethods += "select * from " + tnc.getName() + " where ";
            for (int ii = 0; ii < pksCurrentTable.size(); ii++) {
                if (ii == 0) {
                    makeMethods += pksCurrentTable.get(ii) + " = " + "#{" + Conversor.toJavaFormat(pksCurrentTable.get(ii), "_") + "} ";
                } else {
                    makeMethods += " and " + pksCurrentTable.get(ii) + " = " + "#{" + Conversor.toJavaFormat(pksCurrentTable.get(ii), "_") + "} ";
                }

            }
            makeMethods += ";";
            makeMethods += "</select>";
            String content = "";

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

            String html = "<!DOCTYPE html>\n"
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

            // content += (makeAssociatonColumns);
            //  content += (methods);
            FileBuilder.writeFolderAndFile("org\\proyecto\\views\\" + tableName + "\\", tableName + ".html", html);
            System.out.println(html);
        }
    }

}
*/