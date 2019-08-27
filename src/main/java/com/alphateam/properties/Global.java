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

    public static final String DEFAULT_PROJECT_LOCATION = "D:\\Software Development\\crud-generator-project\\generated-projects\\DSP\\";
    public static final String MAPPER_LOCATION = "org\\proyecto\\mapper\\";
    public static final String SERVICE_LOCATION = "org\\proyecto\\services\\";
    public static final String BEAN_LOCATION = "org\\proyecto\\bean\\" ;
    public static final String VIEW_LOCATION = "org\\proyecto\\views\\" ;
    public static final String MAPPER_XML_LOCATION = "org\\proyecto\\resources\\xml\\mapper\\" ;
    public static final String CONTROLLER_LOCATION = "org\\proyecto\\controller\\";
    public static final String SQL_SCRIPT_LOCATION = "\\sql\\";

    /*change when update server (for cache conflicts)*/
    public static String VERSION_CSS = "2.0.3";
    public static String VERSION_JS = "2.3.1";

    /*oracle bd connection */
    public static final String HOSTNAME = "DESKTOP-MS5NN2M";
    public static final String USER = "SDSPMAIN001";
    public static final String USER_PWD = "SDSPMAIN001";
    public static final String PORT = "1522";
    public static final String SID = "orcl";
    public static final int MAX_CHARACTER_IDENTIFIER= 30;

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