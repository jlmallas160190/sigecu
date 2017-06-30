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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_componente_educativo")
@Audited
@AuditTable(value = "academico_componente_educativo_aud", schema = "audit")
@NamedQueries({
    @NamedQuery(name = "ComponenteEducativo.findByGrupo", query = "select c FROM ComponenteEducativo c where c.grupoComponenteEducativo=?1 and c.eliminar=false")
    ,
    @NamedQuery(name = "ComponenteEducativo.findByCodigo", query = "select c FROM ComponenteEducativo c where c.codigo=?1 and c.eliminar=false")
})
public class ComponenteEducativo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "codigo")
    private String codigo;
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "eliminar", columnDefinition = "boolean default false")
    private Boolean eliminar;
    @NotNull
    @Digits(integer = 12, fraction = 2)
    @Column(name = "creditos")
    private BigDecimal creditos;
    @JoinColumn(name = "grupo_id", referencedColumnName = "id")
    @ManyToOne
    private GrupoComponenteEducativo grupoComponenteEducativo;
    @OneToMany(mappedBy = "componenteEducativo")
    private List<InteresComponenteEducativo> interesComponenteEducativos;
    @OneToMany(mappedBy = "componenteEducativo")
    private List<OfertaComponenteEducativo> ofertaComponenteEducativos;

    public ComponenteEducativo() {
    }

    public ComponenteEducativo(String nombre, String descripcion, Boolean eliminado,
            BigDecimal creditos, GrupoComponenteEducativo grupoComponenteEducativo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.eliminar = eliminado;
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

    public Boolean getEliminar() {
        return eliminar;
    }

    public void setEliminar(Boolean eliminar) {
        this.eliminar = eliminar;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<OfertaComponenteEducativo> getOfertaComponenteEducativos() {
        return ofertaComponenteEducativos;
    }

    public void setOfertaComponenteEducativos(List<OfertaComponenteEducativo> ofertaComponenteEducativos) {
        this.ofertaComponenteEducativos = ofertaComponenteEducativos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ComponenteEducativo)) {
            return false;
        }
        ComponenteEducativo other = (ComponenteEducativo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return this.codigo;
    }
}
