/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.seguridad.controller;

import com.megagitel.sigecu.academico.ejb.EstudianteService;
import com.megagitel.sigecu.academico.modelo.Estudiante;
import com.megagitel.sigecu.core.ejb.CatalogoItemService;
import com.megagitel.sigecu.core.ejb.DetalleParametrizacionService;
import com.megagitel.sigecu.core.modelo.CatalogoItem;
import com.megagitel.sigecu.core.modelo.DetalleParametrizacion;
import com.megagitel.sigecu.core.modelo.DireccionPersona;
import com.megagitel.sigecu.dto.MailDto;
import com.megagitel.sigecu.seguridad.ejb.GrupoUsuarioService;
import com.megagitel.sigecu.seguridad.modelo.GrupoUsuario;
import com.megagitel.sigecu.seguridad.modelo.Usuario;
import com.megagitel.sigecu.enumeration.SigecuEnum;
import com.megagitel.sigecu.util.EmailService;
import com.megagitel.sigecu.util.I18nUtil;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.apache.shiro.crypto.hash.Sha256Hash;

/**
 *
 * @author jorgemalla
 */
@Named
@ViewScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "registroUsuario",
            pattern = "/registroUsuario",
            viewId = "/faces/paginas/seguridad/registroUsuario.xhtml"
    )})
public class UsuarioController implements Serializable {

    @EJB
    private CatalogoItemService catalogoItemService;
    @EJB
    private DetalleParametrizacionService detalleParametrizacionService;
    @EJB
    private GrupoUsuarioService grupoUsuarioService;
    @EJB
    private EstudianteService estudianteService;
    @Resource
    private UserTransaction userTransaction;

    private Estudiante estudiante;
    private DireccionPersona direccion;
    private List<CatalogoItem> tiposDocumento;
    private List<CatalogoItem> estadosCiviles;
    private List<CatalogoItem> generos;
    private List<CatalogoItem> nivelesInstruccion;
    private List<CatalogoItem> paises;

    @PostConstruct
    public void init() {
        this.crearUsuario();
        this.crearDireccion();
        this.tiposDocumento = new ArrayList<>();
        this.paises = new ArrayList<>();
        this.estadosCiviles = new ArrayList<>();
        this.generos = new ArrayList<>();
        this.nivelesInstruccion = new ArrayList<>();
    }

    public void crearUsuario() {
        this.estudiante = new Estudiante();
    }

    public void crearDireccion() {
        this.direccion = new DireccionPersona();
    }

    public String guardar() {
        try {
            userTransaction.begin();
            if (this.estudiante.getId() == null) {
                Usuario usuario = new Usuario();
                List<GrupoUsuario> grupoUsuarios = this.grupoUsuarioService.findByNamedQueryWithLimit("GrupoUsuario.findByCodigo", 0, SigecuEnum.ESTUDIANTE.getTipo());
                GrupoUsuario grupoUsuario = !grupoUsuarios.isEmpty() ? grupoUsuarios.get(0) : null;
                List<CatalogoItem> tiposDireccion = this.catalogoItemService.findByNamedQueryWithLimit("CatalogoItem.findByCodigo", 0, SigecuEnum.TIPO_DIRECCION_DOCIMICILIO.getTipo());
                CatalogoItem tipoDireccion = !tiposDireccion.isEmpty() ? tiposDireccion.get(0) : null;
                if (grupoUsuario == null || tipoDireccion == null) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, I18nUtil.getMessages("com.megagitel.sigecu.seguridad.usuario.grabarError"), null);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return "";
                }
                usuario.setNombre(this.estudiante.getEmail());
                String result = new Sha256Hash(this.estudiante.getNumeroIdentificacion()).toHex();
                usuario.setClave(result);
                usuario.setEliminar(Boolean.FALSE);
                usuario.setSuperUsuario(Boolean.FALSE);
                usuario.setToken(result);
                usuario.setGrupoUsuario(grupoUsuario);
                this.direccion.setDescripcion(this.direccion.getReferencia());
                this.direccion.setTipoDireccion(tipoDireccion.getId());
                this.estudiante.getDireccionPersonas().add(direccion);
                this.estudiante.setUsuario(usuario);
                usuario.setPersona(estudiante);
                direccion.setPersona(usuario.getPersona());
                this.estudianteService.create(estudiante);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, I18nUtil.getMessages("com.megagitel.sigecu.seguridad.usuario.grabarExitoso"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                userTransaction.commit();
                boolean send = this.sendTokenResetPassword();
                if (!send) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, I18nUtil.getMessages("email.nosend"), null);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return "";
                }
            }
        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException
                | NotSupportedException | RollbackException | SystemException e) {

            try {
                userTransaction.rollback();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, I18nUtil.getMessages("com.megagitel.sigecu.seguridad.usuario.grabarError"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "";
            } catch (IllegalStateException | SecurityException | SystemException ex) {
                Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return "pretty:login";
    }

    private boolean sendTokenResetPassword() {
        try {
            MailDto mailDto = new MailDto();
            List<DetalleParametrizacion> detallesToken = this.detalleParametrizacionService.findByNamedQueryWithLimit("DetalleParametrizacion.findByCodigo", 0, SigecuEnum.DETALLE_PARAM_TOKEN_RESET_PASSWORD.getTipo());
            DetalleParametrizacion detalleParametrizacionToken = !detallesToken.isEmpty() ? detallesToken.get(0) : null;
            List<DetalleParametrizacion> detallesHost = this.detalleParametrizacionService.findByNamedQueryWithLimit("DetalleParametrizacion.findByCodigo", 0, SigecuEnum.DETALLE_PARAM_HOST.getTipo());
            DetalleParametrizacion detalleParametrizacionHost = !detallesHost.isEmpty() ? detallesHost.get(0) : null;
            if (detalleParametrizacionToken == null || detalleParametrizacionHost == null) {
                return false;
            }
            mailDto.setDestino(this.estudiante.getEmail());
            HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = req.getContextPath();
            mailDto.setMensaje(detalleParametrizacionHost.getValor() + "" + url + "/" + detalleParametrizacionToken.getValor() + "" + this.estudiante.getUsuario().getToken());
            return EmailService.enviar(mailDto);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean validarCedula() {
        if (estudiante.getTipoIdentificacion() == null) {
            return false;
        }
        List<CatalogoItem> cedulas = this.catalogoItemService.findByNamedQueryWithLimit("CatalogoItem.findByCodigo", 0, SigecuEnum.TIPO_DOCUMENTO_CEDULA.getTipo());
        CatalogoItem cedula = !cedulas.isEmpty() ? cedulas.get(0) : null;
        return cedula != null ? Objects.equals(cedula.getId(), estudiante.getTipoIdentificacion()) : false;
    }

    public boolean validarRuc() {
        if (estudiante.getTipoIdentificacion() == null) {
            return false;
        }
        List<CatalogoItem> rucs = this.catalogoItemService.findByNamedQueryWithLimit("CatalogoItem.findByCodigo", 0, SigecuEnum.TIPO_DOCUMENTO_RUC.getTipo());
        CatalogoItem ruc = !rucs.isEmpty() ? rucs.get(0) : null;
        return ruc != null ? Objects.equals(ruc.getId(), estudiante.getTipoIdentificacion()) : false;
    }

    public DireccionPersona getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionPersona direccion) {
        this.direccion = direccion;
    }

    public List<CatalogoItem> getTiposDocumento() {
        if (this.tiposDocumento.isEmpty()) {
            this.tiposDocumento = this.catalogoItemService.findByNamedQueryWithLimit("CatalogoItem.findByCatalogo", 0, SigecuEnum.TIPO_DOCUMENTO.getTipo());
        }
        return tiposDocumento;
    }

    public void setTiposDocumento(List<CatalogoItem> tiposDocumento) {
        this.tiposDocumento = tiposDocumento;
    }

    public List<CatalogoItem> getEstadosCiviles() {
        if (this.estadosCiviles.isEmpty()) {
            this.estadosCiviles = this.catalogoItemService.findByNamedQueryWithLimit("CatalogoItem.findByCatalogo", 0, SigecuEnum.ESTADO_CIVIL.getTipo());
        }
        return estadosCiviles;
    }

    public void setEstadosCiviles(List<CatalogoItem> estadosCiviles) {
        this.estadosCiviles = estadosCiviles;
    }

    public List<CatalogoItem> getGeneros() {
        if (this.generos.isEmpty()) {
            this.generos = this.catalogoItemService.findByNamedQueryWithLimit("CatalogoItem.findByCatalogo", 0, SigecuEnum.GENERO.getTipo());
        }
        return generos;
    }

    public void setGeneros(List<CatalogoItem> generos) {
        this.generos = generos;
    }

    public List<CatalogoItem> getNivelesInstruccion() {
        if (this.nivelesInstruccion.isEmpty()) {
            this.nivelesInstruccion = this.catalogoItemService.findByNamedQueryWithLimit("CatalogoItem.findByCatalogo", 0, SigecuEnum.NIVEL_INSTRUCCION.getTipo());
        }
        return nivelesInstruccion;
    }

    public void setNivelesInstruccion(List<CatalogoItem> nivelesInstruccion) {
        this.nivelesInstruccion = nivelesInstruccion;
    }

    public List<CatalogoItem> getPaises() {
        if (this.paises.isEmpty()) {
            this.paises = this.catalogoItemService.findByNamedQueryWithLimit("CatalogoItem.findByCatalogo", 0, SigecuEnum.PAIS.getTipo());
        }
        return paises;
    }

    public void setPaises(List<CatalogoItem> paises) {
        this.paises = paises;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

}
