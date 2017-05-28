/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.core.ejb;

import com.megagitel.sigecu.core.modelo.DetalleParametrizacion;
import com.megagitel.sigecu.dao.AbstractDao;
import java.io.Serializable;
import javax.ejb.Stateless;

/**
 *
 * @author jorgemalla
 */
@Stateless
public class DetalleParametrizacionService extends AbstractDao<DetalleParametrizacion> implements Serializable {

    public DetalleParametrizacionService() {
        super(DetalleParametrizacion.class);
    }

}
