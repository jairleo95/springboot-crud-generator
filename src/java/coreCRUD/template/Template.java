/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreCRUD.template;

import conexionDB.FactoryConnectionDB;
import convert.sql;
import java.util.ArrayList;
import java.util.List;
import query.DAO;
import query.makeQuery;
import utiles.conversor;

/**
 *
 * @author JAIR
 */
public class Template extends core {

    @Override
    public void initValues() {
        database = FactoryConnectionDB.getDefaultDatabase();
        returnId = false;
        this.conn = FactoryConnectionDB.open(database);
        dao = new DAO();
        listTableXNumColumns = dao.listTableXNumColumns(database, makeQuery.getSQLTableXNumColums(database));
        listTableXColumsP = dao.listTableXColumsP(database, makeQuery.getTableXColumsProperties(database));
        listForeignKey = dao.listForeignKey(database, makeQuery.getTableColumnsForeignProperties(database));
        listPrimaryKey = dao.listPrimaryKey(database, makeQuery.getTableColumnsPrimaryKeyProperties(database));

    }

    public void init() {
        for (int r = 0; r < listTableXNumColumns.size(); r++) {
            /*one or more IDs*/
            Entity e = new Entity();
            e.setTableNameTNC(listTableXNumColumns.get(r).get("TableName"));
            e.setTableName(conversor.toJavaFormat(e.getTableNameTNC(), "_"));
            e.setTableEntity(conversor.firstCharacterToUpper(e.getTableName()));
            String content = "";
            /*
            String makeAssociatonColumns = "";
            String content = "";
            List<String> pksCurrentTable = new ArrayList<String>();*/
            String makeColumns = "", makeAllParamsMethods = "", makeParamsUpdate = "", makeParamsMethods = "", paramsPrimaryKey = "", makeColumnsTable = "";

            System.out.println("/*TABLA :" + e.getTableNameTNC() + " */");

            for (int h = 0; h < listTableXColumsP.size(); h++) {
                String tableNameTCP = listTableXColumsP.get(h).get("TableName");
                String columnNameTCP = listTableXColumsP.get(h).get("ColumnName");
                String dataTypeColumnTCP = listTableXColumsP.get(h).get("DataType");
                String bytes = listTableXColumsP.get(h).get("NumAtributo");
                /*Compare Tables*/
                if (e.getTableNameTNC().equals(tableNameTCP)) {
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
                        // System.err.println(e.getTableNameTNC() + " : " + pkTableName);
                        if (e.getTableNameTNC().equalsIgnoreCase(pkTableName) & columnNameTCP.equalsIgnoreCase(pkColumnName)) {
                            e.getPksCurrentTable().add(pkColumnName);
                            paramsPrimaryKey += pkColumnName + " " + sql.getDataType(dataType, database) + ",";
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
                            if (e.getTableNameTNC().equalsIgnoreCase(table) & columnNameTCP.equalsIgnoreCase(column)) {
                                // String ColumnaBean = conversor.toJavaFormat(columna, "_");
                                isForean = true;

                                /*MAKE HEADER PARAMETERS*/
                                makeParamsMethods += sql.headerParamsProcedure(parametersProcedure, sql.getDataType(dataType, database), bytes, database);

                                makeColumns += "sp" + conversor.firstCharacterToUpper(columna) + ",";
                                makeColumnsTable += columnNameTCP + ",";
                                makeParamsUpdate += columnNameTCP + "=" + parametersProcedure + ", ";
                            }
                        }
                    }
                    if (!isForean & !isPrimaryKey) {
                        makeColumns += parametersProcedure + ",";

                        makeParamsMethods += sql.headerParamsProcedure(parametersProcedure, sql.getDataType(dataType, database), bytes, database);

                        makeColumnsTable += columnNameTCP + ",";
                        makeParamsUpdate += columnNameTCP + "=" + parametersProcedure + ", ";
                    }

                    makeAllParamsMethods += "sp" + conversor.firstCharacterToUpper(columna) + " " + sql.getDataType(dataType, database);

                    makeParamsUpdate += columnNameTCP + "=" + "sp" + conversor.firstCharacterToUpper(columna) + ",";
                }
            }

            /*PARAMS SEQUENCE*/
            if (!makeParamsMethods.equals("")) {
                makeParamsMethods = makeParamsMethods.substring(0, (makeParamsMethods.length() - 1));
            }
            if (!paramsPrimaryKey.equals("")) {
                paramsPrimaryKey = paramsPrimaryKey.substring(0, (paramsPrimaryKey.length() - 1));
            }
            if (!makeColumns.equals("")) {
                makeColumns = makeColumns.substring(0, (makeColumns.length() - 1));
            }
            if (!makeColumnsTable.equals("")) {
                makeColumnsTable = makeColumnsTable.substring(0, (makeColumnsTable.length() - 1));
            }
            if (!makeParamsUpdate.equals("")) {
                makeParamsUpdate = makeParamsUpdate.substring(0, (makeParamsUpdate.length() - 1));
            }

            /*Insert record procedure */
            content += sql.headerProcedure("spi_" + e.getTableNameTNC(), makeParamsMethods, database);
            content += "INSERT INTO " + e.getTableNameTNC() + "(" + makeColumnsTable + ") values (" + makeColumns + ")";

            /*ruturn id*/
            if (e.getPksCurrentTable().size() == 1 & returnId) {
                content += " returning " + e.getPksCurrentTable().get(0) + " into result ;";
            } else {
                content += ";";
            }
            content += sql.footerProcedure("spi_" + e.getTableNameTNC(), database);

            //*EDIT procedure*/
            content += sql.headerProcedure("spu_" + e.getTableNameTNC(), makeParamsMethods, database);
            /*procedure body*/
            content += "UPDATE " + e.getTableNameTNC() + " set " + makeParamsUpdate + " where ";
            for (int t = 0; t < e.getPksCurrentTable().size(); t++) {
                if (t == 0) {
                    content += e.getPksCurrentTable().get(t) + " = " + "sp" + conversor.firstCharacterToUpper(conversor.toJavaFormat(e.getPksCurrentTable().get(t), "_"));
                } else {
                    content += " and " + e.getPksCurrentTable().get(t) + " = " + "sp" + conversor.firstCharacterToUpper(conversor.toJavaFormat(e.getPksCurrentTable().get(t), "_"));
                }
            }
            content += ";";
            content += sql.footerProcedure("spi_" + e.getTableNameTNC(), database);
            /*DELETE procedure*/
            content += sql.headerProcedure("spd_" + e.getTableNameTNC(), paramsPrimaryKey, database);
            /*DELETE body*/
            content += "UPDATE " + e.getTableNameTNC() + " set recordStatus=0 where ";
            for (int t = 0; t < e.getPksCurrentTable().size(); t++) {
                if (t == 0) {
                    content += e.getPksCurrentTable().get(t) + " = " + "sp" + conversor.firstCharacterToUpper(conversor.toJavaFormat(e.getPksCurrentTable().get(t), "_"));
                } else {
                    content += " and " + e.getPksCurrentTable().get(t) + " = " + "sp" + conversor.firstCharacterToUpper(conversor.toJavaFormat(e.getPksCurrentTable().get(t), "_"));
                }
            }
            content += ";";
            content += sql.footerProcedure("spi_" + e.getTableNameTNC(), database);
            /*Print */
            System.out.println(content);
            /*Contructor*/
        }
    }

    public String header(SyntaxType opc) {
        String c;
        switch (opc) {
            case SP_MYSQL:

                break;
        }

        return null;
    }

    public enum SyntaxType {
        SP_MYSQL,
        FORM_VIEW
    }
}
