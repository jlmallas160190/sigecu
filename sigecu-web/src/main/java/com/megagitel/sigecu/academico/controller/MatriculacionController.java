/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.controller;

import com.megagitel.sigecu.academico.ejb.JornadaService;
import com.megagitel.sigecu.academico.modelo.Jornada;
import com.megagitel.sigecu.util.SigecuController;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author jorgemalla
 */
@Named
@RequestScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "matriculacion",
            pattern = "/matriculacion",
            viewId = "/faces/paginas/matriculacion/index.xhtml"
    )})
public class MatriculacionController extends SigecuController implements Serializable {

    private List<Jornada> jornadas;

    @EJB
    private JornadaService jornadaService;

    @PostConstruct
    public void init() {
        this.jornadas = new ArrayList<>();
    }

    public List<Jornada> getJornadas() {
        if (jornadas.isEmpty()) {
            Date fechaActual = new Date();
            jornadas = jornadaService.findByNamedQueryWithLimit("Jornada.findByOfertaActual", 0, fechaActual);
        }
        return jornadas;
    }

    public void setJornadas(List<Jornada> jornadas) {
        this.jornadas = jornadas;
    }

}
