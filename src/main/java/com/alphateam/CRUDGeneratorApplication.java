package com.alphateam;

import com.alphateam.app.configurtions.AppConfiguration;
import com.alphateam.app.configurtions.Config;
import com.alphateam.core.pattern.spring.*;

import com.alphateam.utiles.ZipUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class CRUDGeneratorApplication implements CommandLineRunner {
     public static void main(String[] args) {
          SpringApplication.run(CRUDGeneratorApplication.class, args);
     }

     @Override
     public void run(String... args) throws Exception {

          AppConfiguration.initialize("D:\\Software Development\\crud-generator-project\\config.json");

          Config c = AppConfiguration.instance().getConfig();

          new JavaBeans().build();
          new SpringController().build();
          new HtmlForm().build();
          new JavaScriptsFunctions().build();
          new Mapper().build();
          new MapperXml().build();
          new MyBatisConf().build();
          new SpringController().build();
          new SpringService().build();
          new StoreProcedure().build();
          new ViewController().build();

          //compress

          new ZipUtils(c.getOutputLocation(), c.getZipFile()).generateZip();
     }
}