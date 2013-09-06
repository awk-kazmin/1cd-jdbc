/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.parcer;

import java.sql.SQLException;
import ru.spb.awk.driver.for1c.core.ResultMap;
import ru.spb.awk.driver.for1c.jdbc.Cursor1C;

/**
 *
 * @author Василий Казьмин
 */
public interface IWhere {
    
    public static enum Result {
       BOTH, LEFT, RIGHT, NOTHING
    }
    public Result test(Cursor1C left, Cursor1C right) throws SQLException;
    public Result test(Cursor1C left, Cursor1C right, Result result) throws SQLException;
    public boolean test(ResultMap<String ,?> record);
}
