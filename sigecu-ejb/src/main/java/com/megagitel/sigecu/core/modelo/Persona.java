/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.core.modelo;

import com.megagitel.sigecu.seguridad.modelo.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "core_persona")
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "numero_identificacion", unique = true)
    private String numeroIdentificacion;
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "primer_nombre")
    private String primerNombre;
    @Column(name = "segundo_nombre")
    private String segundoNombre;
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "primer_apellido")
    private String primerApellido;
    @Column(name = "segundo_apellido")
    private String segundoApellido;
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "email")
    private String email;
    @Column(name = "celular")
    private String celular;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;
    @NotNull
    @Column(name = "tipo_identificacion")
    private Integer tipoIdentificacion;
    @NotNull
    @Column(name = "estado_civil")
    private Integer estadoCivil;
    @NotNull
    @Column(name = "nivel_instruccion")
    private Integer nivelInstuccion;
    @NotNull
    @Column(name = "genero")
    private Integer genero;
    @OneToMany(mappedBy = "persona")
    private List<DireccionPersona> direccionPersonas;
    @OneToOne(mappedBy = "persona")
    private Usuario usuario;

    public Persona() {
    }

    public Persona(Long id, String numeroIdentificacion, String primerNombre,
            String segundoNombre, String primerApellido, String segundoApellido,
            String email, Date fechaNacimiento, Integer tipoIdentificacion,
            Integer estadoCivil, Integer nivelInstuccion, Integer genero,
            String celular,Usuario usuario) {
        this.id = id;
        this.numeroIdentificacion = numeroIdentificacion;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.email = email;
        this.celular = celular;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoIdentificacion = tipoIdentificacion;
        this.estadoCivil = estadoCivil;
        this.nivelInstuccion = nivelInstuccion;
        this.genero = genero;
        this.direccionPersonas = new ArrayList<>();
        this.usuario=usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(Integer tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public Integer getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(Integer estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Integer getNivelInstuccion() {
        return nivelInstuccion;
    }

    public void setNivelInstuccion(Integer nivelInstuccion) {
        this.nivelInstuccion = nivelInstuccion;
    }

    public Integer getGenero() {
        return genero;
    }

    public void setGenero(Integer genero) {
        this.genero = genero;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public List<DireccionPersona> getDireccionPersonas() {
        return direccionPersonas;
    }

    public void setDireccionPersonas(List<DireccionPersona> direccionPersonas) {
        this.direccionPersonas = direccionPersonas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
