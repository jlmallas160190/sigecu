/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_componente_educativo_planificado")
@Audited
@AuditTable(value = "academico_componente_educativo_planificado_aud", schema = "audit")
public class ComponenteEducativoPlanificado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @ManyToOne
    private OfertaComponenteEducativo ofertaComponenteEducativo;
    @ManyToOne
    private Jornada jornada;
    @OneToMany(mappedBy = "componenteEducativoPlanificado")
    private List<MatriculaComponenteEducativo> matriculaComponenteEducativos;

    public ComponenteEducativoPlanificado() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OfertaComponenteEducativo getOfertaComponenteEducativo() {
        return ofertaComponenteEducativo;
    }

    public void setOfertaComponenteEducativo(OfertaComponenteEducativo ofertaComponenteEducativo) {
        this.ofertaComponenteEducativo = ofertaComponenteEducativo;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public List<MatriculaComponenteEducativo> getMatriculaComponenteEducativos() {
        return matriculaComponenteEducativos;
    }

    public void setMatriculaComponenteEducativos(List<MatriculaComponenteEducativo> matriculaComponenteEducativos) {
        this.matriculaComponenteEducativos = matriculaComponenteEducativos;
    }

}
