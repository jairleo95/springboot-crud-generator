/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexionDB;

import properties.globalProperties;

/**
 *
 * @author Docente
 */
public class FactoryConnectionDB {

    public static final int MYSQL = 1;
    public static final int ORACLE = 2;
    public static final int POSGRESQL = 3;

    public static String[] configMYSQL = {"localhost", "alphabi", "root", ""};

    public static String[] configORACLE = {globalProperties.USER, globalProperties.USER_PWD, globalProperties.HOSTNAME, globalProperties.PORT, globalProperties.SID};

    public static String[] configPOSGRESQL = {"localhost", "venta", "postgres", "admin"};

    public static int getDefaultDatabase() {
        return MYSQL;
    }

    public static ConexionBD open(int typeDB) {
        switch (typeDB) {
            case FactoryConnectionDB.MYSQL:
                return new MYSQLConnectionDB(configMYSQL);

            case FactoryConnectionDB.ORACLE:
                return new ORACLEConnectionDB(configORACLE);

            case FactoryConnectionDB.POSGRESQL:
                return new POSGRESQLConnectionDB(configPOSGRESQL);
            default:
                return null;
        }
    }

}
