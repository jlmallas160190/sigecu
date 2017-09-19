/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.controller;

import com.megagitel.sigecu.academico.ejb.EstudianteService;
import com.megagitel.sigecu.academico.modelo.Estudiante;
import com.megagitel.sigecu.core.ejb.DetalleParametrizacionService;
import com.megagitel.sigecu.enumeration.SigecuEnum;
import com.megagitel.sigecu.ui.model.LazyEstudianteDataModel;
import com.megagitel.sigecu.util.SigecuController;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
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
            id = "estudiantes",
            pattern = "/admin/estudiantes",
            viewId = "/faces/paginas/academico/estudiante/estudianteList.xhtml"
    )
    ,
@URLMapping(
            id = "estudiante",
            pattern = "/admin/estudiante",
            viewId = "/faces/paginas/academico/estudiante/estudianteAdmin.xhtml"
    ),})
public class EstudianteController extends SigecuController implements Serializable {

    @EJB
    private EstudianteService estudianteService;
    @EJB
    private DetalleParametrizacionService detalleParametrizacionService;

    private Estudiante estudiante;
    private Long estudianteId;

    private LazyEstudianteDataModel dataModel;

    @PostConstruct
    public void init() {
        this.estudiante = new Estudiante();
    }

    public void filter() {
        if (dataModel == null) {
            dataModel = new LazyEstudianteDataModel(this.estudianteService);
        }
        dataModel.setStart(getStart());
        dataModel.setEnd(getEnd());
    }

    public String getTableRows() {
        return this.getDetalleParametrizacion(detalleParametrizacionService, SigecuEnum.DETALLE_PARAM_TABLEROWS.getTipo(), "20");
    }

    public Estudiante getEstudiante() {
        if (this.estudianteId != null && this.estudiante.getId() == null) {
            this.estudiante = estudianteService.find(estudianteId);
        }
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public LazyEstudianteDataModel getDataModel() {
        this.filter();
        return dataModel;
    }

    public void setDataModel(LazyEstudianteDataModel dataModel) {
        this.dataModel = dataModel;
    }

}
