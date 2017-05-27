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
@Table(name = "core_catalogo_item")
@NamedQueries({
    @NamedQuery(name = "CatalogoItem.findByCodigo", query = "select c FROM CatalogoItem c where c.codigo=?1 ORDER BY c.codigo DESC")
    ,@NamedQuery(name = "CatalogoItem.findByCatalogo", query = "select c FROM CatalogoItem c where c.catalogo.codigo=?1 ORDER BY c.codigo DESC")})
public class CatalogoItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "codigo", unique = true)
    private String codigo;
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre")
    private String nombre;
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "catalogo_id", referencedColumnName = "id")
    @ManyToOne
    private Catalogo catalogo;
    @Column(name = "eliminado", columnDefinition = "boolean default false")
    private Boolean eliminado;

    public CatalogoItem() {
    }

    public CatalogoItem(String codigo, String nombre, String descripcion,
            Catalogo catalogo, Boolean eliminado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.catalogo = catalogo;
        this.eliminado = eliminado;
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

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

}
