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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "seguridad_usuario")
@Audited
@AuditTable(value = "seguridad_usuario_aud", schema = "audit")
@NamedQueries({
    @NamedQuery(name = "Usuario.findByNombre", query = "select u FROM Usuario u where u.nombre=?1 ORDER BY u.nombre DESC")
    ,@NamedQuery(name = "Usuario.findByToken", query = "select u FROM Usuario u where u.token=?1 ORDER BY u.nombre DESC")
    ,@NamedQuery(name = "Usuario.findByEmail", query = "select u FROM Usuario u where u.persona.email=?1 ORDER BY u.nombre DESC")})
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre", unique = true)
    private String nombre;
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "clave")
    private String clave;
    @NotNull
    @Column(name = "super_usuario")
    private Boolean superUsuario;
    @Column(name = "token")
    private String token;
    @Column(name = "eliminar", columnDefinition = "boolean default false")
    private Boolean eliminar;
    @JoinColumn(name = "persona_id", referencedColumnName = "id")
    @OneToOne
    private Persona persona;
    @JoinColumn(name = "grupo_usuario_id", referencedColumnName = "id")
    @ManyToOne
    private GrupoUsuario grupoUsuario;
    @ManyToMany(mappedBy = "usuarios", fetch = FetchType.EAGER)
    private List<Menu> menus;
    @Column(name = "activo", columnDefinition = "boolean default true")
    private Boolean activo;
    @Transient
    private String confirmaClave;

    public Usuario() {
        this.activo = Boolean.TRUE;
        this.eliminar = Boolean.FALSE;
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

    public Boolean getEliminar() {
        return eliminar;
    }

    public void setEliminar(Boolean eliminar) {
        this.eliminar = eliminar;
    }

    public String getConfirmaClave() {
        return confirmaClave;
    }

    public void setConfirmaClave(String confirmaClave) {
        this.confirmaClave = confirmaClave;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

}
