/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_ofertador_componente_educativo")
@Audited
@AuditTable(value = "academico_ofertador_componente_educativo_aud", schema = "audit")
@NamedQueries({
    @NamedQuery(name = "OfertadorComponenteEducativo.findByOfertaAcademica", query = "select c FROM OfertadorComponenteEducativo c where c.ofertaAcademica=?1 and c.eliminar=false")
})
public class OfertadorComponenteEducativo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column
    private Boolean eliminar;
    @ManyToOne
    private OfertaAcademica ofertaAcademica;
    @OneToMany(mappedBy = "ofertadorComponenteEducativo", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OfertaComponenteEducativo> ofertaComponenteEducativos;
    @OneToMany(mappedBy = "ofertadorComponenteEducativo", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Paralelo> paralelos;

    public OfertadorComponenteEducativo() {
        this.ofertaComponenteEducativos = new ArrayList<>();
    }

    public void agregarOfertaComponenteEducativo(OfertaComponenteEducativo ofertaComponenteEducativo) {
        if (!this.ofertaComponenteEducativos.contains(ofertaComponenteEducativo)) {
            ofertaComponenteEducativo.setOfertadorComponenteEducativo(this);
            this.ofertaComponenteEducativos.add(ofertaComponenteEducativo);
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public OfertaAcademica getOfertaAcademica() {
        return ofertaAcademica;
    }

    public void setOfertaAcademica(OfertaAcademica ofertaAcademica) {
        this.ofertaAcademica = ofertaAcademica;
    }

    public List<OfertaComponenteEducativo> getOfertaComponenteEducativos() {
        return ofertaComponenteEducativos;
    }

    public void setOfertaComponenteEducativos(List<OfertaComponenteEducativo> ofertaComponenteEducativos) {
        this.ofertaComponenteEducativos = ofertaComponenteEducativos;
    }

    public Boolean getEliminar() {
        return eliminar;
    }

    public void setEliminar(Boolean eliminar) {
        this.eliminar = eliminar;
    }

    public List<Paralelo> getParalelos() {
        return paralelos;
    }

    public void setParalelos(List<Paralelo> paralelos) {
        this.paralelos = paralelos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OfertadorComponenteEducativo)) {
            return false;
        }
        OfertadorComponenteEducativo other = (OfertadorComponenteEducativo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}
