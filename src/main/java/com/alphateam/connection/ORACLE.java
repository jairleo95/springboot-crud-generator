package com.alphateam.connection;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Docente
 */
public final class ORACLE extends Connection {

    ORACLE(String[] parametro) {
        this.parametro = parametro;
        this.open();
    }

    @Override
    java.sql.Connection open() {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:" + this.parametro[0] + "/" + this.parametro[1] + "@" + this.parametro[2] + ":" + this.parametro[3] + ":" + this.parametro[4];
        try {
            Class.forName(driver).newInstance();
            this.conex = DriverManager.getConnection(url);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            System.out.println("Error al conectar : "+e);
            e.printStackTrace();
        }
        return this.conex;
    }
}
