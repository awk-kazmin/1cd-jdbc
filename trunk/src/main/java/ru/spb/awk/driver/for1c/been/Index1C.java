/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.been;

/**
 *
 * @author Василий Казьмин
 */
public class Index1C extends Object1C {
    private int lenght;
    private Field1C field;
    private Table1C source;
    private boolean unique;

    public Index1C(String name, int i, Field1C field) {
        setName(name);
        lenght = i;
        this.field = field;
    }

    public Index1C(Table1C table) {
        source = table;
    }


    public void setUnique(boolean equals) {
        unique = equals;
    }
}
