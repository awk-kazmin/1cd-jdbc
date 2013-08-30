/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.jdbc;

/**
 *
 * @author Василий Казьмин
 */
public class Field {
    private String source, field, name;

    public Field(String source, String field, String name) {
        this.source = source;
        this.field = field;
        this.name = name;
    }

    String getField() {
        return field;
    }

    String getName() {
        if(name==null)
            return field;
        return name;
    }

    String getSource() {
        return source;
    }

    
}
