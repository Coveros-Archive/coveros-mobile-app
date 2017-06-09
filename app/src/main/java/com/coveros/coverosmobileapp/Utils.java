package com.coveros.coverosmobileapp;

/**
 * Created by maria on 6/9/2017.
 */

public class Utils {
    public static String getCharacterByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }
}
