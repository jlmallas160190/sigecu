/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.core.modelo;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "core_direccion_persona")
public class DireccionPersona extends Direccion {

    @JoinColumn(name = "persona_id", referencedColumnName = "id")
    @ManyToOne
    private Persona persona;

    public DireccionPersona() {
    }

    public DireccionPersona(Persona persona) {
        this.persona = persona;
    }

    public DireccionPersona(Persona persona, String descripcion, String referencia,
            Integer tipoDireccion, String telefono, String ciudad, Integer pais) {
        super(descripcion, referencia, tipoDireccion, telefono, ciudad, pais);
        this.persona = persona;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

}
