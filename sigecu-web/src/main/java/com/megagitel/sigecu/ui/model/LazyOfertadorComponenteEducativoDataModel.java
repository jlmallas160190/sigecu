/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.ui.model;

import com.megagitel.sigecu.academico.ejb.OfertadorComponenteEducativoService;
import com.megagitel.sigecu.academico.modelo.OfertadorComponenteEducativo;
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
public class LazyOfertadorComponenteEducativoDataModel extends LazyDataModel<OfertadorComponenteEducativo> implements Serializable {

    private OfertadorComponenteEducativoService ofertadorComponenteEducativoService;
    private List<OfertadorComponenteEducativo> resultado;
    private String filtro;
    private Date start;
    private Date end;
    private static final int MAX_RESULTS = 1;
    private int firstResult = 0;

    public LazyOfertadorComponenteEducativoDataModel(OfertadorComponenteEducativoService oas) {
        setPageSize(MAX_RESULTS);
        this.resultado = new ArrayList<>();
        this.ofertadorComponenteEducativoService = oas;
    }

    @Override
    public List<OfertadorComponenteEducativo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
        QueryData<OfertadorComponenteEducativo> qData = this.ofertadorComponenteEducativoService.find(first, finPagination, sortField, order, _filters);
        this.setRowCount(qData.getTotalResultCount().intValue());
        return qData.getResult();
    }

    @Override
    public OfertadorComponenteEducativo getRowData(String rowKey) {
        return ofertadorComponenteEducativoService.find(Long.valueOf(rowKey));
    }

    @Override
    public Object getRowKey(OfertadorComponenteEducativo entity) {
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
        return ofertadorComponenteEducativoService.count() > this.getPageSize() + firstResult;
    }

    public OfertadorComponenteEducativoService getOfertadorComponenteEducativoService() {
        return ofertadorComponenteEducativoService;
    }

    public void setOfertadorComponenteEducativoService(OfertadorComponenteEducativoService ofertadorComponenteEducativoService) {
        this.ofertadorComponenteEducativoService = ofertadorComponenteEducativoService;
    }

    public List<OfertadorComponenteEducativo> getResultado() {
        return resultado;
    }

    public void setResultado(List<OfertadorComponenteEducativo> resultado) {
        this.resultado = resultado;
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

}
