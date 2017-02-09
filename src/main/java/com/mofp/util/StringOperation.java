package com.mofp.util;

/**
 * @author rivasyafri
 */
public class StringOperation {

    public static boolean isNullOrEmpty(String obj) {
        if (obj == null) {
            return true;
        } else if (obj.length() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
