/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.jdbc;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.spb.awk.driver.for1c.core.Block;
import ru.spb.awk.driver.for1c.core.Head;
import ru.spb.awk.driver.for1c.core.IBlockReader;

/**
 *
 * @author Василий Казьмин
 */
public class Cursor1C implements Closeable {
    private static final int PAGE_SIZE = 4096;
    private final int record_size;
    
    public int getSize() {
        return record_size;
    }
    
    byte[] getBytes(int indx) {
        int size = getSize(indx);
        int offset = getOffset(indx);
        return _position.getBytes(offset, size);
    }


    class BadRecords {
        class Part {
            long position;
            int cost;
        }
        Map<Long, List<Part>> parts = new HashMap<>();

        private boolean contains(long pos) {
            return parts.containsKey(pos);
        }

        private void read(long pos) throws IOException {
            List<Part> list = parts.get(pos);
            int offset = 0;
            for (Part p : list) {
                raf.seek(pos);
                raf.read(_position.record_buffer, offset, p.cost);
                offset+=p.cost;
            }
        }

        private void add(long i, int cost, long p) {
            if(!contains(i)) {
                List<Part> list = new ArrayList<>();
                parts.put(i, list);
            }
            List<Part> list = parts.get(i);
            Part part = new Part();
            part.cost = cost;
            part.position = p;
            list.add(part);
        }
        
    }
    class Position {
        int    count = 0;
        int    max;
        int    record = -1;
        long[] records;
        BadRecords bad_records;
        byte[] record_buffer;
        boolean next() throws IOException {
            if(isAfterLast()) {
                return false;
            }
            record += 1;
            if(record == records.length) {
                return false;
            }
            if(readRecord()) {
                return true;
            } else {
                return next();
            }
        }

        private boolean readRecord() throws IOException {
            long pos = records[record];
            raf.seek(pos);
            if(!bad_records.contains(pos)) {
                raf.read(record_buffer);
            } else {
                bad_records.read(pos);
            }
            if(record_buffer[0] == 0) {
                count ++;
                return true;
            } else {
                return false;
            }
        }

        private boolean isAfterLast() {
            return count == max;
        }

        private boolean isBeforeFirst() {
            return count == 0;
        }

        private boolean first() throws IOException {
            count = 0;
            record = -1;
            return next();
        }

        private byte[] getBytes(int offset, int size) {
            return Arrays.copyOfRange(record_buffer, offset, offset + size);
        }

    }
    Position _position;
    private Block block;
    private RandomAccessFile raf;
    private boolean open;
    private int[] columns_sizes;
    private int[] offsets;

    public Cursor1C(File f, int page, int record_size, int[] columns_sizes) throws SQLDataException {
        try {
            raf  = new RandomAccessFile(f, "r");
            open = true;
            IBlockReader reader = Block.getBlockReader();
            
            block = reader.readBlock(raf, Head.getHeadReader(), page);
            List<Integer> pages = block.getData();
            _position = new Position();
            _position.record = -1;
            _position.record_buffer = new byte[record_size];
            _position.records = new long[pages.size() * PAGE_SIZE / record_size];
            _position.max = block.getHead().getLenght() / record_size;
            int total = 0;
            int i = 0;
            int cost = 0;
            _position.bad_records = new BadRecords();
            Iterator<Integer> iterator = pages.iterator();
            int p = 0;
            while(i<_position.records.length) {
                if(cost==0 && iterator.hasNext()) {
                    p = iterator.next();
                }
                while(total<PAGE_SIZE && i<_position.records.length) {
                    _position.records[i] = p * PAGE_SIZE + total;
                    total += record_size;
                    if(total>PAGE_SIZE) {
                        cost = total - PAGE_SIZE;
                        _position.bad_records.add(i, record_size - cost, _position.records[i]);
                        if(iterator.hasNext()) {
                            p = iterator.next();
                        } else {
                            throw new SQLDataException();
                        }
                        _position.bad_records.add(i, cost, p * PAGE_SIZE);
                        while(cost>PAGE_SIZE) {
                            cost -= PAGE_SIZE;
                            if(iterator.hasNext()) {
                                p = iterator.next();
                            } else {
                                throw new SQLDataException();
                            }
                            _position.bad_records.add(i, cost, p * PAGE_SIZE);
                        }
                    }
                    i += 1;
                }
                total = cost;
            }
            this.columns_sizes = columns_sizes;
            offsets = new int[columns_sizes.length];
            offsets[0] = 1;
            for (i = 1;i<columns_sizes.length;i++) {
                offsets[i] = offsets[i-1] + columns_sizes[i-1];
            }
            this.record_size = record_size;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cursor1C.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLDataException(ex);
        } catch (IOException ex) {
            Logger.getLogger(Cursor1C.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLDataException(ex);            
        }
    }

    @Override
    public void close() throws IOException {
        if(open) {
            raf.close();
        }
    }

    boolean next() throws IOException {
        return _position.next();
    }

    boolean first() throws IOException {
        return _position.first();
    }

    boolean isAfterLast() {
        return _position.isAfterLast();
    }

    boolean isBeforeFirst() {
        return _position.isBeforeFirst();
    }

    private int getSize(int columnIndex) {
        return columns_sizes[columnIndex];
    }

    private int getOffset(int columnIndex) {
        return offsets[columnIndex];
    }
    
}
