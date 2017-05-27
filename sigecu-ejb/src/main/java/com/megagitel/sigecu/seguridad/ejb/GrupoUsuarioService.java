/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.seguridad.ejb;

import com.megagitel.sigecu.dao.AbstractDao;
import com.megagitel.sigecu.seguridad.modelo.GrupoUsuario;
import java.io.Serializable;
import javax.ejb.Stateless;

/**
 *
 * @author jorgemalla
 */
@Stateless
public class GrupoUsuarioService extends AbstractDao<GrupoUsuario> implements Serializable {

    public GrupoUsuarioService() {
        super(GrupoUsuario.class);
    }

}
