/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Docente
 */
public final class MYSQLConnectionDB extends ConexionBD {

    public MYSQLConnectionDB(String[] parametro) {
        this.parametro = parametro;
        this.open();
    }

    @Override
    public Connection open() {
        try {
            String url = "jdbc:mysql://" + this.parametro[0] + "/" + this.parametro[1] + "?connectTimeout=3000";
            Class.forName("com.mysql.jdbc.Driver");
            this.conex = DriverManager.getConnection(url, this.parametro[2], this.parametro[3]);
        } catch (ClassNotFoundException e) {
            System.out.println("SQLException: " + e.getMessage());
        } catch (SQLException e) {
            // handle any errors
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return this.conex;
    }

}
