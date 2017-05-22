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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_grupo_componente_educativo")
public class GrupoComponenteEducativo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "eliminado", columnDefinition = "boolean default false")
    private Boolean eliminado;
    @OneToMany(mappedBy = "grupoComponenteEducativo")
    private List<GrupoComponenteEducativo> grupoComponenteEducativos;
    @JoinColumn(name = "grupo_id", referencedColumnName = "id")
    @ManyToOne
    private GrupoComponenteEducativo grupoComponenteEducativo;
    @JoinColumn(name = "institucion_id", referencedColumnName = "id")
    @ManyToOne
    private Institucion institucion;
    @OneToMany(mappedBy = "grupoComponenteEducativo")
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

}
