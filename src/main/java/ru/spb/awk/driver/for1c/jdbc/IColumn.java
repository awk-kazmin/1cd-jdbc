/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.jdbc;

import ru.spb.awk.driver.for1c.been.Field1C;

/**
 *
 * @author Василий Казьмин
 */
public interface IColumn {

    public boolean compareTo(String columnLabel);

    public Object getSource();

    public String getName();

    public String getAlias();
    
    public int getSize();

    public static class ColumnBuilder {

        public ColumnBuilder() {
        }

        IColumn make(Field1C field1C) {
            return new IColumnImpl(field1C);
        }

        IColumn make(Field1C field1C, String alias) {
            return new IColumnImpl(field1C, alias);
        }

        private class IColumnImpl implements IColumn {

            private String alias;
            public IColumnImpl(Field1C f) {
                field = f;
                alias = field.getName();
            }
            private Field1C field;

            private IColumnImpl(Field1C field1C, String alias) {
                field = field1C;
                this.alias = alias;
            }

            @Override
            public boolean compareTo(String columnLabel) {
                return field.getName().equalsIgnoreCase(columnLabel);
            }

            @Override
            public Object getSource() {
                return field.getSource();
            }

            @Override
            public String getName() {
                return field.getName();
            }

            @Override
            public int getSize() {
                return field.getSize();
            }

            @Override
            public String getAlias() {
                return alias;
            }
        }
    }
    
}
