/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.ejb;

import com.megagitel.sigecu.academico.modelo.Matricula;
import com.megagitel.sigecu.dao.AbstractDao;
import com.megagitel.sigecu.enumeration.SigecuEnum;
import java.io.Serializable;
import javax.ejb.Stateless;

/**
 *
 * @author jorgemalla
 */
@Stateless
public class MatriculaService extends AbstractDao<Matricula> implements Serializable {

    public MatriculaService() {
        super(Matricula.class);
    }

    public String generarCodigoBarra(String tipoCodigo) {
        String tipoCodigoBarra = tipoCodigo;
        long limite = 1000;
        int limiteSubStr = 0;
        if (tipoCodigoBarra.equals(SigecuEnum.TIPO_CODIGO_BARRA_UPCA.getTipo())) {
            limite = 100000;
            limiteSubStr = 11;
        }
        long timeSeed = System.nanoTime();
        double randSeed = Math.random() * limite;
        long midSeed = (long) (timeSeed * randSeed);
        String midSeedStr = midSeed + "";
        String codigo = midSeedStr.substring(0, limiteSubStr);
        return codigo;
    }
}
