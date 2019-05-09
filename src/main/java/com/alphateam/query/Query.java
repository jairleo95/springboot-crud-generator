/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.query;

import com.alphateam.connection.Factory;

/**
 *
 * @author Jairleo95
 */
public class Query {

    /*query for posgresql*/
    public static final String tableXNumColumsPOSGRESQL = "select a.relname,count(b.attname)from pg_catalog.pg_stat_user_tables  a, pg_attribute b, pg_type c where b.attrelid = a.relid and b.attnum >0 and b.atttypid = c.oid group by  a.relname";
    /*tabla,columna,typo de dato, null,num attributo*/
    public static final String tableXColumsPropertiesPOSGRESQL = "select  a.relname,b.attname,c.typname,null,b.attnum from pg_catalog.pg_stat_user_tables  a, pg_attribute b, pg_type c where b.attrelid = a.relid and b.attnum >0 and b.atttypid = c.oid";
    public static final String numTablesPOSGRESQL = " select count(*) from pg_catalog.pg_stat_user_tables";
    public static final String numAllColumnsPOSGRESQL = "select count(*) from pg_catalog.pg_stat_user_tables  a, pg_attribute b, pg_type c where b.attrelid = a.relid and b.attnum >0 and b.atttypid = c.oid";
    public static final String tableColumnsForeignPropertiesPOSGRESQL = " SELECT (A.relname) AS tabla, (C.attname) AS columna, (B.relname) AS tabla_foranea, (D.attname) AS columna_foranea FROM pg_catalog.pg_constraint, pg_catalog.pg_class AS A, pg_catalog.pg_class AS B, pg_catalog.pg_attribute C, pg_catalog.pg_attribute D WHERE contype = 'f' AND conrelid = A.oid  AND confrelid = B.oid AND conrelid = C.attrelid AND confrelid = D.attrelid AND C.attnum = pg_catalog.pg_constraint.conkey[1] AND D.attnum = pg_catalog.pg_constraint.confkey[1] ORDER BY UPPER(A.relname), UPPER(B.relname), UPPER(C.attname), UPPER(D.attname)";
    public static final String tableColumnsPrimaryKeyPropertiesPOSGRESQL = "SELECT (conname) AS restriccion, (relname) AS tabla, (pg_catalog.pg_attribute.attname) AS columna FROM pg_catalog.pg_constraint, pg_catalog.pg_class, pg_catalog.pg_attribute"
            + " WHERE contype = 'p' AND conrelid = pg_catalog.pg_class.oid AND conrelid = pg_catalog.pg_attribute.attrelid AND pg_catalog.pg_attribute.attnum = pg_catalog.pg_constraint.conkey[1]"
            + " ORDER BY UPPER(conname), UPPER(relname), UPPER(pg_catalog.pg_attribute.attname)";

    /*MYSQL*/
    public static final String allTablesMysql = "SELECT TABLE_NAME, 0 FROM INFORMATION_SCHEMA.table WHERE TABLE_SCHEMA='alpha_analytics'";
    public static final String tableXColumsPropertiesMysql = "SELECT table_name,column_name,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH,ORDINAL_POSITION FROM INFORMATION_SCHEMA.COLUMNS where TABLE_SCHEMA='alpha_analytics' ORDER by TABLE_NAME ,ORDINAL_POSITION";

    public static final String tableColumnsForeignPropertiesMYSQL = "SELECT TABLE_NAME as tabla, column_name as columna, referenced_table_name as tabla_foranea, referenced_column_name as columna_foranea FROM information_schema.key_column_usage WHERE referenced_table_name IS NOT NULL and constraint_schema='alpha_analytics'";

    public static final String tableColumnsPrimaryKeyPropertiesMYSQL = "SELECT TABLE_NAME as tablename, column_name as columnname, referenced_table_name as constraintname, referenced_column_name as columna_foranea FROM information_schema.key_column_usage where CONSTRAINT_name='PRIMARY' AND CONSTRAINT_SCHEMA='alpha_analytics'";

    public static String getSQLTableXNumColums(int database) {
        String sql = "";
        switch (database) {
            case Factory.MYSQL:
                sql = allTablesMysql;
                return sql;
            case Factory.POSGRESQL:
                sql = tableXNumColumsPOSGRESQL;
                return sql;
            default:
                return "";
        }

    }

    public static String getTableXColumsProperties(int database) {
        String sql = "";
        switch (database) {
            case Factory.MYSQL:
                sql = tableXColumsPropertiesMysql;
                return sql;
            case Factory.POSGRESQL:
                sql = tableXColumsPropertiesPOSGRESQL;
                return sql;
            default:
                return sql;
        }
    }

    public static String getTableColumnsForeignProperties(int database) {
        String sql = "";
        switch (database) {
            case Factory.MYSQL:
                sql = tableColumnsForeignPropertiesMYSQL;
                return sql;
            case Factory.POSGRESQL:
                sql = tableColumnsForeignPropertiesPOSGRESQL;
                return sql;
            default:
                return sql;
        }
    }
    public static String getTableColumnsPrimaryKeyProperties(int database) {
        String sql = "";
        switch (database) {
            case Factory.MYSQL:
                sql = tableColumnsPrimaryKeyPropertiesMYSQL;
                return sql;
            case Factory.POSGRESQL:
                sql = tableColumnsPrimaryKeyPropertiesPOSGRESQL;
                return sql;
            default:
                return sql;
        }
    }
}
