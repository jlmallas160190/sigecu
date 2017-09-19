package com.megagitel.sigecu.matriculacion.controller;

import com.megagitel.sigecu.academico.ejb.MatriculaComponenteEducativoService;
import com.megagitel.sigecu.academico.modelo.MatriculaComponenteEducativo;
import com.megagitel.sigecu.core.ejb.CatalogoItemService;
import com.megagitel.sigecu.core.modelo.CatalogoItem;
import com.megagitel.sigecu.enumeration.SigecuEnum;
import com.megagitel.sigecu.util.SigecuController;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.awt.Component;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jorgemalla
 */
@Named
@ViewScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "validarMatricula",
            pattern = "/admin/validarMatricula",
            viewId = "/faces/paginas/matriculacion/validarMatricula.xhtml"
    )})
public class ValidarMatriculaController extends SigecuController implements Serializable {

    private String codigoBarra;
    private MatriculaComponenteEducativo matriculaComponenteEducativo;
    private CatalogoItem estadoMatriculado;

    @EJB
    private MatriculaComponenteEducativoService matriculaComponenteEducativoService;
    @EJB
    private CatalogoItemService catalogoItemService;

    @PostConstruct
    public void init() {
        this.matriculaComponenteEducativo = new MatriculaComponenteEducativo();
        this.codigoBarra = "";

    }

    public void validarMatricula() {
        try {
            List<MatriculaComponenteEducativo> matriculaComponenteEducativos = this.matriculaComponenteEducativoService.findByNamedQueryWithLimit("MatriculaComponenteEducativo.findCodigoEstado", 0, codigoBarra, getEstadoMatriculado().getId());
            matriculaComponenteEducativo = !matriculaComponenteEducativos.isEmpty() ? matriculaComponenteEducativos.get(0) : null;
            if (matriculaComponenteEducativo != null && matriculaComponenteEducativo.getId() != null) {
                agregarMensajeExitoso("com.megagitel.sigecu.matriculacion.codigobarraexitoso");
            } else {
                agregarMensajeError("com.megagitel.sigecu.matriculacion.codigobarraexitoso");
            }
        } catch (Exception e) {
        }
    }

    public void nuevo() {
        this.codigoBarra = "";
        this.matriculaComponenteEducativo = new MatriculaComponenteEducativo();
    }

    public CatalogoItem getEstadoMatriculado() {
        if (estadoMatriculado == null) {
            List<CatalogoItem> result = this.catalogoItemService.findByNamedQueryWithLimit("CatalogoItem.findByCodigo", 0, SigecuEnum.ESTADO_MATRICULA_MATRICULADO.getTipo());
            estadoMatriculado = !result.isEmpty() ? result.get(0) : null;
        }
        return estadoMatriculado;
    }

    public void setEstadoMatriculado(CatalogoItem estadoMatriculado) {
        this.estadoMatriculado = estadoMatriculado;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public MatriculaComponenteEducativo getMatriculaComponenteEducativo() {
        return matriculaComponenteEducativo;
    }

    public void setMatriculaComponenteEducativo(MatriculaComponenteEducativo matriculaComponenteEducativo) {
        this.matriculaComponenteEducativo = matriculaComponenteEducativo;
    }

}
