/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.template;

import com.alphateam.connection.Connection;
import java.util.List;

import com.alphateam.query.Column;
import com.alphateam.query.DAO;
import com.alphateam.query.Table;

/**
 *
 * @author JAIR
 */
public abstract class Core {

    protected Connection conn;
    protected int database;
    protected boolean returnId;
    protected DAO dao;
    public String content = "";

    /*todo: refactor this*/
    public String pkParameters ="";
    public String pkVariables="";
    public String pkDecrypt="";
    public String pkinput="";
    public String pkMethVarInput ="";
    public String pkPathVarInput ="";
    public String pkParams ="";
    public String pkParamsRequest ="";
    public String pkSetter ="";
    public String pkMapVarInput ="";

    protected List<Table> tables ,listPrimaryKey, listForeignKey;
    protected List<Column> columns;
    protected abstract void init();
}
