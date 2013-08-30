/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.core;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.spb.awk.driver.for1c.been.Table1C;
import ru.spb.awk.driver.for1c.parcer.cc._1C.ParseException;
import ru.spb.awk.driver.for1c.parcer.cc._1C._1CParser;


/**
 *
 * @author Василий Казьмин
 */
class TableVisitor implements IBlockVisitor {
    private static TableVisitor visitor;

    static TableVisitor getVisitor() {
        if(visitor == null) {
            visitor = new TableVisitor();
        }
        return visitor;
    }
    private Map<String, Table1C> tables;

    TableVisitor() {
        this.tables = new LinkedHashMap<>();
    }


    int getTables() {
        return tables.size();
    }

    void clear() {
        tables.clear();
    }

    Table1C getTable(String value) {
        return tables.get(value.toUpperCase());
    }
    
    Table1C getTable(int i) {
        Object[] entrys = tables.values().toArray();
        return (Table1C) entrys[i];
    }

    @Override
    public void visit(RandomAccessFile raf, Head head, List<Integer> data) throws IOException {
        for(int page : data) {
            try {
                raf.seek(page*FileHelper.PAGE_SIZE);
                String str = FileHelper.readString(raf);
                _1CParser parser = _1CParser.makeParser(str);
                Table1C table = parser.parse();
                tables.put(table.getName().toUpperCase(), table);
            } catch (ParseException ex) {
                Logger.getLogger(TableVisitor.class.getName()).log(Level.SEVERE, null, ex);
                throw new IOException(ex);
            }
        }
    }
    
}
