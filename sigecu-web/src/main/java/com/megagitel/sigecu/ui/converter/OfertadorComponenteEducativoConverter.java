/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.ui.converter;

import com.megagitel.sigecu.academico.ejb.OfertadorComponenteEducativoService;
import com.megagitel.sigecu.academico.modelo.OfertadorComponenteEducativo;
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
@Named("ofertadorComponenteEducativoConverter")
@RequestScoped
@FacesConverter(forClass = OfertadorComponenteEducativo.class)
public class OfertadorComponenteEducativoConverter implements Converter, Serializable {

    @EJB
    private OfertadorComponenteEducativoService ofertadorComponenteEducativoService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return this.ofertadorComponenteEducativoService.find(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return String.valueOf(value);
    }

}
