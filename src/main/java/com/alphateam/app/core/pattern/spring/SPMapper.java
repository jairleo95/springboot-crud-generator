package com.alphateam.app.core.pattern.spring;

import com.alphateam.app.bean.Column;
import com.alphateam.app.bean.StoreProcedure;
import com.alphateam.app.bean.Table;
import com.alphateam.app.core.builder.Builder;
import com.alphateam.app.core.persistence.EntityDAO;
import com.alphateam.connection.Factory;
import com.alphateam.connection.ORACLE;
import com.alphateam.util.Conversor;
import com.alphateam.util.ToJava;
import com.alphateam.util.ToSql;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class SPMapper {

    private final Logger log = LogManager.getLogger(getClass().getName());

    public static void build() {

        List<StoreProcedure> sp =  new EntityDAO().getStoredProcedures();
        //SP parameter mybatis
//        sp.forEach(x->{
//
//            if (x.getDataType().equalsIgnoreCase("NUMBER")&& x.getDataPrecision()!=null){
//                System.out.println("#{"+x.getArgument().toLowerCase()+", mode=IN, javaType=Double, " +
//                        "jdbcType" +
//                        "="+ ToSql.getDataType(x.getDataType(), Factory.ORACLE) +"},");
//            } else{
//                System.out.println("#{"+x.getArgument().toLowerCase()+", mode=IN, javaType="+ ToJava.getDataType(x.getDataType())+
//                        ", " +
//                        "jdbcType" +
//                        "="+ ToSql.getDataType(x.getDataType(), Factory.ORACLE) +"},");
//            }
//        });

        //MAP
        sp.forEach(x->{
            if (x.getInout().equalsIgnoreCase("IN")){
                System.out.println("map.put(\""+x.getArgument().toLowerCase()+"\", x.get"+Conversor.firstCharacterToUpper(Conversor.toJavaFormat(x.getArgument(),"_"))+"());");
            } else {
                System.out.println("x.set"+Conversor.firstCharacterToUpper(Conversor.toJavaFormat(x.getArgument(),"_"))+
                        "(" +
                        "("+ToJava.getDataType(x.getDataType())+")map.get(\""+x.getArgument().toLowerCase()+"\"));");
            }

        });

//        sp.forEach(x->{
//            System.out.println("private "+ ToJava.getDataType(x.getDataType())+" "+Conversor.toJavaFormat(x.getArgument(),"_")+";");
//        });

        //SETTERS
//        sp.forEach(x->{
//            System.out.println("x.set"+Conversor.firstCharacterToUpper(Conversor.toJavaFormat(x.getArgument(),"_")+
//                    "();"));
//        });

        //mapper parameter v2
        /*sp.forEach(x->{

            System.out.println("#{"+x.getArgument().toLowerCase()+", mode="+x.getInout()+", javaType="+ ToJava.getDataType(x.getDataType())+
                    ", " +
                    "jdbcType" +
                    "="+ ToSql.getDataType(x.getDataType(), Factory.ORACLE) +"},");
        });*/
    }

}
