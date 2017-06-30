/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.controller;

import com.megagitel.sigecu.academico.ejb.EstudianteService;
import com.megagitel.sigecu.academico.ejb.JornadaService;
import com.megagitel.sigecu.academico.ejb.MatriculaService;
import com.megagitel.sigecu.academico.ejb.OfertaAcademicaService;
import com.megagitel.sigecu.academico.modelo.ComponenteEducativoPlanificado;
import com.megagitel.sigecu.academico.modelo.Estudiante;
import com.megagitel.sigecu.academico.modelo.Jornada;
import com.megagitel.sigecu.academico.modelo.Matricula;
import com.megagitel.sigecu.academico.modelo.OfertaAcademica;
import com.megagitel.sigecu.core.ejb.CatalogoItemService;
import com.megagitel.sigecu.core.ejb.DetalleParametrizacionService;
import com.megagitel.sigecu.core.modelo.CatalogoItem;
import com.megagitel.sigecu.core.modelo.DetalleParametrizacion;
import com.megagitel.sigecu.enumeration.SigecuEnum;
import com.megagitel.sigecu.seguridad.ejb.UsuarioService;
import com.megagitel.sigecu.seguridad.modelo.Usuario;
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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

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
    private List<ComponenteEducativoPlanificado> componenteEducativoPlanificados;

    @EJB
    private JornadaService jornadaService;
    @EJB
    private UsuarioService usuarioService;
    @EJB
    private EstudianteService estudianteService;
    @EJB
    private DetalleParametrizacionService detalleParametrizacionService;
    @EJB
    private MatriculaService matriculaService;
    @EJB
    private OfertaAcademicaService ofertaAcademicaService;
    @EJB
    private CatalogoItemService catalogoItemService;

    @PostConstruct
    public void init() {
        this.componenteEducativoPlanificados = new ArrayList<>();
        this.jornadas = new ArrayList<>();
    }

    public void seleccionarComponenteEducativo(ComponenteEducativoPlanificado componenteEducativoPlanificado) {
        if (componenteEducativoPlanificado.getSeleccionar()) {
            this.componenteEducativoPlanificados.add(componenteEducativoPlanificado);
        } else {
            this.componenteEducativoPlanificados.remove(componenteEducativoPlanificado);
        }
    }

    public void matricular() {
        Subject subject = SecurityUtils.getSubject();
        Usuario usuario = null;

        if (subject.isAuthenticated()) {
            List<Usuario> usuarios = this.usuarioService.findByNamedQueryWithLimit("Usuario.findByNombre", 0, subject.getPrincipal());
            usuario = !usuarios.isEmpty() ? usuarios.get(0) : null;
        }
        Estudiante estudiante = estudianteService.find(usuario != null && usuario.getPersona() != null ? usuario.getPersona().getId() : null);
        Date fechaActual = new Date();
        OfertaAcademica ofertaAcademicaActual = ofertaAcademicaService.getOfertaAcademicaActual(fechaActual);
        if (estudiante != null) {
            List<Matricula> matriculas = matriculaService.findByNamedQueryWithLimit("Matricula.findByOfertaAcademica", 0, ofertaAcademicaActual.getId(), estudiante.getId());
            Matricula matricula = !matriculas.isEmpty() ? matriculas.get(0) : null;
            if (matricula == null) {
                matricula = new Matricula();
                List<CatalogoItem> catalogoItems = catalogoItemService.findByNamedQueryWithLimit("CatalogoItem.findByCodigo", 0, SigecuEnum.ESTADO_MATRICULA_REGISTRAD0.getTipo());
                matricula.setEstado(!catalogoItems.isEmpty() ? catalogoItems.get(0).getId() : null);
                matricula.setOfertaAcademica(ofertaAcademicaActual);
                matricula.setFechaRegistro(fechaActual);
                matricula.setEstudiante(estudiante);
            }
            for (ComponenteEducativoPlanificado componenteEducativoPlanificado : this.componenteEducativoPlanificados) {
                
            }
        } else {
            agregarMensajeError("com.megagitel.sigecu.matriculacion.noestudiante");
        }
    }

    public String getDetalleParametrizacion(String codigo, String valorDefecto) {
        List<DetalleParametrizacion> detallesParametrizacion = this.detalleParametrizacionService.findByNamedQueryWithLimit("DetalleParametrizacion.findByCodigo", 0, codigo);
        return !detallesParametrizacion.isEmpty() ? detallesParametrizacion.get(0).getValor() : valorDefecto;
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
