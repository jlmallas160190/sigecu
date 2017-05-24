/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.seguridad;

import com.megagitel.sigecu.core.modelo.Direccion;
import com.megagitel.sigecu.seguridad.modelo.Usuario;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author jorgemalla
 */
@Named
@RequestScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "registroUsuario",
            pattern = "/registroUsuario",
            viewId = "/faces/paginas/seguridad/registroUsuario.xhtml"
    )})
public class UsuarioController implements Serializable {

    private Usuario usuario;
    private Direccion direccion;

    public void guardar() {

    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

}
