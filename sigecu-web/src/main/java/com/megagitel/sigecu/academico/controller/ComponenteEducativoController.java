/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.controller;

import com.megagitel.sigecu.academico.ejb.ComponenteEducativoService;
import com.megagitel.sigecu.academico.ejb.GrupoComponenteEducativoService;
import com.megagitel.sigecu.academico.modelo.ComponenteEducativo;
import com.megagitel.sigecu.academico.modelo.GrupoComponenteEducativo;
import com.megagitel.sigecu.core.ejb.DetalleParametrizacionService;
import com.megagitel.sigecu.core.modelo.Institucion;
import com.megagitel.sigecu.enumeration.SigecuEnum;
import com.megagitel.sigecu.seguridad.ejb.UsuarioService;
import com.megagitel.sigecu.seguridad.modelo.Usuario;
import com.megagitel.sigecu.ui.model.LazyComponenteEducativoDataModel;
import com.megagitel.sigecu.util.SigecuController;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
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
            id = "componentesEducativos",
            pattern = "/admin/componentesEducativos",
            viewId = "/faces/paginas/academico/componenteEducativo/componenteEducativoList.xhtml"
    )
    ,
@URLMapping(
            id = "componenteEducativo",
            pattern = "/admin/componenteEducativo",
            viewId = "/faces/paginas/academico/componenteEducativo/componenteEducativoAdmin.xhtml"
    ),})
public class ComponenteEducativoController extends SigecuController implements Serializable {

    @EJB
    private ComponenteEducativoService componenteEducativoService;
    @EJB
    private GrupoComponenteEducativoService grupoComponenteEducativoService;
    @EJB
    private UsuarioService usuarioService;
    @EJB
    private DetalleParametrizacionService detalleParametrizacionService;

    private ComponenteEducativo componenteEducativo;
    private Integer componenteEducativoId;
    private LazyComponenteEducativoDataModel dataModel;
    private List<GrupoComponenteEducativo> grupoComponenteEducativos;
    private Institucion institucion;
    private String currency;

    @Resource
    private UserTransaction userTransaction;

    @PostConstruct
    public void init() {
        this.componenteEducativo = new ComponenteEducativo();
        this.grupoComponenteEducativos = new ArrayList<>();
    }

    public String crear() {
        return "pretty:componenteEducativo";
    }

    public void filter() {
        if (dataModel == null) {
            dataModel = new LazyComponenteEducativoDataModel(this.componenteEducativoService);
        }
        dataModel.setStart(getStart());
        dataModel.setEnd(getEnd());
    }

    public String guardar() {
        try {
            this.userTransaction.begin();
            String param_duracion = getDetalleParametrizacion(detalleParametrizacionService, SigecuEnum.DETALLE_PARAM_DURACION_CREDITOS.getTipo(), "DURACION_CREDITOS");
            componenteEducativo.setDuracion(componenteEducativo.getCreditos().multiply(param_duracion != null ? new BigDecimal(param_duracion) : BigDecimal.ONE));
            if (this.componenteEducativo.getId() == null) {
                this.componenteEducativoService.create(componenteEducativo);
            } else {
                this.componenteEducativoService.edit(componenteEducativo);
            }
            agregarMensajeExitoso("save.succesful");
            this.userTransaction.commit();
        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException
                | NotSupportedException | RollbackException | SystemException e) {
            agregarMensajeFatal("save.error");
            try {
                this.userTransaction.rollback();
            } catch (IllegalStateException | SecurityException | SystemException ex) {
                Logger.getLogger(ComponenteEducativoController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "";
        }
        return "";
    }

    public String eliminar(ComponenteEducativo componenteEducativo) {
        try {
            if (componenteEducativo.getId() != null) {
                componenteEducativo.setEliminar(Boolean.TRUE);
                this.componenteEducativoService.edit(componenteEducativo);
            }
        } catch (Exception e) {
            agregarMensajeFatal("remove.error");
        }
        agregarMensajeExitoso("remove.succesful");
        return "";
    }

    public ComponenteEducativo getComponenteEducativo() {
        if (this.componenteEducativoId != null && this.componenteEducativo.getId() == null) {
            this.componenteEducativo = componenteEducativoService.find(componenteEducativoId);
        }
        return componenteEducativo;
    }

    public void setComponenteEducativo(ComponenteEducativo componenteEducativo) {
        this.componenteEducativo = componenteEducativo;
    }

    public LazyComponenteEducativoDataModel getDataModel() {
        this.filter();
        return dataModel;
    }

    public void setDataModel(LazyComponenteEducativoDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public List<GrupoComponenteEducativo> getGrupoComponenteEducativos() {
        if (this.grupoComponenteEducativos.isEmpty()) {
            this.grupoComponenteEducativos = this.grupoComponenteEducativoService.findByNamedQueryWithLimit("GrupoComponenteEducativo.findByInstitucion", 0, getInstitucion());
        }
        return grupoComponenteEducativos;
    }

    public void setGrupoComponenteEducativos(List<GrupoComponenteEducativo> grupoComponenteEducativos) {
        this.grupoComponenteEducativos = grupoComponenteEducativos;
    }

    public Institucion getInstitucion() {
        if (this.institucion == null) {
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated()) {
                List<Usuario> usuarios = this.usuarioService.findByNamedQueryWithLimit("Usuario.findByNombre", 0, subject.getPrincipal());
                Usuario usuario = !usuarios.isEmpty() ? usuarios.get(0) : null;
                institucion = usuario != null && usuario.getGrupoUsuario() != null && usuario.getGrupoUsuario().getInstitucion() != null
                        ? usuario.getGrupoUsuario().getInstitucion() : null;
            }
        }
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public String getTableRows() {
        return getDetalleParametrizacion(detalleParametrizacionService, SigecuEnum.DETALLE_PARAM_TABLEROWS.getTipo(), "20");
    }

    public Integer getComponenteEducativoId() {
        return componenteEducativoId;
    }

    public void setComponenteEducativoId(Integer componenteEducativoId) {
        this.componenteEducativoId = componenteEducativoId;
    }

    public String getCurrency() {
        if (currency==null){
            currency=getDetalleParametrizacion(detalleParametrizacionService, SigecuEnum.DETALLE_PARAM_CURRENCY.getTipo(), "");
        }
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
}
