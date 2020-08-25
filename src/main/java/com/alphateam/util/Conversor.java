/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.util;

/**
 *
 * @author Jairleo95
 */
public class Conversor {

    public static String toJavaFormat(String wordConvert, String split) {

        String[] word = wordConvert.toLowerCase().split(split);
        String acum = "";
        for (int ff = 0; ff < word.length; ff++) {
            if (ff == 0) {
                acum = word[ff];
            } else {
                acum = acum + firstCharacterToUpper(word[ff]);
            }
        }
        return acum;
    }

    public static String firstCharacterToUpper(String x) {
        char[] c = x.toCharArray();
        for (int ii = 0; ii < c.length; ii++) {
            if (ii == 0) {
                c[0] = Character.toUpperCase(c[0]);
            }
        }

        return new String(c);
    }
    public static String extractCharacters(String string, int l) {
        char[] c = string.toCharArray();
        String acum = "";
        if (c.length<=l){
             return string;
        }else{
            for (int i = 0; i < l; i++) {
                acum += c[i];
            }

            return acum;
        }

    }
}
