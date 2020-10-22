/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.app.core.pattern.spring;

import com.alphateam.util.ToJava;
import com.alphateam.app.core.builder.Builder;
import com.alphateam.properties.Global;
import com.alphateam.app.bean.Column;
import com.alphateam.app.bean.Table;
import com.alphateam.util.Conversor;

import java.util.List;

/**
 *
 * @author Jairleo95
 */
public class JavaBeans extends Builder {

    String variables = "";
    String instanceBean = "";
    String gettersAndSetters = "";
    String imports = "";

    String encryptContent="";
    String decryptContent = "";

    String toStringColumns="";
    String projectID = Global.PACKAGE_NAME;

    //private final Logger log = LogManager.getLogger(getClass().getName());

    @Override
    public void foreignKeys(Table table, Column column) {
        super.foreignKeys(table, column);

        String columnName = column.getName();
        String dataType = ToJava.getDataType(column.getDataType());

        variables += ("@JsonProperty(\""+columnName+"\") \n");
        variables += ("private " + dataType + " " + columnName + "; \n");

        instanceBean += "this." + columnName + "=" + ToJava.getInstanceByDataType(dataType) + "; \n";
        gettersAndSetters += ("public void set" + Conversor.firstCharacterToUpper(columnName) + "(" + dataType + " " + columnName + "){ \n"
                + "this." + columnName + "=" + columnName + ";} \n");
        gettersAndSetters += "public " + dataType + "  get" + Conversor.firstCharacterToUpper(columnName) + "(){ \n"
                + "return " + columnName + ";} \n";
        encryptContent += "this."+columnName + " = "+"Security.encrypt("+columnName+"); \n";
        decryptContent += "this."+columnName + " = "+"Security.decrypt("+columnName+"); \n";

    }

    @Override
    public void primaryKeys(Table table, Column pk) {
        super.primaryKeys(table, pk);

        String columnName = pk.getName();
        String dataType = ToJava.getDataType(pk.getDataType());

        variables += ("@JsonProperty(\""+columnName+"\") \n");
        variables += ("private " + dataType + " " + columnName + "; \n");
        gettersAndSetters += ("public void set" + Conversor.firstCharacterToUpper(columnName) + "(" + dataType + " " + columnName + "){ \n"
                + "this." + columnName + "=" + columnName + ";} \n");
        gettersAndSetters += "public " + dataType + "  get" + Conversor.firstCharacterToUpper(columnName) + "(){ \n" + "return " + columnName + ";}";

        instanceBean += "this." + columnName + "=" + ToJava.getInstanceByDataType(dataType) + "; \n";

        encryptContent += "this."+columnName + " = "+"Security.encrypt("+columnName+"); \n";
        decryptContent += "this."+columnName + " = "+"Security.decrypt("+columnName+"); \n";
    }

    @Override
    public void buildParameters(Table table, Column column) {
        super.buildParameters(table, column);

        String columna = column.getName();
        String dataType = ToJava.getDataType(column.getDataType());

        variables += ("@JsonProperty(\""+columna+"\") \n");
        if (dataType.equalsIgnoreCase("date")){
            imports +="import com.fasterxml.jackson.annotation.JsonFormat;";
            variables += "@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = \"yyyy-MM-dd'T'HH:mm:ss.SSSXXX\") \n";
        }
        variables += ("private " + dataType + " " + columna + "; \n");

        gettersAndSetters += ("public void set" + Conversor.firstCharacterToUpper(columna) + "(" + dataType + " " + columna + "){ \n"
                + "this." + columna + "=" + columna + ";} \n");
        gettersAndSetters += "public " + dataType + "  get" + Conversor.firstCharacterToUpper(columna) + "(){ \n"
                + "return " + columna + ";} \n";

        imports += ToJava.getImportsByDataType(dataType);

        instanceBean += "this." + columna + "=" + ToJava.getInstanceByDataType(dataType) + "; \n";
    }

    @Override
    public void column(Column column) {
        super.column(column);

        toStringColumns += ("\n + \"" +  column.getName() + "='\" +"+column.getName()+"+ '\\'' + "+"\",\"");
    }

    @Override
    public void buildMethods(Table table, List<Column> pks) {
        super.buildMethods(table, pks);

        String tableName = table.getName();
        String beanName = Conversor.firstCharacterToUpper(tableName + "Bean");


        content += ("package "+ Global.PACKAGE_NAME +".bean" + "; \n");
        content += ("import com.fasterxml.jackson.annotation.JsonProperty; \n");
        content += ("import "+projectID+".util.Security;");
        content += (imports);
        content += ("\n");
        content += ("\n");

        content += ("public class " + beanName + " { \n");

        content += (variables);

        content += ("\n");

        //Contructor
        content += ("public " + beanName + "(){ \n");
        content += (instanceBean);
        content += ("}");
        content += ("\n");
        content += (gettersAndSetters);

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
    }

    @Override
    public void resetValues() {
        super.resetValues();
         variables = "";
         instanceBean = "";
         gettersAndSetters = "";
         imports = "";
         content = "";
        toStringColumns = "";
        encryptContent = "";
        decryptContent = "";
    }
}
