/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.ui.converter;

import com.megagitel.sigecu.core.ejb.CatalogoItemService;
import com.megagitel.sigecu.core.modelo.CatalogoItem;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

/**
 *
 * @author jorgemalla
 */
@Named("catalogoItemConverter")
@RequestScoped
public class CatalogoItemConverter implements Converter, Serializable {

    @EJB
    private CatalogoItemService catalogoItemService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        List<CatalogoItem> catalogosItems = this.catalogoItemService.findByNamedQueryWithLimit("CatalogoItem.findByCodigo", 0, value);
        return !catalogosItems.isEmpty() ? catalogosItems.get(0).getId() : null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return String.valueOf(value);
    }
}
