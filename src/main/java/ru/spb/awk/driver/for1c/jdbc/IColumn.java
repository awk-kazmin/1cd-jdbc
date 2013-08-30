/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.jdbc;

/**
 *
 * @author Василий Казьмин
 */
public interface IColumn {

    public boolean compareTo(String columnLabel);

    public Object getSource();

    public String getName();

    public int getSize();
    
}
