/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.jdbc;

import java.io.IOException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.spb.awk.driver.for1c.been.Field1C;
import ru.spb.awk.driver.for1c.been.Table1C;
import ru.spb.awk.driver.for1c.core.FileHelper;
import ru.spb.awk.driver.for1c.core.ResultMap;
import ru.spb.awk.driver.for1c.parcer.WhereGroup;

/**
 *
 * @author Василий Казьмин
 */
public class Select {

    Map<String, Table1C> tables = new HashMap<>();
    ResultMap<String, IColumn> columns = new ResultMap<>();
    Map<String, Cursor1C> tables_cursors = new HashMap<>();
    private final FileHelper helper;
    private List<byte[]> records = new ArrayList<>();
    private int _cursor = -1;
    
    public Select(FileHelper helper, List<Field> fields, List<Table> tables, WhereGroup where) throws SQLException {
        this.helper = helper;
        if(fields==null) {
            throw new SQLSyntaxErrorException("SELECT ?");
        }
        if(tables!=null) {
            makeTables(tables);
        }
        makeFields(fields);
        try {
            makeRecords(where);
        } catch (IOException ex) {
            Logger.getLogger(Select.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLDataException(ex);
        }
    }

    private Table1C getSource(Table t) throws SQLSyntaxErrorException {
        try {
            String source = t.getSource();
            if(source == null) {
                throw new SQLSyntaxErrorException("Source is null");
            }
            return helper.getTable(source);
        } catch (IOException ex) {
            Logger.getLogger(Select.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLSyntaxErrorException(ex);
        }
    }

    private void addFields(Table1C t, int i) throws SQLSyntaxErrorException {
        String alias_suffix = "";
        if(i>0) {
            alias_suffix += i;
        }
        for (Field1C field1C : t.getFields()) {
            if(columns.containsKey(field1C.getName()+alias_suffix)) {
                throw new SQLSyntaxErrorException("Field " +field1C.getName() + " allready exists in query.");
            }
            columns.put(field1C.getName() + alias_suffix, field1C);
        }
    }

    private void makeTables(List<Table> tables) throws SQLSyntaxErrorException, SQLDataException {
        for(Table t : tables) {
            if(this.tables.containsKey(t.getAlias())) {
                throw new SQLSyntaxErrorException("Table "+t.getAlias()+" allready exists in query.");
            }
            Table1C source = getSource(t);
            this.tables.put(t.getAlias(), source);
            Cursor1C cursor = new Cursor1C(helper.getFile(), source.getRecordPage(), source.getRecordSize(), source.getFieldsSizes());
            tables_cursors.put(t.getAlias(), cursor);
        }
    }

    public boolean first() throws SQLDataException {
        if(records.isEmpty()) {
            return false;
        }
        _cursor = 0;
        return true;
    }

    boolean next() throws SQLDataException {
        _cursor++;
        if(_cursor==records.size()) {
            return false;
        }
        return true;
    }

    int findColumn(String columnLabel) {
        return columns.getIndex(columnLabel) + 1;
    }

    String getString(int columnIndex) throws SQLDataException {
        IColumn c = columns.getValue(columnIndex-1);
        int size = c.getSize();
        int offset = getOffset(c);
        byte[] record = records.get(_cursor);
        StringBuilder builder = new StringBuilder();
        
        for(int i = offset; i<offset+size;i+=2) {
            char ch = getChar(record, i);
            if(ch==0) break;
            builder.append(ch);
        }
        return builder.toString().trim();
    }

    boolean isAfterLast() {
        return _cursor == records.size();
    }

    boolean isBeforeFirst() {
        return _cursor < 0;
    }

    private void makeFields(List<Field> fields) throws SQLSyntaxErrorException {
        for(Field f : fields) {
            if(f.getField().equals("*")) {
                if(f.getSource() == null) {
                    int i = 0;
                    for (Table1C t : this.tables.values()) {
                        addFields(t, i);
                    }
                } else {
                    Table1C t = this.tables.get(f.getSource());
                    if(t==null) {
                        throw new SQLSyntaxErrorException("Table "+f.getSource()+" not found in query.");
                    }
                    addFields(t, 0);
                }
            } else {
                if(this.columns.containsKey(f.getName())) {
                    throw new SQLSyntaxErrorException("Field "+f.getName()+" allready exists in query.");
                }
                if (f.getSource() == null) {
                    Field1C ff = null;
                    for (Table1C table1C : tables.values()) {
                        Field1C ff1 = table1C.getField(f.getField());
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
                    this.columns.put(f.getName(), ff);
                } else {
                    this.columns.put(f.getName(),tables.get(f.getSource()).getField(f.getField()));
                }
            }
        }
    }

    private void makeRecords(WhereGroup where) throws IOException {
        if (tables_cursors.size() == 1) {
            Cursor1C c = tables_cursors.values().iterator().next();
            for (c.first(); !c.isAfterLast(); c.next()) {
                records.add(makeRecord(c, null, where));
            }
        } else {
            for (Map.Entry<String, Cursor1C> e : tables_cursors.entrySet()) {
                Cursor1C c = e.getValue();
                for (c.first(); !c.isAfterLast(); c.next()) {
                    for (Map.Entry<String, Cursor1C> eT : tables_cursors.entrySet()) {
                        if (!eT.getKey().equals(e.getKey())) {
                            Cursor1C cT = e.getValue();
                            for (cT.first(); !cT.isAfterLast(); cT.next()) {
                                byte[] r = makeRecord(c, cT, where);
                                if (r != null) {
                                    records.add(r);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private int getSize() {
        int size = 0;
        for(Cursor1C c : tables_cursors.values()) {
            size += c.getSize()+1;
        }
        return size;
    }

    private byte[] makeRecord(Cursor1C source, Cursor1C target, WhereGroup where) {
        byte[] result = null;
        switch(where.test(source, target)) {
            case BOTH:
                result = both(source, target);
                break;
            case LEFT:
                result = one(source);
                break;
            case RIGHT:
                result = one(target);
                break;
        }
        return result;
    }

    private byte[] both(Cursor1C source, Cursor1C target) {
        byte[] record = new byte[getSize()];
        fill(source, record);
        fill(target, record);
        return record;
    }

    private byte[] one(Cursor1C source) {
        byte[] record = new byte[getSize()];
        fill(source, record);
        return record;
    }


    private int getOffset(IColumn c) {
        int i = 0;
        for(IColumn nc : columns.valuesByOrder()) {
            if(nc.equals(c)) break;
            i+=nc.getSize();
        }
        return i;
    }

    private char getChar(byte[] record, int i) {
        return (char) ((((int) record[i + 1]) << 8) + record[i]);
    }

    private void fill(Cursor1C source, byte[] record) {
        for(IColumn c : columns.values()) {
            Table1C table = (Table1C) c.getSource();
            byte[] field = source.getBytes(table.getFieldIndex(c));
            System.arraycopy(field, 0, record, getOffset(c), field.length);
        }
    }

}
