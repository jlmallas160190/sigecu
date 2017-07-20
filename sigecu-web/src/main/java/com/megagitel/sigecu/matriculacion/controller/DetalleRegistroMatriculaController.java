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
import com.megagitel.sigecu.core.modelo.CatalogoItem;
import com.megagitel.sigecu.core.modelo.DetalleParametrizacion;
import com.megagitel.sigecu.dto.MailDto;
import com.megagitel.sigecu.util.EmailService;
import com.megagitel.sigecu.util.SigecuController;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.krysalis.barcode4j.impl.upcean.UPCABean;
import org.krysalis.barcode4j.impl.upcean.UPCEANBean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

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

    @EJB
    private MatriculaService matriculaService;
    @EJB
    private DetalleParametrizacionService detalleParametrizacionService;
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

    public void generarImagenBarCode(String code) {
        try {
            String path = getDetalleParametrizacion("RUTA_BARCODE", "");
            UPCEANBean bean = new UPCABean();
            final int dpi = 200;
            bean.setModuleWidth(UnitConv.in2mm(2.8f / dpi));
            bean.doQuietZone(false);
            File outputFile = new File(path + code + ".png");
            FileOutputStream out = new FileOutputStream(outputFile);
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                    out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            bean.generateBarcode(canvas, code);
            canvas.finish();
        } catch (IOException ex) {
            try {
                throw ex;
            } catch (IOException ex1) {
                Logger.getLogger(DetalleRegistroMatriculaController.class.getName()).log(Level.SEVERE, null, ex1);
            }
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

}
