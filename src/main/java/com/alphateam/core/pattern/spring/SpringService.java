/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.pattern.spring;

import com.alphateam.connection.Factory;
import com.alphateam.convert.ToJava;
import com.alphateam.core.template.Template;
import com.alphateam.properties.Global;
import com.alphateam.query.Column;
import com.alphateam.query.Table;
import com.alphateam.utiles.Conversor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 *
 * @author Jairleo95
 */
public class SpringService extends Template {
    String projectID = Global.PACKAGE_NAME;
    private final Logger log = LogManager.getLogger(getClass().getName());

    @Override
    public void primaryKeys(Table table, Column pk) {
        super.primaryKeys(table, pk);
    }

    @Override
    public void buildMethods(Table table, List<String> pks) {
        super.buildMethods(table, pks);
               /*table name - columnList*/

        String tableName = table.getName();

        String tableEntity = Conversor.firstCharacterToUpper(tableName);
        String beanName = tableEntity + "Bean";

        log.debug("//TABLA :" + table.getName());
        /*Print*/
        content += ("package "+projectID+".services;");

        content += ("\n\n");
        content += ("import "+projectID+".constants.R;");
        content += ("import java.util.List;");
        content += ("import java.util.HashMap;");
        content += ("import org.apache.ibatis.session.SqlSession;\n" +
                "import org.apache.logging.log4j.LogManager;\n" +
                "import org.apache.logging.log4j.Logger;\n" +
                "import com.google.gson.Gson;\n"
                + "import "+projectID+".bean."+ beanName + ";"
                +"import java.util.Map;\n");
        content += ("import java.util.Collection;");
        content += ("import "+projectID+".util.Security;");
        content += ("import org.springframework.beans.factory.annotation.Autowired;");
        content += ("import org.springframework.stereotype.Component;");
        content += ("import "+projectID+".bean."  + beanName + ";"
                + "import "+projectID+".mapper."  + tableEntity + "Mapper;");
        content += ("\n");
        content += ("@Component \n");
        content += ("public class " + tableEntity + "Service" + " { \n");
        content += ("private final Logger log = LogManager.getLogger(getClass().getName()); \n");
        content += ("private "+beanName+" "+tableName+"Bean; \n");
        content += ("private final SqlSession sqlSession;\n" +
                "    private Gson gson;\n" +
                "    private Map<String, Object> data; \n");

        content += ("private " + tableEntity + "Mapper " + "mapper; \n");
        content += ("public "+tableEntity+"Service(SqlSession sqlSession, "+tableEntity+"Mapper mapper) {\n" +
                "        this.sqlSession = sqlSession;\n" +
                "       this.mapper = mapper;\n" +
                "    }\n" +
                "    public void init() {\n" +
                "        this.gson = new Gson();\n" +
                "        this.data = new HashMap<String, Object>();\n" +
                "        this."+tableName+"Bean = new "+beanName+"();\n" +
                "    }\n");

        /*Methods*/

        /*create*/
        content += ("public "+beanName+" create(" + beanName + " " + tableName + "){ \n");
        content += ("log.info(\"result:\"+"+tableName+".toString()); \n");
        content += ("mapper.create(" + tableName + ".decrypt()); \n");
        content += ("return mapper.getById(" +pkParameters+ "); \n");
        content += ("}");
        content += ("\n\n");

        /*delete*/
        content += ("public String delete(" + pkMethVarInput + "){ \n");
        content += ("   init(); \n");
        content += (pkDecrypt+" \n");
        content += ("   try {\n" +
                "            data.put(R.global.STATUS, mapper.delete("+pkinput+"));\n" +
                "            data.put(R.global.MESSAGE,\"El acceso fue eliminado correctamente\");\n" +
                "        }catch (Exception e){\n" +
                "            log.info(e.getMessage());\n" +
                "            data.put(R.global.STATUS,false);\n" +
                "            data.put(R.global.MESSAGE,\"Hubo un problema en el Sitema, intentelo nuevamente\");\n" +
                "        }\n" +
                "        return gson.toJson(data);");
        content += ("} \n");
        content += ("\n\n");

        /*update*/
        content += ("public String update(" + beanName + " " + tableName + "){ \n");
        content += ("init();\n" +
                "        try {\n" +
                "            data.put(R.global.MESSAGE,\"El acceso fue actualizado correctamente\");\n" +
                "            data.put(R.global.STATUS, mapper.update("+tableName+"));\n" +
                "        }catch (Exception e){\n" +
                "            log.info(e.getMessage());\n" +
                "            data.put(R.global.MESSAGE, \"Hubo un problema en el Sistema, intentelo nuevamente.\");\n" +
                "        }\n" +
                "        return gson.toJson(data); \n");
        content += ("} \n");
        content += ("\n\n");

        content += ("public " + beanName + " getByID("+pkMethVarInput+"){ \n");
        content += (pkDecrypt+" \n");
        content += ("return mapper.getById("+pkinput+"); \n");
        content += ("} \n");

        content += ("public Collection<" + beanName + "> getAll(){ \n");
        content += ("List<"+beanName+"> list = mapper.read();\n" +
                "        for (int i = 0; i < list.size(); i++) {\n" +
                "            list.set(i,  list.get(i).encrypt());\n" +
                "        }\n" +
                "        return list; \n");
        content += ("} \n");

        content += ("} \n");

        generateProject(Global.SERVICE_LOCATION+ "\\", tableEntity + "Service.java");
        System.out.println(content);
    }

    @Override
    public void resetValues() {
        super.resetValues();
    }
}
