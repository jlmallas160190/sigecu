/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.ui.model;

import com.megagitel.sigecu.academico.ejb.EstudianteService;
import com.megagitel.sigecu.academico.modelo.Estudiante;
import com.megagitel.sigecu.util.QueryData;
import com.megagitel.sigecu.util.QuerySortOrder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author jorgemalla
 */
public class LazyEstudianteDataModel extends LazyDataModel<Estudiante> implements Serializable {

    @EJB
    private EstudianteService estudianteService;

    private List<Estudiante> resultado;
    private String filtro;
    private Date start;
    private Date end;
    private static final int MAX_RESULTS = 1;
    private int firstResult = 0;

    public LazyEstudianteDataModel(EstudianteService estudianteService) {
        setPageSize(MAX_RESULTS);
        this.resultado = new ArrayList<>();
        this.estudianteService = estudianteService;
    }

    @Override
    public List<Estudiante> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        int finPagination = first + pageSize;
        QuerySortOrder order = QuerySortOrder.DESC;
        if (sortOrder == SortOrder.ASCENDING) {
            order = QuerySortOrder.ASC;
        }
        QueryData<Estudiante> query = this.estudianteService.find(first, finPagination, sortField, order, filters);
        this.setRowCount(query.getTotalResultCount().intValue());
        return query.getResult();
    }

    @Override
    public Estudiante getRowData(String rowKey) {
        return this.estudianteService.find(Long.valueOf(rowKey));
    }

    @Override
    public Object getRowKey(Estudiante entity) {
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
        return estudianteService.count() > this.getPageSize() + firstResult;
    }

    public EstudianteService getEstudianteService() {
        return estudianteService;
    }

    public void setEstudianteService(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    public List<Estudiante> getResultado() {
        return resultado;
    }

    public void setResultado(List<Estudiante> resultado) {
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
