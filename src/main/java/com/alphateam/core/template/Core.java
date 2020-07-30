/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.template;

import com.alphateam.app.base.ApplicationClass;

import java.util.List;

import com.alphateam.query.Column;
import com.alphateam.query.DAO;
import com.alphateam.query.Table;

/**
 *
 * @author JAIR
 */
public abstract class Core {

    protected ApplicationClass app;
    //protected Connection conn;
    protected int database;
    protected boolean returnId;
    protected DAO dao;

    public String content = "";

    protected List<Table> tables;
    protected List<Column> columns;
    protected abstract void init();
}
