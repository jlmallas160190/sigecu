/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.util;

import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author jorgemalla
 */
public abstract class SigecuController {

    private Date start;
    private Date end;

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void agregarMensajeExitoso(String mensaje) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, I18nUtil.getMessages(mensaje), null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void agregarMensajeFatal(String mensaje) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, I18nUtil.getMessages(mensaje), null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void agregarMensajeError(String mensaje) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, I18nUtil.getMessages(mensaje), null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
