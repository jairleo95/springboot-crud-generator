/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreCRUD.spring;

import conexionDB.ConexionBD;
import conexionDB.FactoryConnectionDB;
import convert.java;
import convert.sql;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import query.DAO;
import query.makeQuery;
import utiles.conversor;
import utiles.makeFile;

/**
 *
 * @author Jairleo95
 */
public class springControllerGenerator {

    ConexionBD conn;

    public Boolean makeSpringController() throws Exception {
        int database = FactoryConnectionDB.getDefaultDatabase();
        boolean returnId = false;
        this.conn = FactoryConnectionDB.open(database);
        DAO dao = new DAO();
        List<Map<String, String>> listTableXNumColumns = dao.listTableXNumColumns(database,
                makeQuery.getSQLTableXNumColums(database));

        List<Map<String, String>> listTableXColumsP = dao.listTableXColumsP(database,
                makeQuery.getTableXColumsProperties(database));
        List<Map<String, String>> listForeignKey = dao.listForeignKey(database,
                makeQuery.getTableColumnsForeignProperties(database));

        List<Map<String, String>> listPrimaryKey = dao.listPrimaryKey(database,
                makeQuery.getTableColumnsPrimaryKeyProperties(database));

        for (int r = 0; r < listTableXNumColumns.size(); r++) {
            /*one or more ids*/
            List<String> pksCurrentTable = new ArrayList<String>();
            String tableNameTNC = listTableXNumColumns.get(r).get("TableName");
            // String tableName = conversor.toJavaFormat(tableNameTNC.substring(6), "_");
            String tableName = conversor.toJavaFormat(tableNameTNC, "_");
            String tableEntity = conversor.firstCharacterToUpper(tableName);
            List<String> listIdOfTbale = new ArrayList<String>();
            String content = "";
            System.out.println("//TABLA :" + tableNameTNC);
            String makeAssociatonColumns = "";
            String makeColumns = "";
            String makeMethods = "";
            String makeParamsMethods = "";
            String makeAllParamsMethods = "";
            String makeParamsUpdate = "";
            String paramsPrimaryKey = "";
            String makeColumnsTable = "";
            String makeImports = "";
            String originalTableName = tableNameTNC;

            String beanName = tableEntity + "Bean ";
            String tableEntityService = tableEntity + "Service";
            content += ("package org.proyecto.controller." + tableName + ";");
            makeImports += "import java.util.HashMap;"
                    + "import java.util.Map;"
                    + "import javax.servlet.http.HttpServletRequest;"
                    + "import javax.servlet.http.HttpServletResponse;"
                    + "import javax.servlet.http.HttpSession;"
                    + "import org.proyecto.bean." + tableName + "." + beanName + ";"
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
            content += ("private " + tableEntityService + " " + tableName + "Service;");
            content += ("@RequestMapping(value = \"" + tableName + "/executeCrud"
                    + conversor.firstCharacterToUpper(tableName)
                    + ".htm\", method = { RequestMethod.GET, RequestMethod.POST })");
            content += ("@ResponseBody");
            content += ("public String execute" + conversor.firstCharacterToUpper(tableName) + "(HttpServletRequest request, HttpServletResponse response) {");
            content += ("Gson gson = new Gson(); Map<String, Object> rpta = new HashMap<String, Object>(); String opc = request.getParameter(\"opc\");HttpSession sesion = request.getSession(true);Integer resultado = 0;"
                    + "String usuario = (String) sesion.getAttribute(\"usuario\");");
            content += (beanName + tableName + " = new " + beanName + "();");
            content += ("try {"
                    + "if (opc.equals(\"list\")) {");
            content += ("rpta.put(\"data\", " + tableName + "Service.list());");
            content += ("} ");
            content += ("else if (opc.equals(\"delete\")) {");
            content += ("Integer id = (Integer.parseInt(request.getParameter(\"id\")));");
            content += (tableName + ".setId" + conversor.firstCharacterToUpper(tableName) + "(id);");
            content += (tableName + ".getUsuEli().setVarUsuario(usuario);");
            content += ("resultado =" + tableName + "Service.delete(" + tableName + ");rpta.put(\"id\", resultado);");
            content += ("} ");
            content += (" else if (opc.equals(\"save\") || opc.equals(\"edit\")) {");
            /*Tipos de datos*/
            for (int h = 0; h < listTableXColumsP.size(); h++) {
                String tableNameTCP = listTableXColumsP.get(h).get("TableName");
                String columnNameTCP = listTableXColumsP.get(h).get("ColumnName");
                String dataTypeColumnTCP = listTableXColumsP.get(h).get("DataType");
                String bytes = listTableXColumsP.get(h).get("NumAtributo");
                //for (String[] listTableColumn1 : listTableColumn) {
                if (originalTableName.equals(tableNameTCP)) {
                    /*Variables*/
                    String columna = conversor.toJavaFormat(columnNameTCP, "_");
                    String dataType = "";
                    Boolean isForean = false;
                    Boolean isPrimaryKey = false;
                    dataType = dataTypeColumnTCP;
                    /*Llaves Primarias*/
                    String parametersProcedure = "sp" + conversor.firstCharacterToUpper(columna);
                    for (int g = 0; g < listPrimaryKey.size(); g++) {
                        String pkTableName = String.valueOf(listPrimaryKey.get(g).get("TableName"));
                        String pkColumnName = String.valueOf(listPrimaryKey.get(g).get("ColumnName"));
                        String pkConstraintName = String.valueOf(listPrimaryKey.get(g).get("ConstraintName"));
                        if (tableNameTNC.equals(pkTableName) & columna.equals(pkColumnName)) {
                            listIdOfTbale.add(pkColumnName);
                            paramsPrimaryKey += pkColumnName + " " + sql.getDataType(dataType, 2) + ",";
                            isPrimaryKey = true;
                        }
                    }
                    if (!isPrimaryKey) {
                        /*Llaves Foraneas*/
                        for (int d = 0; d < listForeignKey.size(); d++) {
                            String table = String.valueOf(listForeignKey.get(d).get("TableName"));
                            String column = String.valueOf(listForeignKey.get(d).get("ColumnName"));
                            String ForeignTable = String.valueOf(listForeignKey.get(d).get("ForeignTable"));
                            String ForeignColumn = String.valueOf(listForeignKey.get(d).get("ForeignColumn"));
                            if (tableNameTNC.equals(table) & columnNameTCP.equals(column)) {
                                // String ColumnaBean = conversor.toJavaFormat(columna, "_");
                                String ForeignColumnEnty = conversor.firstCharacterToUpper(conversor.toJavaFormat(ForeignColumn, "_"));
                                isForean = true;
                                makeParamsMethods += parametersProcedure + " " + sql.getDataType(dataType, 2) + ",";
                                makeColumns += ".get" + conversor.firstCharacterToUpper(columna) + "().set" + conversor.firstCharacterToUpper(conversor.toJavaFormat(ForeignColumn, "_")) + "(";
                                dataType = java.getDataType(dataType, FactoryConnectionDB.getDefaultDatabase());
                                content += (tableName + ".get" + conversor.firstCharacterToUpper(columna) + "().set" + ForeignColumnEnty + " ( " + java.getParseByDataType(dataType) + "(request.getParameter(\"" + columna + "\")));");
                            }
                        }
                    }
                    if (!isForean & !isPrimaryKey) {
                        makeColumns += "sp" + conversor.firstCharacterToUpper(columna) + ",";
                        makeParamsMethods += "sp" + conversor.firstCharacterToUpper(columna) + " " + sql.getDataType(dataType, 2) + ",";
                        makeColumnsTable += columnNameTCP + ",";
                        makeParamsUpdate += columnNameTCP + "=" + parametersProcedure + ", ";
                        dataType = java.getDataType(dataType, FactoryConnectionDB.getDefaultDatabase());
                        content += (tableName + ".set" + conversor.firstCharacterToUpper(columna) + " ( " + java.getParseByDataType(dataType) + "(request.getParameter(\"" + columna + "\")));");
                    }
                    makeAllParamsMethods += "sp" + conversor.firstCharacterToUpper(columna) + " " + sql.getDataType(dataType, 2);
                    makeParamsUpdate += columnNameTCP + "=" + "sp" + conversor.firstCharacterToUpper(columna) + ",";
                }
            }
            content += ("if (opc.equals(\"save\")) {rpta.put(\"id\", " + tableName + "Service.save(" + tableName + "));}");
            content += ("else if (opc.equals(\"edit\")) {");
            content += ("rpta.put(\"id\", " + tableName + "Service.edit(" + tableName + "));}");
            content += ("rpta.put(\"status\", true);"
                    + "} }catch (Exception e) {System.out.println(\"1ER - ERROR \" + e.getStackTrace()); System.out.println(\"2DO - ERROR \" + e.getMessage());rpta.put(\"status\", false);}return gson.toJson(rpta);}");
            content += ("}");
            makeFile.writeFolderAndFile("org\\proyecto\\controller\\" + tableName + "\\", tableEntity + "Controller.java", content);
            System.out.println(content);
            content = "";
        }
        return true;
    }
}
