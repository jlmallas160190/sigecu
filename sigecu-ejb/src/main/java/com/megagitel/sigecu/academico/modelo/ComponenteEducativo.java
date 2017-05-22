/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_componente_educativo")
public class ComponenteEducativo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "eliminado", columnDefinition = "boolean default false")
    private Boolean eliminado;
    @NotNull
    @Digits(integer = 12, fraction = 2)
    @Column(name = "creditos")
    private BigDecimal creditos;
    @JoinColumn(name = "grupo_id", referencedColumnName = "id")
    @ManyToOne
    private GrupoComponenteEducativo grupoComponenteEducativo;
    @OneToMany(mappedBy = "componenteEducativo")
    private List<InteresComponenteEducativo> interesComponenteEducativos;

    public ComponenteEducativo() {
    }

    public ComponenteEducativo(String nombre, String descripcion, Boolean eliminado,
            BigDecimal creditos, GrupoComponenteEducativo grupoComponenteEducativo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.eliminado = eliminado;
        this.creditos = creditos;
        this.grupoComponenteEducativo = grupoComponenteEducativo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public BigDecimal getCreditos() {
        return creditos;
    }

    public void setCreditos(BigDecimal creditos) {
        this.creditos = creditos;
    }

    public GrupoComponenteEducativo getGrupoComponenteEducativo() {
        return grupoComponenteEducativo;
    }

    public void setGrupoComponenteEducativo(GrupoComponenteEducativo grupoComponenteEducativo) {
        this.grupoComponenteEducativo = grupoComponenteEducativo;
    }

    public List<InteresComponenteEducativo> getInteresComponenteEducativos() {
        return interesComponenteEducativos;
    }

    public void setInteresComponenteEducativos(List<InteresComponenteEducativo> interesComponenteEducativos) {
        this.interesComponenteEducativos = interesComponenteEducativos;
    }

}
