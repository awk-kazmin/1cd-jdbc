/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.core;

import ru.spb.awk.driver.for1c.been.Table1C;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Василий Казьмин
 */
public class FileHelper implements Closeable {

    public static final int PAGE_SIZE = 0x1000;

    static String readSign(RandomAccessFile raf, int i, Charset charset) throws IOException {
        byte[] b = new byte[i];
        raf.read(b);
        String result = new String(b, 0, i, charset);
        return result;
    }

    static String readString(RandomAccessFile raf) throws IOException {
        StringBuilder builder = new StringBuilder();
        while (true) {
            int ch1 = raf.read();
            int ch2 = raf.read();
            int ch = ((ch2 << 8) + ch1);
            if (ch == 0) {
                break;
            }
            builder.append((char) ch);
        }
        return builder.toString();
    }


    private final RandomAccessFile channel;
    private RootObject1CD root;
    private File file;


    private String readSign(int l, Charset charset) throws IOException {
        return readSign(channel, l, charset);
    }

    private int readInt() throws IOException {
        return readInt(channel);
    }
    private long readUInt() throws IOException {
        return readUInt(channel);
    }    
    private Head readHead() throws IOException {
        String s = readSign(8, Charset.forName("cp866"));
        int len = readInt();
        int v1 = readInt();
        int v2 = readInt();
        long ver = readUInt();
        Head h = new Head(s, len, v1, v2, ver);
        int[] indxs = new int[h.getSize()];
        for (int i = 0; i < h.getSize(); i++) {
            indxs[i] = readInt();
        }
        h.setBlocks(indxs);
        return h;
    }
    private List<Integer> readTable(Head head) throws IOException {
        List<Integer> table = new ArrayList<>();
        for(int i : head.getBlocks()) {
            channel.seek(i * PAGE_SIZE);
            int l = readInt();
            for(int it=0;it<l;it++) {
                table.add(readInt());
            }
        }
        return table;
    }
    public void open() throws IOException {
        channel.seek(PAGE_SIZE * 1);
        channel.seek(PAGE_SIZE * 2);
        IBlockReader reader = Block.getBlockReader();
        Block block = reader.readBlock(channel, Head.getHeadReader(), 2);
        RootBlockReader rbr = new RootBlockReader(channel);
        block.visit(channel, rbr);
        root = rbr.getBlock();
    }

    public int getTables() throws IOException {
        return root.getTables();
    }

    public Table1C getTable(String value) throws IOException {
        return root.getTable(value);
    }

    public Table1C getTable(int value) throws IOException {
        return root.getTable(value);
    }

    public ResultSet getTableRecords(String object) throws IOException {
        return getTableRecords(root.getTable(object));
    }
    
    ResultSet getTableRecords(Table1C object) {
        return null;
    }

    public Version getVersion() {
        return version;
    }

    public String getSign() {
        return sign;
    }

    public RandomAccessFile getRaf() {
        return channel;
    }

    public File getFile() {
        return file;
    }
    private final String sign;
    private final Version version;
    private final long lenght;
    private final long unknown;

    /**
     *
     * @param file
     * @throws IOException
     */
    public FileHelper(File file) throws IOException {
        channel = new RandomAccessFile(file, "r");
        sign = readSign(8, Charset.forName("cp866"));
        byte[] b = new byte[8];
        channel.read(b);
        version = new Version(b, 0);
        lenght = readInt();
        unknown = readInt();
        this.file = file;
    }

    public static int readInt(RandomAccessFile raf) throws IOException {
        int ch1 = raf.read();
        int ch2 = raf.read();
        int ch3 = raf.read();
        int ch4 = raf.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1));
    }

    public static long readUInt(RandomAccessFile raf) throws IOException {
        long ch1 = raf.read();
        long ch2 = raf.read();
        long ch3 = raf.read();
        long ch4 = raf.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1));
    }

    @Override
    public void close() throws IOException {
        if (channel != null && channel.getChannel().isOpen()) {
            channel.close();
        }
    }


}
