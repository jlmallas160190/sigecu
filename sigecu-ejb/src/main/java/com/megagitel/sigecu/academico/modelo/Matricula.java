/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_matricula")
public class Matricula implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "fecha_registro")
    private Date fechaRegistro;
    @Column(name = "observacion")
    private String observacion;
    @NotNull
    @Column(name = "estado")
    private Integer estado;
    @JoinColumn(name = "estudiante_id", referencedColumnName = "id")
    @ManyToOne
    private Estudiante estudiante;

    public Matricula() {
    }

    public Matricula(Date fechaRegistro, String observacion, Integer estado,
            Estudiante estudiante) {
        this.fechaRegistro = fechaRegistro;
        this.observacion = observacion;
        this.estado = estado;
        this.estudiante = estudiante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

}
