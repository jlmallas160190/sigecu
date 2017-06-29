/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.controller;

import com.megagitel.sigecu.academico.ejb.JornadaService;
import com.megagitel.sigecu.academico.ejb.OfertaAcademicaService;
import com.megagitel.sigecu.academico.ejb.OfertadorComponenteEducativoService;
import com.megagitel.sigecu.academico.ejb.ParaleloService;
import com.megagitel.sigecu.academico.modelo.ComponenteEducativoPlanificado;
import com.megagitel.sigecu.academico.modelo.Jornada;
import com.megagitel.sigecu.academico.modelo.OfertaAcademica;
import com.megagitel.sigecu.academico.modelo.OfertaComponenteEducativo;
import com.megagitel.sigecu.academico.modelo.OfertadorComponenteEducativo;
import com.megagitel.sigecu.academico.modelo.Paralelo;
import com.megagitel.sigecu.core.ejb.DetalleParametrizacionService;
import com.megagitel.sigecu.core.modelo.DetalleParametrizacion;
import com.megagitel.sigecu.ui.model.LazyJornadaDataModel;
import com.megagitel.sigecu.util.SigecuController;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
import java.util.ArrayList;
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
            id = "jornadas",
            pattern = "/admin/jornadas",
            viewId = "/faces/paginas/academico/jornada/jornadaList.xhtml"
    )
    ,
@URLMapping(
            id = "jornada",
            pattern = "/admin/jornada",
            viewId = "/faces/paginas/academico/jornada/jornadaAdmin.xhtml"
    ),})
public class JornadaController extends SigecuController implements Serializable {
    
    @EJB
    private JornadaService jornadaService;
    @EJB
    private OfertadorComponenteEducativoService ofertadorComponenteEducativoService;
    @EJB
    private ParaleloService paraleloService;
    @EJB
    private DetalleParametrizacionService detalleParametrizacionService;
    @EJB
    private OfertaAcademicaService ofertaAcademicaService;
    
    private Jornada jornada;
    private Long jornadaId;
    private OfertadorComponenteEducativo ofertadorComponenteEducativo;
    
    private List<OfertadorComponenteEducativo> ofertadorComponenteEducativos;
    private List<Paralelo> paralelos;
    private LazyJornadaDataModel dataModel;
    
    @PostConstruct
    public void init() {
        this.jornada = new Jornada();
        this.jornada.setComponenteEducativoPlanificados(new ArrayList<ComponenteEducativoPlanificado>());
        this.paralelos = new ArrayList<>();
        this.ofertadorComponenteEducativos = new ArrayList<>();
    }
    
    public void filter() {
        if (dataModel == null) {
            dataModel = new LazyJornadaDataModel(this.jornadaService);
        }
        dataModel.setStart(getStart());
        dataModel.setEnd(getEnd());
    }
    
    public void agregarComponentesEducativosPlanificados(Paralelo paralelo) {
        for (OfertaComponenteEducativo oce : this.jornada.getParalelo().getOfertadorComponenteEducativo().getOfertaComponenteEducativos()) {
            if (!this.contieneComponenteEducativo(oce)) {
                ComponenteEducativoPlanificado componenteEducativoPlanificado = new ComponenteEducativoPlanificado();
                componenteEducativoPlanificado.setOfertaComponenteEducativo(oce);
                this.jornada.agregarComponenteEducativoPlanificado(componenteEducativoPlanificado);
            }
        }
    }
    
    public String crear() {
        return "pretty:jornada";
    }
    
    public String guardar() {
        try {
            if (this.jornada.getId() == null) {
                this.jornada.setEliminar(Boolean.FALSE);
                this.jornadaService.create(jornada);
            } else {
                this.jornadaService.edit(jornada);
            }
            agregarMensajeExitoso("save.succesful");
            
        } catch (Exception e) {
            agregarMensajeFatal("save.error");
            return "";
        }
        return "";
    }
    
    public String eliminar(Jornada jornada) {
        try {
            if (jornada.getId() != null) {
                jornada.setEliminar(Boolean.TRUE);
                this.jornadaService.edit(jornada);
            }
        } catch (Exception e) {
            agregarMensajeFatal("remove.error");
        }
        agregarMensajeExitoso("remove.succesful");
        return "";
    }
    
    public void seleccionarOfertador() {
        this.paralelos = this.paraleloService.findByNamedQueryWithLimit("Paralelo.findByOfertador", 0, this.ofertadorComponenteEducativo);
    }
    
    public boolean contieneComponenteEducativo(OfertaComponenteEducativo oce) {
        for (ComponenteEducativoPlanificado cep : this.jornada.getComponenteEducativoPlanificados()) {
            if (oce.equals(cep.getOfertaComponenteEducativo())) {
                return true;
            }
        }
        return false;
    }
    
    public String getDetalleParametrizacion(String codigo, String valorDefecto) {
        List<DetalleParametrizacion> detallesParametrizacion = this.detalleParametrizacionService.findByNamedQueryWithLimit("DetalleParametrizacion.findByCodigo", 0, codigo);
        return !detallesParametrizacion.isEmpty() ? detallesParametrizacion.get(0).getValor() : valorDefecto;
    }
    
    public Jornada getJornada() {
        if (this.jornadaId != null && this.jornada.getId() == null) {
            this.jornada = jornadaService.find(jornadaId);
            this.agregarComponentesEducativosPlanificados(this.jornada.getParalelo());
        }
        return jornada;
    }
    
    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }
    
    public Long getJornadaId() {
        return jornadaId;
    }
    
    public void setJornadaId(Long jornadaId) {
        this.jornadaId = jornadaId;
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
    
    public List<Paralelo> getParalelos() {
        return paralelos;
    }
    
    public void setParalelos(List<Paralelo> paralelos) {
        this.paralelos = paralelos;
    }
    
    public LazyJornadaDataModel getDataModel() {
        this.filter();
        return dataModel;
    }
    
    public void setDataModel(LazyJornadaDataModel dataModel) {
        this.dataModel = dataModel;
    }
    
    public OfertadorComponenteEducativo getOfertadorComponenteEducativo() {
        return ofertadorComponenteEducativo;
    }
    
    public void setOfertadorComponenteEducativo(OfertadorComponenteEducativo ofertadorComponenteEducativo) {
        this.ofertadorComponenteEducativo = ofertadorComponenteEducativo;
    }
    
}
