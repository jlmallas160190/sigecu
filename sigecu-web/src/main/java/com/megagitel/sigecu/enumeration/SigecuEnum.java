/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.enumeration;

/**
 *
 * @author jorgemalla
 */
public enum SigecuEnum {
    TIPO_DOCUMENTO("TIPO_DOCUMENTO"),
    ACTIVIDAD_ECONOMICA("ACTIVIDAD_ECONOMICA"),
    ESTADO_CIVIL("ESTADO_CIVIL"),
    NIVEL_INSTRUCCION("NIVEL_INSTRUCCION"),
    PAIS("PAIS"),
    GENERO("TIPO_SEXO"),
    TIPO_DOCUMENTO_CEDULA("CEDULA"),
    TIPO_DIRECCION_DOCIMICILIO("DOMICILIO"),
    DETALLE_PARAM_TOKEN_RESET_PASSWORD("URL_TOKEN_RESET_PASSWORD"),
    DETALLE_PARAM_HOST("HOST"),
    ESTUDIANTE("ESTUDIANTE");

    private String tipo;

    private SigecuEnum(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
