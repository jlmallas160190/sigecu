/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.modelo;

import com.megagitel.sigecu.core.modelo.Institucion;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_oferta_academica")
public class OfertaAcademica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    private Date fechaInicio;
    @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaFin;
    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;
    @ManyToOne
    private Institucion institucion;
    @OneToMany(mappedBy = "ofertaAcademica")
    private List<OfertadorComponenteEducativo> ofertadorComponenteEducativos;

    public OfertaAcademica() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public List<OfertadorComponenteEducativo> getOfertadorComponenteEducativos() {
        return ofertadorComponenteEducativos;
    }

    public void setOfertadorComponenteEducativos(List<OfertadorComponenteEducativo> ofertadorComponenteEducativos) {
        this.ofertadorComponenteEducativos = ofertadorComponenteEducativos;
    }

}
