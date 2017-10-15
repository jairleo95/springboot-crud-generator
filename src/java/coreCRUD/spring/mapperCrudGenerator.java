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
public class mapperCrudGenerator {

    ConexionBD conn;

    public Boolean makeMapperCrud() throws Exception {

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
            content += ("package org.proyecto.mapper." + tableName + ";");
            content += ("import java.util.List;");
            content += ("import org.springframework.stereotype.Component;");
            content += ("import org.proyecto.bean." + tableName + "." + beanName + ";");
            content += ("@Component");
            content += (" public interface " + tableEntity + "Mapper" + " {");
            /*Methods*/
            content += ("public List<" + beanName + "> getAll();");
            content += ("public " + beanName + " findById(Integer id);");
            content += ("public Integer delete(" + beanName + " " + tableName + ");");
            content += ("public Integer save(" + beanName + " " + tableName + ");");
            content += ("public Integer edit(" + beanName + " " + tableName + ");");
            content += ("}");
            makeFile.writeFolderAndFile("org\\proyecto\\mapper\\" + tableName + "\\", tableEntity + "Mapper.java", content);
            System.out.println(content);
        }
        return true;
    }
}
