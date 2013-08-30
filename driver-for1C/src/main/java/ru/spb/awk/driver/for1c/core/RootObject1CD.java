/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.core;

import java.io.IOException;
import java.io.RandomAccessFile;
import ru.spb.awk.driver.for1c.been.Table1C;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Василий Казьмин
 */
class RootObject1CD {
    private Locale locale;
    List<Integer> tableDefPages = new ArrayList<>();
    private Map<String, Table1C> tables = new HashMap<>();
    private List<Block> blocks = new ArrayList<>();
    private final RandomAccessFile raf;
    RootObject1CD(RandomAccessFile raf) {
        this.raf = raf;
    }

    void setLocale(String l) {
        locale = new Locale(l);
    }

    void addTablePage(int indxt) {
        tableDefPages.add(indxt);
    }

    List<Integer> getIndxs() {
        return tableDefPages;
    }


    int getTables() throws IOException {
        return  visit().getTables();
    }

    Table1C getTable(String value) throws IOException {
        return visit().getTable(value);
    }

    Table1C getTable(int value) throws IOException {
        return visit().getTable(value);
    }

    void addChild(Block readBlock) {
        blocks.add(readBlock);
    }

    private TableVisitor visit() throws IOException {
        TableVisitor visitor = TableVisitor.getVisitor();
        visitor.clear();
        for(Block b : blocks) {
             b.visit(raf, visitor);
         }
        return visitor;
    }

  
}
