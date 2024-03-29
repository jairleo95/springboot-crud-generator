/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.app.core.persistence;

import com.alphateam.app.bean.Column;
import com.alphateam.app.bean.StoreProcedure;
import com.alphateam.app.bean.Table;
import com.alphateam.connection.Connection;
import com.alphateam.connection.Factory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * @author JAIR
 */
public class EntityDAO {

    Connection conn;
    int database = Factory.getDefaultDB();
    private final Logger log = LogManager.getLogger(getClass().getName());

    public Column getForeignKey(String table, String column) {
        Column c  = new Column();
        String sql = Query.getTableColumnsForeignProperties(database);
        sql = sql.replace("{schema}",Factory.getSchema());
        sql = sql.replace("{table}",table);
        sql = sql.replace("{column}",column);
                try {
                    this.conn = Factory.open(database);
                    ResultSet rs = this.conn.query(sql);
                    while (rs.next()) {
                        c.setName(rs.getString(1));
                        c.setForeignTable(rs.getString(3));
                        c.setForeignColumn(rs.getString(4));
                    }
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
        return c;
    }
    public Boolean isForeignKey(String table, String column) {
        Boolean x;
        String sql = Query.getTableColumnsForeignProperties(database);
        sql = sql.replace("{schema}",Factory.getSchema());
        sql = sql.replace("{table}",table);
        sql = sql.replace("{column}",column);
        try {
            this.conn = Factory.open(database);
            ResultSet rs = this.conn.query(sql);
            x = rs.next();
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

    public Boolean isPrimaryKey(String table, String column) {
        boolean x;
        String sql = Query.getTableColumnsPrimaryKeyProperties(database);
        sql = sql.replace("{schema}",Factory.getSchema());
        sql = sql.replace("{table}",table);
        sql = sql.replace("{column}",column);
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

    public Set<Column> getColumsProperties() {
        Set<Column> x = new HashSet<>();
        String sql = Query.getTableXColumsProperties(database);
        try {
            this.conn = Factory.open(database);
            sql = sql.replace("{schema}",Factory.getSchema());
            //sql = sql.replace("{tableName}",tableName);
            ResultSet rs = this.conn.query(sql);
            while (rs.next()) {
                Column c  = new Column();
                c.setTableName(rs.getString(1));/*table name*/
                //todo:refactor
                c.setName(rs.getString(2).replace("#", ""));
                c.setRawName(rs.getString(2).replace("#", ""));
                c.setDataType(rs.getString(3));
                c.setLength(rs.getString(4));
                c.setPrimaryKey(Boolean.parseBoolean(rs.getString("PK_COLUMN")));
                c.setForeignKey(Boolean.parseBoolean(rs.getString("fk_column")));
                c.setForeignTable(rs.getString("r_table_name"));
                c.setForeignColumn(rs.getString("pk_column"));

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
            log.debug("tipo bd:" + database);
            sql = sql.replace("{schema}",Factory.getSchema());
            ResultSet rs = this.conn.query(sql);

            while (rs.next()) {
                Table table = new Table();
                table.setName(rs.getString(1));
                table.setRawName(rs.getString(1));
                table.setNumColumns(Integer.parseInt(rs.getString(2)));
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
                log.error(e);
                throw new RuntimeException(e.getMessage());
            }
        }
        return x;
    }

    public List<StoreProcedure> getStoredProcedures() {
        String sql =  Query.spObjectMappingORACLE;
        List<StoreProcedure> x = new ArrayList<>();
        try {
            this.conn = Factory.open(database);
            ResultSet rs = this.conn.query(sql);

            while (rs.next()) {
                StoreProcedure s = new StoreProcedure();
                s.setObject(rs.getString(1));
                s.setArgument(rs.getString(2));
                s.setDataType(rs.getString(3));
                s.setInout(rs.getString(4));
                s.setPosition(rs.getString(5));
                s.setDataLength(rs.getString("data_length"));
                s.setDataPrecision(rs.getString("data_precision"));
                s.setDataScale(rs.getString("data_scale"));
                x.add(s);
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
                log.error(e);
                throw new RuntimeException(e.getMessage());
            }
        }
        return x;
    }
}
