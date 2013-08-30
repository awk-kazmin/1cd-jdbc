/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author Василий Казьмин
 */
public class Driver1C implements Driver {
    
    public static final String PREFIX = "jdbc:1c:";

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return createConnection(url, info);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return isValid(url);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return Config1C.getDriverPropertyInfo();
    }

    @Override
    public int getMajorVersion() {
        return Config1C.MAJOR_VERSION;
    }

    @Override
    public int getMinorVersion() {
        return Config1C.MINOR_VERSION;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Config1C.getLogger();
    }
    /**
     * Validates a URL
     * @param url
     * @return true if the URL is valid, false otherwise
     */
    private boolean isValid(String url) {
        return url != null && url.toLowerCase().startsWith(PREFIX);
    }
    /**
     * Gets the location to the database from a given URL.
     * @param url The URL to extract the location from.
     * @return The location to the database.
     */
    private String extractAddress(String url) {
        // if no file name is given use a memory database
        return url.substring(PREFIX.length());
    }
    /**
     * Creates a new database connection to a given URL.
     * @param url the URL
     * @param prop the properties
     * @return a Connection object that represents a connection to the URL
     * @throws SQLException
     * @see java.sql.Driver#connect(java.lang.String, java.util.Properties)
     */
    private Connection createConnection(String url, Properties info) throws SQLException {
        if (!isValid(url))
            throw new SQLException("invalid database address: " + url);

        url = url.trim();
        return new Connection1C(url, extractAddress(url), info);
    }
    
}
