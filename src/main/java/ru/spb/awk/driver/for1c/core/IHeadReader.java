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
public interface IHeadReader {

    public Head read(RandomAccessFile raf, int page) throws IOException;
    
}
