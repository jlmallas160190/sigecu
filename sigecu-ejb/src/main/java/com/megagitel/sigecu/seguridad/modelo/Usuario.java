/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.seguridad.modelo;

import com.megagitel.sigecu.core.modelo.Persona;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "seguridad_usuario")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre", unique = true)
    private String nombre;
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "clave")
    private String clave;
    @NotNull
    @Column(name = "super_usuario")
    private Boolean superUsuario;
    @Column(name = "token")
    private String token;
    @Column(name = "eliminado", columnDefinition = "boolean default false")
    private Boolean eliminado;
    @JoinColumn(name = "persona_id", referencedColumnName = "id")
    @OneToOne
    private Persona persona;
    @JoinColumn(name = "grupo_usuario_id", referencedColumnName = "id")
    @ManyToOne
    private GrupoUsuario grupoUsuario;
    @ManyToMany(mappedBy = "usuarios")
    private List<Menu> menus;

    public Usuario() {
    }

    public Usuario(String nombre, String clave, Boolean superUsuario, Persona persona,
            GrupoUsuario grupoUsuario, List<Menu> menus, String token) {
        this.nombre = nombre;
        this.clave = clave;
        this.superUsuario = superUsuario;
        this.token = token;
        this.persona = persona;
        this.grupoUsuario = grupoUsuario;
        this.menus = menus;
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Boolean getSuperUsuario() {
        return superUsuario;
    }

    public void setSuperUsuario(Boolean superUsuario) {
        this.superUsuario = superUsuario;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public GrupoUsuario getGrupoUsuario() {
        return grupoUsuario;
    }

    public void setGrupoUsuario(GrupoUsuario grupoUsuario) {
        this.grupoUsuario = grupoUsuario;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

}
