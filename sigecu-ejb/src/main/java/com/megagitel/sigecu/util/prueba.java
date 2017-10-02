/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.util;

/**
 *
 * @author jorgemalla
 */
public class prueba {

    public static void main(String[] args) {
        String eanDigit = "7577306586254";
        String eanWithoutDigit = eanDigit.substring(0, 12);
        String verifyDigit=eanDigit.substring(13);
        int sum = 0;
        for (int i = 0; i < eanWithoutDigit.length(); i++) {
            sum = sum + (Integer.parseInt(String.valueOf(eanWithoutDigit.charAt(i))) * (i % 2 == 0 ? 1 : 3));
        }
        int checkDigit = sum % 10 == 0 ? 0 : (10 - (sum % 10));

        System.out.println(checkDigit);
    }
}
