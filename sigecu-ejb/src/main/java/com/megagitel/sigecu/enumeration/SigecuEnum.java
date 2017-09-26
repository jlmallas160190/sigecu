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
    TIPO_DOCUMENTO_RUC("RUC"),
    TIPO_DOCUMENTO_PASAPORTE("PASAPORTE"),
    TIPO_DIRECCION_DOCIMICILIO("DOMICILIO"),
    DETALLE_PARAM_TOKEN_RESET_PASSWORD("URL_TOKEN_RESET_PASSWORD"),
    DETALLE_PARAM_HOST("HOST"),
    DETALLE_PARAM_TABLEROWS("TABLE_ROWS"),
    ACTIVIDAD_ECONOMICA_TELECOMUNICACIONES("TELECOMUNICACIONES"),
    SECCION("SECCION"),
    PARAMETRIZACION_SISTEMA("SIGECU"),
    ESTADO_MATRICULA("ESTADO_MATRICULA"),
    ESTADO_MATRICULA_REGISTRAD0("REGISTRADO"),
    ESTADO_MATRICULA_MATRICULADO("MATRICULADO"),
    ESTADO_MATRICULA_APROBADO("APROBADO"),
    TIPO_CODIGO_BARRA("TYPE_BARCODE"),
    KEY_FILTER_PATH("KEY_FILTER_PATH"),
    VALUE_FILTER_PATH("VALUE_FILTER_PATH"),
    FILTER_PATH("FILTER_PATH"),
    TIPO_CODIGO_BARRA_UPCA("upca"),
    TIPO_CODIGO_BARRA_EAN13("EAN-13"),
    DETALLE_PARAM_CURRENCY("CURRENCY"),
    DETALLE_PARAM_RUTA_BARCODE("RUTA_BARCODE"),
    DETALLE_PARAM_DURACION_CREDITOS("DURACION_CREDITOS"),
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
