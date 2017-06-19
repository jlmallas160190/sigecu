/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_oferta_componente_educativo")
@Audited
@AuditTable(value = "academico_oferta_componente_educativo_aud", schema = "audit")
public class OfertaComponenteEducativo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @ManyToOne
    private OfertadorComponenteEducativo ofertadorComponenteEducativo;
    @ManyToOne
    private ComponenteEducativo componenteEducativo;
    @OneToMany(mappedBy = "ofertaComponenteEducativo")
    private List<ComponenteEducativoPlanificado> componenteEducativoPlanificados;

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

    public List<ComponenteEducativoPlanificado> getComponenteEducativoPlanificados() {
        return componenteEducativoPlanificados;
    }

    public void setComponenteEducativoPlanificados(List<ComponenteEducativoPlanificado> componenteEducativoPlanificados) {
        this.componenteEducativoPlanificados = componenteEducativoPlanificados;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OfertaComponenteEducativo)) {
            return false;
        }
        OfertaComponenteEducativo other = (OfertaComponenteEducativo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
}
