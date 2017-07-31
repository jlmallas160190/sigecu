/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_matricula")
@Audited
@AuditTable(value = "academico_matricula_aud", schema = "audit")
@NamedQueries({
    @NamedQuery(name = "Matricula.findByOfertaAcademica", query = "select c FROM Matricula c where c.ofertaAcademica=?1 and c.estudiante=?2")
    ,
@NamedQuery(name = "Matricula.findByEstudiante", query = "select c FROM Matricula c where  c.estudiante=?1")})
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
    @Digits(integer = 12, fraction = 4)
    @Column(name = "costo", columnDefinition = "Decimal(10,4) default '0.0000'")
    private BigDecimal costo;
    @JoinColumn(name = "estudiante_id", referencedColumnName = "id")
    @ManyToOne
    private Estudiante estudiante;
    @JoinColumn(name = "oferta_academica_id", referencedColumnName = "id")
    @ManyToOne
    private OfertaAcademica ofertaAcademica;
    @OneToMany(mappedBy = "matricula", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MatriculaComponenteEducativo> matriculaComponenteEducativos;
    @Transient
    private String estadoMatricula;
    

    public Matricula() {
        this.costo = BigDecimal.ZERO;
        this.matriculaComponenteEducativos = new ArrayList<>();
    }

    public Matricula(Date fechaRegistro, String observacion, Integer estado,
            Estudiante estudiante) {
        this.fechaRegistro = fechaRegistro;
        this.observacion = observacion;
        this.estado = estado;
        this.estudiante = estudiante;
    }

    public void agregarMatriculasComponentes(MatriculaComponenteEducativo matriculaComponenteEducativo) {
        if (!this.matriculaComponenteEducativos.contains(matriculaComponenteEducativo)) {
            this.matriculaComponenteEducativos.add(matriculaComponenteEducativo);
            matriculaComponenteEducativo.setMatricula(this);
        }

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

    public List<MatriculaComponenteEducativo> getMatriculaComponenteEducativos() {
        return matriculaComponenteEducativos;
    }

    public void setMatriculaComponenteEducativos(List<MatriculaComponenteEducativo> matriculaComponenteEducativos) {
        this.matriculaComponenteEducativos = matriculaComponenteEducativos;
    }

    public OfertaAcademica getOfertaAcademica() {
        return ofertaAcademica;
    }

    public void setOfertaAcademica(OfertaAcademica ofertaAcademica) {
        this.ofertaAcademica = ofertaAcademica;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public String getEstadoMatricula() {
        return estadoMatricula;
    }

    public void setEstadoMatricula(String estadoMatricula) {
        this.estadoMatricula = estadoMatricula;
    }

}
