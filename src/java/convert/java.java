/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package convert;

import conexionDB.FactoryConnectionDB;

/**
 *
 * @author JAIR
 */
public class java {

    public static String getDataType(String dataType, Integer typeDB) {
        switch (typeDB) {
            case FactoryConnectionDB.POSGRESQL:
                if (dataType.equals("int4")) {
                    dataType = "Integer";
                } else if (dataType.equals("float8")) {
                    dataType = "Double";
                } else {
                    dataType = "String";
                }
                break;
            case FactoryConnectionDB.MYSQL:
                /*mysql*/
                if (dataType.equalsIgnoreCase("int")) {
                    dataType = "Integer";
                } else if (dataType.equalsIgnoreCase("varchar")) {
                    dataType = "String";
                } else if (dataType.equalsIgnoreCase("tinyint")) {
                    dataType = "Boolean";
                } else if (dataType.equalsIgnoreCase("char")) {
                    dataType = "String";
                } else if (dataType.equalsIgnoreCase("decimal")) {
                    dataType = "Double";
                } else if (dataType.equalsIgnoreCase("date")) {
                    dataType = "Date";
                } else {
                    dataType = "character varying";
                }
                break;
            default:
                break;
        }

        return dataType;
    }

    public static String getParseByDataType(String data) {

        if (data.equalsIgnoreCase("Integer")) {
            data = "Integer.parseIn";
        } else if (data.equalsIgnoreCase("String")) {
            data = "String.valueOf";
        } else if (data.equalsIgnoreCase("Boolean")) {
            data = "Boolean.parseBoolean";
        } else if (data.equalsIgnoreCase("Double")) {
            data = "Double.parseDouble";
        } else if (data.equalsIgnoreCase("Date")) {
            data = " Date.valueOf";
        } else {
            data = "String.valueOf";
        }
        return data;
    }

    public static String getInstanceByDataType(String data) {

        if (data.equalsIgnoreCase("Integer")) {
            data = "0";
        } else if (data.equalsIgnoreCase("String")) {
            data = "\"\"";
        } else if (data.equalsIgnoreCase("Boolean")) {
            data = "false";
        } else if (data.equalsIgnoreCase("Double")) {
            data = "0.0";
        } else if (data.equalsIgnoreCase("Date")) {
            data = "null";
        } else {
            data = "null";
        }
        return data;
    }

    public static String getImportsByDataType(String data) {

        if (data.equalsIgnoreCase("Date")) {
            data = " import java.util.Date;";
        } else {
            data = "";
        }
        return data;
    }
}
