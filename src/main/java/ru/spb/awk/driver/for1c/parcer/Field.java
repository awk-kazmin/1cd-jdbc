/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.parcer;

import ru.spb.awk.driver.for1c.jdbc.IColumn;

/**
 *
 * @author Василий Казьмин
 */
public class Field implements IColumn {
    private String source, name, alias;

    public Field(String source, String field, String name) {
        this.source = source;
        this.name = field;
        this.alias = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public String getAlias() {
        return alias==null?name:alias;
    }

    @Override
    public int getSize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
