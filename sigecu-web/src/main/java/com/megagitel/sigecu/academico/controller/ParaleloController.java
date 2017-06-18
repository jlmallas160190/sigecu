/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.controller;

import com.megagitel.sigecu.academico.ejb.OfertaAcademicaService;
import com.megagitel.sigecu.academico.ejb.OfertadorComponenteEducativoService;
import com.megagitel.sigecu.academico.ejb.ParaleloService;
import com.megagitel.sigecu.academico.modelo.OfertaAcademica;
import com.megagitel.sigecu.academico.modelo.OfertadorComponenteEducativo;
import com.megagitel.sigecu.academico.modelo.Paralelo;
import com.megagitel.sigecu.core.ejb.DetalleParametrizacionService;
import com.megagitel.sigecu.core.modelo.DetalleParametrizacion;
import com.megagitel.sigecu.ui.model.LazyParaleloDataModel;
import com.megagitel.sigecu.util.SigecuController;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
import java.util.Date;
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
            id = "paralelos",
            pattern = "/admin/paralelos",
            viewId = "/faces/paginas/academico/paralelo/paralelosList.xhtml"
    )
    ,
@URLMapping(
            id = "paralelo",
            pattern = "/admin/paralelo",
            viewId = "/faces/paginas/academico/paralelo/paraleloAdmin.xhtml"
    ),})
public class ParaleloController extends SigecuController implements Serializable {

    @EJB
    private ParaleloService paraleloService;
    @EJB
    private DetalleParametrizacionService detalleParametrizacionService;
    @EJB
    private OfertadorComponenteEducativoService ofertadorComponenteEducativoService;
    @EJB
    private OfertaAcademicaService ofertaAcademicaService;
    private LazyParaleloDataModel dataModel;
    private Paralelo paralelo;
    private Long paraleloId;
    private List<OfertadorComponenteEducativo> ofertadorComponenteEducativos;
    private OfertadorComponenteEducativo ofertadorComponenteEducativo;

    @PostConstruct
    public void init() {
        this.paralelo = new Paralelo();
    }

    public void filter() {
        if (dataModel == null) {
            dataModel = new LazyParaleloDataModel(this.paraleloService);
        }
        dataModel.setStart(getStart());
        dataModel.setEnd(getEnd());
    }

    public String crear() {
        return "pretty:paralelo";
    }

    public String guardar() {
        try {
            this.paralelo.setOfertadorComponenteEducativo(ofertadorComponenteEducativo);
            if (this.paralelo.getId() == null) {
                this.paraleloService.create(paralelo);
            } else {
                this.paraleloService.edit(paralelo);
            }
            agregarMensajeExitoso("save.succesful");
        } catch (Exception e) {
            agregarMensajeFatal("save.error");
            return "";
        }
        return "";
    }

    public String eliminar(Paralelo paralelo) {
        try {
            if (paralelo.getId() != null) {
                paralelo.setEliminar(Boolean.TRUE);
                this.paraleloService.edit(paralelo);
            }
        } catch (Exception e) {
            agregarMensajeFatal("remove.error");
        }
        agregarMensajeExitoso("remove.succesful");
        return "";
    }

    public String getDetalleParametrizacion(String codigo, String valorDefecto) {
        List<DetalleParametrizacion> detallesParametrizacion = this.detalleParametrizacionService.findByNamedQueryWithLimit("DetalleParametrizacion.findByCodigo", 0, codigo);
        return !detallesParametrizacion.isEmpty() ? detallesParametrizacion.get(0).getValor() : valorDefecto;
    }

    public LazyParaleloDataModel getDataModel() {
        this.filter();
        return dataModel;
    }

    public void setDataModel(LazyParaleloDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public Paralelo getParalelo() {
        return paralelo;
    }

    public void setParalelo(Paralelo paralelo) {
        this.paralelo = paralelo;
    }

    public Long getParaleloId() {
        return paraleloId;
    }

    public void setParaleloId(Long paraleloId) {
        this.paraleloId = paraleloId;
    }

    public List<OfertadorComponenteEducativo> getOfertadorComponenteEducativos() {
        if (this.ofertadorComponenteEducativos.isEmpty()) {
            Date fechaActual = new Date();
            OfertaAcademica ofertaAcademica = this.ofertaAcademicaService.getOfertaAcademicaActual(fechaActual);

            this.ofertadorComponenteEducativos = this.ofertadorComponenteEducativoService.findByNamedQueryWithLimit("OfertadorComponenteEducativo.findByOfertaAcademica", 0, ofertaAcademica);
        }
        return ofertadorComponenteEducativos;
    }

    public void setOfertadorComponenteEducativos(List<OfertadorComponenteEducativo> ofertadorComponenteEducativos) {
        this.ofertadorComponenteEducativos = ofertadorComponenteEducativos;
    }

    public OfertadorComponenteEducativo getOfertadorComponenteEducativo() {
        return ofertadorComponenteEducativo;
    }

    public void setOfertadorComponenteEducativo(OfertadorComponenteEducativo ofertadorComponenteEducativo) {
        this.ofertadorComponenteEducativo = ofertadorComponenteEducativo;
    }

}
