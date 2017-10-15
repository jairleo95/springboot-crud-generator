/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

import conexionDB.ConexionBD;
import conexionDB.FactoryConnectionDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author JAIR
 */
public class DAO {

    ConexionBD conn;

    public List<Map<String, String>> listForeignKey(int bd, String sql) {
        List<Map<String, String>> x = new ArrayList<Map<String, String>>();
        try {
            this.conn = FactoryConnectionDB.open(bd);
            // this.conn = FactoryConnectionDB.open(FactoryConnectionDB.POSGRESQL);
            ResultSet rsForeignKey = this.conn.query(sql);
            while (rsForeignKey.next()) {
                Map<String, String> rpta = new HashMap<String, String>();
                rpta.put("TableName", rsForeignKey.getString(1));
                rpta.put("ColumnName", rsForeignKey.getString(2));
                rpta.put("ForeignTable", rsForeignKey.getString(3));
                rpta.put("ForeignColumn", rsForeignKey.getString(4));
                x.add(rpta);
            }
            rsForeignKey.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("ERROR");
        } finally {
            try {
                this.conn.close();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return x;
    }

    public List<Map<String, String>> listPrimaryKey(int bd, String sql) {
        List<Map<String, String>> x = new ArrayList<Map<String, String>>();
        try {
            this.conn = FactoryConnectionDB.open(bd);
            ResultSet rsForeignKey = this.conn.query(sql);
            while (rsForeignKey.next()) {
                Map<String, String> rpta = new HashMap<String, String>();
                rpta.put("ConstraintName", rsForeignKey.getString("constraintname"));
                rpta.put("TableName", rsForeignKey.getString("tablename"));
                rpta.put("ColumnName", rsForeignKey.getString("columnname"));
                x.add(rpta);
            }
            rsForeignKey.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("ERROR");
        } finally {
            try {
                this.conn.close();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return x;
    }

    public List<Map<String, String>> listTableXColumsP(int bd, String sql) {
        List<Map<String, String>> x = new ArrayList<Map<String, String>>();
        try {
            this.conn = FactoryConnectionDB.open(bd);
            ResultSet rs = this.conn.query(sql);

            while (rs.next()) {
                Map<String, String> rpta = new HashMap<String, String>();
                rpta.put("TableName", rs.getString(1));
                rpta.put("ColumnName", rs.getString(2));
                rpta.put("DataType", rs.getString(3));
                rpta.put("NumAtributo", rs.getString(4));
                x.add(rpta);
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("ERROR");
        } finally {
            try {
                this.conn.close();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return x;
    }

    public List<Map<String, String>> listTableXNumColumns(int bd, String sql) {
        List<Map<String, String>> x = new ArrayList<Map<String, String>>();
        try {
            this.conn = FactoryConnectionDB.open(bd);
            System.out.println("tipo bd:" + bd);
            ResultSet rs = this.conn.query(sql);

            while (rs.next()) {
                Map<String, String> rpta = new HashMap<String, String>();
                rpta.put("TableName", rs.getString(1));
                rpta.put("NumColumns", rs.getString(2));
                x.add(rpta);
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("ERROR");
        } finally {
            try {
                this.conn.close();
            } catch (Exception e) {
                System.out.println(e);
                throw new RuntimeException(e.getMessage());
            }
        }
        return x;
    }
}
