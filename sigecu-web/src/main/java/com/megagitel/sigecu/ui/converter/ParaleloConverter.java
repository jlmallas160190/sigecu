/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.ui.converter;

import com.megagitel.sigecu.academico.ejb.ParaleloService;
import java.io.Serializable;
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
@Named("paraleloConverter")
@RequestScoped
@FacesConverter(forClass = ParaleloConverter.class)
public class ParaleloConverter implements Converter, Serializable {

    @EJB
    private ParaleloService paraleloService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return this.paraleloService.find(Long.parseLong(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return String.valueOf(value);
    }

}
