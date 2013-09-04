/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ru.spb.awk.driver.for1c.core.ResultMap;

/**
 *
 * @author Василий Казьмин
 */
public class Cursor<T extends ResultMap<String, ?>> {
    private boolean closed = false;
    private int cursor = -1;

    T[] data;
    
    public Cursor(T[] list) {
        data = list;
    }

    public void afterLast() throws SQLException {
        cursor = data.length;
    }

    public void beforeFirst() throws SQLException {
        cursor = -1;
    }

    public boolean first() throws SQLException {
        cursor = 0;
        return true;
    }

    public int getRow() throws SQLException {
        return cursor + 1;
    }

    public boolean isAfterLast() throws SQLException {
        return cursor == data.length;
    }

    public boolean isBeforeFirst() throws SQLException {
        return cursor < 0;
    }

    public boolean isClosed() throws SQLException {
        return closed;
    }

    public boolean isFirst() throws SQLException {
        return cursor == 0;
    }

    public boolean isLast() throws SQLException {
        return cursor == data.length - 1;
    }

    public boolean last() throws SQLException {
        cursor = data.length - 1;
        return cursor == data.length - 1;
    }

    public boolean next() throws SQLException {
        cursor++;
        return cursor < data.length;
    }

    public boolean previous() throws SQLException {
        cursor --;
        return cursor >=0;
    }

    public void close() throws SQLException {
        closed = true;
    }
    
    public T get() throws SQLException {
        validateGetData();
        return data[cursor];
    }

    public void add(T record) {
        data = Arrays.copyOf(data, data.length+1);
        data[data.length - 1] = record;
    }
    
    private void validateGetData() throws SQLException {
        if(isAfterLast() || isBeforeFirst() || isClosed()) {
            throw new SQLException("Not valid position");
        }
    }
}
