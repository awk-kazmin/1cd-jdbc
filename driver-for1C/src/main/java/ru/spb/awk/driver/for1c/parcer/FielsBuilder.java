/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.parcer;

import ru.spb.awk.driver.for1c.jdbc.Field;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Василий Казьмин
 */
public class FielsBuilder extends AbstractBuilder<List<Field>> {

    private final List<Field> fields = new ArrayList<>();

    /**
     *
     * @param parent
     */
    public FielsBuilder() {
    }


    @Override
    public void appendStar() throws SQLSyntaxErrorException {
        fields.add(new Field(null, "*", null));
    }

    
    
    @Override
    public List<Field> create() {
        return fields;
    }

    @Override
    public void appendName(String source, String name, String alias) throws SQLSyntaxErrorException {
        fields.add(new Field(source, name, alias));
    }
    
    
    
    
}
