/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Василий Казьмин
 */
public abstract class AbstractResultSet implements ResultSet {
    private boolean closed = false;
    private int cursor = -1;

    List data = new ArrayList<>();

    @Override
    public void afterLast() throws SQLException {
        cursor = data.size();
    }

    @Override
    public void beforeFirst() throws SQLException {
        cursor = -1;
    }

    @Override
    public boolean first() throws SQLException {
        cursor = 0;
        return true;
    }

    @Override
    public int getRow() throws SQLException {
        return cursor + 1;
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        return cursor == data.size();
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        return cursor < 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public boolean isFirst() throws SQLException {
        return cursor == 0;
    }

    @Override
    public boolean isLast() throws SQLException {
        return cursor == data.size() - 1;
    }

    @Override
    public boolean last() throws SQLException {
        cursor = data.size() - 1;
        return cursor == data.size() - 1;
    }

    @Override
    public boolean next() throws SQLException {
        cursor++;
        return cursor < data.size();
    }

    @Override
    public boolean previous() throws SQLException {
        cursor --;
        return cursor >=0;
    }

    @Override
    public void close() throws SQLException {
        closed = true;
    }
    
    public Object get() throws SQLException {
        validateGetData();
        return data.get(cursor);
    }

    public void add(Object record) {
        data.add(record);
    }
    
    private void validateGetData() throws SQLException {
        if(isAfterLast() || isBeforeFirst() || isClosed()) {
            throw new SQLException("Not valid position");
        }
    }
}
