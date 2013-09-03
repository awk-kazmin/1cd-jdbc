/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.parcer;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;
import ru.spb.awk.driver.for1c.jdbc.IColumn;

/**
 *
 * @author Василий Казьмин
 */
public class FieldsBuilder extends AbstractBuilder<List<IColumn>> {

    private final List<IColumn> fields = new ArrayList<>();

    /**
     *
     * @param parent
     */
    public FieldsBuilder() {
    }


    @Override
    public void appendStar() throws SQLSyntaxErrorException {
        fields.add(new Field(null, "*", null));
    }

    
    
    @Override
    public List<IColumn> create() {
        return fields;
    }

    @Override
    public void appendName(String source, String name, String alias) throws SQLSyntaxErrorException {
        fields.add(new Field(source, name, alias));
    }
    
    
    
    
}
