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
public class ValidadorCampos {

    public static final String EMAIL_REGEX = "(?:[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e"
            + "-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[A-Za-z0-9](?:[A-Za-z0-9-]"
            + "*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4]"
            + "[0-9]|[01]?[0-9][0-9]?|[A-Za-z0-9-]*[A-Za-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b"
            + "\\x0c\\x0e-\\x7f])+)\\])";

    public static boolean esEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    public static boolean esPassword(final String password) {
        return esPassword(password, 8);
    }

    public static boolean esPassword(final String password, final int length) {
        if (password != null) {
            return password.matches("^[a-zA-Z0-9!@#$%^&*\\s\\(\\)_\\+=-]{" + length + ",}$");
        }
        return false;
    }

    public static boolean esRuc(String ruc) {
        if (ruc.length() == 13) {
            String cedula = ruc.substring(0, 10);
            String verificador = ruc.substring(10, 13);
            if (esNumeroIdentificacionNacional(cedula) && verificador.equals("001")) {
                return true;
            }
        }
        return false;
    }

    public static boolean esNumeroIdentificacionNacional(String numeroIdentificacion) {
        int dig1;
        int operacion1;
        int dig2;
        int operacion2;
        int dig3;
        int operacion3;
        int dig4;
        int operacion4;
        int dig5;
        int operacion5;
        int dig6;
        int operacion6;
        int dig7;
        int operacion7;
        int dig8;
        int operacion8;
        int dig9;
        int operacion9;
        int suma;
        int respuesta;
        int digVerificador;
        char A;
        int contador = 0;
        boolean bandera = false;

//        if (numeroIdentificacion.length() == 13 && tipoDocumentacion.equals(SigecuEnum.TIPO_DOCUMENTO_RUC.getTipo())) {
//            ruc = numeroIdentificacion.substring(10, 13);
//            numeroIdentificacion = numeroIdentificacion.substring(0, 10);
//        }
        if (numeroIdentificacion.length() == 10) {
            for (int i = 0; i < numeroIdentificacion.length(); i++) {
                A = numeroIdentificacion.charAt(i);
                if (Character.isDigit(A)) {
                    contador++;
                }
                if (contador == 10) {
                    dig1 = Integer.valueOf(numeroIdentificacion.substring(0, 1));
                    dig2 = Integer.valueOf(numeroIdentificacion.substring(1, 2));
                    dig3 = Integer.valueOf(numeroIdentificacion.substring(2, 3));
                    dig4 = Integer.valueOf(numeroIdentificacion.substring(3, 4));
                    dig5 = Integer.valueOf(numeroIdentificacion.substring(4, 5));
                    dig6 = Integer.valueOf(numeroIdentificacion.substring(5, 6));
                    dig7 = Integer.valueOf(numeroIdentificacion.substring(6, 7));
                    dig8 = Integer.valueOf(numeroIdentificacion.substring(7, 8));
                    dig9 = Integer.valueOf(numeroIdentificacion.substring(8, 9));
                    digVerificador = Integer.valueOf(numeroIdentificacion.substring(9, 10));
                    if (dig3 < 6) {
                        //personas naturales.
                        operacion1 = dig1 * 2;
                        if (operacion1 >= 10) {
                            operacion1 -= 9;
                        }
                        operacion2 = dig2 * 1;
                        if (operacion2 >= 10) {
                            operacion2 -= 9;
                        }
                        operacion3 = dig3 * 2;
                        if (operacion3 >= 10) {
                            operacion3 -= 9;
                        }
                        operacion4 = dig4 * 1;
                        if (operacion4 >= 10) {
                            operacion4 -= 9;
                        }
                        operacion5 = dig5 * 2;
                        if (operacion5 >= 10) {
                            operacion5 -= 9;
                        }
                        operacion6 = dig6 * 1;
                        if (operacion6 >= 10) {
                            operacion6 -= 9;
                        }
                        operacion7 = dig7 * 2;
                        if (operacion7 >= 10) {
                            operacion7 -= 9;
                        }
                        operacion8 = dig8 * 1;
                        if (operacion8 >= 10) {
                            operacion8 -= 9;
                        }
                        operacion9 = dig9 * 2;
                        if (operacion9 >= 10) {
                            operacion9 -= 9;
                        }
                        suma = operacion1 + operacion2 + operacion3 + operacion4 + operacion5 + operacion6 + operacion7 + operacion8 + operacion9;
                        respuesta = suma % 10;
                        if (respuesta == 0) {
                            bandera = respuesta == digVerificador;
                        } else {
                            respuesta = 10 - respuesta;
                            bandera = respuesta == digVerificador;
                        }
                    } else {
                        if (dig3 == 7 || dig3 == 8) {
                            bandera = false;
                        }
//                        } else {
//                            if (dig3 == 6) {
//                                //sociedades publicas
//                            } else {
//                                if (dig3 == 9) {
//                                    //sociedades privadas
//                                }
//                            }
//                        }
                    }
                } else {
                    bandera = false;
                }
            }
        }

        return bandera;

    }
}
