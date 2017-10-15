/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utiles;

/**
 *
 * @author Jairleo95
 */
public class conversor {

    public static String toJavaFormat(String wordConvert, String split) {

        String[] word = wordConvert.split(split);
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

    public static String firstCharacterToUpper(String cadena) {
        char[] caracteres = cadena.toCharArray();
        for (int ii = 0; ii < caracteres.length; ii++) {
            if (ii == 0) {
                caracteres[0] = Character.toUpperCase(caracteres[0]);
            }
        }

        return new String(caracteres);
    }
}
