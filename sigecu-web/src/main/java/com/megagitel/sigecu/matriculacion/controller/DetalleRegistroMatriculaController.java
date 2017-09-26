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
import com.megagitel.sigecu.dto.MailDto;
import com.megagitel.sigecu.enumeration.SigecuEnum;
import com.megagitel.sigecu.util.EmailService;
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
            id = "detalleRegistroMatricula",
            pattern = "/matriculacion/detalleRegistroMatricula",
            viewId = "/faces/paginas/matriculacion/detalleRegistroMatricula.xhtml"
    )})
public class DetalleRegistroMatriculaController extends SigecuController implements Serializable {

    private Matricula matricula;
    private Long matriculaId;
    private String patternDecimal;

    @EJB
    private MatriculaService matriculaService;
    @EJB
    private DetalleParametrizacionService detalleParametrizacionService;
    @EJB
    private ParametrizacionService parametrizacionService;
    @EJB
    private CatalogoItemService catalogoItemService;

    @PostConstruct
    public void init() {
        this.matricula = new Matricula();
    }

    public void enviarCodigoPorEmail(MatriculaComponenteEducativo matriculaComponenteEducativo) {
        try {
            MailDto mailDto = new MailDto();
            mailDto.setMensaje("probando");
            mailDto.setDatosDestinatario(matriculaComponenteEducativo.getMatricula().getEstudiante().getNombresCompletos());
            mailDto.setDestino(matriculaComponenteEducativo.getMatricula().getEstudiante().getEmail());
            EmailService.enviar(mailDto);
        } catch (Exception e) {
            throw e;
        }
    }

    public String getDetalleParametrizacion(String codigo, String valorDefecto) {
        List<DetalleParametrizacion> detallesParametrizacion = this.detalleParametrizacionService.findByNamedQueryWithLimit("DetalleParametrizacion.findByCodigo", 0, codigo);
        return !detallesParametrizacion.isEmpty() ? detallesParametrizacion.get(0).getValor() : valorDefecto;
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
            if (detalleParametrizacion.getCodigo().equals(SigecuEnum.DETALLE_PARAM_CURRENCY.getTipo())) {
                this.patternDecimal = detalleParametrizacion.getValor();
            }
        }
        return patternDecimal;
    }

    public void setPatternDecimal(String patternDecimal) {
        this.patternDecimal = patternDecimal;
    }

}
