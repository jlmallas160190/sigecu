/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.controller;

import com.megagitel.sigecu.academico.ejb.ComponenteEducativoService;
import com.megagitel.sigecu.academico.ejb.GrupoComponenteEducativoService;
import com.megagitel.sigecu.academico.ejb.OfertaAcademicaService;
import com.megagitel.sigecu.academico.ejb.OfertaComponenteEducativoService;
import com.megagitel.sigecu.academico.ejb.OfertadorComponenteEducativoService;
import com.megagitel.sigecu.academico.modelo.ComponenteEducativo;
import com.megagitel.sigecu.academico.modelo.GrupoComponenteEducativo;
import com.megagitel.sigecu.academico.modelo.OfertaComponenteEducativo;
import com.megagitel.sigecu.academico.modelo.OfertadorComponenteEducativo;
import com.megagitel.sigecu.core.modelo.Institucion;
import com.megagitel.sigecu.seguridad.ejb.UsuarioService;
import com.megagitel.sigecu.seguridad.modelo.Usuario;
import com.megagitel.sigecu.ui.model.LazyOfertadorComponenteEducativoDataModel;
import com.megagitel.sigecu.util.SigecuController;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author jorgemalla
 */
@Named
@ViewScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "ofertadoresComponentesEducativos",
            pattern = "/home/ofertadoresComponentesEducativos",
            viewId = "/faces/paginas/academico/ofertadorComponenteEducativo/ofertadoresComponentesEducativosList.xhtml"
    )
    ,
@URLMapping(
            id = "ofertadorComponenteEducativo",
            pattern = "/home/ofertadorComponenteEducativo",
            viewId = "/faces/paginas/academico/ofertadorComponenteEducativo/ofertadorComponenteEducativoAdmin.xhtml"
    ),})
public class OfertadorComponenteEducativoController extends SigecuController implements Serializable {

    @EJB
    private OfertadorComponenteEducativoService ofertadorComponenteEducativoService;
    @EJB
    private OfertaAcademicaService ofertaAcademicaService;
    @EJB
    private OfertaComponenteEducativoService ofertaComponenteEducativoService;
    @EJB
    private GrupoComponenteEducativoService grupoComponenteEducativoService;
    @EJB
    private ComponenteEducativoService componenteEducativoService;
    @EJB
    private UsuarioService usuarioService;

    private OfertadorComponenteEducativo ofertadorComponenteEducativo;
    private Long ofertadorComponenteEducativoId;
    private OfertaComponenteEducativo ofertaComponenteEducativo;
    private LazyOfertadorComponenteEducativoDataModel lazyOfertadorComponenteEducativoDataModel;
    private List<GrupoComponenteEducativo> grupoComponenteEducativos;
    private GrupoComponenteEducativo grupoComponenteEducativo;
    private String codigoGrupoComponenteEducativo;
    private ComponenteEducativo componenteEducativo;
    private List<ComponenteEducativo> componenteEducativos;

    @PostConstruct
    public void init() {
        this.ofertadorComponenteEducativo = new OfertadorComponenteEducativo();
        this.ofertaComponenteEducativo = new OfertaComponenteEducativo();
        this.ofertaComponenteEducativo = new OfertaComponenteEducativo();
        this.componenteEducativos = new ArrayList<>();
        this.grupoComponenteEducativos = new ArrayList<>();
    }

    public String crear() {
        return "pretty:ofertadorComponenteEducativo";
    }

    public String guardar() {
        try {
            if (this.ofertadorComponenteEducativo.getOfertaAcademica() == null) {
                Date fechaActual = new Date();
                this.ofertadorComponenteEducativo.setOfertaAcademica(this.ofertaAcademicaService.getOfertaAcademicaActual(fechaActual));
            }
            if (this.ofertadorComponenteEducativo.getId() == null) {
                this.ofertadorComponenteEducativoService.create(ofertadorComponenteEducativo);
            } else {
                this.ofertadorComponenteEducativoService.edit(ofertadorComponenteEducativo);
            }
            agregarMensajeExitoso("save.succesful");
        } catch (Exception e) {
            agregarMensajeFatal("save.error");
            return "";
        }
        return "";
    }

    public String desactivar(OfertadorComponenteEducativo oce) {
        try {
            if (oce.getId() != null) {
                oce.setEliminar(Boolean.TRUE);
                this.ofertadorComponenteEducativoService.edit(oce);
            }
        } catch (Exception e) {
            return "";
        }
        agregarMensajeExitoso("remove.succesful");
        return "";
    }

    public String agregarOfertaComponenteEducativo() {
        try {
            if (ofertadorComponenteEducativo != null) {
                this.ofertaComponenteEducativo.setComponenteEducativo(componenteEducativo);
                this.ofertadorComponenteEducativo.agregarOfertaComponenteEducativo(ofertaComponenteEducativo);
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public void filter() {
        if (lazyOfertadorComponenteEducativoDataModel == null) {
            lazyOfertadorComponenteEducativoDataModel = new LazyOfertadorComponenteEducativoDataModel(this.ofertadorComponenteEducativoService);
        }
        lazyOfertadorComponenteEducativoDataModel.setStart(getStart());
        lazyOfertadorComponenteEducativoDataModel.setEnd(getEnd());

    }

    public void seleccionarGrupoComponenteEducativo() {
        this.componenteEducativos = this.componenteEducativoService.findByNamedQueryWithLimit("ComponenteEducativo.findByGrupo", 0, this.grupoComponenteEducativo);
    }

    public OfertadorComponenteEducativo getOfertadorComponenteEducativo() {
        if (ofertadorComponenteEducativoId != null && this.ofertadorComponenteEducativo.getId() == null) {
            ofertadorComponenteEducativo = this.ofertadorComponenteEducativoService.find(ofertadorComponenteEducativoId);
        }
        return ofertadorComponenteEducativo;
    }

    public void setOfertadorComponenteEducativo(OfertadorComponenteEducativo ofertadorComponenteEducativo) {
        this.ofertadorComponenteEducativo = ofertadorComponenteEducativo;
    }

    public Long getOfertadorComponenteEducativoId() {
        return ofertadorComponenteEducativoId;
    }

    public void setOfertadorComponenteEducativoId(Long ofertadorComponenteEducativoId) {
        this.ofertadorComponenteEducativoId = ofertadorComponenteEducativoId;
    }

    public LazyOfertadorComponenteEducativoDataModel getLazyOfertadorComponenteEducativoDataModel() {
        this.filter();
        return lazyOfertadorComponenteEducativoDataModel;
    }

    public void setLazyOfertadorComponenteEducativoDataModel(LazyOfertadorComponenteEducativoDataModel lazyOfertadorComponenteEducativoDataModel) {
        this.lazyOfertadorComponenteEducativoDataModel = lazyOfertadorComponenteEducativoDataModel;
    }

    public OfertaComponenteEducativo getOfertaComponenteEducativo() {
        return ofertaComponenteEducativo;
    }

    public void setOfertaComponenteEducativo(OfertaComponenteEducativo ofertaComponenteEducativo) {
        this.ofertaComponenteEducativo = ofertaComponenteEducativo;
    }

    public List<GrupoComponenteEducativo> getGrupoComponenteEducativos() {
        if (this.grupoComponenteEducativos.isEmpty()) {
            Subject subject = SecurityUtils.getSubject();
            Usuario usuario = null;
            if (subject.isAuthenticated()) {
                List<Usuario> usuarios = this.usuarioService.findByNamedQueryWithLimit("Usuario.findByNombre", 0, subject.getPrincipal());
                usuario = !usuarios.isEmpty() ? usuarios.get(0) : null;
            }
            Institucion institucion = usuario != null && usuario.getGrupoUsuario() != null && usuario.getGrupoUsuario().getInstitucion() != null
                    ? usuario.getGrupoUsuario().getInstitucion() : null;
            this.grupoComponenteEducativos = this.grupoComponenteEducativoService.findByNamedQueryWithLimit("GrupoComponenteEducativo.findByInstitucion", 0, institucion);
        }
        return grupoComponenteEducativos;
    }

    public void setGrupoComponenteEducativos(List<GrupoComponenteEducativo> grupoComponenteEducativos) {
        this.grupoComponenteEducativos = grupoComponenteEducativos;
    }

    public GrupoComponenteEducativo getGrupoComponenteEducativo() {
        return grupoComponenteEducativo;
    }

    public void setGrupoComponenteEducativo(GrupoComponenteEducativo grupoComponenteEducativo) {
        this.grupoComponenteEducativo = grupoComponenteEducativo;
    }

    public ComponenteEducativo getComponenteEducativo() {
        return componenteEducativo;
    }

    public void setComponenteEducativo(ComponenteEducativo componenteEducativo) {
        this.componenteEducativo = componenteEducativo;
    }

    public List<ComponenteEducativo> getComponenteEducativos() {
        return componenteEducativos;
    }

    public void setComponenteEducativos(List<ComponenteEducativo> componenteEducativos) {
        this.componenteEducativos = componenteEducativos;
    }

    public String getCodigoGrupoComponenteEducativo() {
        return codigoGrupoComponenteEducativo;
    }

    public void setCodigoGrupoComponenteEducativo(String codigoGrupoComponenteEducativo) {
        this.codigoGrupoComponenteEducativo = codigoGrupoComponenteEducativo;
    }

}
