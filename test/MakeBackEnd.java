/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import coreCRUD.spring.javaBeansGenerator;
import coreCRUD.spring.mapperCrudGenerator;
import coreCRUD.spring.mapperXmlGenerator;
import coreCRUD.spring.serviceCrudGenerator;
import coreCRUD.spring.springControllerGenerator;
import coreCRUD.spring.storeProcedureCrudGenerator;

/**
 *
 * @author Jairleo95
 */
public class MakeBackEnd {

    public static void main(String args[]) throws Exception {
        /*    javaBeansGenerator.makeJavaBeans();
        mapperCrudGenerator.makeMapperCrud();
        mapperXmlGenerator.makeMapperXml();
        serviceCrudGenerator.makeServiceCrud();*/

        springControllerGenerator h = new springControllerGenerator();
        h.makeSpringController();

        /* serviceCrudGenerator z = new serviceCrudGenerator();
        z.makeServiceCrud();

      mapperCrudGenerator y = new mapperCrudGenerator();
        y.makeMapperCrud();

        javaBeansGenerator x = new javaBeansGenerator();
        x.makeJavaBeans();
        mapperXmlGenerator s = new mapperXmlGenerator();
        s.makeMapperXml();*/
    }
}
