package com.alphateam;

import com.alphateam.core.pattern.spring.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by JairL on 3/30/2020.
 */

@RestController
public class MainController {
    private final Logger log = LogManager.getLogger(getClass().getName());

    @RequestMapping(value = "/test",method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<?> test() {
        JavaBeans j = new JavaBeans();
        j.build();

        SpringController sc =  new SpringController();
        sc.build();

        SpringService s = new SpringService();
        s.build();


        MapperXml m =  new MapperXml();
        m.build();

        StoreProcedure sp = new StoreProcedure();
        sp.build();

        Mapper mapper  = new Mapper();
        mapper.build();
        MyBatisConf conf = new MyBatisConf();
        conf.build();
        ViewController v = new ViewController();
        v.build();
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
