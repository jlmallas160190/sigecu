/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.ui.model;

import com.megagitel.sigecu.academico.ejb.OfertaAcademicaService;
import com.megagitel.sigecu.academico.modelo.OfertaAcademica;
import com.megagitel.sigecu.util.QueryData;
import com.megagitel.sigecu.util.QuerySortOrder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author jorgemalla
 */
public class LazyOfertaAcademicaDataModel extends LazyDataModel<OfertaAcademica> implements Serializable {

    private OfertaAcademicaService ofertaAcademicaService;
    private List<OfertaAcademica> resultado;

    private String filtro;
    private Date start;
    private Date end;
    private static final int MAX_RESULTS = 5;
    private int firstResult = 0;

    public LazyOfertaAcademicaDataModel(OfertaAcademicaService ofertaAcademicaService) {
        setPageSize(MAX_RESULTS);
        this.resultado = new ArrayList<>();
        this.ofertaAcademicaService = ofertaAcademicaService;
    }

    @Override
    public List<OfertaAcademica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        int finPagination = first + pageSize;
        Date fechaActual = new Date(System.currentTimeMillis());
        QuerySortOrder order = QuerySortOrder.DESC;
        if (sortOrder == SortOrder.ASCENDING) {
            order = QuerySortOrder.ASC;
        }
        Map<String, Object> _filters = new HashMap<>();
        Map<String, Date> range = new HashMap<>();
        if (getStart() != null) {
            range.put("start", getStart());
            if (getEnd() != null) {
                range.put("end", getEnd());
            } else {
                range.put("end", fechaActual);
            }
        }
        QueryData<OfertaAcademica> qData = this.ofertaAcademicaService.find(first, finPagination, sortField, order, _filters);
        this.setRowCount(qData.getTotalResultCount().intValue());
        return qData.getResult();
    }

    @Override
    public OfertaAcademica getRowData(String rowKey) {
        return ofertaAcademicaService.find(Long.valueOf(rowKey));
    }

    @Override
    public Object getRowKey(OfertaAcademica entity) {
        return entity.getId();
    }

    public int getNextFirstResult() {
        return firstResult + this.getPageSize();
    }

    public int getPreviousFirstResult() {
        return this.getPageSize() >= firstResult ? 0 : firstResult - this.getPageSize();
    }

    public boolean isPreviousExists() {
        return firstResult > 0;
    }

    public boolean isNextExists() {
        return ofertaAcademicaService.count() > this.getPageSize() + firstResult;
    }

    public List<OfertaAcademica> getResultado() {
        return resultado;
    }

    public void setResultado(List<OfertaAcademica> resultado) {
        this.resultado = resultado;
    }

    public OfertaAcademicaService getOfertaAcademicaService() {
        return ofertaAcademicaService;
    }

    public void setOfertaAcademicaService(OfertaAcademicaService ofertaAcademicaService) {
        this.ofertaAcademicaService = ofertaAcademicaService;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

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

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        this.resultado = null;
    }

}
