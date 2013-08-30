/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.core;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

/**
 *
 * @author Василий Казьмин
 */
public class Head {

    static IHeadReader reader;
    
    public static IHeadReader getHeadReader() {
        if(reader==null){
            reader = new HeadReader();
        }
        return reader;
    }
    
    private final String sign;
    private final int lenght;
    private final int ver1, ver2;
    private final long version;
    private int[] blocks;

    public Head(String s, int len, int v1, int v2, long v) {
        sign = s;
        lenght = len;
        ver1 = v1;
        ver2 = v2;
        version = v;
    }
            

    private static class HeadReader implements IHeadReader {

        @Override
        public Head read(RandomAccessFile raf, int page) throws IOException {
            String s = FileHelper.readSign(raf, 8, Charset.forName("cp866"));
            int len = FileHelper.readInt(raf);
            int v1 = FileHelper.readInt(raf);
            int v2 = FileHelper.readInt(raf);
            long ver = FileHelper.readUInt(raf);
            Head h = new Head(s, len, v1, v2, ver);
            int[] indxs = new int[h.getSize()];
            for (int i = 0; i < h.getSize(); i++) {
                indxs[i] = FileHelper.readInt(raf);
            }
            h.setBlocks(indxs);
            return h;        
        }
        
    }

    @Override
    public String toString() {
        return
        new StringBuilder()
                .append("Sig.:").append(sign).append("\n")
                .append("Ver.:").append(version).append("\n")
                .append("Len.:").append(lenght).append("\n")
                .append("Ver1.:").append(ver1).append("\n")
                .append("Ver2.:").append(ver2).append("\n")
                .toString();
    }

    long getVersion() {
        return version;
    }

    public int getLenght() {
        return lenght;
    }

    int getSize() {
        if(lenght == 0) {
            return 0;
        }
        return (lenght - 1) / 0x3ff000 + 1;
    }

    void setBlocks(int[] indxs) {
        blocks = indxs;
    }

    int[] getBlocks() {
        return blocks;
    }
    
    
}
