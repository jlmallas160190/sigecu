/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.util;

import java.util.List;
import java.util.Map;

/**
 *
 * @author jorgemalla
 */
public class QueryData<E> {

    private List<E> result;
    private Long totalResultCount;

    private int start;
    private int end;
    private String sortField;
    private QuerySortOrder order;
    private Map<String, String> filters;

    public QueryData() {
        this(0, 0, "", QuerySortOrder.ASC, null);
    }

    public QueryData(int start, int end, String field, QuerySortOrder order,
            Map<String, String> filters) {

        this.start = start;
        this.end = end;
        this.sortField = field;
        this.order = order;
        this.filters = filters;
    }

    public List<E> getResult() {
        return result;
    }

    public Long getTotalResultCount() {
        return totalResultCount;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public QuerySortOrder getOrder() {
        return order;
    }

    public Map<String, String> getFilters() {
        return filters;
    }

    public void setResult(List<E> result) {
        this.result = result;
    }

    public void setTotalResultCount(Long totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    public String getSortField() {
        return sortField;
    }

    @Override
    public String toString() {
        return "QueryData [start=" + start + ", end=" + end
                + ", sortField=" + sortField + ", order=" + order
                + ", filters=" + filters + "]";
    }
}
