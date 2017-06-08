/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.controller;

import com.megagitel.sigecu.academico.ejb.OfertaAcademicaService;
import com.megagitel.sigecu.academico.modelo.OfertaAcademica;
import com.megagitel.sigecu.core.ejb.DetalleParametrizacionService;
import com.megagitel.sigecu.core.modelo.DetalleParametrizacion;
import com.megagitel.sigecu.ui.model.LazyOfertaAcademicaDataModel;
import com.megagitel.sigecu.util.HomeController;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
import java.util.List;
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
            id = "ofertasAcademicas",
            pattern = "/home/ofertasAcademicas",
            viewId = "/faces/paginas/academico/ofertaAcademica/ofertasAcademicasList.xhtml"
    )
    ,
@URLMapping(
            id = "ofertaAcademica",
            pattern = "/home/ofertaAcademica",
            viewId = "/faces/paginas/academico/ofertaAcademica/ofertaAcademicaAdmin.xhtml"
    ),
})
public class OfertaAcademicaController extends HomeController implements Serializable {
    
    private OfertaAcademica ofertaAcademica;
    private LazyOfertaAcademicaDataModel lazyOfertaAcademicaDataModel;
    
    @EJB
    private OfertaAcademicaService ofertaAcademicaService;
    @EJB
    private DetalleParametrizacionService detalleParametrizacionService;
    private Long ofertaAcademicaId;
    
    @PostConstruct
    public void init() {
        this.ofertaAcademicaId = Long.parseLong("0");
        setOfertaAcademica(new OfertaAcademica());
    }
    
    public OfertaAcademica getOfertaAcademica() {
        if (this.ofertaAcademicaId != null) {
            this.ofertaAcademica = ofertaAcademicaService.find(ofertaAcademicaId);
        }
        return ofertaAcademica;
    }
    
    public void filter() {
        if (lazyOfertaAcademicaDataModel == null) {
            lazyOfertaAcademicaDataModel = new LazyOfertaAcademicaDataModel(this.ofertaAcademicaService);
        }
        lazyOfertaAcademicaDataModel.setStart(getStart());
        lazyOfertaAcademicaDataModel.setEnd(getEnd());
        
    }
    
    public String crear() {
        return "pretty:ofertaAcademica";
    }
    
    public String guardar() {
        try {
            if (this.ofertaAcademicaId == null) {
                this.ofertaAcademicaService.create(getOfertaAcademica());
            } else {
                this.ofertaAcademicaService.edit(ofertaAcademica);
            }
            agregarMensajeExitoso("save.succesful");
        } catch (Exception e) {
            throw e;
        }
        return "";
    }
    
    public String getDetalleParametrizacion(String codigo, String valorDefecto) {
        List<DetalleParametrizacion> detallesParametrizacion = this.detalleParametrizacionService.findByNamedQueryWithLimit("DetalleParametrizacion.findByCodigo", 0, codigo);
        return !detallesParametrizacion.isEmpty() ? detallesParametrizacion.get(0).getValor() : valorDefecto;
    }
    
    public void setOfertaAcademica(OfertaAcademica ofertaAcademica) {
        this.ofertaAcademica = ofertaAcademica;
    }
    
    public LazyOfertaAcademicaDataModel getLazyOfertaAcademicaDataModel() {
        filter();
        return lazyOfertaAcademicaDataModel;
    }
    
    public void setLazyOfertaAcademicaDataModel(LazyOfertaAcademicaDataModel lazyOfertaAcademicaDataModel) {
        this.lazyOfertaAcademicaDataModel = lazyOfertaAcademicaDataModel;
    }
    
    public Long getOfertaAcademicaId() {
        return ofertaAcademicaId;
    }
    
    public void setOfertaAcademicaId(Long ofertaAcademicaId) {
        this.ofertaAcademicaId = ofertaAcademicaId;
    }
    
}
