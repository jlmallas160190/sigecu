/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.matriculacion.controller;

import com.megagitel.sigecu.academico.ejb.MatriculaService;
import com.megagitel.sigecu.academico.ejb.OfertaAcademicaService;
import com.megagitel.sigecu.academico.modelo.Matricula;
import com.megagitel.sigecu.academico.modelo.OfertaAcademica;
import com.megagitel.sigecu.ui.model.LazyMatriculaDataModel;
import com.megagitel.sigecu.ui.model.LazyOfertaAcademicaDataModel;
import com.megagitel.sigecu.util.SigecuController;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
            id = "listadoMatriculasValidar",
            pattern = "/admin/listadoMatriculasValidar",
            viewId = "/faces/paginas/matriculacion/listadoMatriculasValidar.xhtml"
    )})
public class ListaMatriculasValidarController extends SigecuController implements Serializable {

    private LazyMatriculaDataModel lazyMatriculaDataModel;
    private LazyOfertaAcademicaDataModel lazyOfertaAcademicaDataModel;
    private OfertaAcademica ofertaAcademicaSeleccionada;
    private List<Matricula> matriculasFilter;

    @EJB
    private MatriculaService matriculaService;
    @EJB
    private OfertaAcademicaService ofertaAcademicaService;

    @PostConstruct
    public void init() {
    }

    public void filterLazyMatricula() {
        if (lazyMatriculaDataModel == null) {
            lazyMatriculaDataModel = new LazyMatriculaDataModel(this.matriculaService);
        }
        lazyMatriculaDataModel.setStart(getStart());
        lazyMatriculaDataModel.setEnd(getEnd());
        HashMap<String, Object> filtros = new HashMap<>();
        if (getOfertaAcademicaSeleccionada() != null) {
            filtros.put("ofertaAcademica", getOfertaAcademicaSeleccionada());
        }
        filtros.put("estado", 329);

        lazyMatriculaDataModel.setFiltros(filtros);
    }

    public void filterLazyOfertaAcademica() {
        if (lazyOfertaAcademicaDataModel == null) {
            lazyOfertaAcademicaDataModel = new LazyOfertaAcademicaDataModel(this.ofertaAcademicaService);
        }
        lazyOfertaAcademicaDataModel.setStart(getStart());
        lazyOfertaAcademicaDataModel.setEnd(getEnd());
    }

    public LazyMatriculaDataModel getLazyMatriculaDataModel() {
        this.filterLazyMatricula();
        return lazyMatriculaDataModel;
    }

    public void setLazyMatriculaDataModel(LazyMatriculaDataModel lazyMatriculaDataModel) {
        this.lazyMatriculaDataModel = lazyMatriculaDataModel;
    }

    public LazyOfertaAcademicaDataModel getLazyOfertaAcademicaDataModel() {
        this.filterLazyOfertaAcademica();
        return lazyOfertaAcademicaDataModel;
    }

    public void setLazyOfertaAcademicaDataModel(LazyOfertaAcademicaDataModel lazyOfertaAcademicaDataModel) {
        this.lazyOfertaAcademicaDataModel = lazyOfertaAcademicaDataModel;
    }

    public OfertaAcademica getOfertaAcademicaSeleccionada() {
        return ofertaAcademicaSeleccionada;
    }

    public void setOfertaAcademicaSeleccionada(OfertaAcademica ofertaAcademicaSeleccionada) {
        this.ofertaAcademicaSeleccionada = ofertaAcademicaSeleccionada;
    }

    public List<Matricula> getMatriculasFilter() {
        return matriculasFilter;
    }

    public void setMatriculasFilter(List<Matricula> matriculasFilter) {
        this.matriculasFilter = matriculasFilter;
    }

}
