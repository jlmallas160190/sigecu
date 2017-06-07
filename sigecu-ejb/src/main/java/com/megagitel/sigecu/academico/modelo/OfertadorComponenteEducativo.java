/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.modelo;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_ofertador_componente_educativo")
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
    @ManyToOne
    private OfertaAcademica ofertaAcademica;
    @OneToMany(mappedBy = "ofertadorComponenteEducativo")
    private List<OfertaComponenteEducativo> ofertaComponenteEducativos;

    public OfertadorComponenteEducativo() {
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

}
