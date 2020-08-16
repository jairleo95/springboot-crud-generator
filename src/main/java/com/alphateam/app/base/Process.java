package com.alphateam.app.base;

import com.alphateam.app.configurtions.Config;
import com.alphateam.app.configurtions.ReadConfig;
import com.alphateam.core.pattern.spring.*;
import com.alphateam.core.template.Template;
import com.alphateam.utiles.ZipUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;

public class Process {

    private final Logger log = LogManager.getLogger(getClass().getName());

    public void build(Config input){


        if (input == null){
            try {
                input = new ReadConfig()
                        .loadConfigFile("D:\\Software Development\\crud-generator-project\\config.json");
                log.debug("input: "+ input.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        //initialize app
        ApplicationClass.initialize(input);
        ApplicationClass.instance().loadData();

        //build code

//    new JavaBeans().build();
//        new Mapper().build();
          new MapperXml().build();
//        new MyBatisConf().build();
        new SpringController().build();
        //new SpringService().build();
//        new StoreProcedure().build();
        //new ViewController().build();
     //   new HtmlForm().build();
        //new JavaScriptsFunctions().build();
       // new PageBaseView().build();


        //compress
        Config c = ApplicationClass.instance().getConfig();
        new ZipUtils(c.getOutputLocation(), c.getZipFile()).generateZip();
    }
}
