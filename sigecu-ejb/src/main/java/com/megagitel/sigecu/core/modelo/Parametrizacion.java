/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.core.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "core_parametrizacion")
@Audited
@AuditTable(value = "core_parametrizacion_aud", schema = "audit")
public class Parametrizacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "codigo", unique = true)
    private String codigo;
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "parametrizacion")
    private List<DetalleParametrizacion> detalleParametrizacions;

    public Parametrizacion() {
    }

    public Parametrizacion(String codigo, String nombre, String descripcion,
            List<DetalleParametrizacion> detalleParametrizacions) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.detalleParametrizacions = detalleParametrizacions;
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

    public List<DetalleParametrizacion> getDetalleParametrizacions() {
        return detalleParametrizacions;
    }

    public void setDetalleParametrizacions(List<DetalleParametrizacion> detalleParametrizacions) {
        this.detalleParametrizacions = detalleParametrizacions;
    }

}
