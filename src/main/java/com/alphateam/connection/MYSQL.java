/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Docente
 */
public final class MYSQL extends Connection {

    private final Logger log = LogManager.getLogger(getClass().getName());
    public MYSQL(String[] parametro) {
        this.parametro = parametro;
        this.open();
    }

    @Override
    public java.sql.Connection open() {
        try {
            String url = "jdbc:mysql://" + this.parametro[0] + "/" + this.parametro[1] + "?connectTimeout=3000";
            Class.forName("com.mysql.jdbc.Driver");
            this.conex = DriverManager.getConnection(url, this.parametro[2], this.parametro[3]);
        } catch (ClassNotFoundException e) {
            log.error("SQLException: " + e.getMessage());
        } catch (SQLException e) {
            // handle any errors
            log.error("SQLException: " + e.getMessage());
            log.error("SQLState: " + e.getSQLState());
            log.error("VendorError: " + e.getErrorCode());
        }
        return this.conex;
    }

}
