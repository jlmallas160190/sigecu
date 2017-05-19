/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.core.modelo;

import java.util.Date;

/**
 *
 * @author jorgemalla
 */
public class Persona {

    private Long id;
    private String numeroIdentificacion;
    private String primerNombre;
    private String segundoNombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private Date fechaNacimiento;
    private CatalogoItem tipoIdentificacion;
    private CatalogoItem estadoCivil;
    private CatalogoItem nivelInstuccion;

    public Persona(Long id, String numeroIdentificacion, String primerNombre,
            String segundoNombre, String apellidoPaterno, String apellidoMaterno,
            String email, Date fechaNacimiento, CatalogoItem tipoIdentificacion,
            CatalogoItem estadoCivil, CatalogoItem nivelInstuccion) {
        this.id = id;
        this.numeroIdentificacion = numeroIdentificacion;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoIdentificacion = tipoIdentificacion;
        this.estadoCivil = estadoCivil;
        this.nivelInstuccion = nivelInstuccion;
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

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
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

    public CatalogoItem getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(CatalogoItem tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public CatalogoItem getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(CatalogoItem estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public CatalogoItem getNivelInstuccion() {
        return nivelInstuccion;
    }

    public void setNivelInstuccion(CatalogoItem nivelInstuccion) {
        this.nivelInstuccion = nivelInstuccion;
    }

}
