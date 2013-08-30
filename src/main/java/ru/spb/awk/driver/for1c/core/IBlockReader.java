/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.core;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Василий Казьмин
 */
public interface IBlockReader {

    /**
     *
     * @param raf RandomAccessFile
     * @param reader Head reader
     * @param page index page
     * @return
     * @throws IOException
     */
    Block readBlock(RandomAccessFile raf, IHeadReader reader, int page) throws IOException;
    
}
