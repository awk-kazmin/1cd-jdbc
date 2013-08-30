/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.parcer;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;
import ru.spb.awk.driver.for1c.jdbc.Field;

/**
 *
 * @author Василий Казьмин
 */
class RightClauseBuilder extends AbstractBuilder<Clause>{

    List expr = new ArrayList();
    
    @Override
    public void appendName(String source, String name, String alias) throws SQLSyntaxErrorException {
        expr.add(new Field(source, name, alias));
    }
    
    @Override
    public Clause create() throws SQLException {
        return new Clause(expr);
    }
    
}
