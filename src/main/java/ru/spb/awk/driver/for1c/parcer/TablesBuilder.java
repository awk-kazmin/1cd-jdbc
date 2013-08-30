/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.parcer;

import ru.spb.awk.driver.for1c.jdbc.TableBeen;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Василий Казьмин
 */
public class TablesBuilder extends AbstractBuilder<List<TableBeen>> {
    List<TableBeen> tables = new ArrayList<>();

    @Override
    public List<TableBeen> create() throws SQLSyntaxErrorException {
        return tables;
    }

    @Override
    public void appendName(String source, String name, String alias) throws SQLSyntaxErrorException {
        tables.add(new TableBeen(name, alias));
    }
    
    
    
}
