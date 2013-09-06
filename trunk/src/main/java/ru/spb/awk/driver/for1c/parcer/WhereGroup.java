/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.parcer;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ru.spb.awk.driver.for1c.core.ResultMap;
import ru.spb.awk.driver.for1c.jdbc.Cursor1C;
import ru.spb.awk.driver.for1c.parcer.IWhere.Result;

/**
 *
 * @author Василий Казьмин
 */
public class WhereGroup implements Iterable<IWhere>, IWhere {
    private final Clause left;
    private final Clause right;
    private Result type = Result.NOTHING;

    WhereGroup(Clause left, Clause right) {
        if(left == null) {
            this.left = right;
            this.right = null;
        } else {
            this.left = left;
            this.right = right;
        }
    }

    @Override
    public boolean test(ResultMap<String, ?> record) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public static enum WhereOperation {

        AND,
        OR
    }
    List<WhereOperation> ops = new ArrayList<>();
    private final List<IWhere> items = new ArrayList<>();

    public WhereGroup() {
        this.left = null;
        this.right = null;
    }

    IWhere add(IWhere current) {
        if(!items.isEmpty()) {
            throw new IllegalStateException("items not empty");
        }
        items.add(current);
        return current;
    }

    IWhere add(IWhere current, WhereOperation op) {
        if(items.isEmpty()) {
            throw new IllegalStateException("items empty");
        }
        items.add(current);
        ops.add(op);
        return current;
    }
    
    @Override
    public Iterator<IWhere> iterator() {
        return items.iterator();
    }

    @Override
    public Result test(Cursor1C source, Cursor1C target, Result result) throws SQLSyntaxErrorException, SQLException {
        WhereOperation op = null;
        Iterator<WhereOperation> iterator = ops.iterator();
        if(left!=null) {
            if(!left.test(right, source, target)) {
                return type;
            }
        }
        for (IWhere w : items) {
            if(result == Result.NOTHING && op == WhereOperation.AND) {
                return result;
            }
            if(result == Result.BOTH && op == WhereOperation.OR) {
                return result;
            }
            result = w.test(source, target, result);
            if(iterator.hasNext()) {
                op = iterator.next();
            }
        }
        return result;
    }

    @Override
    public Result test(Cursor1C source, Cursor1C target) throws SQLSyntaxErrorException, SQLException {
        if(source==null && target==null) return Result.NOTHING;
        if(source==null) return test(source, target, Result.RIGHT);
        if(target==null) return test(source, target, Result.LEFT);
        return test(source, target, Result.BOTH);
    }

}
