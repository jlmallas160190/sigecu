/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.controller;

import com.megagitel.sigecu.academico.ejb.OfertaAcademicaService;
import com.megagitel.sigecu.academico.ejb.OfertadorComponenteEducativoService;
import com.megagitel.sigecu.academico.modelo.OfertadorComponenteEducativo;
import com.megagitel.sigecu.ui.model.LazyOfertadorComponenteEducativoDataModel;
import com.megagitel.sigecu.util.SigecuController;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

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

    private OfertadorComponenteEducativo ofertadorComponenteEducativo;
    private Long ofertadorComponenteEducativoId;
    private LazyOfertadorComponenteEducativoDataModel lazyOfertadorComponenteEducativoDataModel;

    @PostConstruct
    public void init() {
        this.ofertadorComponenteEducativo = new OfertadorComponenteEducativo();
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

    public void filter() {
        if (lazyOfertadorComponenteEducativoDataModel == null) {
            lazyOfertadorComponenteEducativoDataModel = new LazyOfertadorComponenteEducativoDataModel(this.ofertadorComponenteEducativoService);
        }
        lazyOfertadorComponenteEducativoDataModel.setStart(getStart());
        lazyOfertadorComponenteEducativoDataModel.setEnd(getEnd());

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

}
