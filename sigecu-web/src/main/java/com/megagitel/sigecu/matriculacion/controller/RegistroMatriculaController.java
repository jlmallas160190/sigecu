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
import com.megagitel.sigecu.core.ejb.ParametrizacionService;
import com.megagitel.sigecu.core.modelo.CatalogoItem;
import com.megagitel.sigecu.core.modelo.DetalleParametrizacion;
import com.megagitel.sigecu.core.modelo.Parametrizacion;
import com.megagitel.sigecu.dto.MailDto;
import com.megagitel.sigecu.enumeration.SigecuEnum;
import com.megagitel.sigecu.seguridad.ejb.UsuarioService;
import com.megagitel.sigecu.seguridad.modelo.Usuario;
import com.megagitel.sigecu.util.EmailService;
import com.megagitel.sigecu.util.I18nUtil;
import com.megagitel.sigecu.util.SigecuController;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
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
            pattern = "/matriculacion/registroMatricula",
            viewId = "/faces/paginas/matriculacion/registroMatricula.xhtml"
    )})
public class RegistroMatriculaController extends SigecuController implements Serializable {

    private List<Jornada> jornadas;
    private List<ComponenteEducativoPlanificado> componentesEducativoPlanificadosSeleccionados;
    private Matricula matricula;
    private Estudiante estudiante;
    private BigDecimal costoMatricula;
    private String patternDecimal;

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
    private ParametrizacionService parametrizacionService;
    @EJB
    private MatriculaComponenteEducativoService matriculaComponenteEducativoService;

    @Resource
    private UserTransaction userTransaction;

    @PostConstruct
    public void init() {
        this.componentesEducativoPlanificadosSeleccionados = new ArrayList<>();
        this.jornadas = new ArrayList<>();
        this.estudiante = new Estudiante();
        this.costoMatricula = BigDecimal.ZERO;
        setMatricula(new Matricula());
        getMatricula();
    }

    public void seleccionarComponenteEducativo(ComponenteEducativoPlanificado componenteEducativoPlanificado) {
        if (componenteEducativoPlanificado.getSeleccionar()) {
            this.componentesEducativoPlanificadosSeleccionados.add(componenteEducativoPlanificado);
        } else {
            this.componentesEducativoPlanificadosSeleccionados.remove(componenteEducativoPlanificado);
        }
    }

    public String registrar() {
        try {
            userTransaction.begin();
            if (this.estudiante != null) {
                if (!this.componentesEducativoPlanificadosSeleccionados.isEmpty()) {
                    List<CatalogoItem> catalogoItems = catalogoItemService.findByNamedQueryWithLimit("CatalogoItem.findByCodigo", 0, SigecuEnum.ESTADO_MATRICULA_REGISTRAD0.getTipo());
                    for (ComponenteEducativoPlanificado componenteEducativoPlanificado : this.componentesEducativoPlanificadosSeleccionados) {
                        if (validarNumeroMatriculadoParalelo(componenteEducativoPlanificado)) {
                            Boolean verificarCursosRepetidos = this.verificarCursosRepetidos(componenteEducativoPlanificado, matricula.getMatriculaComponenteEducativos());
                            if (!verificarCursosRepetidos) {
                                List<MatriculaComponenteEducativo> matriculaComponenteEducativos = new ArrayList<>();
                                if (matricula.getId() != null) {
                                    matriculaComponenteEducativos = matriculaComponenteEducativoService.findByNamedQueryWithLimit("MatriculaComponenteEducativo.findMatricula", 0, matricula);
                                }
                                MatriculaComponenteEducativo matriculaComponenteEducativo = !matriculaComponenteEducativos.isEmpty() ? matriculaComponenteEducativos.get(0) : null;
                                if (matriculaComponenteEducativo == null) {
                                    matriculaComponenteEducativo = new MatriculaComponenteEducativo();
                                    matriculaComponenteEducativo.setEstado(!catalogoItems.isEmpty() ? catalogoItems.get(0).getId() : null);
                                    matriculaComponenteEducativo.setComponenteEducativoPlanificado(componenteEducativoPlanificado);
                                    matricula.agregarMatriculasComponentes(matriculaComponenteEducativo);
                                }
                            } else {
                                this.matricula.setMatriculaComponenteEducativos(new ArrayList<MatriculaComponenteEducativo>());
                                agregarMensajeError("com.megagitel.sigecu.matriculacion.cursosrepetidos");
                                return "";
                            }
                        } else {
                            String mensaje = I18nUtil.getMessages("com.megagitel.sigecu.matriculacion.cupoerror");
                            mensaje = String.format(mensaje, componenteEducativoPlanificado.getJornada().getParalelo().getNombre(), componenteEducativoPlanificado.getOfertaComponenteEducativo().getComponenteEducativo().getNombre());
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null);
                            FacesContext.getCurrentInstance().addMessage(null, message);
                            return "";
                        }
                    }
                    this.matricula.setCosto(costoMatricula);
                    if (matricula.getId() == null) {
                        Date fechaActual = new Date();
                        OfertaAcademica ofertaAcademicaActual = ofertaAcademicaService.getOfertaAcademicaActual(fechaActual);
                        matricula.setEstado(!catalogoItems.isEmpty() ? catalogoItems.get(0).getId() : null);
                        matricula.setOfertaAcademica(ofertaAcademicaActual);
                        matricula.setFechaRegistro(fechaActual);
                        matricula.setEstudiante(estudiante);
                        this.matriculaService.create(matricula);
                    } else {
                        this.matriculaService.edit(matricula);
                    }
                } else {
                    agregarMensajeError("com.megagitel.sigecu.matriculacion.nocursosseleccionados");
                    return "";
                }
            } else {
                agregarMensajeError("com.megagitel.sigecu.matriculacion.noestudiante");
                return "";
            }
            notificarMatriculacionEstudiante(matricula);
            userTransaction.commit();
        } catch (IllegalStateException | SecurityException | HeuristicMixedException
                | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException e) {
            try {
                userTransaction.rollback();
            } catch (IllegalStateException | SecurityException | SystemException ex) {
                Logger.getLogger(RegistroMatriculaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "";
        }

        return "/faces/paginas/matriculacion/listadoMatriculasEstudiante.xhtml?faces-redirect=true&matriculaId=" + this.matricula.getId();
    }

    private Boolean validarNumeroMatriculadoParalelo(ComponenteEducativoPlanificado componenteEducativoPlanificado) {
        List<MatriculaComponenteEducativo> matriculaComponenteEducativos = this.matriculaComponenteEducativoService.findByNamedQueryWithLimit("MatriculaComponenteEducativo.findComponentePlanificado", 0, componenteEducativoPlanificado);
        if (componenteEducativoPlanificado.getJornada().getParalelo().getNumeroMaximoMatriculados() >= matriculaComponenteEducativos.size() + 1) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private Boolean verificarCursosRepetidos(ComponenteEducativoPlanificado componenteEducativoPlanificado,
            List<MatriculaComponenteEducativo> matriculaComponenteEducativos) {
        for (MatriculaComponenteEducativo matriculaComponenteEducativo : matriculaComponenteEducativos) {
            if (matriculaComponenteEducativo.getComponenteEducativoPlanificado().getOfertaComponenteEducativo().getComponenteEducativo().equals(componenteEducativoPlanificado.getOfertaComponenteEducativo().getComponenteEducativo())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private void notificarMatriculacionEstudiante(Matricula matricula) {
        MailDto mailDto = new MailDto();
        String mensaje = I18nUtil.getMessages("com.megagitel.sigecu.matriculacion.registro");
        String cursosString = "<br></br>";
        for (MatriculaComponenteEducativo matriculaComponenteEducativo : matricula.getMatriculaComponenteEducativos()) {
            cursosString = cursosString.concat(matriculaComponenteEducativo.getComponenteEducativoPlanificado().getOfertaComponenteEducativo().getComponenteEducativo().getNombre()).concat("<br></br>");
        }

        mailDto.setMensaje(String.format(mensaje, "<b>" + matricula.getEstudiante().getNombresCompletos() + "</b>", cursosString));
        mailDto.setDatosDestinatario(matricula.getEstudiante().getNombresCompletos());
        mailDto.setDestino(matricula.getEstudiante().getEmail());
        EmailService.enviar(mailDto);
    }

    public String getDetalleParametrizacion(String codigo, String valorDefecto) {
        List<DetalleParametrizacion> detallesParametrizacion = this.detalleParametrizacionService.findByNamedQueryWithLimit("DetalleParametrizacion.findByCodigo", 0, codigo);
        return !detallesParametrizacion.isEmpty() ? detallesParametrizacion.get(0).getValor() : valorDefecto;
    }

    public List<Jornada> getJornadas() {
        try {
            if (this.jornadas.isEmpty()) {
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
        if (matricula.getId() == null) {
            Date fechaActual = new Date();
            OfertaAcademica ofertaAcademicaActual = ofertaAcademicaService.getOfertaAcademicaActual(fechaActual);
            List<Matricula> matriculas = matriculaService.findByNamedQueryWithLimit("Matricula.findByOfertaAcademicaEstudiante", 0, ofertaAcademicaActual, getEstudiante());
            this.matricula = !matriculas.isEmpty() ? matriculas.get(0) : new Matricula();
        }
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
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

    public BigDecimal getCostoMatricula() {
        this.costoMatricula = BigDecimal.ZERO;
        for (ComponenteEducativoPlanificado componenteEducativoPlanificado : this.componentesEducativoPlanificadosSeleccionados) {
            this.costoMatricula = (costoMatricula.add(componenteEducativoPlanificado.getOfertaComponenteEducativo().getComponenteEducativo().getCosto()));
        }
        return costoMatricula;
    }

    public void setCostoMatricula(BigDecimal costoMatricula) {
        this.costoMatricula = costoMatricula;
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
