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
public class AbstractBuilder<T> implements IBuilder<T> {

    @Override
    public T create() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appendWorld(String toString)  throws SQLSyntaxErrorException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void end() throws SQLSyntaxErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appendName(String source, String name, String alias) throws SQLSyntaxErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appendOp(String op) throws SQLSyntaxErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appendStar() throws SQLSyntaxErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
