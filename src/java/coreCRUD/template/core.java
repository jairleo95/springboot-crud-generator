/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coreCRUD.template;

import conexionDB.ConexionBD;
import java.util.List;
import java.util.Map;
import query.DAO;

/**
 *
 * @author JAIR
 */
public abstract class core {

    protected ConexionBD conn;
    protected int database;
    protected boolean returnId;
    protected DAO dao;
    protected List<Map<String, String>> listTableXNumColumns, listTableXColumsP, listForeignKey, listPrimaryKey;
    protected abstract void initValues();
}
