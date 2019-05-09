/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.query;

import com.alphateam.connection.Connection;
import com.alphateam.connection.Factory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JAIR
 */
public class DAO {

    Connection conn;
    int database = Factory.getDefaultDatabase();

    public List<Column> getForeignKeys(String table, String column) {
        List<Column> x = new ArrayList<>();
        String sql = Query.getTableColumnsForeignProperties(database);
        sql +="  and TABLE_NAME ='"+table+"' AND column_name ='"+column+"'";
        try {
            this.conn = Factory.open(database);
            ResultSet rs = this.conn.query(sql);
            while (rs.next()) {
                Column rpta = new Column();
                //rpta.setName(rs.getString(1));
                rpta.setName(rs.getString(2));
                rpta.setForeignTable(rs.getString(3));
                rpta.setForeignColumn(rs.getString(4));
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

    public Boolean getPrimaryKeys(String table, String column) {
       // List<Table> x = new ArrayList<>();
        boolean x= false;
        String sql = Query.getTableColumnsPrimaryKeyProperties(database);
        sql += " and TABLE_NAME ='"+table+"' AND column_name ='"+column+"'";
        try {
            this.conn = Factory.open(database);
            ResultSet rs = this.conn.query(sql);

                x = rs.next();
                //Table rpta = new Table();
               // rpta.setConstraintName(rs.getString("constraintname"));
                //rpta.setName(rs.getString("tablename"));
                //rpta.getColumn().setName(rs.getString("columnname"));
               // x.add(rpta);

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

    public List<Column> getColumsProperties(String tableName ) {
        List<Column> x = new ArrayList<>();
        String sql = Query.getTableXColumsProperties(database);
        try {
            this.conn = Factory.open(database);
            sql += " and table_name ='"+tableName+"'";
            ResultSet rs = this.conn.query(sql);
            while (rs.next()) {
               // Table table = new Table();
                Column c  = new Column();
               // c.setName(rs.getString(1));/*table name*/
                c.setName(rs.getString(2));
                c.setDataType(rs.getString(3));
                c.setAttributeNumber(rs.getString(4));
                x.add(c);
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

    public List<Table> getWithColumnsNumber() {
        String sql =  Query.getSQLTableXNumColums(database);
        List<Table> x = new ArrayList<>();
        try {
            this.conn = Factory.open(database);
            System.out.println("tipo bd:" + database);
            ResultSet rs = this.conn.query(sql);

            while (rs.next()) {
                Table table = new Table();
                table.setName(rs.getString(1));
                table.setNumColumns(rs.getString(2));
                x.add(table);
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
