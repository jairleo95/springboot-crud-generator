/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexionDB;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Jairleo95
 */
public final class POSGRESQLConnectionDB extends ConexionBD {

    POSGRESQLConnectionDB(String[] parametro) {
        this.parametro = parametro;
        this.open();
    }

    @Override
    Connection open() {
        //Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            this.conex = DriverManager
                    .getConnection("jdbc:postgresql://" + this.parametro[0] + ":5432/" + this.parametro[1], this.parametro[2], this.parametro[3]);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        return  this.conex;
    }

    /* public static void main(String args[]) {
    
   }*/
}
