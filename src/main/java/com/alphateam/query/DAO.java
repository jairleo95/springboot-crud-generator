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

    public Boolean getForeignKeys(String table, String column) {
        Boolean x;
        String sql = Query.getTableColumnsForeignProperties(database);
        sql = sql.replace("{schema}",Factory.getSchema());
        sql +="  and TABLE_NAME ='"+table+"' AND column_name ='"+column+"'";
        try {
            this.conn = Factory.open(database);
            ResultSet rs = this.conn.query(sql);
            x = rs.next();
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
        boolean x;
        String sql = Query.getTableColumnsPrimaryKeyProperties(database);
        sql = sql.replace("{schema}",Factory.getSchema());
        sql += " and TABLE_NAME ='"+table+"' AND column_name ='"+column+"'";
        try {
            this.conn = Factory.open(database);
            ResultSet rs = this.conn.query(sql);
                x = rs.next();
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
            sql = sql.replace("{schema}",Factory.getSchema());
            sql = sql.replace("{tableName}",tableName);
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
    //ready
    public List<Table> getWithColumnsNumber() {
        String sql =  Query.getSQLTableXNumColums(database);
        List<Table> x = new ArrayList<>();
        try {
            this.conn = Factory.open(database);
            System.out.println("tipo bd:" + database);
            sql = sql.replace("{schema}",Factory.getSchema());
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
