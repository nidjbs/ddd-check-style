package com.hyl.ddd.check.style.common;

/**
 * @author huayuanlin on 2022/10/22
 */
public class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException();
    }


    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }


    public static boolean isEmpty(String str) {
        return !isNotEmpty(str);
    }

}