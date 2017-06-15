/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.ui.converter;

import com.megagitel.sigecu.academico.ejb.ComponenteEducativoService;
import com.megagitel.sigecu.academico.modelo.ComponenteEducativo;
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
@Named("componenteEducativoConverter")
@RequestScoped
@FacesConverter(forClass = ComponenteEducativo.class)
public class ComponenteEducativoConverter implements Converter, Serializable {

    @EJB
    private ComponenteEducativoService componenteEducativoService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        List<ComponenteEducativo> componenteEducativos = this.componenteEducativoService.findByNamedQueryWithLimit("ComponenteEducativo.findByCodigo", 0, value);
        return !componenteEducativos.isEmpty() ? componenteEducativos.get(0) : null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return String.valueOf(value);
    }

}
