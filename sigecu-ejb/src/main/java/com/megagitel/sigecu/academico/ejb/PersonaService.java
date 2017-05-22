/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.ejb;

import com.megagitel.sigecu.core.modelo.Persona;
import com.megagitel.sigecu.dao.AbstractDao;
import javax.ejb.Stateless;

/**
 *
 * @author jorgemalla
 */
@Stateless
public class PersonaService extends AbstractDao<Persona> {

    public PersonaService() {
        super(Persona.class);
    }
}
