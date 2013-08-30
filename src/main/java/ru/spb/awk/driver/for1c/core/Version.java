/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.core;

/**
 *
 * @author Василий Казьмин
 */
public class Version {
    private final byte a1;
    private final byte a2;
    private final byte a3;
    private final byte a4;

    public Version(byte[] buf, int start) {
        a1 = buf[0 + start];
        a2 = buf[1 + start];
        a3 = buf[2 + start];
        a4 = buf[3 + start];
    }

    @Override
    public String toString() {
        return "" + a1 + "." + a2 + "." + a3 + "." + a4;
    }

    private boolean afterOrEq(int i, int i0, int i1, int i2) {
        if (i > a1) {
            return true;
        } else if (i < a1) {
            return false;
        }
        if (i0 > a2) {
            return true;
        } else if (i0 < a2) {
            return false;
        }
        if (i1 > a3) {
            return true;
        } else if (i1 < a3) {
            return false;
        }
        if (i2 > a4) {
            return true;
        } else if (i2 < a4) {
            return false;
        }
        return true;
    }

    public int getMajor() {
        return a1 << 8 + a2;
    }

    public int getMinor() {
        return a3 << 8 + a4;
    }
    
}
