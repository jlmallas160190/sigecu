/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author jorgemalla
 */
public class Formateador {

    public String fecha(Date fecha, String formato) {
        SimpleDateFormat formato_fecha = new SimpleDateFormat(formato);
        if (fecha != null) {
            String fecha_formateda = formato_fecha.format(fecha);
            return fecha_formateda;
        } else {
            return "SIN REGISTRO DE FECHA";
        }
    }
}
