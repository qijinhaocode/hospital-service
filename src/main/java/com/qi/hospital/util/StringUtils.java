package com.qi.hospital.util;

import java.util.Locale;

public class StringUtils {
    public static void main(String[] args) {
        System.out.println(uppercase("fasfaFEFffaef"));
    }

    //字符串首字母大写
    public static String uppercase(String word) {
        word = word.toLowerCase(Locale.ROOT);
        word = word.substring(0, 1).toUpperCase() + word.substring(1);
        return word;
    }
}