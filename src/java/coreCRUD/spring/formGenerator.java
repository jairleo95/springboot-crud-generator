/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreCRUD.spring;

import conexionDB.ConexionBD;
import conexionDB.FactoryConnectionDB;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import query.DAO;
import query.makeQuery;
import utiles.conversor;

/**
 *
 * @author Jairleo95
 */
public class formGenerator {

    ConexionBD conn;

    public Boolean makeMapperXml() throws Exception {
        int database = FactoryConnectionDB.getDefaultDatabase();
        this.conn = FactoryConnectionDB.open(FactoryConnectionDB.getDefaultDatabase());
        DAO dao = new DAO();
        List<Map<String, String>> listTableXNumColumns = dao.listTableXNumColumns(FactoryConnectionDB.getDefaultDatabase(),
                makeQuery.getSQLTableXNumColums(FactoryConnectionDB.getDefaultDatabase()));
        List<Map<String, String>> listTableXColumsP = dao.listTableXColumsP(FactoryConnectionDB.getDefaultDatabase(),
                makeQuery.getTableXColumsProperties(FactoryConnectionDB.getDefaultDatabase()));
        List<Map<String, String>> listForeignKey = dao.listForeignKey(FactoryConnectionDB.getDefaultDatabase(),
                makeQuery.getTableColumnsForeignProperties(FactoryConnectionDB.getDefaultDatabase()));
        List<Map<String, String>> listPrimaryKey = dao.listPrimaryKey(database,
                makeQuery.getTableColumnsPrimaryKeyProperties(database));
        for (int f = 0; f < listTableXNumColumns.size(); f++) {
            //for (String[] List11 : List1) {
            /*one or more ids*/
            List<String> pksCurrentTable = new ArrayList<String>();
            String tableNameTNC = listTableXNumColumns.get(f).get("TableName");
            // String tableName = conversor.toJavaFormat(List11[0].substring(6), "_");
            String tableName = conversor.toJavaFormat(tableNameTNC, "_");
            String tableEntity = conversor.firstCharacterToUpper(tableName);
            //String beanName = tableEntity + "Bean ";
            System.out.println("/*TABLA :" + tableNameTNC + " */");
            String makeAssociatonColumns = "";
            String makeColumns = "";
            String makeMethods = "";
            String makeParamsMethods = "";
            String paramsPrimaryKey = "";

            for (int h = 0; h < listTableXColumsP.size(); h++) {
                String tableNameTCP = listTableXColumsP.get(h).get("TableName");
                String columnNameTCP = listTableXColumsP.get(h).get("ColumnName");
                // String dataTypeColumnTCP = listTableXColumsP.get(h).get("DataType");
                //String bytes = listTableXColumsP.get(h).get("NumAtributo");
                /*Compare Tables*/
                if (tableNameTNC.equals(tableNameTCP)) {
                    /*Variables*/
                    String columna = conversor.toJavaFormat(columnNameTCP, "_");
                    Boolean isForean = false;
                    Boolean isPrimaryKey = false;
                    /*Llaves Foraneas*/
                    for (int g = 0; g < listPrimaryKey.size(); g++) {
                        String pkTableName = String.valueOf(listPrimaryKey.get(g).get("TableName"));
                        String pkColumnName = String.valueOf(listPrimaryKey.get(g).get("ColumnName"));
                        String pkConstraintName = String.valueOf(listPrimaryKey.get(g).get("ConstraintName"));
                        if (tableNameTNC.equals(pkTableName) & columnNameTCP.equals(pkColumnName)) {
                            makeColumns += ("<id property=\"" + columna + "\" column=\"" + columnNameTCP + "\" />");
                            pksCurrentTable.add(pkColumnName);
                            paramsPrimaryKey += "#{" + pkColumnName + "},";
                            isPrimaryKey = true;
                        }
                    }
                    if (!isPrimaryKey) {
                        for (int d = 0; d < listForeignKey.size(); d++) {
                            String table = String.valueOf(listForeignKey.get(d).get("TableName"));
                            String column = String.valueOf(listForeignKey.get(d).get("ColumnName"));
                            String ForeignTable = String.valueOf(listForeignKey.get(d).get("ForeignTable"));
                            String ForeignColumn = String.valueOf(listForeignKey.get(d).get("ForeignColumn"));
                            if (tableNameTNC.equals(table) & columnNameTCP.equals(column)) {
                                String foreignTableEntity = conversor.firstCharacterToUpper(conversor.toJavaFormat(ForeignTable.substring(6), "_"));
                                String ForeignColumnBean = conversor.toJavaFormat(ForeignColumn, "_");
                                // String ColumnaBean = conversor.toJavaFormat(columna, "_");
                                makeAssociatonColumns += "<association property=\"" + columna + "\" javaType=\"" + foreignTableEntity + "\">";
                                for (int hh = 0; hh < listTableXColumsP.size(); hh++) {
                                    String tableNameFk = listTableXColumsP.get(h).get("TableName");
                                    String columnNameFk = listTableXColumsP.get(h).get("ColumnName");
                                    if (tableNameFk.equals(ForeignTable)) {
                                        if (columnNameFk.equals(ForeignColumn)) {
                                            makeAssociatonColumns += "<id column=\"" + columnNameFk + "\" property=\"" + conversor.toJavaFormat(columnNameFk, "_") + "\"></id>";
                                        } else {
                                            makeAssociatonColumns += "<result column=\"" + columnNameFk + "\" property=\"" + conversor.toJavaFormat(columnNameFk, "_") + "\"></result>";
                                        }
                                    }
                                }
                                makeAssociatonColumns += "</association>";
                                isForean = true;
                                makeParamsMethods += "#{" + columna + "." + ForeignColumnBean + "},";
                            }
                        }
                    }
                    if (!isForean & !isPrimaryKey) {
                        makeColumns += ("<result property=\"" + columna + "\" column=\"" + columnNameTCP + "\" />");
                        makeParamsMethods += "#{" + columna + "},";
                    }
                }
            }
            if (!makeParamsMethods.equals("")) {
                makeParamsMethods = makeParamsMethods.substring(0, (makeParamsMethods.length() - 1));
            }
            if (!paramsPrimaryKey.equals("")) {
                paramsPrimaryKey = paramsPrimaryKey.substring(0, (paramsPrimaryKey.length() - 1));
            }
            /*Save Method */
            makeMethods += "<select id=\"save\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
            makeMethods += "select spi_" + tableNameTNC + "(" + makeParamsMethods + ");";
            makeMethods += "</select>";
            //*EDIT METHOD*/
            makeMethods += "<select id=\"edit\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
            makeMethods += "select spu_" + tableNameTNC + "(" + makeParamsMethods + ");";
            makeMethods += "</select>";
            //*DELETE METHOD*/
            makeMethods += "<select id=\"delete\" resultType=\"Integer\" parameterType=\"" + tableEntity + "\">";
            makeMethods += "select spd_" + tableNameTNC + "(" + paramsPrimaryKey + ",usuEli.varUsuario);";
            makeMethods += "</select>";
            //*LIST METHOD*/
            makeMethods += "<select id=\"getAll\" resultMap=\"" + tableEntity + "Map" + "\">";
            makeMethods += "select * from " + tableNameTNC + ";";
            makeMethods += "</select>";
            //*FIND BY ID*/
            makeMethods += "<select id=\"findById\"  parameterType=\"Integer\" resultMap=\"" + tableEntity + "Map" + "\">";
            makeMethods += "select * from " + tableNameTNC + " where ";
            for (int ii = 0; ii < pksCurrentTable.size(); ii++) {
                if (ii == 0) {
                    makeMethods += pksCurrentTable.get(ii) + " = " + "#{" + conversor.toJavaFormat(pksCurrentTable.get(ii), "_") + "} ";
                } else {
                    makeMethods += " and " + pksCurrentTable.get(ii) + " = " + "#{" + conversor.toJavaFormat(pksCurrentTable.get(ii), "_") + "} ";
                }

            }
            makeMethods += ";";
            makeMethods += "</select>";
            String content = "";
            /*Print */
            System.out.println("<div class=\"row\">");
            System.out.println("<div class=\"col-sm-12\">");
            System.out.println("<div class=\"well\">");
            /*Botones*/
            System.out.println("<div class=\"row\">");
            System.out.println("<div class=\"col col-sm-9\"><h1>" + tableEntity + "</h1></div>");
            System.out.println("<section class=\"col col-sm-3\">");
            System.out.println("<button type=\"button\" id=\"btn-registrar\"\n"
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
            System.out.println("</section>");
            System.out.println("</div>");
            System.out.println("<div class=\"row\">\n"
                    + "<div class=\"col col-sm-12\">");
            System.out.println("<form id=\"form-submit\" class=\"smart-form formSubmit\" style=\"display: none\">");
            content += (makeColumns);
            System.out.println("");
            System.out.println("</form>");
            content += (makeAssociatonColumns);
            content += ("</resultMap>");
            content += (makeMethods);
            content += ("</mapper>");
            // makeFile.writeFolderAndFile("org\\proyecto\\resources\\xml\\mapper\\" + tableName + "\\", tableName + "-mapper.xml", content);
            System.out.println(content);
        }
        return true;
    }

}
