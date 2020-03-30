/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.connection;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Docente
 */
public abstract class Connection {

    protected String[] parametro;
    public java.sql.Connection conex;

    abstract java.sql.Connection open();
    private final Logger log = LogManager.getLogger(getClass().getName());

    public ResultSet query(String sql) {
        log.debug("query :" + sql);
        Statement st;
        ResultSet rs = null;
        try {
            st = conex.createStatement();
            rs = st.executeQuery(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public boolean ejecutar(String sql) {
        Statement st;
        boolean op = true;
        try {
            st = conex.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return op;
    }

    public boolean close() {
        boolean ok = true;
        try {
            conex.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;
    }
}
