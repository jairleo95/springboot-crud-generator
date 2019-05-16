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
import com.alphateam.query.Column;
import com.alphateam.query.Table;
import com.alphateam.utiles.Conversor;
import com.alphateam.utiles.FileBuilder;
/*
*
 *
 * @author Jairleo95
 */

public class SpringController extends Template {
    String paramsPrimaryKey = "";

    String makeAssociatonColumns = "";
    String makeColumns = "";
    String makeMethods = "";
    String makeParamsMethods = "";
    String makeAllParamsMethods = "";
    String makeParamsUpdate = "";
    String makeColumnsTable = "";

    String dataType = "";

    String content = "";


    @Override
    public void table(Table table) {
        super.table(table);


        //one or more ids
        String tableName = Conversor.toJavaFormat(table.getName(), "_");
        String tableEntity = Conversor.firstCharacterToUpper(tableName);

        System.out.println("//TABLA :" + table.getName());

        String makeImports = "";
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

    }

    @Override
    public void primaryKeys(Table table, Column pk) {
        super.primaryKeys(table, pk);

        paramsPrimaryKey += pk.getName() + " " + ToSql.getDataType(dataType, Factory.getDefaultDatabase()) + ",";
    }

    @Override
    public void column(Column column) {
        super.column(column);
        ///String tableNameTCP = columnList.get(h).getName();
        String columnNameTCP = column.getName();

        makeAllParamsMethods += "sp" + Conversor.firstCharacterToUpper(column.getName()) + " " + ToSql.getDataType(dataType, Factory.getDefaultDatabase());
        makeParamsUpdate += columnNameTCP + "=" + "sp" + Conversor.firstCharacterToUpper(column.getName()) + ",";
    }

    @Override
    public void buildParameters(Table table, Column column) {
        super.buildParameters(table, column);

        String parametersProcedure = "sp" + Conversor.firstCharacterToUpper(column.getName());
        String tableName = Conversor.toJavaFormat(table.getName(), "_");

        String columna = Conversor.toJavaFormat(column.getName(), "_");
        makeColumns += "sp" + Conversor.firstCharacterToUpper(columna) + ",";
        makeParamsMethods += "sp" + Conversor.firstCharacterToUpper(columna) + " " + ToSql.getDataType(dataType, Factory.getDefaultDatabase()) + ",";
        makeColumnsTable += column.getName() + ",";
        makeParamsUpdate += column.getName() + "=" + parametersProcedure + ", ";
        dataType = ToJava.getDataType(dataType, Factory.getDefaultDatabase());
        content += (tableName + ".set" + Conversor.firstCharacterToUpper(columna) + " ( " + ToJava.getParseByDataType(dataType) + "(request.getParameter(\"" + columna + "\")));");

    }

    @Override
    public void foreignKeys(Table tcp, Column fk) {
        super.foreignKeys(tcp, fk);

        ///Table fk = listForeignKey.get(d);
        String tableName = Conversor.toJavaFormat(tcp.getName(), "_");
        String parametersProcedure = "sp" + Conversor.firstCharacterToUpper(fk.getName());

        String ForeignColumnEnty = Conversor.firstCharacterToUpper(Conversor.toJavaFormat(fk.getForeignColumn(), "_"));
           /// isForean = true;
            makeParamsMethods += parametersProcedure + " " + ToSql.getDataType(dataType, Factory.getDefaultDatabase()) + ",";
            makeColumns += ".get" + Conversor.firstCharacterToUpper(fk.getName()) + "().set" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(fk.getForeignColumn(), "_")) + "(";
            dataType = ToJava.getDataType(dataType, Factory.getDefaultDatabase());
            content += (tableName + ".get" + Conversor.firstCharacterToUpper(fk.getName()) + "().set" + ForeignColumnEnty + " ( " + ToJava.getParseByDataType(dataType) + "(request.getParameter(\"" + fk.getName() + "\")));");
       /// }
    }

    @Override
    public void buildMethods(Table tnc, List<String> pks) {
        super.buildMethods(tnc, pks);

        String tableName = Conversor.toJavaFormat(tnc.getName(), "_");
        String tableEntity = Conversor.firstCharacterToUpper(tableName);
        content += ("if (opc.equals(\"save\")) {rpta.put(\"id\", " + tableName + "SpringService.save(" + tableName + "));}");
        content += ("else if (opc.equals(\"edit\")) {");
        content += ("rpta.put(\"id\", " + tableName + "SpringService.edit(" + tableName + "));}");
        content += ("rpta.put(\"status\", true);"
                + "} }catch (Exception e) {System.out.println(\"1ER - ERROR \" + e.getStackTrace()); System.out.println(\"2DO - ERROR \" + e.getMessage());rpta.put(\"status\", false);}return gson.toJson(rpta);}");
        content += ("}");
        //FileBuilder.writeFolderAndFile("org\\proyecto\\controller\\" + tableName + "\\", tableEntity + "Controller.ToJava", content);
        System.out.println(content);
        content = "";
    }
}