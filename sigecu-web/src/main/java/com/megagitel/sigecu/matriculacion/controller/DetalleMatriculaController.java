/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.matriculacion.controller;

import com.megagitel.sigecu.academico.ejb.MatriculaService;
import com.megagitel.sigecu.academico.modelo.Matricula;
import com.megagitel.sigecu.academico.modelo.MatriculaComponenteEducativo;
import com.megagitel.sigecu.core.ejb.CatalogoItemService;
import com.megagitel.sigecu.core.ejb.DetalleParametrizacionService;
import com.megagitel.sigecu.core.ejb.ParametrizacionService;
import com.megagitel.sigecu.core.modelo.CatalogoItem;
import com.megagitel.sigecu.core.modelo.DetalleParametrizacion;
import com.megagitel.sigecu.core.modelo.Parametrizacion;
import com.megagitel.sigecu.enumeration.SigecuEnum;
import com.megagitel.sigecu.util.BarCodeService;
import com.megagitel.sigecu.util.SigecuController;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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

/**
 *
 * @author jorgemalla
 */
@Named
@ViewScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "detalleMatricula",
            pattern = "/admin/detalleMatricula",
            viewId = "/faces/paginas/matriculacion/detalleMatricula.xhtml"
    )})
public class DetalleMatriculaController extends SigecuController implements Serializable {

    private Matricula matricula;
    private Long matriculaId;
    private String patternDecimal;
    private CatalogoItem estadoMatriculaMatriculada;

    @EJB
    private MatriculaService matriculaService;
    @EJB
    private DetalleParametrizacionService detalleParametrizacionService;
    @EJB
    private ParametrizacionService parametrizacionService;
    @EJB
    private CatalogoItemService catalogoItemService;

    @Resource
    private UserTransaction userTransaction;

    @PostConstruct
    public void init() {
        this.matricula = new Matricula();

    }

    public void matricular(MatriculaComponenteEducativo matriculaComponenteEducativo) {
        try {
            userTransaction.begin();
            matriculaComponenteEducativo.setEstado(getEstadoMatriculaMatriculada() != null ? getEstadoMatriculaMatriculada().getId() : null);
            matriculaComponenteEducativo.setCodigo(generarCodigoBarra());
            BarCodeService.generarImagenBarCode(matriculaComponenteEducativo.getCodigo(), getDetalleParametrizacion(detalleParametrizacionService, SigecuEnum.DETALLE_PARAM_RUTA_BARCODE.getTipo(), ""));
            if (verificarEstadosMaticulaComponentes()) {
                matricula.setEstado(getEstadoMatriculaMatriculada().getId());
            }
            matriculaService.edit(matricula);
            userTransaction.commit();
        } catch (NotSupportedException | SystemException e) {
            try {
                userTransaction.rollback();
            } catch (IllegalStateException | SecurityException | SystemException ex) {
                Logger.getLogger(DetalleMatriculaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            Logger.getLogger(DetalleMatriculaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Boolean verificarEstadosMaticulaComponentes() {
        for (MatriculaComponenteEducativo matriculaComponenteEducativo : this.matricula.getMatriculaComponenteEducativos()) {
            if (!Objects.equals(getEstadoMatriculaMatriculada().getId(), matriculaComponenteEducativo.getEstado())) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public Matricula getMatricula() {
        if (this.matriculaId != null && this.matricula.getId() == null) {
            this.matricula = this.matriculaService.find(matriculaId);
            for (MatriculaComponenteEducativo matriculaComponenteEducativo : matricula.getMatriculaComponenteEducativos()) {
                CatalogoItem estadoMatriculaItem = catalogoItemService.find(matriculaComponenteEducativo.getEstado());
                matriculaComponenteEducativo.setEstadoMatricula(estadoMatriculaItem.getDescripcion());
            }
        }
        return matricula;
    }

    public String generarCodigoBarra() {
        String tipoCodigoBarra = getDetalleParametrizacion(detalleParametrizacionService, SigecuEnum.TIPO_CODIGO_BARRA.getTipo(), SigecuEnum.TIPO_CODIGO_BARRA_UPCA.getTipo());
        long limite = 1000;
        int limiteSubStr = 0;
        if (tipoCodigoBarra.equals(SigecuEnum.TIPO_CODIGO_BARRA_UPCA.getTipo())) {
            limite = 100000;
            limiteSubStr = 11;
        }
        long timeSeed = System.nanoTime();
        double randSeed = Math.random() * limite;
        long midSeed = (long) (timeSeed * randSeed);
        String midSeedStr = midSeed + "";
        String codigo = midSeedStr.substring(0, limiteSubStr);
        return codigo;
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

    public String getPatternDecimal() {
        Parametrizacion parametrizacion = this.parametrizacionService.getParametrizacion();
        for (DetalleParametrizacion detalleParametrizacion : parametrizacion.getDetalleParametrizacions()) {
            if (detalleParametrizacion.getCodigo().equals(SigecuEnum.DETALLE_PARAM_PATTERN_COSTO_MATRICULA.getTipo())) {
                this.patternDecimal = detalleParametrizacion.getValor();
            }
        }
        return patternDecimal;
    }

    public void setPatternDecimal(String patternDecimal) {
        this.patternDecimal = patternDecimal;
    }

    public CatalogoItem getEstadoMatriculaMatriculada() {
        if (estadoMatriculaMatriculada == null) {
            List<CatalogoItem> estadosMatriculaMatriculada = catalogoItemService.findByNamedQueryWithLimit("CatalogoItem.findByCodigo", 0, SigecuEnum.ESTADO_MATRICULA_MATRICULADO.getTipo());
            estadoMatriculaMatriculada = !estadosMatriculaMatriculada.isEmpty() ? estadosMatriculaMatriculada.get(0) : null;
        }
        return estadoMatriculaMatriculada;
    }

    public void setEstadoMatriculaMatriculada(CatalogoItem estadoMatriculaMatriculada) {
        this.estadoMatriculaMatriculada = estadoMatriculaMatriculada;
    }

}
