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

/**
 *
 * @author Jairleo95
 */
public class JavaBeans extends Template {


    String makeColumns = "";
    String makeInstanceBean = "";
    String makeSettersAndGetters = "";
    String makeImports = "";
   //String content = "";

    String encryptContent="";
    String decryptContent = "";

    String toStringColumns="";
    String projectID = Global.PACKAGE_NAME;

    @Override
    public void foreignKeys(Table tcp, Column column) {
        super.foreignKeys(tcp, column);

        String columna = Conversor.toJavaFormat(column.getName(), "_");
       // System.out.println("Enter to foreignKeys method");
        String TableBean = Conversor.firstCharacterToUpper(Conversor.toJavaFormat(column.getForeignTable(), "_")) + "Bean";
        String ColumnaBean = Conversor.toJavaFormat(column.getName(), "_");
        makeColumns += ("private " + TableBean + " " + ColumnaBean + "; \n");
        // s++;
        //isForean = true;
        makeInstanceBean += ColumnaBean + " = new " + TableBean + "(); \n";
        makeSettersAndGetters += ("public void set" + Conversor.firstCharacterToUpper(ColumnaBean) + "(" + TableBean + " " + ColumnaBean + "){"
                + "this." + ColumnaBean + "=" + ColumnaBean + ";} \n");
        makeSettersAndGetters += "public " + TableBean + " get" + Conversor.firstCharacterToUpper(ColumnaBean) + "(){"
                + "return " + ColumnaBean + ";} \n";
        //makeImports += " import " + Global.PACKAGE_NAME + ".bean; \n";

        /*security*/
        encryptContent += "this."+columna + " = "+columna+".decrypt(); \n";
        decryptContent += "this."+columna + " = "+columna+".decrypt(); \n";
    }

    @Override
    public void primaryKeys(Table table, Column pk) {
        super.primaryKeys(table, pk);
        String columna = Conversor.toJavaFormat(pk.getName(), "_");
        String dataType = ToJava.getDataType(pk.getDataType(), Factory.getDefaultDatabase());
        makeColumns += ("@JsonProperty(\""+columna+"\") \n");
        makeColumns += ("private " + dataType + " " + columna + "; \n");
        makeSettersAndGetters += ("public void set" + Conversor.firstCharacterToUpper(columna) + "(" + dataType + " " + columna + "){ \n"
                + "this." + columna + "=" + columna + ";} \n");
        makeSettersAndGetters += "public " + dataType + "  get" + Conversor.firstCharacterToUpper(columna) + "(){ \n"
                + "return " + columna + ";}";

        makeInstanceBean += "this." + columna + "=" + ToJava.getInstanceByDataType(dataType) + "; \n";
        makeInstanceBean += "this." + columna + "=" + ToJava.getInstanceByDataType(dataType) + "; \n";

        /*security*/
         encryptContent += "this."+columna + " = "+"Security.encrypt("+columna+"); \n";
        decryptContent += "this."+columna + " = "+"Security.decrypt("+columna+"); \n";
    }

    @Override
    public void buildParameters(Table table, Column column) {
        super.buildParameters(table, column);
        String columna = Conversor.toJavaFormat(column.getName(), "_");
        String dataType = ToJava.getDataType(column.getDataType(), Factory.getDefaultDatabase());

        makeColumns += ("@JsonProperty(\""+columna+"\") \n");
        if (dataType.equalsIgnoreCase("date")){
            makeImports+="import com.fasterxml.jackson.annotation.JsonFormat;";
            makeColumns += "@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = \"yyyy-MM-dd'T'HH:mm:ss.SSSXXX\")";
        }
        makeColumns += ("private " + dataType + " " + columna + "; \n");

        makeSettersAndGetters += ("public void set" + Conversor.firstCharacterToUpper(columna) + "(" + dataType + " " + columna + "){ \n"
                + "this." + columna + "=" + columna + ";} \n");
        makeSettersAndGetters += "public " + dataType + "  get" + Conversor.firstCharacterToUpper(columna) + "(){ \n"
                + "return " + columna + ";} \n";

        makeImports += ToJava.getImportsByDataType(dataType);

        makeInstanceBean += "this." + columna + "=" + ToJava.getInstanceByDataType(dataType) + "; \n";
      //  makeInstanceBean += "this." + columna + "=" + ToJava.getInstanceByDataType(dataType) + "; \n";
    }

    @Override
    public void column(Column column) {
        super.column(column);
        String columna = Conversor.toJavaFormat(column.getName(), "_");
        toStringColumns += ("\n + \"" +  columna + "='\" +"+columna+"+ '\\'' + "+"\",\"");
    }

    @Override
    public void buildMethods(Table table, List<String> pks) {
        super.buildMethods(table, pks);
        String tableName = Conversor.toJavaFormat(table.getName(), "_");
        String beanName = Conversor.firstCharacterToUpper(tableName + "Bean");


        content += ("package "+ Global.PACKAGE_NAME +".bean" + "; \n");
        content += ("import com.fasterxml.jackson.annotation.JsonProperty; \n");
        content += ("import "+projectID+".util.Security;");
        content += (makeImports);
        content += ("\n");
        content += ("\n");
        content += ("public class " + beanName + " { \n");

        content += (makeColumns);

        content += ("\n");
        //Contructor
        content += ("public " + beanName + "(){ \n");
        content += (makeInstanceBean);
        content += ("}");
        content += ("\n");
        content += (makeSettersAndGetters);

        content += ("\n");
        //crypt
        content += "public "+beanName+" encrypt(){ \n";
        content += encryptContent;
        content += ("return this;");
        content += ("}");
        content += ("\n");

        content += "public "+beanName+" decrypt(){ \n";
        content += decryptContent;
        content += ("return this;");
        content += ("}");
        content += ("\n");

        //toString()
        content += ("@Override\n" +
                "public String toString() { \n");


        content += ("return \""+beanName+"{\" ");
        content += (toStringColumns);
        content += ("\n");
        content += ("   + '}';");

        content += ("}");
        content += ("\n");
        content += ("}");

        generateProject(Global.BEAN_LOCATION+ "\\", beanName  + ".java");
        ///System.out.println(content);

    }

    @Override
    public void resetValues() {
        super.resetValues();
         makeColumns = "";
         makeInstanceBean = "";
         makeSettersAndGetters = "";
         makeImports = "";
         content = "";
        toStringColumns = "";

            encryptContent = "";
            decryptContent = "";
    }
}
