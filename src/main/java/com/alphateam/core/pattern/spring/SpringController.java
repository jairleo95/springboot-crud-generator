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
import com.alphateam.properties.Global;
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

    //String makeAssociatonColumns = "";
    String makeColumns = "";
    String makeParameters = "";

    String dataType = "";
    String makeSetters = "";
    String makeImports = "";

    String projectID = Global.PACKAGE_NAME;


    @Override
    public void primaryKeys(Table table, Column pk) {
        super.primaryKeys(table, pk);
        String tableName = Conversor.toJavaFormat(table.getName(), "_");
        String columna = Conversor.toJavaFormat(pk.getName(), "_");
        dataType = ToJava.getDataType(pk.getDataType(), Factory.getDefaultDatabase());
        makeSetters += (tableName + ".set" + Conversor.firstCharacterToUpper(columna) + " ( " + ToJava.getParseByDataType(dataType) + "(request.getParameter(\"" + columna + "\"))); \n");
    }

    @Override
    public void column(Column column) {
        super.column(column);
    }

    @Override
    public void buildParameters(Table table, Column column) {
        super.buildParameters(table, column);

        String tableName = Conversor.toJavaFormat(table.getName(), "_");
        String columna = Conversor.toJavaFormat(column.getName(), "_");

        dataType = ToJava.getDataType(column.getDataType(), Factory.getDefaultDatabase());
        makeSetters += (tableName + ".set" + Conversor.firstCharacterToUpper(columna) + "(" + ToJava.getParseByDataType(dataType) + "(request.getParameter(\"" + columna + "\"))); \n");

    }

    @Override
    public void foreignKeys(Table tcp, Column fk) {
        super.foreignKeys(tcp, fk);

        String tableName = Conversor.toJavaFormat(tcp.getName(), "_");
        String ForeignColumnEnty = Conversor.firstCharacterToUpper(Conversor.toJavaFormat(fk.getForeignColumn(), "_"));

        makeColumns += ".get" + Conversor.firstCharacterToUpper(fk.getName()) + "().set" + Conversor.firstCharacterToUpper(Conversor.toJavaFormat(fk.getForeignColumn(), "_")) + "(";
         dataType = ToJava.getDataType(dataType, Factory.getDefaultDatabase());

        makeSetters += (tableName + ".get" + Conversor.firstCharacterToUpper(fk.getName()) + "().set" + ForeignColumnEnty + " ( " + ToJava.getParseByDataType(dataType) + "(request.getParameter(\"" + fk.getName() + "\"))); \n");
    }

    @Override
    public void buildMethods(Table table, List<String> pks) {
        super.buildMethods(table, pks);

        String tableName = Conversor.toJavaFormat(table.getName(), "_");
        String tableEntity = Conversor.firstCharacterToUpper(tableName);


        makeImports += ToJava.getImportsByDataType(dataType);

        System.out.println("//TABLA :" + table.getName());


        String beanName = tableEntity + "Bean";
        String tableEntityService = tableEntity + "Service";
        content += ("package "+Global.PACKAGE_NAME+".controller;");

        makeImports += "import java.util.HashMap;"
                + "import java.util.Map;"
                + "import javax.servlet.http.HttpServletRequest;"
                + "import javax.servlet.http.HttpServletResponse;"
                + "import javax.servlet.http.HttpSession;"
                + "import org.apache.logging.log4j.LogManager;\n" +
                "import org.apache.logging.log4j.Logger;"
                + "import "+projectID+".bean." + beanName + ";"
                + "import "+projectID+".services." + tableEntityService + ";"
                + "import org.springframework.beans.factory.annotation.Autowired;"
                + "import org.springframework.web.bind.annotation.*;"
                + "import java.util.Collection;"
                + "import org.springframework.http.MediaType;"
                + "import com.google.gson.Gson;" +
                "import org.springframework.http.ResponseEntity;import org.springframework.http.HttpStatus;";
        content += ("import "+projectID+".util.Security;");
        content += ("\n");
        content += (makeImports);
        content += ("\n\n");

        content += ("@RestController \n");
        content += ("@RequestMapping(\"/"+tableEntity+"\") \n");

        content += ("public class " + tableEntity + "Controller { \n");
        content += ("@Autowired \n");
        content += ("private " + tableEntityService + " " + "service; \n");
        content += ("private final Logger log = LogManager.getLogger(getClass().getName()); \n");
        content += ("\n");
        //getAll
        content += ("@RequestMapping (method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE}) \n");
        content += ("public Collection<"+beanName+"> read(){return this.service.getAll();} \n");
        content += ("\n");
        //ByID
        content += ("@RequestMapping(value = \""+pkParamsRequest+"\",method = {RequestMethod.GET},produces = {MediaType.APPLICATION_JSON_VALUE}) \n");
        content += ("@ResponseBody \n");
        content += ("\n");
        content += ("public "+beanName+" read("+pkPathVarInput+"){ \n");
        /*todo*/
        //content += ("log.info(\"id;\"+id); \n");
        content += (beanName+" x = service.getByID("+pkParams+"); \n");
        content += ("if (x== null){\n" +
                "            return null;\n" +
             /*   "        }else if(!x.getRecordStatus()){\n" +
                "            return null;\n" +*/
                "        }else{\n" +
                "            return x.encrypt();\n" +
                "    }\n");
        content += (" }\n");

        /*add*/
        content += (" @RequestMapping(method = {RequestMethod.POST}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})\n" +
                "    @ResponseBody\n" +
                "    public ResponseEntity<?> add (@RequestBody "+beanName+" input){\n" +
                "        log.info(\"Request received:\"+input.toString());\n" +
              //  "        if (service.getByName(input.getName())!=null){\n" +
                //"          log.info(\"A target name \"+input.getName()+ \" already exist\");\n" +
                //"            return new ResponseEntity<Void>(HttpStatus.CONFLICT);\n" +
                //"        }\n" +
                "        "+beanName+" "+tableName+" = service.create(input).encrypt();\n" +
                "        return new ResponseEntity<>("+tableName+",HttpStatus.CREATED);\n" +
                "    } \n");
        content += ("\n");

        /*update*/
        content += (" @RequestMapping(value = \""+pkParamsRequest+"\",method = {RequestMethod.PUT},consumes = {MediaType.APPLICATION_JSON_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE})\n" +
                "    @ResponseBody\n" +
                "    public ResponseEntity<?> update ("+pkPathVarInput+", @RequestBody "+beanName+" "+tableName+"){\n" +
                "      "+pkSetter+"\n" +
                "        log.debug(\"Request received:\"+"+tableName+".toString());\n" +
                "        if (service.getByID("+pkParams+")!=null){\n" +
                "            return new ResponseEntity<>(service.update("+tableName+"),HttpStatus.OK);\n" +
                "        }else{\n" +
                "            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);\n" +
                "        }\n" +
                "    } \n");

        /*delete*/

        content += (" @RequestMapping(value = \""+pkParamsRequest+"\",method = {RequestMethod.DELETE},produces = {MediaType.APPLICATION_JSON_VALUE})\n" +
                "    public ResponseEntity<?> delete("+pkPathVarInput+"){\n" +
              //  "        log.info(\"id received:\"+id);\n" +
                "        "+beanName+" x = service.getByID("+pkParams+");\n" +
                "        if (x==null){\n" +
                "            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);\n" +
                "        }" +
                //"else if (!x.getRecordStatus()){\n" +
                //"            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);\n" +
                //"        }" +
                "else{\n" +
                "            String response = service.delete("+pkParams+");\n" +
                "            return new ResponseEntity<>(response, HttpStatus.OK);\n" +
                "        }\n" +
                "    } \n");

        content += (" \n");

        content += ("} \n");
        generateProject(Global.CONTROLLER_LOCATION  + "\\", tableEntity + "Controller.java");
        //System.out.println(content);
    }

    @Override
    public void resetValues() {
        super.resetValues();
        makeSetters = "";
        makeImports = "";
        content = "";
    }
}