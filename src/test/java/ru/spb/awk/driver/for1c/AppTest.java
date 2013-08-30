package ru.spb.awk.driver.for1c;

import ru.spb.awk.driver.for1c.core.FileHelper;
import ru.spb.awk.driver.for1c.been.Table1C;
import ru.spb.awk.driver.for1c.been.Field1C;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.spb.awk.driver.for1c.jdbc.Config1C;
import ru.spb.awk.driver.for1c.jdbc.Driver1C;
import ru.spb.awk.driver.for1c.parcer.cc._1C.ParseException;
import ru.spb.awk.driver.for1c.parcer.cc._1C._1CParser;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {

    private static final String[] FILE = {
        "\\\\192.168.0.11\\! хранилище конфигураций 1с\\ЛСР УТ ОУП до 2013 07 12\\1cv8ddb.1CD"
    };

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    public void test_1_Parser() throws ParseException {
        String tString = 
                "{\"USERS\",0,\n"
                +   "{\"Fields\",\n"
                +       "{\"USERID\",\"B\",0,16,0,\"CS\"},\n"
                +       "{\"NAME\",\"NVC\",0,256,0,\"CI\"},\n"
                +       "{\"PASSWORD\",\"NC\",0,32,0,\"CI\"},\n"
                +       "{\"REMOVED\",\"L\",0,0,0,\"CS\"},\n"
                +       "{\"BINDID\",\"B\",1,16,0,\"CS\"},\n"
                +       "{\"BINDSTRING\",\"NT\",1,0,0,\"CI\"},\n"
                +       "{\"RIGHTS\",\"B\",0,4,0,\"CS\"}\n"
                +   "},\n"
                +   "{\"Indexes\",\n"
                +       "{\"PK\",0,\n"
                +           "{\"USERID\",16}\n"
                +       "},\n"
                +       "{\"NAME\",0,\n"
                +           "{\"NAME\",256}\n"
                +       "},\n"
                +       "{\"BINDID\",0,\n"
                +           "{\"BINDID\",16}\n"
                +       "}\n"
                +   "},\n"
                +   "{\"Recordlock\",\"0\"},\n"
                +   "{\"Files\",10,11,14}\n"
                + "}";
        _1CParser parcer = _1CParser.makeParser(tString);
        Table1C result = parcer.parse();
        assertNotNull(result);
        assertEquals(result.getFields().size(), 7);
    }
    
    /**
     * Rigourous Test :-)
     */
    public void test_2_App() throws IOException {
        File file = new File("C:\\test\\1cv8ddb.1CD");
        try (FileHelper is = new FileHelper(file)) {
            is.open();
            assertEquals(is.getTables(), 10);
            Table1C table = is.getTable("users");
            assertNotNull("Not found", table);
            assertEquals(table.getName(), "USERS");
            Collection<Field1C> fields = table.getFields();
            assertNotNull(fields);
            assertEquals(fields.size(), 7);
            assertEquals(table.getRecordSize(), 626);
            assertEquals(table.getRecordPage(), 10);
            ResultSet rs = is.getTableRecords("DEPOT");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void test_3_JDBC_Driver_Create_Connection() throws SQLException {
        Driver1C driver = new Driver1C();
        Connection connect = driver.connect("jdbc:1c:C:\\test\\1cv8ddb.1CD", null);
        assertNotNull(connect);
        assertFalse(connect.isClosed());
        connect.close();
        assertTrue(connect.isClosed());
    }
    
    public void test_4_JDBC_Connection_Get_Cataloge() throws SQLException {
        Driver1C driver = new Driver1C();
        try (Connection connect = driver.connect("jdbc:1c:C:\\test\\1cv8ddb.1CD", null)) {
            String catalog = connect.getCatalog();
            assertEquals(catalog, "C:\\test");
        }
    }
    
    public void test_5_JDBC_Connection_Get_Warnings() throws SQLException {
        Driver1C driver = new Driver1C();
        try (Connection connect = driver.connect("jdbc:1c:C:\\test\\1cv8ddb.1CD", null)) {
            assertNull(connect.getWarnings());
        }
    }
    
    public void test_6_JDBC_Connection_Get_MetaData() throws SQLException {
        Driver1C driver = new Driver1C();
        try (Connection connect = driver.connect("jdbc:1c:C:\\test\\1cv8ddb.1CD", null)) {
            DatabaseMetaData metaData = connect.getMetaData();
            assertNotNull(metaData);
            assertEquals(metaData.getConnection(), connect);
            String driverVersion = metaData.getDriverVersion();
            assertEquals(driverVersion, "1.0");
            String driverName = metaData.getDriverName();
            assertEquals(driverName, Driver1C.class.getName());
            int driverMajorVersion = metaData.getDriverMajorVersion();
            assertEquals(driverMajorVersion, Config1C.MAJOR_VERSION);
            int driverMinorVersion = metaData.getDriverMinorVersion();
            assertEquals(driverMinorVersion, Config1C.MINOR_VERSION);

        }
    }
    public void test_7_JDBC_MetaData_TableTypes() throws SQLException {
        Driver1C driver = new Driver1C();
        try (Connection connect = driver.connect("jdbc:1c:C:\\test\\1cv8ddb.1CD", null)) {
            DatabaseMetaData metaData = connect.getMetaData();
            ResultSet tableTypes = metaData.getTableTypes();
            assertNotNull(tableTypes);
            int i = 0;
            for(tableTypes.first();!tableTypes.isAfterLast();tableTypes.next()) {
                int row = tableTypes.getRow();
                assertEquals(row, i+1);
                Object object = tableTypes.getObject(1);
                assertEquals(object.getClass(), String.class);
                object = tableTypes.getObject("TABLE_TYPE");
                assertEquals(object.getClass(), String.class);
                i++;
            }
            assertEquals(i, 2);
        }
    }
    
    public void test_8_JDBC_MetaData_Catalogs() throws SQLException {
        Driver1C driver = new Driver1C();
        try (Connection connect = driver.connect("jdbc:1c:C:\\test\\1cv8ddb.1CD", null)) {
            DatabaseMetaData metaData = connect.getMetaData();
            ResultSet catalogs = metaData.getCatalogs();
            assertNotNull(catalogs);
            int i = 0;
            for(catalogs.first();!catalogs.isAfterLast();catalogs.next()) {
                int row = catalogs.getRow();
                assertEquals(row, i+1);
                Object object = catalogs.getObject(1);
                assertEquals(object.getClass(), String.class);
                assertEquals(object, connect.getCatalog());
                object = catalogs.getObject("TABLE_CAT");
                assertEquals(object.getClass(), String.class);
                assertEquals(object, connect.getCatalog());
                i++;
            }
            assertEquals(i, 1);
        }
    }

    public void test_9_JDBC_MetaData_Tables() throws SQLException {
        Driver1C driver = new Driver1C();
        try (Connection connect = driver.connect("jdbc:1c:C:\\test\\1cv8ddb.1CD", null)) {
            DatabaseMetaData metaData = connect.getMetaData();
            String databaseProductVersion = metaData.getDatabaseProductVersion();
            assertEquals(databaseProductVersion, "8.2.14.0");
            String databaseProductName = metaData.getDatabaseProductName();
            assertEquals(databaseProductName, "1CDBMSV8");
            int databaseMajorVersion = metaData.getDatabaseMajorVersion();
            assertEquals(databaseMajorVersion, 8192);
            int databaseMinorVersion = metaData.getDatabaseMinorVersion();
            assertEquals(databaseMinorVersion, 3584);
            ResultSet tables = metaData.getTables(null, null, null, null);
            assertNotNull(tables);
            int i = 0;
            for(tables.first();!tables.isAfterLast();tables.next()) {
                int row = tables.getRow();
                assertEquals(row, i+1);
                i++;
                int column = tables.findColumn("TABLE_NAME");
                assertEquals(column, 3);
                String string1 = tables.getString(column);
                String string2 = tables.getString("TABLE_NAME");
                assertNotNull(string1);
                assertNotNull(string2);
                assertEquals(string1, string2);
                
            }
            assertEquals(i, 10);
        }
    }
    
    public void test_10_Command_Select() throws SQLException {
        Driver1C driver = new Driver1C();
        try (Connection connect = driver.connect("jdbc:1c:C:\\test\\1cv8ddb.1CD", null)) {
            try (Statement createStatement = connect.createStatement()) {
                assertNotNull(createStatement);
                assertEquals(connect, createStatement.getConnection());
                ResultSet executeQuery = createStatement.executeQuery("SELECT * FROM USERS");
                assertNotNull(executeQuery);
                assertTrue(executeQuery.isBeforeFirst());
                assertFalse(executeQuery.isAfterLast());
                assertTrue(executeQuery.next());
                int i = 0;
                for(executeQuery.first();!executeQuery.isAfterLast();executeQuery.next()) {
                    String string = executeQuery.getString(2);
                    assertNotNull(string);
                    String string1= executeQuery.getString("NAME");
                    assertEquals(string, string1);
                    i++;
                }
                assertEquals(i, 11);
            }
        }
    }
}
