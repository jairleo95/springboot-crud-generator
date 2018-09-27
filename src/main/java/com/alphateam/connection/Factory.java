/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.connection;

import com.alphateam.properties.Global;

/**
 *
 * @author Docente
 */
public class Factory {

    public static final int MYSQL = 1;
    public static final int ORACLE = 2;
    public static final int POSGRESQL = 3;

    public static String[] configMYSQL = {Global.HOSTNAME_MYSQL, Global.DATABASE_MYSQL, Global.USER_MYSQL, Global.PWD_MYSQL};

    public static String[] configORACLE = {Global.USER, Global.USER_PWD, Global.HOSTNAME, Global.PORT, Global.SID};

    public static String[] configPOSGRESQL = {Global.HOSTNAME_PS, Global.DATABASE_PS, Global.USER_PS, Global.PWD_PS};

    public static int getDefaultDatabase() {
        return MYSQL;
    }

    public static Connection open(int typeDB) {
        switch (typeDB) {
            case Factory.MYSQL:
                return new MYSQL(configMYSQL);

            case Factory.ORACLE:
                return new ORACLE(configORACLE);

            case Factory.POSGRESQL:
                return new POSGRESQL(configPOSGRESQL);
            default:
                return null;
        }
    }

}
