/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.ejb;

import com.megagitel.sigecu.academico.modelo.ComponenteEducativo;
import com.megagitel.sigecu.dao.AbstractDao;
import java.io.Serializable;
import javax.ejb.Stateless;

/**
 *
 * @author jorgemalla
 */
@Stateless
public class ComponenteEducativoService extends AbstractDao<ComponenteEducativo> implements Serializable {

    public ComponenteEducativoService() {
        super(ComponenteEducativo.class);
    }

}
