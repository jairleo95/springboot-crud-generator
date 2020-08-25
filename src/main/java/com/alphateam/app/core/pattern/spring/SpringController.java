/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.app.core.pattern.spring;

import com.alphateam.util.ToJava;

import java.util.List;
import com.alphateam.app.core.builder.Builder;
import com.alphateam.properties.Global;
import com.alphateam.app.bean.Column;
import com.alphateam.app.bean.Table;
import com.alphateam.util.Conversor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/*
*
 *
 * @author Jairleo95
 */

public class SpringController extends Builder {
    String paramsPrimaryKey = "";

    //String associatonColumns = "";
    //String colsParams = "";
    String makeParameters = "";

    String dataType = "";
    String setters = "";
    String imports = "";

    String projectID = Global.PACKAGE_NAME;

    String idMatchDecrypt = "";

    private final Logger log = LogManager.getLogger(getClass().getName());

    @Override
    public void primaryKeys(Table table, Column pk) {
        super.primaryKeys(table, pk);

        String tableName = table.getName();
        String columna =pk.getName();
        dataType = ToJava.getDataType(pk.getDataType());
        setters += (tableName + ".set" + Conversor.firstCharacterToUpper(columna) + " ( " + ToJava.getParseByDataType(dataType) + "(request.getParameter(\"" + columna + "\"))); \n");

        idMatchDecrypt += columna+"Decrypt"+".equals("+columna+") &&";
    }

    @Override
    public void buildParameters(Table table, Column column) {
        super.buildParameters(table, column);

        String tableName = table.getName();
        String columna = column.getName();

        dataType = ToJava.getDataType(column.getDataType());
        setters += (tableName + ".set" + Conversor.firstCharacterToUpper(columna) + "(" + ToJava.getParseByDataType(dataType) + "(request.getParameter(\"" + columna + "\"))); \n");

    }

    @Override
    public void foreignKeys(Table table, Column fk) {
        super.foreignKeys(table, fk);

        String tableName = table.getName();
        String ForeignColumnEnty = Conversor.firstCharacterToUpper(fk.getForeignColumn());

        //colsParams += ".get" + Conversor.firstCharacterToUpper(fk.getName()) + "().set" + Conversor.firstCharacterToUpper(fk.getForeignColumn()) + "(";
         dataType = ToJava.getDataType(dataType);

        setters += (tableName + ".get" + Conversor.firstCharacterToUpper(fk.getName()) + "().set" + ForeignColumnEnty + " ( " + ToJava.getParseByDataType(dataType) + "(request.getParameter(\"" + fk.getName() + "\"))); \n");
    }

    @Override
    public void buildMethods(Table table, List<Column> pks) {
        super.buildMethods(table, pks);

        String tableName = table.getName();

        String beanName = Conversor.firstCharacterToUpper(tableName+ "Bean");
        String tableEntityService = Conversor.firstCharacterToUpper(tableName + "Service");
        String className = Conversor.firstCharacterToUpper(tableName + "Controller");
        String serviceName = Conversor.firstCharacterToUpper(tableName);

        imports += ToJava.getImportsByDataType(dataType);

        //log.info("//TABLA :" + table.getName());

        content += ("package "+Global.PACKAGE_NAME+".controller;");

        imports += "import java.util.HashMap;"
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
        content += (imports);
        content += ("\n\n");

        content += ("@RestController \n");
        content += ("@RequestMapping(\"/"+serviceName+"\") \n");

        content += ("public class " + className + " { \n");
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
        //content += ("log.info(\"id requested:\"+id); \n");

        content += (pkDecrypt+" \n");

        content += (" if ("+ idMatchDecrypt.substring(0, (idMatchDecrypt.length() - 2)) +"){\n" +
                "        return null;\n" +
                "    } else { \n");

        content += (beanName+" x = service.getByID("+pkParamDecrypt+"); \n");

        content += ("if (x== null){\n" +
                "            return null;\n" +
                "        }else{\n" +
                "            return x.encrypt();\n" +
                "    }\n");
        content += (" }\n");

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
        content += " @RequestMapping(value = \""+pkParamsRequest+"\",method = {RequestMethod.PUT},consumes = {MediaType.APPLICATION_JSON_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE})\n" +
                "    @ResponseBody\n" +
                "    public ResponseEntity<?> update ("+pkPathVarInput+", @RequestBody "+beanName+" "+tableName+"){\n" ;
                content += (pkDecrypt+" \n") ;

                content += (" if ("+ idMatchDecrypt.substring(0, (idMatchDecrypt.length() - 2)) +"){\n" +
                "        return null;\n" +
                "    } else { \n");
                content += pkSetter+"\n" +
                "        log.debug(\"Request received:\"+"+tableName+".toString());\n" +

                    "        if (service.getByID("+pkParamDecrypt+")!=null){\n" +
                    "            return new ResponseEntity<>(service.update("+tableName+"),HttpStatus.OK);\n" +
                    "        }  else{\n" +
                    "            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);\n" +
                    "          }\n" +
                "       }\n" +
                "    } \n";

        /*delete*/

        content += " @RequestMapping(value = \""+pkParamsRequest+"\",method = {RequestMethod.DELETE},produces = {MediaType.APPLICATION_JSON_VALUE})\n" +
                "    public ResponseEntity<?> delete("+pkPathVarInput+"){\n" ;

            content += (pkDecrypt+" \n");

            content += (" if ("+ idMatchDecrypt.substring(0, (idMatchDecrypt.length() - 2)) +"){\n" +
                    "        return null;\n" +
                    "    } else { \n");

            content += (beanName+" x = service.getByID("+pkParamDecrypt+"); \n");

            content += ("if (x== null){ \n"
                    + "     return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);\n"
                    +
                    "        } else {\n" +
                    "            String response = service.delete("+pkParamDecrypt+");\n" +
                    "            return new ResponseEntity<>(response, HttpStatus.OK);\n" +
                    "    }\n");

            content += (" }\n");

            content += (" }\n");


        content += (" \n");

        content += ("} \n");
        generateProject(Global.CONTROLLER_LOCATION  + "\\", className + ".java" );
    }

    @Override
    public void resetValues() {
        super.resetValues();
        idMatchDecrypt = "";
        setters = "";
        imports = "";
        content = "";
    }
}