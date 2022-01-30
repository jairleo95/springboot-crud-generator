package com.alphateam.app.base;

import com.alphateam.app.configurtions.Config;
import com.alphateam.app.configurtions.ReadConfig;
import com.alphateam.app.core.pattern.spring.*;
import com.alphateam.util.ZipUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Process {

    private final Logger log = LogManager.getLogger(getClass().getName());

    public void build(String configFile){
        log.info("file: "+(configFile));

//        if (input == null){
           Config input = new ReadConfig().loadConfigFile(configFile);
            log.debug("input: "+ input.toString());
//        }
        //initialize app
        ApplicationClass.initialize(input);
        ApplicationClass.instance().loadData();

        //build code

        //new SPMapper().build();
        new JavaBeans().build();
//        new Mapper().build();
//        new MapperXml().build();
//        new MyBatisConf().build();
//        new SpringController().build();
//        new SpringService().build();
         //new StoreProcedure().build();//fix
//        new ViewController().build();
//        new HtmlForm().build();
//        new JavaScriptsFunctions().build();
//        new PageBaseView().build();


        //compress
//        Config c = ApplicationClass.instance().getConfig();
//        System.out.println("configuration file json:" +c.toString());
//        new ZipUtils(c.getOutputLocation(), c.getZipFile()).generateZip();
    }
}
