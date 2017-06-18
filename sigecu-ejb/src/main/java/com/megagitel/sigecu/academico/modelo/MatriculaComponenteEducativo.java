/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_matricula_componente_educativo")
@Audited
@AuditTable(value = "academico_matricula_componente_educativo_aud", schema = "audit")
public class MatriculaComponenteEducativo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @ManyToOne
    private Matricula matricula;
    @ManyToOne
    private ComponenteEducativoPlanificado componenteEducativoPlanificado;

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

}
