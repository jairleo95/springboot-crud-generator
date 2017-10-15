/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreCRUD.spring;

import conexionDB.ConexionBD;
import conexionDB.FactoryConnectionDB;
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
public class serviceCrudGenerator {

    ConexionBD conn;

    public Boolean makeServiceCrud() throws Exception {

        this.conn = FactoryConnectionDB.open(FactoryConnectionDB.getDefaultDatabase());
        DAO dao = new DAO();
        List<Map<String, String>> listTableXNumColumns = dao.listTableXNumColumns(FactoryConnectionDB.getDefaultDatabase(),
                makeQuery.getSQLTableXNumColums(FactoryConnectionDB.getDefaultDatabase()));
        for (int g = 0; g < listTableXNumColumns.size(); g++) {
            String tableNameTNC = listTableXNumColumns.get(g).get("TableName");
            // String numColumnsTNC = listTableXNumColumns.get(g).get("NumColumns");
            String tableName = conversor.toJavaFormat(tableNameTNC, "_");
            /*one or more IDS*/
            String content = "";
            // String tableName = conversor.toJavaFormat(List11[0].substring(6), "_");

            String tableEntity = conversor.firstCharacterToUpper(tableName);
            String beanName = tableEntity + "Bean ";
            System.out.println("//TABLA :" + tableNameTNC);
            /*Print */
            content += ("package org.proyecto.services." + tableName + ";");
            content += ("import java.util.List;");
            content += ("import org.springframework.beans.factory.annotation.Autowired;");
            content += ("import org.springframework.stereotype.Component;");
            content += ("import org.proyecto.bean." + tableName + "." + beanName + ";"
                    + "import org.proyecto.mapper." + tableName + "." + tableEntity + "Mapper;");
            content += ("@Component");
            content += (" public class " + tableEntity + "Service" + " {");
            content += ("@Autowired");
            content += (" private " + tableEntity + "Mapper " + tableName + "Mapper;");
            /*Methods*/
            content += ("public List<" + beanName + "> list(){");
            content += ("return " + tableName + "Mapper.getAll();");
            content += ("}");
            content += ("public " + beanName + " findById(Integer id){");
            content += ("return " + tableName + "Mapper.findById(id);");
            content += ("}");
            content += ("public Integer delete(" + beanName + " " + tableName + "){");
            content += ("return " + tableName + "Mapper.delete(" + tableName + ");");
            content += ("}");
            content += ("public Integer save(" + beanName + " " + tableName + "){");
            content += ("return " + tableName + "Mapper.save(" + tableName + ");");
            content += ("}");
            content += ("public Integer edit(" + beanName + " " + tableName + "){");
            content += ("return " + tableName + "Mapper.edit(" + tableName + ");");
            content += ("}");
            content += ("}");
            makeFile.writeFolderAndFile("org\\proyecto\\services\\" + tableName + "\\", tableEntity + "Service.java", content);
            System.out.println(content);
        }
        return true;
    }
}
