/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.matriculacion.controller;

import com.megagitel.sigecu.academico.ejb.EstudianteService;
import com.megagitel.sigecu.academico.ejb.JornadaService;
import com.megagitel.sigecu.academico.ejb.MatriculaComponenteEducativoService;
import com.megagitel.sigecu.academico.ejb.MatriculaService;
import com.megagitel.sigecu.academico.ejb.OfertaAcademicaService;
import com.megagitel.sigecu.academico.modelo.ComponenteEducativoPlanificado;
import com.megagitel.sigecu.academico.modelo.Estudiante;
import com.megagitel.sigecu.academico.modelo.Jornada;
import com.megagitel.sigecu.academico.modelo.Matricula;
import com.megagitel.sigecu.academico.modelo.MatriculaComponenteEducativo;
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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author jorgemalla
 */
@Named
@ViewScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "matriculacion",
            pattern = "/matriculacion",
            viewId = "/faces/paginas/matriculacion/index.xhtml"
    )})
public class MatriculacionController extends SigecuController implements Serializable {

    private List<Jornada> jornadas;
    private List<ComponenteEducativoPlanificado> componentesEducativoPlanificadosSeleccionados;
    private Matricula matricula;
    private Estudiante estudiante;

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
    @EJB
    private MatriculaComponenteEducativoService matriculaComponenteEducativoService;

    @PostConstruct
    public void init() {
        this.componentesEducativoPlanificadosSeleccionados = new ArrayList<>();
        this.jornadas = new ArrayList<>();
        this.cargarEstudiante();
    }

    private void cargarEstudiante() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Usuario usuario = null;
            if (subject.isAuthenticated()) {
                List<Usuario> usuarios = this.usuarioService.findByNamedQueryWithLimit("Usuario.findByNombre", 0, subject.getPrincipal());
                usuario = !usuarios.isEmpty() ? usuarios.get(0) : null;
            }
            this.estudiante = estudianteService.find(usuario != null && usuario.getPersona() != null ? usuario.getPersona().getId() : null);
            Date fechaActual = new Date();
            OfertaAcademica ofertaAcademicaActual = ofertaAcademicaService.getOfertaAcademicaActual(fechaActual);
            List<Matricula> matriculas = matriculaService.findByNamedQueryWithLimit("Matricula.findByOfertaAcademica", 0, ofertaAcademicaActual, estudiante);
            this.matricula = !matriculas.isEmpty() ? matriculas.get(0) : null;
        } catch (Exception e) {
            throw e;
        }
    }

    public void seleccionarComponenteEducativo(ComponenteEducativoPlanificado componenteEducativoPlanificado) {
        if (componenteEducativoPlanificado.getSeleccionar()) {
            this.componentesEducativoPlanificadosSeleccionados.add(componenteEducativoPlanificado);
        } else {
            this.componentesEducativoPlanificadosSeleccionados.remove(componenteEducativoPlanificado);
        }
    }

    public String matricular() {
        try {
            if (this.estudiante != null) {
                if (matricula == null) {
                    matricula = new Matricula();
                    Date fechaActual = new Date();
                    OfertaAcademica ofertaAcademicaActual = ofertaAcademicaService.getOfertaAcademicaActual(fechaActual);
                    List<CatalogoItem> catalogoItems = catalogoItemService.findByNamedQueryWithLimit("CatalogoItem.findByCodigo", 0, SigecuEnum.ESTADO_MATRICULA_REGISTRAD0.getTipo());
                    matricula.setEstado(!catalogoItems.isEmpty() ? catalogoItems.get(0).getId() : null);
                    matricula.setOfertaAcademica(ofertaAcademicaActual);
                    matricula.setFechaRegistro(fechaActual);
                    matricula.setEstudiante(estudiante);
                    matricula.setCodigo(getCodigoBarra());
                    this.matriculaService.create(matricula);
                }
                for (ComponenteEducativoPlanificado componenteEducativoPlanificado : this.componentesEducativoPlanificadosSeleccionados) {
                    List<MatriculaComponenteEducativo> matriculaComponenteEducativos = matriculaComponenteEducativoService.findByNamedQueryWithLimit("MatriculaComponenteEducativo.findMatricula", 0, matricula);
                    MatriculaComponenteEducativo matriculaComponenteEducativo = !matriculaComponenteEducativos.isEmpty() ? matriculaComponenteEducativos.get(0) : null;
                    if (matriculaComponenteEducativo == null) {
                        matriculaComponenteEducativo = new MatriculaComponenteEducativo();
                        matriculaComponenteEducativo.setComponenteEducativoPlanificado(componenteEducativoPlanificado);
                        matricula.agregarMatriculasComponentes(matriculaComponenteEducativo);
                    }
                }
                if (matricula.getCodigo() == null) {
                    matricula.setCodigo(getCodigoBarra());
                }
                this.matriculaService.edit(matricula);
            } else {
                agregarMensajeError("com.megagitel.sigecu.matriculacion.noestudiante");
            }
        } catch (Exception e) {
            return "";
        }
        return "/faces/paginas/matriculacion/detalleMatricula.xhtml?faces-redirect=true&matriculaId=" + this.matricula.getId();
    }

    public String getCodigoBarra() {
        String tipoCodigoBarra = getDetalleParametrizacion(SigecuEnum.TIPO_CODIGO_BARRA.getTipo(), "upca");
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

    public String getDetalleParametrizacion(String codigo, String valorDefecto) {
        List<DetalleParametrizacion> detallesParametrizacion = this.detalleParametrizacionService.findByNamedQueryWithLimit("DetalleParametrizacion.findByCodigo", 0, codigo);
        return !detallesParametrizacion.isEmpty() ? detallesParametrizacion.get(0).getValor() : valorDefecto;
    }

    public List<Jornada> getJornadas() {
        try {
            Date fechaActual = new Date();
            List<Jornada> query = jornadaService.findByNamedQueryWithLimit("Jornada.findByOfertaActual", 0, fechaActual);
            for (Jornada jornada : query) {
                Jornada jor = new Jornada();
                BeanUtils.copyProperties(jor, jornada);
                List<ComponenteEducativoPlanificado> componenteEducativoPlanificados = new ArrayList<>();
                for (ComponenteEducativoPlanificado c : jor.getComponenteEducativoPlanificados()) {
                    ComponenteEducativoPlanificado componenteEducativoPlanificado = new ComponenteEducativoPlanificado();
                    BeanUtils.copyProperties(componenteEducativoPlanificado, c);
                    componenteEducativoPlanificados.add(componenteEducativoPlanificado);
                }
                for (ComponenteEducativoPlanificado componenteEducativoPlanificado : componenteEducativoPlanificados) {
                    for (MatriculaComponenteEducativo matriculaComponenteEducativo : this.matricula.getMatriculaComponenteEducativos()) {
                        if (componenteEducativoPlanificado.equals(matriculaComponenteEducativo.getComponenteEducativoPlanificado())) {
                            jor.getComponenteEducativoPlanificados().remove(componenteEducativoPlanificado);
                        }
                    }
                }
                this.jornadas.add(jor);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
        }

        return jornadas;
    }

    public void setJornadas(List<Jornada> jornadas) {
        this.jornadas = jornadas;
    }

    public List<ComponenteEducativoPlanificado> getComponentesEducativoPlanificadosSeleccionados() {
        return componentesEducativoPlanificadosSeleccionados;
    }

    public void setComponentesEducativoPlanificadosSeleccionados(List<ComponenteEducativoPlanificado> componentesEducativoPlanificadosSeleccionados) {
        this.componentesEducativoPlanificadosSeleccionados = componentesEducativoPlanificadosSeleccionados;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

}
