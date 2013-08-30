/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.core;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.List;

/**
 *
 * @author Василий Казьмин
 */
class RootBlockReader implements IBlockVisitor {
    private final RandomAccessFile raf;
    private String locale;
    private int[] pages;
    

    public RootBlockReader(RandomAccessFile raf) {
        this.raf = raf;
    }
    
    
    RootObject1CD getBlock() throws IOException {
        RootObject1CD root = new RootObject1CD(raf);
        root.setLocale(locale);
        for(int page : pages) {
            IBlockReader reader = Block.getBlockReader();
            Block readBlock = reader.readBlock(raf, Head.getHeadReader(), page);
            root.addChild(readBlock);
        }
        return root;
    }

    @Override
    public void visit(RandomAccessFile raf, Head head, List<Integer> data)  throws IOException {
        for(int i : data) {
            raf.seek(i*FileHelper.PAGE_SIZE);
            locale = FileHelper.readSign(raf, 32, Charset.forName("UTF-8"));
            int len = FileHelper.readInt(raf);
            pages = new int[len];
            for(i=0;i<len;i++) {
                pages[i] = FileHelper.readInt(raf);
            }
        }
    }
    
}
