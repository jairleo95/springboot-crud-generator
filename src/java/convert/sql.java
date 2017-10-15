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
public class sql {

    public static String getDataType(String dataType, Integer typeDB) {
        switch (typeDB) {
            case FactoryConnectionDB.POSGRESQL:
                /*posgresql*/
                if (dataType.equals("int4")) {
                    dataType = "integer";
                } else if (dataType.equals("float8")) {
                    dataType = "double precision";
                } else {
                    dataType = "character varying";
                }
                break;
            case FactoryConnectionDB.MYSQL:

                break;
            default:
                break;
        }

        return dataType;
    }

    public static String headerParamsProcedure(String parametersProcedure, String dataType, String bytes, int database) {
        String sql = "";
        switch (database) {
            case FactoryConnectionDB.POSGRESQL:
                sql += parametersProcedure + " " + dataType + ",";
                return sql;
            case FactoryConnectionDB.MYSQL:
                /*exception for datatype functions without length*/
                switch (dataType) {
                    case "date":
                        sql += parametersProcedure + " " + dataType + ",";
                        break;
                    default:
                        sql += parametersProcedure + " " + dataType + " (" + bytes + "),";
                        break;
                }
                return sql;
            default:
                return sql;
        }

    }

    public static String headerProcedure(String procedureName, String params, int database) {
        String sql = "";
        switch (database) {
            case FactoryConnectionDB.POSGRESQL:
                sql += "CREATE OR REPLACE FUNCTION " + procedureName + "(";
                sql += params;
                sql += ") returns integer AS $BODY$ declare result integer; " + "begin ";
                return sql;
            case FactoryConnectionDB.MYSQL:
                sql += "\n";
                sql += "DELIMITER  // CREATE OR REPLACE PROCEDURE " + procedureName + "(";
                sql += params;
                sql += ")  begin ";
                return sql;
            default:
                return sql;
        }

    }

    public static String footerProcedure(String procedureName, int database) {
        String sql = "";
        switch (database) {
            case FactoryConnectionDB.POSGRESQL:
                sql += " return result; end $BODY$ LANGUAGE plpgsql VOLATILE COST 100;";
                return sql;
            case FactoryConnectionDB.MYSQL:
                sql += " END //\n"
                        + "DELIMITER ;";
                return sql;
            default:
                return sql;
        }

    }

}
