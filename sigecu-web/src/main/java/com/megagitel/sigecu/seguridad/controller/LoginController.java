/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.seguridad.controller;

import com.megagitel.sigecu.seguridad.modelo.Usuario;
import com.megagitel.sigecu.util.I18nUtil;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author jorgemalla
 */
@Named
@RequestScoped
public class LoginController implements Serializable {

    private Usuario usuario;

    @PostConstruct
    public void init() {
        this.usuario = new Usuario();
    }

    public String autenticar() {
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(this.usuario.getNombre(), this.usuario.getClave());
            subject.login(usernamePasswordToken);
            if (subject.isAuthenticated()) {
                return "pretty:inicio";
            }
        } catch (AuthenticationException e) {
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
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
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
