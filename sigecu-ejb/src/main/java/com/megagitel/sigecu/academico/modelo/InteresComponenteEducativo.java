/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_interes_componente_educativo")
public class InteresComponenteEducativo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Column(name = "eliminado", columnDefinition = "tinyint(1) default 1")
    private Boolean eliminado;
    @JoinColumn(name = "componente_educativo_id", referencedColumnName = "id")
    @ManyToOne
    private ComponenteEducativo componenteEducativo;
    @JoinColumn(name = "estudiante_id", referencedColumnName = "id")
    @ManyToOne
    private Estudiante estudiante;

    public InteresComponenteEducativo() {
    }

    public InteresComponenteEducativo(Boolean eliminado, ComponenteEducativo componenteEducativo,
            Estudiante estudiante) {
        this.eliminado = eliminado;
        this.componenteEducativo = componenteEducativo;
        this.estudiante = estudiante;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public ComponenteEducativo getComponenteEducativo() {
        return componenteEducativo;
    }

    public void setComponenteEducativo(ComponenteEducativo componenteEducativo) {
        this.componenteEducativo = componenteEducativo;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

}
