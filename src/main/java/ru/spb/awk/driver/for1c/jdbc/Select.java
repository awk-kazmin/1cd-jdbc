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
    private List<ResultMap<String, ?>> records = new ArrayList<>();
    
    public Select(FileHelper helper, List<IColumn> fields, List<TableBeen> tables, WhereGroup where) throws SQLException {
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

    private Table1C getSource(TableBeen t) throws SQLSyntaxErrorException {
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
        IColumn.ColumnBuilder builder = new IColumn.ColumnBuilder();
        
        for (Field1C field1C : t.getFields()) {
            IColumn c = builder.make(field1C, field1C.getName()+alias_suffix);
            if(columns.containsKey(c.getAlias())) {
                throw new SQLSyntaxErrorException("Field " +c.getAlias() + " allready exists in query.");
            }
            columns.put(c.getAlias(), c);
        }
    }

    private void makeTables(List<TableBeen> tables) throws SQLSyntaxErrorException, SQLDataException {
        for(TableBeen t : tables) {
            if(this.tables.containsKey(t.getAlias())) {
                throw new SQLSyntaxErrorException("Table "+t.getAlias()+" allready exists in query.");
            }
            Table1C source = getSource(t);
            this.tables.put(t.getAlias(), source);
            Cursor1C cursor = new Cursor1C(helper.getFile(), source.getRecordPage(), source.getRecordSize(), source.getFieldsSizes());
            tables_cursors.put(t.getAlias(), cursor);
        }
    }

    private void makeFields(List<IColumn> fields) throws SQLSyntaxErrorException {
        for(IColumn f : fields) {
            if("*".equals(f.getAlias())) {
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
                    IColumn c = new IColumn.ColumnBuilder().bind(f, tables);
                    this.columns.put(c.getAlias(), c);
                } else {
                    this.columns.put(f.getName(),new IColumn.ColumnBuilder().make(tables.get(f.getSource()).getField(f.getName())));
                }
            }
        }
    }

    private void makeRecords(WhereGroup where) throws IOException, SQLSyntaxErrorException, SQLException {


        for (Map.Entry<String, Cursor1C> e : tables_cursors.entrySet()) {
            Cursor1C c = e.getValue();
            for(c.first();!c.isAfterLast();c.next()) {
                ResultMap<String, ?> record = new ResultMap<>();
                if(where.test(record)) {
                    records.add(record);
                }
                makeRecords(e, where, c);
                
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

    private ResultMap<String, ?> makeRecord(Cursor1C source, Cursor1C target, WhereGroup where) throws SQLSyntaxErrorException, SQLException {
        ResultMap<String, ?> result = null;
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

    private ResultMap<String, ?> both(Cursor1C source, Cursor1C target) {
        ResultMap<String, ?> record = new ResultMap<>();
        fill(source, record);
        fill(target, record);
        return record;
    }

    private ResultMap<String, ?>one(Cursor1C source) {
        ResultMap<String, ?>record = new ResultMap<>();
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

    private void fill(Cursor1C source, ResultMap<String, ?> record) {
        for(IColumn c : columns.valuesByOrder()) {
            Table1C table = (Table1C) c.getSource();
            record.put(c.getAlias(), source.getBytes(table.getFieldIndex(c)));
        }
    }

    ResultMap<String, ? extends IColumn> getColumns() {
        return columns;
    }

    ResultMap[] getData() {
        return records.toArray(new ResultMap[records.size()]);
    }

    private void makeRecords(Map.Entry<String, Cursor1C>[] e, WhereGroup where, Cursor1C ... cursors) {
        for(Map.Entry<String, Cursor1C> et : tables_cursors.entrySet()) {
            
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
