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

    //public static final String DEFAULT_PROJECT_LOCATION = "D:\\Software Development\\crud-generator-project\\generated-projects\\gth-crud\\";

    public static final String SRC_LOCATION = "\\src\\main\\java\\";
    public static final String PACKAGE_NAME2 = "com\\app\\";
    public static final String PROJECT_NAME_LOCATION = SRC_LOCATION + PACKAGE_NAME2;
    public static final String PACKAGE_NAME = "com.app";



    public static final String MAPPER_LOCATION = PROJECT_NAME_LOCATION + "mapper\\";
    public static final String SERVICE_LOCATION = PROJECT_NAME_LOCATION + "services\\";
    public static final String BEAN_LOCATION = PROJECT_NAME_LOCATION + "bean\\" ;

    public static final String SPLIT_CRITERIA = "_" ;


    public static final String RESOURCES_LOCATION = "\\src\\main\\" + "resources\\";
    public static final String MAPPER_XML_LOCATION = RESOURCES_LOCATION+PACKAGE_NAME2+"\\mapper\\" ;
    public static final String VIEW_LOCATION = RESOURCES_LOCATION+ "static\\views\\" ;
    public static final String JS_SCRIPT_LOCATION = RESOURCES_LOCATION+ "static\\js\\business-logic\\" ;

    public static final String CONTROLLER_LOCATION = PROJECT_NAME_LOCATION + "controller\\";
    public static final String SQL_SCRIPT_LOCATION = "\\sql\\";


    public static final int MAX_CHARACTER_IDENTIFIER= 30;

}