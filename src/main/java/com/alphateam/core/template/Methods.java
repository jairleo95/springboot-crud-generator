package com.alphateam.core.template;

import com.alphateam.query.Column;
import com.alphateam.query.Table;

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
    void buildMethods(Table tnc, List<Column> pks);
    void resetValues();
    void generateProject(String path, String filename);
}
