/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.util;

import com.alphateam.connection.Factory;
import com.alphateam.properties.Global;
import com.alphateam.app.bean.Column;

import java.util.List;

/**
 *
 * @author JAIR
 */
public class ToSql {
    //for procedures
    public static String getDataType(String dataType, Integer typeDB) {
        switch (typeDB) {
            case Factory.POSGRESQL:
                /*posgresql*/
                if (dataType.equalsIgnoreCase("int4")) {
                    dataType = "integer";
                } else if (dataType.equalsIgnoreCase("float8")) {
                    dataType = "double precision";
                } else {
                    dataType = "character varying";
                }
                break;
            case Factory.MYSQL:

                break;
            case Factory.ORACLE:
                if (dataType.equalsIgnoreCase("VARCHAR2") || dataType.equalsIgnoreCase("VARCHAR")){
                    dataType="VARCHAR";
                } else if(dataType.equalsIgnoreCase("DATE")){
                    dataType = "DATE";
                } else if(dataType.equalsIgnoreCase("TIMESTAMP")){
                    dataType = "DATE";
                }  else if (dataType.equalsIgnoreCase("NUMBER") || dataType.equalsIgnoreCase("NUMERIC")){
                    dataType= "NUMERIC";
                }

                break;
            default:
                break;
        }

        return dataType;
    }

    public static String headerParamsProcedure(String params, Column c, int database) {
        String sql = "";
        String dataType = c.getDataType();
        String bytes = c.getAttributeNumber();

        dataType = getDataType(dataType,database);
        switch (database) {
            case Factory.POSGRESQL:
                sql += params + " " + dataType + ",";
                return sql;
            case Factory.MYSQL:
                /*exception for datatype functions without length*/
                switch (dataType) {
                    case "date":
                        sql += params + " " + dataType + ",";
                        break;
                    case "int":
                         sql +=params +" "+ dataType+ ",";
                         break;
                    case "decimal":
                         sql +=params +" "+ dataType+ ",";
                         break;
                    case "tinyint":
                         sql +=params +" "+ dataType+ ",";
                         break;
                    default:
                        sql += params + " " + dataType + " (" + bytes + "),";
                        break;
                }
                return sql;
            case Factory.ORACLE:
                sql += params + " " + c.getTableName()+"."+c.getRawName()+"%type" + ",";
                return sql;
            default:
                return sql;
        }

    }
    //todo write oracle scripts
    public static String headerProcedure(String procedureName, String params, int database) {
        String sql = "";
        switch (database) {
            case Factory.POSGRESQL:
                sql += "CREATE OR REPLACE FUNCTION " + procedureName + "(";
                sql += params;
                sql += ") returns integer AS $BODY$ declare result integer; " + "begin ";
                return sql;
            case Factory.MYSQL:
                sql += "\n";
                sql += "DELIMITER  // CREATE OR REPLACE PROCEDURE " + procedureName + "(";
                sql += params;
                sql += ")  begin ";
                return sql;
            case Factory.ORACLE:
                sql += "CREATE OR REPLACE PROCEDURE " + namingIdentifier(procedureName,database) + "(";
                sql += params;
                sql += ") IS BEGIN ";
                //sql += ") returns integer AS $BODY$ declare result integer; " + "begin ";
                return sql;
            default:
                return sql;
        }

    }
    public static String addReturnID(List<Column> pks, boolean returnId, int database){
        String sql = "";
        switch (database) {
            case Factory.POSGRESQL:
                if (pks.size() == 1 & returnId) {
                    sql += " returning " + pks.get(0).getRawName() + " into result ;";
                } else {
                    sql += ";";
                }
                return sql;
            case Factory.MYSQL:
                return sql;
            case Factory.ORACLE:
                if (pks.size() == 1 & returnId) {
                    sql += " RETURNING " + pks.get(0).getRawName() + " INTO sp" + Conversor.firstCharacterToUpper(pks.get(0).getName().toLowerCase())+ "_OUT;";
                } else {
                    sql += ";";
                }
                return sql;
            default:
                return sql;
        }
    }

    public static String footerProcedure(String procedureName, int database) {
        String sql = "";
        switch (database) {
            case Factory.POSGRESQL:
                sql += " return result; end $BODY$ LANGUAGE plpgsql VOLATILE COST 100;";
                return sql;
            case Factory.MYSQL:
                sql += " END //\n"
                        + "DELIMITER ;";
                return sql;
            case Factory.ORACLE:
                sql += " END;\n/\n";
                return sql;
            default:
                return sql;
        }

    }

    public static String namingIdentifier(String name, int database) {
        //String sql = "";
        switch (database) {
            case Factory.POSGRESQL:
                //
                return name;
            case Factory.MYSQL:
                //
                return name;
            case Factory.ORACLE:
                //restrictions(max characteres)
               name = Conversor.extractCharacters(name, Global.MAX_CHARACTER_IDENTIFIER);
                return name;
            default:
                return name;
        }

    }

}