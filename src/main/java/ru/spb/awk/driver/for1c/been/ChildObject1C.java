/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.been;

/**
 *
 * @author Василий Казьмин
 */
public abstract class ChildObject1C extends Object1C {
    private final Object1C source;
    public ChildObject1C(Object1C source) {
        this.source = source;
    }
    
    public Object1C getSource() {
        return source;
    }
    
    public void setName(String value) {
        super.setName(value);
        source.addChild(this);
    }
}
