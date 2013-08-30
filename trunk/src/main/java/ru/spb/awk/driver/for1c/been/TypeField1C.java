/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.been;

import static ru.spb.awk.driver.for1c.been.TypeField1C.Boolean;

/**
 *
 * @author Василий Казьмин
 */
public enum TypeField1C {
    FixedBinary,
    Boolean,
    Numeric,
    FixedString,
    VarString,
    Version,
    String,
    Binary,
    DateTime,
    ShortShadowVersion,
    Unknown;
    private byte byteLen = -1;
    
    private final static String
            FIXED_BINARY = "B",
            BOOLEAN      = "L",
            NUMERIC      = "N",
            FIXED_STRING = "NC",
            VAR_STRING   = "NVC",
            VERSION      = "RV",
            STRING       = "NT",
            BINARY       = "I",
            DATE_TIME    = "DT";
    
    public static TypeField1C fromString(String type) {
        if(type.equalsIgnoreCase(FIXED_BINARY)) {
            return FixedBinary;
        } else if(type.equalsIgnoreCase(BOOLEAN)) {
            return Boolean;
        } else if(type.equalsIgnoreCase(NUMERIC)) {
            return Numeric;
        } else if(type.equalsIgnoreCase(FIXED_STRING)) {
            return FixedString;
        } else if(type.equalsIgnoreCase(VAR_STRING)) {
            return VarString;
        } else if(type.equalsIgnoreCase(VERSION)) {
            return Version;
        } else if(type.equalsIgnoreCase(STRING)) {
            return String;
        } else if(type.equalsIgnoreCase(BINARY)) {
            return Binary;
        } else if(type.equalsIgnoreCase(DATE_TIME)) {
            return DateTime;
        }
        return Unknown;
    }
    public int getSize(int len) {
        switch(this) {
            case FixedBinary:
                return len * 1;
            case Boolean:
                return 1;
            case Numeric:
                return (len+2)/2;
            case FixedString:
                return len * 2;
            case VarString:
                return len * 2 + 2;
            case Version:
                return 16;
            case String:
                return 8;
            case Binary:
                return 8;
            case DateTime:
                return 7;
            case ShortShadowVersion:
                return 8;
        }
        return 0;
    }
}
