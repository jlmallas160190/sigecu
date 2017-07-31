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
    ACTIVIDAD_ECONOMICA_TELECOMUNICACIONES("TELECOMUNICACIONES"),
    SECCION("SECCION"),
    ESTADO_MATRICULA("ESTADO_MATRICULA"),
    ESTADO_MATRICULA_REGISTRAD0("REGISTRADO"),
    ESTADO_MATRICULA_MATRICULADO("MATRICULADO"),
    ESTADO_MATRICULA_APROBADO("APROBADO"),
    TIPO_CODIGO_BARRA("TYPE_BARCODE"),
    TIPO_CODIGO_BARRA_UPCA("upca"),
    TIPO_CODIGO_BARRA_EAN13("EAN-13"),
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
