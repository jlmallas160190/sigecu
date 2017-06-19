/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.modelo;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_paralelo")
@Audited
@AuditTable(value = "academico_paralelo_aud", schema = "audit")
@NamedQueries({
    @NamedQuery(name = "Paralelo.findByOfertador", query = "select c FROM Paralelo c where c.ofertadorComponenteEducativo=?1 and c.eliminar=false")
})
public class Paralelo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "seccion")
    private Integer seccion;
    @Column(name = "eliminar", columnDefinition = "boolean default false")
    private Boolean eliminar;
    @Column(name = "numero_maximo_matriculados")
    @NotNull
    private Integer numeroMaximoMatriculados;
    @ManyToOne
    @NotNull
    private OfertadorComponenteEducativo ofertadorComponenteEducativo;
    @OneToMany(mappedBy = "paralelo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Jornada> jornadas;

    public Paralelo() {
        this.eliminar = Boolean.FALSE;
    }

    public List<Jornada> getJornadas() {
        return jornadas;
    }

    public void setJornadas(List<Jornada> jornadas) {
        this.jornadas = jornadas;
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

    public Integer getSeccion() {
        return seccion;
    }

    public void setSeccion(Integer seccion) {
        this.seccion = seccion;
    }

    public OfertadorComponenteEducativo getOfertadorComponenteEducativo() {
        return ofertadorComponenteEducativo;
    }

    public void setOfertadorComponenteEducativo(OfertadorComponenteEducativo ofertadorComponenteEducativo) {
        this.ofertadorComponenteEducativo = ofertadorComponenteEducativo;
    }

    public Boolean getEliminar() {
        return eliminar;
    }

    public void setEliminar(Boolean eliminar) {
        this.eliminar = eliminar;
    }

    public Integer getNumeroMaximoMatriculados() {
        return numeroMaximoMatriculados;
    }

    public void setNumeroMaximoMatriculados(Integer numeroMaximoMatriculados) {
        this.numeroMaximoMatriculados = numeroMaximoMatriculados;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Paralelo)) {
            return false;
        }
        Paralelo other = (Paralelo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return this.id + "";
    }
}
