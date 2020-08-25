/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.app.core.builder;

import com.alphateam.app.base.ApplicationClass;

/**
 *
 * @author JAIR
 */
public abstract class Core {

    protected ApplicationClass app;
    //protected Connection conn;
    protected int database;
    protected boolean returnId;
    //protected DAO dao;

    public String content = "";

    //protected List<Column> columns;
    protected abstract void init();
}
