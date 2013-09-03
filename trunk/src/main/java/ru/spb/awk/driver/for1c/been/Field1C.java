/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.been;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;


/**
 * Class Field1C is representation field record in table 1cd file.
 * @author Василий Казьмин
 */
public class Field1C extends ChildObject1C {
    PropertyChangeSupport support = new PropertyChangeSupport(this);
    
    private TypeField1C type;
    private int lenght;
    private boolean nullable;
    private int precision;
    private boolean ignoreCase;
    
    public Field1C(Object1C source) {
        super(source);
        support.addPropertyChangeListener(source);
    }


    public void setType(TypeField1C value) {
        support.firePropertyChange("Type", type, value);
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
    

    public int getSize() {
        int size = 0;
        if(nullable) {
            size++;
        }
        size += type.getSize(lenght);
        return size;
    }


    public void setLength(Integer valueOf) {
        lenght = valueOf;
    }

    public void setCaseSensitive(boolean equals) {
        setIgnoreCase(!equals);
    }

    /**
     * @return the precision
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * @return the ignoreCase
     */
    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    /**
     * @param ignoreCase the ignoreCase to set
     */
    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    @Override
    public void addChild(ChildObject1C aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
