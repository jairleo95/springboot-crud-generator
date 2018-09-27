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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author JAIR
 */
public class DAO {

    Connection conn;
    int database = Factory.getDefaultDatabase();

    public List<Table> getForeignKeys() {
        List<Table> x = new ArrayList<>();
        String sql = Query.getTableColumnsForeignProperties(database);
        try {
            this.conn = Factory.open(database);
            ResultSet rs = this.conn.query(sql);
            while (rs.next()) {
                Table rpta = new Table();

                rpta.setTableName(rs.getString(1));
                rpta.setColumnName(rs.getString(2));
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

    public List<Table> getPrimaryKeys() {
        List<Table> x = new ArrayList<>();
        String sql = Query.getTableColumnsPrimaryKeyProperties(database);
        try {
            this.conn = Factory.open(database);
            ResultSet rs = this.conn.query(sql);
            while (rs.next()) {
                Table rpta = new Table();
                rpta.setConstraintName(rs.getString("constraintname"));
                rpta.setTableName(rs.getString("tablename"));
                rpta.setColumnName(rs.getString("columnname"));
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

    public List<Table> getColumsProperties() {
        List<Table> x = new ArrayList<>();
        String sql = Query.getTableXColumsProperties(database);
        try {
            this.conn = Factory.open(database);
            ResultSet rs = this.conn.query(sql);

            while (rs.next()) {
                Table table = new Table();
                table.setTableName(rs.getString(1));
                table.setColumnName(rs.getString(2));
                table.setDataType(rs.getString(3));
                table.setAttributeNumber(rs.getString(4));
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
                table.setTableName(rs.getString(1));
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
