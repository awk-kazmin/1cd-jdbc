/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.jdbc;

import java.sql.SQLSyntaxErrorException;
import java.util.Map;
import ru.spb.awk.driver.for1c.been.Field1C;
import ru.spb.awk.driver.for1c.been.Table1C;

/**
 *
 * @author Василий Казьмин
 */
public interface IColumn {

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

        IColumn bind(IColumn f, Map<String, Table1C> tables) throws SQLSyntaxErrorException {
            Field1C ff = null;
            for (Table1C table1C : tables.values()) {
                Field1C ff1 = table1C.getField(f.getName());
                if (ff1 != null) {
                    if (ff != null) {
                        throw new SQLSyntaxErrorException("Name " + f.getName() + " more in one table.");
                    } else {
                        ff = ff1;
                    }
                }
            }
            if (ff == null) {
                throw new SQLSyntaxErrorException("Name " + f.getName() + " not found/");
            }
            return new IColumn.ColumnBuilder().make(ff);
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
