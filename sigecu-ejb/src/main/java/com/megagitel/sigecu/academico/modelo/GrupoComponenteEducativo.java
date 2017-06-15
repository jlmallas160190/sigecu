/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.modelo;

import com.megagitel.sigecu.core.modelo.Institucion;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_grupo_componente_educativo")
@Audited
@AuditTable(value = "academico_grupo_componente_educativo_aud", schema = "audit")
@NamedQueries({
    @NamedQuery(name = "GrupoComponenteEducativo.findByInstitucion", query = "select c FROM GrupoComponenteEducativo c where c.institucion=?1 and c.eliminado=false")
    ,
    @NamedQuery(name = "GrupoComponenteEducativo.findByCodigo", query = "select c FROM GrupoComponenteEducativo c where c.codigo=?1 and c.eliminado=false")
})
public class GrupoComponenteEducativo implements Serializable {

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
    @Column(name = "eliminado", columnDefinition = "boolean default false")
    private Boolean eliminado;
    @OneToMany(mappedBy = "grupoComponenteEducativo", fetch = FetchType.LAZY)
    private List<GrupoComponenteEducativo> grupoComponenteEducativos;
    @JoinColumn(name = "grupo_id", referencedColumnName = "id")
    @ManyToOne
    private GrupoComponenteEducativo grupoComponenteEducativo;
    @JoinColumn(name = "institucion_id", referencedColumnName = "id")
    @ManyToOne
    private Institucion institucion;
    @OneToMany(mappedBy = "grupoComponenteEducativo", fetch = FetchType.LAZY)
    private List<ComponenteEducativo> componenteEducativos;

    public GrupoComponenteEducativo() {
    }

    public GrupoComponenteEducativo(List<ComponenteEducativo> componenteEducativos,
            String nombre, String descripcion, Boolean eliminado,
            List<GrupoComponenteEducativo> grupoComponenteEducativos,
            GrupoComponenteEducativo grupoComponenteEducativo, Institucion institucion) {
        this.componenteEducativos = componenteEducativos;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.eliminado = eliminado;
        this.grupoComponenteEducativos = grupoComponenteEducativos;
        this.grupoComponenteEducativo = grupoComponenteEducativo;
        this.institucion = institucion;
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

    public List<GrupoComponenteEducativo> getGrupoComponenteEducativos() {
        return grupoComponenteEducativos;
    }

    public void setGrupoComponenteEducativos(List<GrupoComponenteEducativo> grupoComponenteEducativos) {
        this.grupoComponenteEducativos = grupoComponenteEducativos;
    }

    public GrupoComponenteEducativo getGrupoComponenteEducativo() {
        return grupoComponenteEducativo;
    }

    public void setGrupoComponenteEducativo(GrupoComponenteEducativo grupoComponenteEducativo) {
        this.grupoComponenteEducativo = grupoComponenteEducativo;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public List<ComponenteEducativo> getComponenteEducativos() {
        return componenteEducativos;
    }

    public void setComponenteEducativos(List<ComponenteEducativo> componenteEducativos) {
        this.componenteEducativos = componenteEducativos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GrupoComponenteEducativo)) {
            return false;
        }
        GrupoComponenteEducativo other = (GrupoComponenteEducativo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return this.codigo;
    }
}
