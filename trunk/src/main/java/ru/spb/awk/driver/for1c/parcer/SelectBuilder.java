/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.parcer;

import java.sql.SQLException;
import ru.spb.awk.driver.for1c.jdbc.Select;
import java.sql.SQLSyntaxErrorException;
import ru.spb.awk.driver.for1c.core.FileHelper;

/**
 *
 * @author Василий Казьмин
 */
public class SelectBuilder extends AbstractBuilder<Select> {
    private static final String SELECT = "SELECT";
    private static final String FROM = "FROM";
    private static final String WHERE = "WHERE";
    private IBuilder<?> builder = this;
    private final FieldsBuilder fielsBuilder;
    private final TablesBuilder tablesBuilder;
    private final WhereBuilder whereBuilder;
    private final FileHelper helper;

    public SelectBuilder(FileHelper helper) {
        this.helper = helper;
        tablesBuilder = new TablesBuilder();
        fielsBuilder = new FieldsBuilder();
        whereBuilder = new WhereBuilder();
    }

    @Override
    public void end() {
        builder = this;
    }

    @Override
    public void appendWorld(String aString) throws SQLSyntaxErrorException {
        if(aString.toUpperCase().startsWith(SELECT)) {
            builder = fielsBuilder;
        } else if(aString.toUpperCase().startsWith(FROM)) {
            builder = tablesBuilder;
        } else if(aString.toUpperCase().startsWith(WHERE)) {
            builder = whereBuilder;
        } else if(builder!=null) {
            builder.appendWorld(aString);
        } else {
            throw new SQLSyntaxErrorException("?"+aString);
        }
    }

    @Override
    public Select create() throws SQLException  {
        Select select = new Select(helper, fielsBuilder.create(), tablesBuilder.create(), whereBuilder.create());
        return select;
    }

    @Override
    public void appendStar() throws SQLSyntaxErrorException {
        if(builder!=this) {
            builder.appendStar();
            return;
        }
        throw new SQLSyntaxErrorException("?*");
    }

    @Override
    public void appendName(String source, String name, String alias) throws SQLSyntaxErrorException {
        if(builder!=null) {
            builder.appendName(source, name, alias);
        } else {
            super.appendName(source, name, alias); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public void appendOp(String op) throws SQLSyntaxErrorException {
        if(builder!=null) {
            builder.appendOp(op);
        } else {
            super.appendOp(op); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    
    
}
