/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.properties;

/**
 *
 * @author ALPHA
 */
public class Global {

    public static final String DEFAULT_PROJECT_LOCATION = "E:\\AlphaProjects\\AlphaAnalytics\\";

    /*change when update server (for cache conflicts)*/
    public static String VERSION_CSS = "2.0.3";
    public static String VERSION_JS = "2.3.1";

    /*oracle bd connection */
    public static final String HOSTNAME = "192.168.1.63";
    public static final String USER = "SDSPMAIN001";
    public static final String USER_PWD = "SDSPMAIN001";
    public static final String PORT = "1521";
    public static final String SID = "orcl";

    /*Mysql*/
    public static final String HOSTNAME_MYSQL = "localhost";
    public static final String DATABASE_MYSQL = "alpha_analytics";
    public static final String USER_MYSQL = "root";
    public static final String PWD_MYSQL = "";
    /*posgresql*/
    public static final String HOSTNAME_PS = "localhost";
    public static final String DATABASE_PS = "venta";
    public static final String USER_PS = "postgres";
    public static final String PWD_PS = "admin";
}
