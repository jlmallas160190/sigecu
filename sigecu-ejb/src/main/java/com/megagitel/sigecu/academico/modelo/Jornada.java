/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.modelo;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_jornada")
@Audited
@AuditTable(value = "academico_jornada_aud", schema = "audit")
@NamedQueries({
    @NamedQuery(name = "Jornada.findByOfertaActual", query = "select c FROM Jornada c where c.paralelo.eliminar=false and ?1 BETWEEN "
            + "c.paralelo.ofertadorComponenteEducativo.ofertaAcademica.fechaInicio  and c.paralelo.ofertadorComponenteEducativo.ofertaAcademica.fechaFin")
})
public class Jornada implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    @Column(name = "fecha_inicio")
    private Date fechaInicio;
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    @Column(name = "fecha_fin")
    private Date fechaFin;
    @Column(name = "eliminar", columnDefinition = "boolean default false")
    private Boolean eliminar;
    @ManyToOne
    private Paralelo paralelo;
    @OneToMany(mappedBy = "jornada", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ComponenteEducativoPlanificado> componenteEducativoPlanificados;

    public Jornada() {
    }

    public void agregarComponenteEducativoPlanificado(ComponenteEducativoPlanificado componenteEducativoPlanificado) {
        if (!this.componenteEducativoPlanificados.contains(componenteEducativoPlanificado)) {
            componenteEducativoPlanificado.setJornada(this);
            this.componenteEducativoPlanificados.add(componenteEducativoPlanificado);
        }
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

    public Paralelo getParalelo() {
        return paralelo;
    }

    public void setParalelo(Paralelo paralelo) {
        this.paralelo = paralelo;
    }

    public List<ComponenteEducativoPlanificado> getComponenteEducativoPlanificados() {
        return componenteEducativoPlanificados;
    }

    public void setComponenteEducativoPlanificados(List<ComponenteEducativoPlanificado> componenteEducativoPlanificados) {
        this.componenteEducativoPlanificados = componenteEducativoPlanificados;
    }

    public Boolean getEliminar() {
        return eliminar;
    }

    public void setEliminar(Boolean eliminar) {
        this.eliminar = eliminar;
    }

}
