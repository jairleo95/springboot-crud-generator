/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.pattern.spring;

import com.alphateam.connection.Factory;
import com.alphateam.convert.ToJava;
import com.alphateam.convert.ToSql;
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
public class SpringController extends Template {

    public void build() {
        init();
        for (int r = 0; r < table.size(); r++) {
            /*one or more ids*/
            List<String> pksCurrentTable = new ArrayList<String>();
            Table tnc = table.get(r);
            // String tableName = Conversor.toJavaFormat(tableNameTNC.substring(6), "_");
            String tableName = Conversor.toJavaFormat(tnc.getName(), "_");
            String tableEntity = Conversor.firstCharacterToUpper(tableName);

            List<String> listIdOfTbale = new ArrayList<String>();
            String content = "";
            System.out.println("//TABLA :" + tnc.getName());
            String makeAssociatonColumns = "";
            String makeColumns = "";
            String makeMethods = "";
            String makeParamsMethods = "";
            String makeAllParamsMethods = "";
            String makeParamsUpdate = "";
            String paramsPrimaryKey = "";
            String makeColumnsTable = "";
            String makeImports = "";
            String originalTableName = tnc.getName();

            String beanName = tableEntity + "Bean ";
            String tableEntityService = tableEntity + "SpringService";
            content += ("package org.proyecto.controller." + tableName + ";");
            makeImports += "import ToJava.util.HashMap;"
                    + "import ToJava.util.Map;"
                    + "import javax.servlet.http.HttpServletRequest;"
                    + "import javax.servlet.http.HttpServletResponse;"
                    + "import javax.servlet.http.HttpSession;"
                    + "import org.proyecto.bean." + tableName + "" + beanName + ";"
                    + "import org.proyecto.services." + tableName + "." + tableEntityService + ";"
                    + "import org.springframework.beans.factory.annotation.Autowired;"
                    + "import org.springframework.stereotype.Controller;"
                    + "import org.springframework.web.bind.annotation.RequestMapping;"
                    + "import org.springframework.web.bind.annotation.RequestMethod;"
                    + "import org.springframework.web.bind.annotation.ResponseBody;"
                    + "import com.google.gson.Gson;";
            content += (makeImports);
            content += ("@Controller \n");
            content += ("public class " + tableEntity + "Controller {");
            content += ("@Autowired \n");
            content += ("private " + tableEntityService + " " + tableName + "SpringService;");
            content += ("@RequestMapping(value = \"" + tableName + "/executeCrud"
                    + Conversor.firstCharacterToUpper(tableName)
                    + ".htm\", method = { RequestMethod.GET, RequestMethod.POST })");
            content += ("@ResponseBody");
            content += ("public String execute" + Conversor.firstCharacterToUpper(tableName) + "(HttpServletRequest request, HttpServletResponse response) {");
            content += ("Gson gson = new Gson(); Map<String, Object> rpta = new HashMap<String, Object>(); String opc = request.getParameter(\"opc\");HttpSession sesion = request.getSession(true);Integer resultado = 0;"
                    + "String usuario = (String) sesion.getAttribute(\"usuario\");");
            content += (beanName + tableName + " = new " + beanName + "();");
            content += ("try {"
                    + "if (opc.equals(\"list\")) {");
            content += ("rpta.put(\"data\", " + tableName + "SpringService.list());");
            content += ("} ");
            content += ("else if (opc.equals(\"delete\")) {");
            content += ("Integer id = (Integer.parseInt(request.getParameter(\"id\")));");
            content += (tableName + ".setId" + Conversor.firstCharacterToUpper(tableName) + "(id);");
            content += (tableName + ".getUsuEli().setVarUsuario(usuario);");
            content += ("resultado =" + tableName + "SpringService.delete(" + tableName + ");rpta.put(\"id\", resultado);");
            content += ("} ");
            content += (" else if (opc.equals(\"save\") || opc.equals(\"edit\")) {");
            /*Tipos de datos*/
            for (int h = 0; h < columns.size(); h++) {
                String tableNameTCP = columns.get(h).getName();
                String columnNameTCP = columns.get(h).getColumn().getName();
                String dataTypeColumnTCP = columns.get(h).getColumn().getDataType();
                String bytes = columns.get(h).getColumn().getAttributeNumber();
                //for (String[] listTableColumn1 : listTableColumn) {
                if (originalTableName.equals(tableNameTCP)) {
                    /*Variables*/
                    String columna = Conversor.toJavaFormat(columnNameTCP, "_");
                    String dataType = "";
                    Boolean isForean = false;
                    Boolean isPrimaryKey = false;
                    dataType = dataTypeColumnTCP;
                    /*Llaves Primarias*/
                    String parametersProcedure = "sp" + Conversor.firstCharacterToUpper(columna);
                    for (int g = 0; g < listPrimaryKey.size(); g++) {
                        /*primary keys*/
                        Table pk = listPrimaryKey.get(g);

                        if (tnc.getName().equals(pk.getName()) & columna.equals(pk.getColumn().getName())) {
                            listIdOfTbale.add(pk.getColumn().getName());
                            paramsPrimaryKey += pk.getColumn().getName() + " " + ToSql.getDataType(dataType, Factory.getDefaultDatabase()) + ",";
                            isPrimaryKey = true;
                        }
                    }
                    if (!isPrimaryKey) {
                        /*Llaves Foraneas*/
                        for (int d = 0; d < listForeignKey.size(); d++) {
                            Table fk = listForeignKey.get(d);

                           /* String table = String.valueOf(listForeignKey.get(d).get("TableName"));
                            String column = String.valueOf(listForeignKey.get(d).get("ColumnName"));
                            String ForeignTable = String.valueOf(listForeignKey.get(d).get("ForeignTable"));
                            String ForeignColumn = String.valueOf(listForeignKey.get(d).get("ForeignColumn"));*/
                            if (tnc.getName().equals(fk.getName()) & columnNameTCP.equals(fk.getColumn().getName())) {
                                // String ColumnaBean = Conversor.toJavaFormat(column, "_");
                                String ForeignColumnEnty = Conversor.firstCharacterToUpper(Conversor.toJavaFormat(fk.getForeignColumn(), "_"));
                                isForean = true;
                                makeParamsMethods += parametersProcedure + " " + ToSql.getDataType(dataType, Factory.getDefaultDatabase()) + ",";
                                makeColumns += ".get" + Conversor.firstCharacterToUpper(columna) + "().set" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(fk.getForeignColumn(), "_")) + "(";
                                dataType = ToJava.getDataType(dataType, Factory.getDefaultDatabase());
                                content += (tableName + ".get" + Conversor.firstCharacterToUpper(columna) + "().set" + ForeignColumnEnty + " ( " + ToJava.getParseByDataType(dataType) + "(request.getParameter(\"" + columna + "\")));");
                            }
                        }
                    }
                    if (!isForean & !isPrimaryKey) {
                        makeColumns += "sp" + Conversor.firstCharacterToUpper(columna) + ",";
                        makeParamsMethods += "sp" + Conversor.firstCharacterToUpper(columna) + " " + ToSql.getDataType(dataType, Factory.getDefaultDatabase()) + ",";
                        makeColumnsTable += columnNameTCP + ",";
                        makeParamsUpdate += columnNameTCP + "=" + parametersProcedure + ", ";
                        dataType = ToJava.getDataType(dataType, Factory.getDefaultDatabase());
                        content += (tableName + ".set" + Conversor.firstCharacterToUpper(columna) + " ( " + ToJava.getParseByDataType(dataType) + "(request.getParameter(\"" + columna + "\")));");
                    }
                    makeAllParamsMethods += "sp" + Conversor.firstCharacterToUpper(columna) + " " + ToSql.getDataType(dataType, Factory.getDefaultDatabase());
                    makeParamsUpdate += columnNameTCP + "=" + "sp" + Conversor.firstCharacterToUpper(columna) + ",";
                }
            }
            content += ("if (opc.equals(\"save\")) {rpta.put(\"id\", " + tableName + "SpringService.save(" + tableName + "));}");
            content += ("else if (opc.equals(\"edit\")) {");
            content += ("rpta.put(\"id\", " + tableName + "SpringService.edit(" + tableName + "));}");
            content += ("rpta.put(\"status\", true);"
                    + "} }catch (Exception e) {System.out.println(\"1ER - ERROR \" + e.getStackTrace()); System.out.println(\"2DO - ERROR \" + e.getMessage());rpta.put(\"status\", false);}return gson.toJson(rpta);}");
            content += ("}");
            FileBuilder.writeFolderAndFile("org\\proyecto\\controller\\" + tableName + "\\", tableEntity + "Controller.ToJava", content);
            System.out.println(content);
            content = "";
        }
    }
}
