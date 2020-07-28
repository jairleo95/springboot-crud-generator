/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.connection;

import com.alphateam.app.configurtions.AppConfiguration;
import com.alphateam.app.configurtions.Config;
import com.alphateam.properties.Global;

/**
 *
 * @author Docente
 */
public class Factory {

    public static final int MYSQL = 1;
    public static final int ORACLE = 2;
    public static final int POSGRESQL = 3;



    public static int getDefaultDB() {
        return ORACLE;
    }
    public static String getSchema(){
        return AppConfiguration.instance().getConfig().getUsername();
    }

    public static Connection open(int typeDB) {
        Config c = AppConfiguration.instance().getConfig();

        System.out.println("configuration file json:" +c.toString());

        String[] configMYSQL = {c.getHostname(), c.getDbName(), c.getUsername(), c.getPassword()};

        String[] configORACLE = {c.getUsername(), c.getPassword(), c.getHostname(), c.getPort(), c.getDbName()};

        String[] configPOSGRESQL = {c.getHostname(), c.getDbName(), c.getUsername(), c.getPassword()};

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
