/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.been;

import ru.spb.awk.driver.for1c.jdbc.IColumn;


/**
 * Class Field1C is representation field record in table 1cd file.
 * @author Василий Казьмин
 */
public class Field1C implements IColumn {
    
    private String name;
    private TypeField1C type;
    private int lenght;
    private boolean nullable;
    private int precision;
    private boolean ignoreCase;
    private final Table1C source;
    
    public Field1C(Table1C source) {
        this.source = source;
    }

    public void setName(String value) {
        name = value;
        source.addField(this);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setType(TypeField1C value) {
        source.addType(value);
        type = value;
    }

    public void setLenght(int i) {
        lenght = i;
    }

    public void setNullable(boolean i) {
        nullable = i;
    }

    public void setPrecision(int i) {
        precision = i;
    }


    public TypeField1C getType() {
        return type;
    }
    
    
    @Override
    public int getSize() {
        int size = 0;
        if(nullable) {
            size++;
        }
        size += type.getSize(lenght);
        return size;
    }

    @Override
    public boolean compareTo(String columnLabel) {
        return name.equalsIgnoreCase(columnLabel);
    }


    @Override
    public Table1C getSource() {
        return source;
    }

    public void setLength(Integer valueOf) {
        lenght = valueOf;
    }

    public void setCaseSensitive(boolean equals) {
        ignoreCase = !equals;
    }
}
