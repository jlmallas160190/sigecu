/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_oferta_componente_educativo")
public class OfertaComponenteEducativo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @ManyToOne
    private OfertadorComponenteEducativo ofertadorComponenteEducativo;
    @ManyToOne
    private ComponenteEducativo componenteEducativo;

    public OfertaComponenteEducativo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OfertadorComponenteEducativo getOfertadorComponenteEducativo() {
        return ofertadorComponenteEducativo;
    }

    public void setOfertadorComponenteEducativo(OfertadorComponenteEducativo ofertadorComponenteEducativo) {
        this.ofertadorComponenteEducativo = ofertadorComponenteEducativo;
    }

    public ComponenteEducativo getComponenteEducativo() {
        return componenteEducativo;
    }

    public void setComponenteEducativo(ComponenteEducativo componenteEducativo) {
        this.componenteEducativo = componenteEducativo;
    }

}
