/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.jdbc;

import ru.spb.awk.driver.for1c.parcer.SelectBuilder;
import java.sql.SQLDataException;
import java.sql.SQLException;
import ru.spb.awk.driver.for1c.parcer.cc.SQL.ParseException;
import ru.spb.awk.driver.for1c.parcer.cc.SQL.SQLParser;

/**
 *
 * @author Василий Казьмин
 */
class DataSource1C {

    private final Connection1C connection;
    private Select cursor;
 
    DataSource1C(Connection1C connection, String sql) throws SQLException, ParseException {
        this.connection = connection;
        parse(sql, 1);
        prepare();
    }

    private void parse(String sql, int line) throws SQLException, ParseException {
        String trimSQL = sql.trim().toUpperCase();
        if (trimSQL.startsWith("SELECT")) {
            SelectBuilder builder = new SelectBuilder(connection.getHelper());
            SQLParser parser = SQLParser.makeParser(sql, builder);
            parser.Input();
            cursor = builder.create();
            
        } else if (trimSQL.startsWith("INSERT")) {
            parseInsert(trimSQL.substring(7));
        } else if (trimSQL.startsWith("DELETE")) {
            parseDelete(trimSQL.substring(7));
        } else if (trimSQL.startsWith("UPDATE")) {
            parseUpdate(trimSQL.substring(7));
        } else {
            throw new SQLException("Unknown command in line " + line + ".");
        }
    }

    private void parseInsert(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void parseDelete(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void parseUpdate(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void prepare() throws SQLException {

    }

    boolean first() throws SQLException {
        return cursor.first();
    }

    public int findColumn(String columnLabel) throws SQLException {
        return cursor.findColumn(columnLabel);
    }

    boolean next() throws SQLException {
        return cursor.next();
    }

    boolean isAfterLast() {
       return cursor.isAfterLast();
    }

    boolean isBeforeFirst() {
        return cursor.isBeforeFirst();
    }

    String getString(int columnIndex) throws SQLDataException {
        return cursor.getString(columnIndex);
    }
}
