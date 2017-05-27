/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.seguridad.controller;

import com.megagitel.sigecu.core.ejb.CatalogoItemService;
import com.megagitel.sigecu.core.ejb.PersonaService;
import com.megagitel.sigecu.core.modelo.CatalogoItem;
import com.megagitel.sigecu.core.modelo.DireccionPersona;
import com.megagitel.sigecu.core.modelo.Persona;
import com.megagitel.sigecu.seguridad.ejb.GrupoUsuarioService;
import com.megagitel.sigecu.seguridad.ejb.UsuarioService;
import com.megagitel.sigecu.seguridad.modelo.GrupoUsuario;
import com.megagitel.sigecu.seguridad.modelo.Usuario;
import com.megagitel.sigecu.enumeration.SigecuEnum;
import com.megagitel.sigecu.util.I18nUtil;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
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
            id = "registroUsuario",
            pattern = "/registroUsuario",
            viewId = "/faces/paginas/seguridad/registroUsuario.xhtml"
    )})
public class UsuarioController implements Serializable {

    @EJB
    private CatalogoItemService catalogoItemService;
    @EJB
    private UsuarioService usuarioService;
    @EJB
    private GrupoUsuarioService grupoUsuarioService;
    @EJB
    private PersonaService personaService;

    private Usuario usuario;
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
        this.usuario = new Usuario();
        this.usuario.setPersona(new Persona());
    }

    public void crearDireccion() {
        this.direccion = new DireccionPersona();
    }

    public String guardar() {
        try {
            if (this.usuario.getId() == null) {
                List<GrupoUsuario> grupoUsuarios = this.grupoUsuarioService.findByNamedQueryWithLimit("GrupoUsuario.findByCodigo", 0, SigecuEnum.ESTUDIANTE.getTipo());
                GrupoUsuario grupoUsuario = !grupoUsuarios.isEmpty() ? grupoUsuarios.get(0) : null;
                List<CatalogoItem> tiposDireccion = this.catalogoItemService.findByNamedQueryWithLimit("CatalogoItem.findByCodigo", 0, SigecuEnum.TIPO_DIRECCION_DOCIMICILIO.getTipo());
                CatalogoItem tipoDireccion = !tiposDireccion.isEmpty() ? tiposDireccion.get(0) : null;
                if (grupoUsuario == null || tipoDireccion == null) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, I18nUtil.getMessages("com.megagitel.sigecu.seguridad.usuario.grabarError"), null);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return "";
                }
                this.usuario.setNombre(this.usuario.getPersona().getEmail());
                RandomNumberGenerator rng = new SecureRandomNumberGenerator();
                Object salt = rng.nextBytes();
                String result = new Sha256Hash(this.usuario.getPersona().getNumeroIdentificacion(), salt, 1024).toBase64();
                this.usuario.setClave(result);
                this.usuario.setEliminado(Boolean.FALSE);
                this.usuario.setSuperUsuario(Boolean.FALSE);
                this.usuario.setToken(result);
                this.usuario.setGrupoUsuario(grupoUsuario);
                this.direccion.setDescripcion(this.direccion.getReferencia());
                this.direccion.setTipoDireccion(tipoDireccion.getId());
                this.usuario.getPersona().getDireccionPersonas().add(direccion);
                this.personaService.create(usuario.getPersona());
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, I18nUtil.getMessages("com.megagitel.sigecu.seguridad.usuario.grabarExitoso"), null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, I18nUtil.getMessages("com.megagitel.sigecu.seguridad.usuario.grabarError"), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            throw e;
        }
        return "pretty:login";
    }

    public boolean validarCedula() {
        if (usuario.getPersona().getTipoIdentificacion() == null) {
            return false;
        }
        return Integer.parseInt(SigecuEnum.CEDULA.getTipo()) == usuario.getPersona().getTipoIdentificacion();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

}
