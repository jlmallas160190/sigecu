/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.matriculacion.controller;

import com.megagitel.sigecu.academico.ejb.EstudianteService;
import com.megagitel.sigecu.academico.ejb.MatriculaService;
import com.megagitel.sigecu.academico.modelo.Estudiante;
import com.megagitel.sigecu.academico.modelo.Matricula;
import com.megagitel.sigecu.core.ejb.CatalogoItemService;
import com.megagitel.sigecu.core.ejb.ParametrizacionService;
import com.megagitel.sigecu.core.modelo.CatalogoItem;
import com.megagitel.sigecu.core.modelo.DetalleParametrizacion;
import com.megagitel.sigecu.core.modelo.Parametrizacion;
import com.megagitel.sigecu.enumeration.SigecuEnum;
import com.megagitel.sigecu.seguridad.ejb.UsuarioService;
import com.megagitel.sigecu.seguridad.modelo.Usuario;
import com.megagitel.sigecu.util.SigecuController;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
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
            id = "listadoMatriculasEstudiante",
            pattern = "/matriculacion/listadoMatriculasEstudiante",
            viewId = "/faces/paginas/matriculacion/listadoMatriculasEstudiante.xhtml"
    )})
public class ListadoMatriculasEstudianteController extends SigecuController implements Serializable {

    @EJB
    private MatriculaService matriculaService;
    @EJB
    private UsuarioService usuarioService;
    @EJB
    private EstudianteService estudianteService;
    @EJB
    private CatalogoItemService catalogoItemService;
    @EJB
    private ParametrizacionService parametrizacionService;

    private Estudiante estudiante;
    private List<Matricula> matriculas;
    private String patternDecimal;

    @PostConstruct
    public void init() {
        this.matriculas = new ArrayList<>();
        this.estudiante = new Estudiante();
    }

    public Estudiante getEstudiante() {
        if (estudiante.getId() == null) {
            Subject subject = SecurityUtils.getSubject();
            Usuario usuario = null;
            if (subject.isAuthenticated()) {
                List<Usuario> usuarios = this.usuarioService.findByNamedQueryWithLimit("Usuario.findByNombre", 0, subject.getPrincipal());
                usuario = !usuarios.isEmpty() ? usuarios.get(0) : null;
            }
            this.estudiante = estudianteService.find(usuario != null && usuario.getPersona() != null ? usuario.getPersona().getId() : null);
        }
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public List<Matricula> getMatriculas() {
        if (matriculas.isEmpty()) {
            matriculas = matriculaService.findByNamedQueryWithLimit("Matricula.findByEstudiante", 0, getEstudiante());
            for (Matricula matricula : matriculas) {
                CatalogoItem estadoMatriculaItem = catalogoItemService.find(matricula.getEstado());
                matricula.setEstadoMatricula(estadoMatriculaItem.getDescripcion());
            }
        }

        return matriculas;
    }

    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
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
