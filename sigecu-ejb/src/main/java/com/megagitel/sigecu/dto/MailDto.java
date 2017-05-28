/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.dto;

/**
 *
 * @author jorgemalla
 */
public class MailDto {

    private String mensaje;
    private String archivo;
    private String destino;
    private String datosDestinatario;

    public MailDto() {
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDatosDestinatario() {
        return datosDestinatario;
    }

    public void setDatosDestinatario(String datosDestinatario) {
        this.datosDestinatario = datosDestinatario;
    }

}
