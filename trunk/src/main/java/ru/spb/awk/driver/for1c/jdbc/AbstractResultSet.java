/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import ru.spb.awk.driver.for1c.core.ResultMap;

/**
 *
 * @author Василий Казьмин
 */
public abstract class AbstractResultSet<T extends ResultMap<String, ?>> implements ResultSet {
    private final Cursor<T> c;

    public AbstractResultSet(T[] list) {
        c = new Cursor<>(list);
    }
    
    

    @Override
    public void afterLast() throws SQLException {
        c.isAfterLast();
    }

    @Override
    public void beforeFirst() throws SQLException {
        c.beforeFirst();
    }

    @Override
    public boolean first() throws SQLException {
        return c.first();
    }

    @Override
    public int getRow() throws SQLException {
        return c.getRow();
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        return c.isAfterLast();
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        return c.isBeforeFirst();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return c.isClosed();
    }

    @Override
    public boolean isFirst() throws SQLException {
        return c.isFirst();
    }

    @Override
    public boolean isLast() throws SQLException {
        return c.isLast();
    }

    @Override
    public boolean last() throws SQLException {
        return c.last();
    }

    @Override
    public boolean next() throws SQLException {
        return c.next();
    }

    @Override
    public boolean previous() throws SQLException {
        return c.previous();
    }

    @Override
    public void close() throws SQLException {
        c.close();
    }

    protected Cursor<T> getCursor() {
        return c;
    }
}
