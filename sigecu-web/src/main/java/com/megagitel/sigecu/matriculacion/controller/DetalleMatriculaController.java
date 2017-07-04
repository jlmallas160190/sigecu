/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.matriculacion.controller;

import com.megagitel.sigecu.academico.ejb.MatriculaService;
import com.megagitel.sigecu.academico.modelo.Matricula;
import com.megagitel.sigecu.core.ejb.DetalleParametrizacionService;
import com.megagitel.sigecu.core.modelo.DetalleParametrizacion;
import com.megagitel.sigecu.util.SigecuController;
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
            id = "detalleMatricula",
            pattern = "/matriculacion/detalleMatricula",
            viewId = "/faces/paginas/matriculacion/detalleMatricula.xhtml"
    )})
public class DetalleMatriculaController extends SigecuController implements Serializable {

    private Matricula matricula;
    private Long matriculaId;

    @EJB
    private MatriculaService matriculaService;
    @EJB
    private DetalleParametrizacionService detalleParametrizacionService;

    @PostConstruct
    public void init() {
        this.matricula = new Matricula();
    }

    public String getDetalleParametrizacion(String codigo, String valorDefecto) {
        List<DetalleParametrizacion> detallesParametrizacion = this.detalleParametrizacionService.findByNamedQueryWithLimit("DetalleParametrizacion.findByCodigo", 0, codigo);
        return !detallesParametrizacion.isEmpty() ? detallesParametrizacion.get(0).getValor() : valorDefecto;
    }

    public Matricula getMatricula() {
        if (this.matriculaId != null && this.matricula.getId() == null) {
            this.matricula = this.matriculaService.find(matriculaId);
        }
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public Long getMatriculaId() {
        return matriculaId;
    }

    public void setMatriculaId(Long matriculaId) {
        this.matriculaId = matriculaId;
    }

}
