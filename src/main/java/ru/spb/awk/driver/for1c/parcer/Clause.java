/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.parcer;

import java.sql.SQLSyntaxErrorException;
import java.util.List;
import javax.naming.OperationNotSupportedException;
import ru.spb.awk.driver.for1c.jdbc.Cursor1C;

/**
 *
 * @author Василий Казьмин
 */
class Clause {
    private final List items;

    Clause(List expr) {
        items = expr;
    }

    boolean test(Clause right, Cursor1C left, Cursor1C rightc) throws SQLSyntaxErrorException {
        if(right == null) {
            Object eval = eval(left, rightc);
            if(eval instanceof Boolean) {
                return (boolean) eval;
            } else {
                throw new SQLSyntaxErrorException("Left clause not boolean");
            }
            
        }
        return eval(left, rightc).equals(right.eval(left, rightc));
    }

    private Object eval(Cursor1C left, Cursor1C right) throws SQLSyntaxErrorException {
        if(items == null || items.isEmpty()) {
            throw new SQLSyntaxErrorException("Empty items");
        }
        if(items.size() == 1) {
             throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
    }
    
}
