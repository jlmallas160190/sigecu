/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.ui.model;

import com.megagitel.sigecu.academico.ejb.JornadaService;
import com.megagitel.sigecu.academico.modelo.Jornada;
import com.megagitel.sigecu.util.QueryData;
import com.megagitel.sigecu.util.QuerySortOrder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author jorgemalla
 */
public class LazyJornadaDataModel extends LazyDataModel<Jornada> implements Serializable {

    @EJB
    private JornadaService jornadaService;

    private List<Jornada> resultado;
    private String filtro;
    private Date start;
    private Date end;
    private static final int MAX_RESULTS = 1;
    private int firstResult = 0;

    public LazyJornadaDataModel(JornadaService jornadaService) {
        setPageSize(MAX_RESULTS);
        this.resultado = new ArrayList<>();
        this.jornadaService = jornadaService;
    }

    @Override
    public List<Jornada> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        int finPagination = first + pageSize;
        QuerySortOrder order = QuerySortOrder.DESC;
        if (sortOrder == SortOrder.ASCENDING) {
            order = QuerySortOrder.ASC;
        }
        Map<String, Object> _filters = new HashMap<>();
        _filters.put("eliminar", Boolean.FALSE);
        QueryData<Jornada> query = this.jornadaService.find(first, finPagination, sortField, order, _filters);
        this.setRowCount(query.getTotalResultCount().intValue());
        return query.getResult();
    }

    @Override
    public Jornada getRowData(String rowKey) {
        return this.jornadaService.find(Long.valueOf(rowKey));
    }

    @Override
    public Object getRowKey(Jornada entity) {
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
        return jornadaService.count() > this.getPageSize() + firstResult;
    }

    public JornadaService getJornadaService() {
        return jornadaService;
    }

    public void setJornadaService(JornadaService jornadaService) {
        this.jornadaService = jornadaService;
    }

    public List<Jornada> getResultado() {
        return resultado;
    }

    public void setResultado(List<Jornada> resultado) {
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
