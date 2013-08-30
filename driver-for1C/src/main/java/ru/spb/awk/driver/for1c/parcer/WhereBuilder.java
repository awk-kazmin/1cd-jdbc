/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.parcer;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Василий Казьмин
 */
public class WhereBuilder extends AbstractBuilder<WhereGroup>{

    IBuilder builder;
    private WhereGroup where, current;
    private Clause left;
    private WhereGroup.WhereOperation op;
    
    
    public WhereBuilder() {
        builder = new LeftClauseBuilder();
        where = new WhereGroup();
    }

    @Override
    public void appendName(String source, String name, String alias) throws SQLSyntaxErrorException {
        if(builder!=null) {
            builder.appendName(source, name, alias);
            return;
        }
        super.appendName(source, name, alias); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appendOp(String op) throws SQLSyntaxErrorException {
        try {
            left = (Clause) builder.create();
        } catch (SQLException ex) {
            Logger.getLogger(WhereBuilder.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLSyntaxErrorException(ex);
        }
        builder = new RightClauseBuilder();
    }

    @Override
    public void appendWorld(String toString) throws SQLSyntaxErrorException {
        try {
            current = new WhereGroup(left, (Clause) builder.create());
        } catch (SQLException ex) {
            Logger.getLogger(WhereBuilder.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLSyntaxErrorException(ex);
        }
        builder = new LeftClauseBuilder();
        if(op!=null) {
            where.add(current, op);
        } else {
            where.add(current);
        }
        op = WhereGroup.WhereOperation.valueOf(toString.toUpperCase());

    }

    
    
    @Override
    public WhereGroup create() throws SQLException {
        
        return where;
    }
    
    
    
}
