package com.alphateam;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 

import com.alphateam.core.pattern.spring.*;
import com.alphateam.core.template.Template;

/**
 *
 * @author JAIR
 */
public class testCrud {

     public static void main(String args[]) throws Exception {

         JavaBeans j = new JavaBeans();
         j.build();

         SpringController sc =  new SpringController();
         sc.build();

         /*SpringService s = new SpringService();
         s.build();


         MapperXml m =  new MapperXml();
         m.build();

         StoreProcedure sp = new StoreProcedure();
         sp.build();

         Mapper mapper  = new Mapper();
         mapper.build();
         MyBatisConf conf = new MyBatisConf();
         conf.build();
*/
        /* HtmlForm h = new HtmlForm();
         h.build();
         ViewController v = new ViewController();
         v.build();

         JavaScriptsFunctions jsf = new JavaScriptsFunctions();
         jsf.build();
*/

     }
}
