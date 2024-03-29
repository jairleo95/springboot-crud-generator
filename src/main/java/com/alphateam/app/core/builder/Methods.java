package com.alphateam.app.core.builder;

import com.alphateam.app.bean.Column;
import com.alphateam.app.bean.Table;

import java.util.List;

/**
 *
 * @author JAIR
 */
interface Methods {
    void build();
    Table processTable(Table table);
    void column(Column column);
    void foreignKeys(Table table, Column fk);
    void primaryKeys(Table table, Column pk);

    void buildParameters(Table table, Column column);
    void buildMethods(Table tnc);
    void resetValues();
    void generateProject(String path, String filename);
}
