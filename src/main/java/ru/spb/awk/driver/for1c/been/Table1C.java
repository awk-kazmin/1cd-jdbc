/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.been;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ru.spb.awk.driver.for1c.core.ResultMap;
import ru.spb.awk.driver.for1c.jdbc.IColumn;

/**
 *
 * @author Василий Казьмин
 */
public class Table1C extends Object1C {
    ResultMap<String, Field1C> fields = new ResultMap<>();
    Map<String, Index1C> indxs = new HashMap<>();
    Set<TypeField1C> types = new HashSet<>();
    private boolean recordLock;
    private int recordPage;
    private int blobPage;
    private int indexPage;
    private int recordsCount;

    public void addField(Field1C field) {
        if(field.getType() == TypeField1C.Version || field.getType() == TypeField1C.ShortShadowVersion) {
            fields.put(field.getName().toUpperCase(), field, 0);
        } else {
            fields.put(field.getName().toUpperCase(), field);
        }
        
    }

    public void setRecordLock(boolean b) {
        recordLock = b;
        if(recordLock && !types.contains(TypeField1C.Version) && !types.contains(TypeField1C.ShortShadowVersion)) {
            Field1C field = new Field1C(this);
            field.setType(TypeField1C.ShortShadowVersion);
            field.setName("_SHADOW_VERSION");
        }
    }

    public void addIndex(Index1C indx) {
        indxs.put(indx.getName().toUpperCase(), indx);
    }

    public void setRecordPage(int i) {
        recordPage = i;
    }

    public void setBlobPage(int i) {
        blobPage = i;
    }

    public void setIndexPage(int i) {
        indexPage = i;
    }
    
    public Field1C getField(String name) {
        return fields.get(name.toUpperCase());
    }

    public Collection<Field1C> getFields() {
        return fields.valuesByOrder();
    }

    public int getRecordSize() {
        int size = 1;
        for (Field1C field : fields.values()) {
            size += field.getSize();
        }
        return size<5?5:size;
    }

    public int getRecordPage() {
        return recordPage;
    }

    public String getTypeName() {
        return "TABLE";
    }

    public int getRecordsSize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getRecords() {
        return recordsCount;
    }

    public void setRecords(int i) {
        recordsCount = i;
    }

    public int[] getFieldsSizes() {
        int[] result = new int[fields.size()];
        int i = 0;
        for (Field1C f : fields.valuesByOrder()) {
            result[i] = f.getSize();
            i++;
        }
        return result;
    }

    public int getFieldIndex(IColumn c) {
        return fields.getIndex(c.getName().toUpperCase());
    }

    public void setRecordlock(boolean equals) {
        recordLock = equals;
    }

    public void setRecordBlock(Integer valueOf) {
        recordPage = valueOf;
    }

    public void setBlobBlock(Integer valueOf) {
        blobPage = valueOf;
    }

    public void setIndexBlock(Integer valueOf) {
        indexPage = valueOf;
    }

    void addType(TypeField1C value) {
        types.add(value);
    }
}
