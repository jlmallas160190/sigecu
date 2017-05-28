/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.seguridad.controller;

import com.megagitel.sigecu.core.ejb.DetalleParametrizacionService;
import com.megagitel.sigecu.core.modelo.DetalleParametrizacion;
import com.megagitel.sigecu.core.modelo.Persona;
import com.megagitel.sigecu.dto.MailDto;
import com.megagitel.sigecu.enumeration.SigecuEnum;
import com.megagitel.sigecu.seguridad.ejb.UsuarioService;
import com.megagitel.sigecu.seguridad.modelo.Usuario;
import com.megagitel.sigecu.util.EmailService;
import com.megagitel.sigecu.util.I18nUtil;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;

/**
 *
 * @author jorgemalla
 */
@Named
@ViewScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "tokenResetPassword",
            pattern = "/tokenResetPassword",
            viewId = "/faces/paginas/seguridad/tokenResetPassword.xhtml"
    )
    ,@URLMapping(
            id = "olvidarClave",
            pattern = "/olvidarClave",
            viewId = "/faces/paginas/seguridad/olvidarClave.xhtml"
    )})
public class ResetPasswordController implements Serializable {

    private Usuario usuario;

    @EJB
    private UsuarioService usuarioService;
    @EJB
    private DetalleParametrizacionService detalleParametrizacionService;

    @PostConstruct
    public void init() {
        this.usuario = new Usuario();
        this.usuario.setPersona(new Persona());
    }

    public String resetPassword() {
        try {
            if (!this.usuario.getConfirmaClave().equals(this.usuario.getClave())) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, I18nUtil.getMessages("password.nomatch"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "";
            }
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
            String token = paramMap.get("token");
            if (token == null) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, I18nUtil.getMessages("token.incorrect"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "";
            }
            List<Usuario> usuarios = this.usuarioService.findByNamedQueryWithLimit("Usuario.findByToken", 0, token);
            Usuario usuarioReset = !usuarios.isEmpty() ? usuarios.get(0) : null;
            if (usuarioReset == null) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, I18nUtil.getMessages("token.incorrect"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "";
            }
            String result = new Sha256Hash(this.usuario.getClave()).toBase64();
            usuarioReset.setClave(result);
            this.usuarioService.create(usuarioReset);
        } catch (Exception e) {
            throw e;
        }
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, I18nUtil.getMessages("password.changed"), null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        return "pretty:login";
    }

    public String olvidarClave() {
        try {
            List<Usuario> usuarios = this.usuarioService.findByNamedQueryWithLimit("Usuario.findByEmail", 0, this.usuario.getPersona().getEmail());
            Usuario usuarioOlvidarClave = !usuarios.isEmpty() ? usuarios.get(0) : null;
            if (usuarioOlvidarClave == null) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, I18nUtil.getMessages("email.noregistred"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "";
            }
            MailDto mailDto = new MailDto();
            List<DetalleParametrizacion> detallesToken = this.detalleParametrizacionService.findByNamedQueryWithLimit("DetalleParametrizacion.findByCodigo", 0, SigecuEnum.DETALLE_PARAM_TOKEN_RESET_PASSWORD.getTipo());
            DetalleParametrizacion detalleParametrizacionToken = !detallesToken.isEmpty() ? detallesToken.get(0) : null;
            List<DetalleParametrizacion> detallesHost = this.detalleParametrizacionService.findByNamedQueryWithLimit("DetalleParametrizacion.findByCodigo", 0, SigecuEnum.DETALLE_PARAM_HOST.getTipo());
            DetalleParametrizacion detalleParametrizacionHost = !detallesHost.isEmpty() ? detallesHost.get(0) : null;
            if (detalleParametrizacionToken == null || detalleParametrizacionHost == null) {
                return "";
            }
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            Object random = rng.nextBytes();
            String result = new Sha256Hash(random).toBase64();
            usuarioOlvidarClave.setToken(result);
            mailDto.setDestino(this.usuario.getPersona().getEmail());
            HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = req.getContextPath();
            mailDto.setMensaje(detalleParametrizacionToken.getValor() + "/" + url + "/" + detalleParametrizacionToken.getValor() + "/" + usuarioOlvidarClave.getToken());
            boolean send = EmailService.enviar(mailDto);
            if (!send) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, I18nUtil.getMessages("email.nosend"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "";
            }
            usuarioService.edit(usuarioOlvidarClave);
        } catch (Exception e) {
            throw e;
        }
        return "pretty:login";
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
