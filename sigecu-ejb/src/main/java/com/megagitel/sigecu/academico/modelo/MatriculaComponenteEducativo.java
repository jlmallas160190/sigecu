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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_matricula_componente_educativo")
@NamedQueries({
    @NamedQuery(name = "MatriculaComponenteEducativo.findMatricula", query = "select c FROM MatriculaComponenteEducativo c where c.matricula=?1")
    ,
    @NamedQuery(name = "MatriculaComponenteEducativo.findCodigo", query = "select c FROM MatriculaComponenteEducativo c where c.codigo=?1")
})
@Audited
@AuditTable(value = "academico_matricula_componente_educativo_aud", schema = "audit")
public class MatriculaComponenteEducativo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @Column(name = "codigo", unique = true)
    private String codigo;
    @NotNull
    @Column(name = "estado")
    private Integer estado;
    @ManyToOne
    private Matricula matricula;
    @ManyToOne
    private ComponenteEducativoPlanificado componenteEducativoPlanificado;
    @Transient
    private String estadoMatricula;

    public MatriculaComponenteEducativo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public ComponenteEducativoPlanificado getComponenteEducativoPlanificado() {
        return componenteEducativoPlanificado;
    }

    public void setComponenteEducativoPlanificado(ComponenteEducativoPlanificado componenteEducativoPlanificado) {
        this.componenteEducativoPlanificado = componenteEducativoPlanificado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getEstadoMatricula() {
        return estadoMatricula;
    }

    public void setEstadoMatricula(String estadoMatricula) {
        this.estadoMatricula = estadoMatricula;
    }

}
