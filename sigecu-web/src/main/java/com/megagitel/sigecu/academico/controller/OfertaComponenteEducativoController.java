/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.controller;

import com.megagitel.sigecu.academico.ejb.OfertaComponenteEducativoService;
import com.megagitel.sigecu.academico.ejb.OfertadorComponenteEducativoService;
import com.megagitel.sigecu.academico.modelo.OfertaComponenteEducativo;
import com.megagitel.sigecu.academico.modelo.OfertadorComponenteEducativo;
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
            id = "ofertaComponenteEducativo",
            pattern = "/home/ofertaComponenteEducativo",
            viewId = "/faces/paginas/academico/ofertaComponenteEducativo/ofertaComponenteEducativoAdmin.xhtml"
    ),})
public class OfertaComponenteEducativoController extends SigecuController implements Serializable {

    private OfertaComponenteEducativo ofertaComponenteEducativo;
    private OfertadorComponenteEducativo ofertadorComponenteEducativo;
    private Long ofertadorComponenteEducativoId;
    private Long ofertaComponenteEducativoId;

    @EJB
    private OfertadorComponenteEducativoService ofertadorComponenteEducativoService;
    @EJB
    private OfertaComponenteEducativoService ofertaComponenteEducativoService;

    public OfertaComponenteEducativoController() {
    }

    @PostConstruct
    public void init() {
        this.ofertaComponenteEducativo = new OfertaComponenteEducativo();
    }

    public String agregar() {
        try {
            if (ofertadorComponenteEducativo != null) {
                this.ofertadorComponenteEducativo.agregarOfertaComponenteEducativo(ofertaComponenteEducativo);
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public OfertaComponenteEducativo getOfertaComponenteEducativo() {
        if (this.ofertaComponenteEducativoId != null && this.ofertaComponenteEducativo.getId() != null) {
            this.ofertaComponenteEducativo = this.ofertaComponenteEducativoService.find(this.ofertaComponenteEducativoId);
        }
        return ofertaComponenteEducativo;
    }

    public void setOfertaComponenteEducativo(OfertaComponenteEducativo ofertaComponenteEducativo) {
        this.ofertaComponenteEducativo = ofertaComponenteEducativo;
    }

    public OfertadorComponenteEducativo getOfertadorComponenteEducativo() {
        if (this.ofertadorComponenteEducativoId != null && this.ofertadorComponenteEducativo.getId() == null) {
            this.ofertadorComponenteEducativo = this.ofertadorComponenteEducativoService.find(this.ofertadorComponenteEducativoId);
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

    public Long getOfertaComponenteEducativoId() {
        return ofertaComponenteEducativoId;
    }

    public void setOfertaComponenteEducativoId(Long ofertaComponenteEducativoId) {
        this.ofertaComponenteEducativoId = ofertaComponenteEducativoId;
    }

}
