/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.core;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 *
 * @author Василий Казьмин
 */
public interface IBlockVisitor {

    public void visit(RandomAccessFile raf, Head head, List<Integer> data)  throws IOException;
    
}
