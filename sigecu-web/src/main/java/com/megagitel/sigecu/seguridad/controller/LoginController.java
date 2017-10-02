/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.seguridad.controller;

import com.megagitel.sigecu.seguridad.ejb.UsuarioService;
import com.megagitel.sigecu.seguridad.modelo.Usuario;
import com.megagitel.sigecu.util.I18nUtil;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author jorgemalla
 */
@Named
@RequestScoped
public class LoginController implements Serializable {

    private Usuario usuario;

    @EJB
    private UsuarioService usuarioService;

    @PostConstruct
    public void init() {
        this.usuario = new Usuario();
    }

    public String autenticar() {
        try {
            if (usuarioService.autenticar(this.usuario.getNombre(), this.usuario.getClave())) {
                return "pretty:inicio";
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, I18nUtil.getMessages("apache.shiro.authenticationException"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "";
        }
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, I18nUtil.getMessages("apache.shiro.authenticationException"), null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        return "";
    }

    public String logout() {
        try {
            this.usuarioService.logout();
        } catch (Exception e) {
            throw e;
        }
        return "pretty:login";
    }

    public Usuario getUsuario() {
        if (usuario == null) {
            usuario = new Usuario();
        }
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
