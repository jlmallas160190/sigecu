/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.ui.converter;

import com.megagitel.sigecu.academico.ejb.GrupoComponenteEducativoService;
import com.megagitel.sigecu.academico.modelo.GrupoComponenteEducativo;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

/**
 *
 * @author jorgemalla
 */
@Named("grupoComponenteEducativoConverter")
@RequestScoped
@FacesConverter(forClass = GrupoComponenteEducativo.class)
public class GrupoComponenteEducativoConverter implements Converter, Serializable {

    @EJB
    private GrupoComponenteEducativoService grupoComponenteEducativoService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        List<GrupoComponenteEducativo> grupoComponenteEducativos = this.grupoComponenteEducativoService.findByNamedQueryWithLimit("GrupoComponenteEducativo.findByCodigo", 0, value);
        return !grupoComponenteEducativos.isEmpty() ? grupoComponenteEducativos.get(0) : null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return String.valueOf(value);
    }

}
