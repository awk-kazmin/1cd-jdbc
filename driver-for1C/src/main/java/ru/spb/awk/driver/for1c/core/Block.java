/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.core;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Василий Казьмин
 */
public class Block {
    private static IBlockReader reader;

    public static IBlockReader getBlockReader() {
        if(reader == null) {
            reader = new BlockReader();
        }
        return reader;
    }
    int offset = 0;
    Head head;
    List<Integer> data;
    
    /**
     *
     * @param page
     * @param h
     * @param d
     */
    public Block(int page, Head h, List<Integer> d) {
        head = h;
        data = d;
        this.offset = page;
    }
    
    /**
     *
     * @param v
     */
    public void visit(RandomAccessFile raf, IBlockVisitor v) throws IOException {
        v.visit(raf, head, data);
    }

    public Head getHead() {
        return head;
    }

    public List<Integer> getData() {
        return data;
    }
    
    
    private static class BlockReader implements IBlockReader {
        private RandomAccessFile raf;
        /**
         * 
         * @param raf RandomAccessFile
         * @param reader Head reader
         * @param page index page
         * @return
         * @throws IOException 
         */
        @Override
        public Block readBlock(RandomAccessFile raf, IHeadReader reader, int page) throws IOException {
            this.raf = raf;
            raf.seek(page*FileHelper.PAGE_SIZE);
            Head  h = reader.read(raf, page);
            List<Integer> data = new ArrayList<>();
            for(int i : h.getBlocks()) {
                data.addAll(readTable(i));
            }
            return new Block(page, h, data);
        }

        private Collection<? extends Integer> readTable(int page) throws IOException {
            List<Integer> data = new ArrayList<>();
            raf.seek(page*FileHelper.PAGE_SIZE);
            long l = FileHelper.readInt(raf);
            for(int i = 0; i < l; i++) {
                data.add(FileHelper.readInt(raf));
            }
            return data;
        }


    }
}
