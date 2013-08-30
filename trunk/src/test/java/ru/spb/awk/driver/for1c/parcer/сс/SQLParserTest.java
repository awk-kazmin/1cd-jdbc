/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.parcer.сс;

import ru.spb.awk.driver.for1c.parcer.cc.SQL.SQLParser;
import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;
import ru.spb.awk.driver.for1c.core.FileHelper;
import ru.spb.awk.driver.for1c.parcer.IBuilder;
import ru.spb.awk.driver.for1c.jdbc.Select;
import ru.spb.awk.driver.for1c.parcer.SelectBuilder;

/**
 *
 * @author Василий Казьмин
 */
public class SQLParserTest extends TestCase {
    
    public SQLParserTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of makeParser method, of class SQLParser.
     */
    public void testMakeParser() throws IOException {
        System.out.println("makeParser");
        String query = "";
        try(FileHelper helper = new FileHelper(new File("C:\\test\\1cv8ddb.1CD"))) {
            IBuilder builder = new SelectBuilder(helper);
            SQLParser result = SQLParser.makeParser(query, builder);
            assertNotNull(result);

        }
    }



    /**
     * Test of Input method, of class SQLParser.
     */
    public void testInput() throws Exception {
        System.out.println("testInput");
        String query = "SELECT USERS.*, NAME AS NAME1 FROM USERS WHERE NAME=NAME1 AND Name=1";
        try(FileHelper helper = new FileHelper(new File("C:\\test\\1cv8ddb.1CD"))) {
            helper.open();
            SelectBuilder builder = new SelectBuilder(helper);
            SQLParser result = SQLParser.makeParser(query, builder);
            assertNotNull(result);
            result.Input();
            Select create = builder.create();
            assertNotNull(create);
        }
    }

}
