/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.been;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Василий Казьмин
 */
public class Index1C extends ChildObject1C {
    private int lenght;
    private List<ChildObject1C> fields = new ArrayList<>();
    private boolean unique;

    public Index1C(Object1C source) {
        super(source);
    }

    public void setUnique(boolean equals) {
        unique = equals;
    }

    @Override
    public void addChild(ChildObject1C aThis) {
        fields.add(aThis);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
