/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.been;

import java.beans.PropertyChangeListener;

/**
 *
 * @author Василий Казьмин
 */
public abstract class Object1C implements PropertyChangeListener {
    private String name;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public abstract void addChild(ChildObject1C aThis);
}
