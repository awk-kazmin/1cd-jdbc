/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.parcer;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

/**
 *
 * @author Василий Казьмин
 */
public interface IBuilder<T> {
    T create() throws SQLException;

    public void appendWorld(String toString) throws SQLSyntaxErrorException;

    public void appendStar() throws SQLSyntaxErrorException;

    public void end() throws SQLSyntaxErrorException;
    
    public void appendName(String source, String name, String alias) throws SQLSyntaxErrorException;

    public void appendOp(String op) throws SQLSyntaxErrorException;
}
