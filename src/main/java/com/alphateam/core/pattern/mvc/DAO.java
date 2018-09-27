/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.core.pattern.mvc;

import com.alphateam.connection.Connection;
import com.alphateam.connection.Factory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author JAIR
 */
public class DAO {

    Connection conn;

    public Boolean makeDAOCrudGenerator() throws SQLException {

        ResultSet rs2 = null;
        ResultSet cnt2 = null;
        ResultSet cnt1 = null;
        ResultSet rs = null;
        conn = Factory.open(Factory.ORACLE);

        rs2 = conn.query("select table_name, column_name,data_type,data_length,column_id from user_tab_columns order by table_name,column_id");

        cnt1 = conn.query("select count(*) from (select table_name ,count(column_name)from user_tab_columns group by table_name order by table_name)");

        cnt2 = conn.query("select  count(*) from (select table_name, column_name,data_type,data_length,column_id from user_tab_columns order by table_name,column_id)");
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

        int t1 = cnt1.getInt(1);
        int t2 = cnt2.getInt(1);

        for (String[] List11 : List1) {
            System.out.println("@Override");
            System.out.println(" public String INSERT" + List11[0].substring(4).toUpperCase() + " (");
            for (int s = 0; s < List2.length; s++) {
                if (List11[0].equals(List2[s][0])) {
                    if (List2[s][2].equals("NUMBER")) {
                        System.out.println(" double " + List2[s][1]);
                    } else {
                        System.out.println(" String " + List2[s][1]);
                    }
                    if (List2[s][4].equals(List11[1]) == false) {
                        System.out.print(",");
                    } else {
                        System.out.print("){ ");
                    }
                }
            }
            System.out.print("  String id = \"\";  try { this.conn = Factory.open(Factory.ORACLE);CallableStatement cst = this.conn.conex.prepareCall(\"{CALL RHSP_INSERT_" + List11[0] + "(");
            for (int v = 1; v < List2.length; v++) {
                if (List11[0].equals(List2[v][0])) {
                    System.out.print("?");
                    if (List2[v][4].equals(List11[1]) == false) {
                        System.out.print(",");
                    } else {
                        System.out.print(",? )} \" );");
                    }
                }
            }
            int l = 0;
            for (int h = 1; h < List2.length; h++) {
                if (List11[0].equals(List2[h][0])) {
                    l++;
                    System.out.println("cst.setString(" + l + "," + List2[h][1] + "); ");
                    if (List2[h][4].equals(List11[1]) == false) {
                    } else {
                        System.out.println("cst.registerOutParameter(" + (l + 1) + ", Types.CHAR); ");
                        System.out.print(" cst.execute(); id = cst.getString(" + (l + 1) + ");");
                        System.out.print(" } catch (SQLException e) {throw new RuntimeException(e.getMessage());} catch (Exception e) {throw new RuntimeException(\"ERROR :\" + e.getMessage());} finally {try {this.conn.close();} catch (Exception e) {throw new RuntimeException(e.getMessage());}}return id;}");
                    }
                }
            }
        }
        return true;
    }
}
