/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.template;

import com.alphateam.connection.Connection;
import java.util.List;
import java.util.Map;

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
    protected List<Table> tables /*table*/, columns/*column*/,listPrimaryKey, listForeignKey;
    protected abstract void init();
}
