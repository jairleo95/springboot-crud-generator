/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.pattern.simplemvc;

import com.alphateam.connection.Connection;
import com.alphateam.connection.Factory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author JAIR
 */
public class Controller {

    Connection conn;

    public Boolean build() throws SQLException {
        ResultSet rs2 = null;
        ResultSet cnt2 = null;
        ResultSet cnt1 = null;
        ResultSet rs = null;
        conn = Factory.open(Factory.ORACLE);
        rs2 = this.conn.query("select table_name, column_name,data_type,data_length,column_id from user_tab_columns order by table_name,column_id");

        cnt1 = this.conn.query("select count(*) from (select table_name ,count(column_name)from user_tab_columns group by table_name order by table_name)");

        cnt2 = this.conn.query("select  count(*) from (select table_name, column_name,data_type,data_length,column_id from user_tab_columns order by table_name,column_id)");

        cnt1.next();

        cnt2.next();
        rs = conn.query("select table_name ,count(column_name)from user_tab_columns group by table_name order by table_name");

        String List1[][] = new String[cnt1.getInt(1)][2];
        String List2[][] = new String[cnt2.getInt(1)][5];
        int i = 0;

        while (rs.next()) {

            List1[i][0] = rs.getString(1);
            List1[i][1] = rs.getString(2);
            i++;
        }

        int j = 0;

        while (rs2.next()) {

            List2[j][0] = rs2.getString(1);
            List2[j][1] = rs2.getString(2);
            List2[j][2] = rs2.getString(3);
            List2[j][3] = rs2.getString(4);
            List2[j][4] = rs2.getString(5);
            j++;
        }

        //     int t1 = cnt1.getInt(1);
        //   int t2 = cnt2.getInt(1);
        for (String[] List11 : List1) {
            System.out.println("//TABLA : " + List11[0] + " ");
            System.out.println("if (opc.equals(\"Registrar\")) {");
            for (String[] List21 : List2) {
                if (List11[0].equals(List21[0])) {
                    if (List21[2].equals("NUMBER")) {
                        System.out.println("Double " + List21[1] + " = Double.parseDouble(request.getParameter(\"" + List21[1] + "\"));");
                    } else {
                        System.out.println("String " + List21[1] + " = request.getParameter(\"" + List21[1] + "\");");
                    }
                }
            }
            System.out.println("a.INSERT" + List11[0].substring(4).toUpperCase() + "(");
            for (int v = 1; v < List2.length; v++) {
                if (List11[0].equals(List2[v][0])) {
                    System.out.print(List2[v][1]);
                    if (List2[v][4].equals(List11[1]) == false) {
                        System.out.print(",");
                    } else {
                        System.out.print(");");
                    }
                }
            }
            System.out.print("RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(\"\");");
            System.out.print("dispatcher.forward(request, response);}");
        }
        return true;
    }
}
