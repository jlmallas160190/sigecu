/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.validator;

import com.megagitel.sigecu.util.I18nUtil;
import com.megagitel.sigecu.util.ValidadorCampos;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author jorgemalla
 */
@FacesValidator("numeroIdentificacionValidator")
@RequestScoped
public class NumeroIdentificacionValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value != null) {
            String numeroIdentificacion = String.valueOf(value).trim();
            if (!ValidadorCampos.esNumeroIdentificacionNacional(numeroIdentificacion)) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, I18nUtil.getMessages("numeroIdentificacion.novalid"),
                        null));
            }
        }
    }

}