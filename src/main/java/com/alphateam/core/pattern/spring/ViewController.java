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

import java.util.List;
/*
*
 *
 * @author Jairleo95
 */

public class ViewController extends Template {

    String dataType = "";
    String makeSetters = "";
    String makeImports = "";

    String projectID = Global.PACKAGE_NAME;

    String controllers="";

    @Override
    public Table table(Table table) {
        String tableName = Conversor.toJavaFormat(table.getName(), "_");
        String tableEntity = Conversor.firstCharacterToUpper(tableName);

        System.out.println("//TABLA :" + table.getName());

        controllers += ("\t@RequestMapping(value = \"form"+tableEntity+"\") public ModelAndView form"+tableEntity+"() {\n" +
                "\t\treturn  new ModelAndView(\"/views/"+tableEntity+"/form"+tableEntity+".html\");\n" +
                "\t}\n" +
                "\t@RequestMapping(value = \"detail"+tableEntity+"\") public ModelAndView details"+tableEntity+"() {\n" +
                "\t\treturn  new ModelAndView(\"/views/"+tableEntity+"/details-"+tableEntity+".html\");\n" +
                "\t} \n");

        return super.table(table);
    }

    @Override
    public void build() {
        super.build();

        content += ("package "+Global.PACKAGE_NAME+".controller;");

        makeImports += "import java.util.HashMap;"
                + "import java.util.Map;"
                + "import javax.servlet.http.HttpServletRequest;"
                + "import javax.servlet.http.HttpServletResponse;"
                + "import javax.servlet.http.HttpSession;"
                + "import org.apache.logging.log4j.LogManager;\n" +
                "import org.apache.logging.log4j.Logger;"
                + "import org.springframework.ui.ModelMap;"
                + "import org.springframework.web.bind.annotation.*;"
                + "import java.util.Collection;"
                + "import org.springframework.http.MediaType;"
                + "import com.google.gson.Gson;" +
                "import org.springframework.http.ResponseEntity;import org.springframework.web.servlet.ModelAndView;";
        content += ("import "+projectID+".util.Security;");
        content += ("\n");
        content += (makeImports);
        content += ("\n\n");

        content += ("@RestController(\"gui\") \n");
        content += ("public class MainController { \n");

        content += ("private final Logger log = LogManager.getLogger(getClass().getName()); \n");
        content += ("\n");

        content += ("\t@RequestMapping(value = \"blank\", method = { RequestMethod.GET, RequestMethod.POST })\n" +
                "\tpublic ModelAndView blank(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {\n" +
                "\t\tModelAndView modelAndView = new ModelAndView(\"initPage\", modelMap);\n" +
                "\t\treturn modelAndView;\n" +
                "\t} \n");

        content += ("\t@RequestMapping(value = \"/\")\n" +
                "\tpublic ModelAndView index() {\n" +
                "                return new ModelAndView(\"/views/security/pageBase.html\");\n" +
                "\t} \n");

        //views controllers
        content+= controllers;
        content += ("} \n");


        generateProject(Global.CONTROLLER_LOCATION  + "\\",  "MainController.java");
    }

    @Override
    public void resetValues() {
        super.resetValues();
        makeSetters = "";
        makeImports = "";
        content = "";
    }
}