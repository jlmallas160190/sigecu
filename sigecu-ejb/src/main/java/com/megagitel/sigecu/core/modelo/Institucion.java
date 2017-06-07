/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.core.modelo;

import com.megagitel.sigecu.academico.modelo.GrupoComponenteEducativo;
import com.megagitel.sigecu.academico.modelo.OfertaAcademica;
import com.megagitel.sigecu.seguridad.modelo.GrupoUsuario;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "core_institucion")
public class Institucion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
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
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "numero_identificacion", unique = true)
    private String numeroIdentificacion;
    @NotNull
    @Column(name = "tipo_identificacion")
    private Integer tipoIdentificacion;
    @NotNull
    @Column(name = "actividad_economica")
    private Integer actividadEconomica;
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    @Column(name = "celular")
    private String celular;
    @OneToMany(mappedBy = "institucion")
    private List<GrupoComponenteEducativo> grupoComponenteEducativos;
    @OneToMany(mappedBy = "institucion")
    private List<GrupoUsuario> grupoUsuarios;
    @OneToMany(mappedBy = "institucion")
    private List<OfertaAcademica> ofertaAcademicas;

    public Institucion() {
    }

    public Institucion(String nombre, String descripcion, String numeroIdentificacion,
            Integer tipoIdentificacion, Integer actividadEconomica, String email, String celular) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.numeroIdentificacion = numeroIdentificacion;
        this.tipoIdentificacion = tipoIdentificacion;
        this.actividadEconomica = actividadEconomica;
        this.email = email;
        this.celular = celular;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public Integer getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(Integer tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public Integer getActividadEconomica() {
        return actividadEconomica;
    }

    public void setActividadEconomica(Integer actividadEconomica) {
        this.actividadEconomica = actividadEconomica;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public List<GrupoComponenteEducativo> getGrupoComponenteEducativos() {
        return grupoComponenteEducativos;
    }

    public void setGrupoComponenteEducativos(List<GrupoComponenteEducativo> grupoComponenteEducativos) {
        this.grupoComponenteEducativos = grupoComponenteEducativos;
    }

    public List<GrupoUsuario> getGrupoUsuarios() {
        return grupoUsuarios;
    }

    public void setGrupoUsuarios(List<GrupoUsuario> grupoUsuarios) {
        this.grupoUsuarios = grupoUsuarios;
    }

    public List<OfertaAcademica> getOfertaAcademicas() {
        return ofertaAcademicas;
    }

    public void setOfertaAcademicas(List<OfertaAcademica> ofertaAcademicas) {
        this.ofertaAcademicas = ofertaAcademicas;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
