/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.core.modelo;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "core_direccion")
@Inheritance(strategy = InheritanceType.JOINED)
public class Direccion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "referencia")
    private String referencia;
    @NotNull
    @Column(name = "tipo_direccion")
    private Integer tipoDireccion;
    @Column(name = "telefono")
    private String telefono;
    @NotNull
    @Column(name = "pais")
    private Integer pais;
    @NotNull
    @Column(name = "ciudad")
    private String ciudad;

    public Direccion() {
    }

    public Direccion(String descripcion, String referencia, Integer tipoDireccion,
            String telefono, String ciudad, Integer pais) {
        this.descripcion = descripcion;
        this.referencia = referencia;
        this.tipoDireccion = tipoDireccion;
        this.telefono = telefono;
        this.pais = pais;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Integer getTipoDireccion() {
        return tipoDireccion;
    }

    public void setTipoDireccion(Integer tipoDireccion) {
        this.tipoDireccion = tipoDireccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

}
