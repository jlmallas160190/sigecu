/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.core.ejb;

import com.megagitel.sigecu.core.modelo.Parametrizacion;
import com.megagitel.sigecu.dao.AbstractDao;
import com.megagitel.sigecu.enumeration.SigecuEnum;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

/**
 *
 * @author jorgemalla
 */
@Stateless
public class ParametrizacionService extends AbstractDao<Parametrizacion> implements Serializable {

    private Parametrizacion parametrizacion;

    public ParametrizacionService() {
        super(Parametrizacion.class);
    }

    @PostConstruct
    public void init() {
        this.cargarParametros();
    }

    private void cargarParametros() {
        if (parametrizacion == null) {
            List<Parametrizacion> parametrizacions = this.findByNamedQueryWithLimit("Parametrizacion.findByCodigo", 0, SigecuEnum.PARAMETRIZACION_SISTEMA.getTipo());
            parametrizacion = !parametrizacions.isEmpty() ? parametrizacions.get(0) : null;
        }
    }

    public Parametrizacion getParametrizacion() {
        return parametrizacion;
    }

    public void setParametrizacion(Parametrizacion parametrizacion) {
        this.parametrizacion = parametrizacion;
    }

}
