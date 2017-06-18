/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.ui.model;

import com.megagitel.sigecu.academico.ejb.ParaleloService;
import com.megagitel.sigecu.academico.modelo.Paralelo;
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
public class LazyParaleloDataModel extends LazyDataModel<Paralelo> implements Serializable {

    private ParaleloService paraleloService;
    private List<Paralelo> resultado;
    private String filtro;
    private Date start;
    private Date end;
    private static final int MAX_RESULTS = 1;
    private int firstResult = 0;

    public LazyParaleloDataModel(ParaleloService paraleloService) {
        setPageSize(MAX_RESULTS);
        this.resultado = new ArrayList<>();
        this.paraleloService = paraleloService;
    }

    @Override
    public List<Paralelo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
        _filters.put("eliminar", Boolean.FALSE);
        QueryData<Paralelo> qData = this.paraleloService.find(first, finPagination, sortField, order, _filters);
        this.setRowCount(qData.getTotalResultCount().intValue());
        return qData.getResult();
    }

    @Override
    public Paralelo getRowData(String rowKey) {
        return this.paraleloService.find(Long.valueOf(rowKey));
    }

    @Override
    public Object getRowKey(Paralelo entity) {
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
        return paraleloService.count() > this.getPageSize() + firstResult;
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
    }

    public List<Paralelo> getResultado() {
        return resultado;
    }

    public void setResultado(List<Paralelo> resultado) {
        this.resultado = resultado;
    }

    public ParaleloService getParaleloService() {
        return paraleloService;
    }

    public void setParaleloService(ParaleloService paraleloService) {
        this.paraleloService = paraleloService;
    }

}
