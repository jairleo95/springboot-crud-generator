/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.convert;

import com.alphateam.connection.Factory;
import com.alphateam.properties.Global;
import com.alphateam.query.Column;
import com.alphateam.utiles.Conversor;

/**
 *
 * @author JAIR
 */
public class ToSql {

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

                break;
            default:
                break;
        }

        return dataType;
    }

    public static String headerParamsProcedure(String parametersProcedure, Column c, int database) {
        String sql = "";
        String dataType = c.getDataType();
        String bytes = c.getAttributeNumber();

        dataType = getDataType(dataType,database);
        switch (database) {
            case Factory.POSGRESQL:
                sql += parametersProcedure + " " + dataType + ",";
                return sql;
            case Factory.MYSQL:
                /*exception for datatype functions without length*/
                switch (dataType) {
                    case "date":
                        sql += parametersProcedure + " " + dataType + ",";
                        break;
                    case "int":
                         sql +=parametersProcedure +" "+ dataType+ ",";
                         break;
                    case "decimal":
                         sql +=parametersProcedure +" "+ dataType+ ",";
                         break;
                    case "tinyint":
                         sql +=parametersProcedure +" "+ dataType+ ",";
                         break;
                    default:
                        sql += parametersProcedure + " " + dataType + " (" + bytes + "),";
                        break;
                }
                return sql;
            case Factory.ORACLE:
                sql += parametersProcedure + " " + c.getTableName()+"."+c.getName()+"%type" + ",";
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