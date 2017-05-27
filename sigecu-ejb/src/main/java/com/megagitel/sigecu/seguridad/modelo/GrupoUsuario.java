/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.seguridad.modelo;

import com.megagitel.sigecu.core.modelo.Institucion;
import java.io.Serializable;
import java.util.ArrayList;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "seguridad_grupo_usuario")
@NamedQueries({
    @NamedQuery(name = "GrupoUsuario.findByCodigo", query = "select g FROM GrupoUsuario g where g.codigo=?1 ORDER BY g.codigo DESC")})
public class GrupoUsuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "codigo", unique = true)
    private String codigo;
    @Column(name = "descripcion")
    private String descripcion;
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "grupoUsuario")
    private List<Usuario> usuarios;
    @JoinColumn(name = "institucion_id", referencedColumnName = "id")
    @ManyToOne
    private Institucion institucion;
    @Column(name = "eliminado", columnDefinition = "boolean default false")
    private Boolean eliminado;

    public GrupoUsuario() {
    }

    public GrupoUsuario(String codigo, String descripcion, String nombre, Boolean eliminado) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.eliminado = eliminado;
        this.usuarios = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

}
