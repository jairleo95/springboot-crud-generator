package com.alphateam;

import com.alphateam.connection.Connection;
import com.alphateam.connection.Factory;

import java.sql.ResultSet;

/**
 * Created by JairL on 6/3/2019.
 */
public class TestConnection {

    public static void main(String args[]) throws Exception {
        int database = Factory.ORACLE;
        Connection conn = Factory.open(database);

        ResultSet rs = conn.query("Select 'Hello!' from dual");

        if (rs.next()){
            System.out.print(rs.getString(1));

        }
        rs.close();
    }
}