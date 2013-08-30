/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.jdbc;

/**
 *
 * @author Василий Казьмин
 */
public class Table {

    private String source, alias;
    public Table(String source, String alias) {
        this.source = source;
        if(alias==null) {
            this.alias = source;
        } else {
            this.alias = alias;
        }
    }

    String getAlias() {
        return alias;
    }

    String getSource() {
        return source;
    }
    
}
