/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.dao;

import com.megagitel.sigecu.enumeration.SigecuEnum;
import com.megagitel.sigecu.util.QueryData;
import com.megagitel.sigecu.util.QuerySortOrder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author jorgemalla
 * @param <T>
 */
public abstract class AbstractDao<T> {

    private final Class<T> entityClass;

    public AbstractDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @PersistenceContext(unitName = "sigecuPU")
    protected EntityManager em;

    public void create(T entity) {
        em.persist(entity);
    }

    public void edit(T entity) {
        em.merge(entity);
    }

    public void remove(T entity) {
        em.remove(em.merge(entity));
    }

    public T find(Object id) {
        return em.find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(em.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public <E> List<E> findByNamedQueryWithLimit(final String namedQueryName, final int limit, final Object... params) {
        Query query = em.createNamedQuery(namedQueryName);
        int i = 1;
        for (Object p : params) {
            query.setParameter(i++, p);
        }
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public QueryData<T> find(int start, int end, String sortField, QuerySortOrder querySortOrder, Map<String, Object> filters) {
        QueryData<T> queryData = new QueryData<>();
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<T> c = cb.createQuery(entityClass);
        Root<T> root = c.from(entityClass);
        c.select(root);
        CriteriaQuery<Long> countQ = cb.createQuery(Long.class);
        Root<T> rootCount = countQ.from(entityClass);
        countQ.select(cb.count(rootCount));
        List<Predicate> criteria = new ArrayList<>();
        List<Predicate> predicates = null;
        String keyFilterPath = "";
        Object valueFilterPath = null;
        if (filters != null) {
            for (String filterProperty : filters.keySet()) {
                Object filterValue = filters.get(filterProperty);
                Object filterValueAux = filterValue;
                if (filterProperty.indexOf(".") > 0) {
                    filterValue = processMap(filterProperty.substring(filterProperty.indexOf(".") + 1), filterValueAux);
                    filterProperty = filterProperty.substring(0, filterProperty.indexOf("."));
                }

                if (filterValue instanceof Map) {
                    predicates = new ArrayList<>();
                    Path<String> filterPropertyPath = root.<String>get((String) filterProperty);
                    HashMap resultFilterPath = getFilterPath(filterValue, filterPropertyPath);
                    keyFilterPath = (String) resultFilterPath.get(SigecuEnum.KEY_FILTER_PATH.getTipo());
                    valueFilterPath = resultFilterPath.get(SigecuEnum.VALUE_FILTER_PATH.getTipo());
                    Path<String> filterPath = (Path<String>) resultFilterPath.get(SigecuEnum.FILTER_PATH.getTipo());
                    Predicate predicate = null;
                    if (valueFilterPath instanceof String) {
                        ParameterExpression<String> pexp = cb.parameter(String.class, (String) keyFilterPath);
                        predicate = cb.like(cb.lower(filterPath), pexp);
                        valueFilterPath = "%".concat((String) valueFilterPath).concat("%");
                        predicates.add(predicate);
                    } else {
                        ParameterExpression<?> pexp = cb.parameter(valueFilterPath.getClass(), (String) keyFilterPath);
                        predicate = cb.equal(filterPath, pexp);
                        predicates.add(predicate);
                    }
                    if (!predicates.isEmpty()) {
                        Predicate[] array = new Predicate[predicates.size()];
                        criteria.add(cb.or(predicates.toArray(array)));
                    }
                } else if (filterValue instanceof List) {
                    Path<String> filterPropertyPath = root.<String>get((String) filterProperty);
                    ParameterExpression<List> pexp = cb.parameter(List.class, filterProperty);
                    Predicate predicate = filterPropertyPath.in(pexp);
                    criteria.add(predicate);
                } else if (filterValue instanceof String) {
                    valueFilterPath = "%".concat((String) filterValue).concat("%");
                    keyFilterPath = filterProperty;
                    ParameterExpression<String> pexp = cb.parameter(String.class, filterProperty);
                    Predicate predicate = cb.like(cb.lower(root.<String>get(filterProperty)), pexp);
                    criteria.add(predicate);
                } else if (filterValue instanceof Date) {
                    valueFilterPath = filterValue;
                    keyFilterPath = filterProperty;
                    Path<Date> filterPropertyPath = root.<Date>get(filterProperty);
                    ParameterExpression<Date> pexpStart = cb.parameter(Date.class, "start");
                    ParameterExpression<Date> pexpEnd = cb.parameter(Date.class, "end");
                    Predicate predicate = cb.between(filterPropertyPath, pexpStart, pexpEnd);
                    criteria.add(predicate);
                    break;
                } else {
                    valueFilterPath = filterValue;
                    keyFilterPath = filterProperty;
                    ParameterExpression<?> pexp = cb.parameter(filterValue != null ? filterValue.getClass() : Object.class,
                            filterProperty);
                    Predicate predicate = null;
                    if (filterValue == null) {
                        predicate = cb.isNull(root.get(filterProperty));
                    } else {
                        predicate = cb.equal(root.get(filterProperty), pexp);
                    }
                    criteria.add(predicate);
                }
            }
        }
        if (criteria.size() == 1) {
            c.where(criteria.get(0));
            countQ.where(criteria.get(0));
        } else if (criteria.size() > 1) {
            c.where(cb.and(criteria.toArray(new Predicate[0])));
            countQ.where(cb.and(criteria.toArray(new Predicate[0])));
        }
        if (sortField != null) {
            List<Order> orders = new ArrayList<>();
            Path<String> path = null;
            if (!sortField.contains(",")) {
                path = root.get(sortField);
                if (querySortOrder == QuerySortOrder.ASC) {
                    orders.add(cb.asc(path));
                } else {
                    orders.add(cb.desc(path));
                }

            } else {
                for (String field : sortField.split(",")) {
                    path = root.get(field.trim());
                    if (querySortOrder == QuerySortOrder.ASC) {
                        orders.add(cb.asc(path));
                    } else {
                        orders.add(cb.desc(path));
                    }
                }
            }
            c.orderBy(orders);
        }
        TypedQuery<T> q = (TypedQuery<T>) createQuery(c);
        q.setHint("org.hibernate.cacheable", true);
        TypedQuery<Long> countquery = (TypedQuery<Long>) createQuery(countQ);
        countquery.setHint("org.hibernate.cacheable", true);
        if (filters != null) {
            for (String filterProperty : filters.keySet()) {
                Object filterValue = filters.get(filterProperty);
                Object filterValueAux = filterValue;
                if (filterProperty.indexOf(".") > 0) {
                    filterValue = processMap(filterProperty.substring(filterProperty.indexOf(".") + 1), filterValueAux);
                    filterProperty = filterProperty.substring(0, filterProperty.indexOf("."));
                }

                if (filterValue instanceof Map) {
                    Map resultParameterMap = getParameterMap(filterValue);
                    String k = (String) resultParameterMap.get(SigecuEnum.KEY_FILTER_PATH.getTipo());
                    Object v = resultParameterMap.get(SigecuEnum.VALUE_FILTER_PATH.getTipo());
                    if (v instanceof String) {
                        q.setParameter(k, "%" + v + "%");
                        countquery.setParameter(k, "%" + v + "%");
                    }
                } else if (filterValue instanceof String) {
                    q.setParameter(filterProperty, "%" + filterValue + "%");
                    countquery.setParameter(filterProperty, "%" + filterValue + "%");
                } else if (filterValue instanceof Date) {
                    q.setParameter(q.getParameter((String) filterProperty, Date.class), (Date) filterValue, TemporalType.TIMESTAMP);
                    countquery.setParameter(q.getParameter((String) filterProperty, Date.class), (Date) filterValue, TemporalType.TIMESTAMP);
                } else {
                    q.setParameter(filterProperty, filterValue);
                    countquery.setParameter(filterProperty, filterValue);
                }
            }

        }
        if (start != -1 && end != -1) {
            q.setMaxResults(end - start);
            q.setFirstResult(start);
        }
        queryData.setResult(q.getResultList());
        Long totalResultCount = countquery.getSingleResult();
        queryData.setTotalResultCount(totalResultCount);
        return queryData;
    }

    private HashMap getFilterPath(Object filterValue, Path<String> filterPropertyPath) {
        HashMap<String, Object> data = new HashMap<>();
        for (Object key : ((Map) filterValue).keySet()) {
            filterPropertyPath = filterPropertyPath.get(String.valueOf(key));
            Object value = ((Map) filterValue).get((String) key);
            if (value instanceof Map) {
                data = getFilterPath(value, filterPropertyPath);
            } else {
                data.put(SigecuEnum.KEY_FILTER_PATH.getTipo(), key);
                data.put(SigecuEnum.VALUE_FILTER_PATH.getTipo(), value);
                data.put(SigecuEnum.FILTER_PATH.getTipo(), filterPropertyPath);
            }
        }
        return data;
    }

    private Map getParameterMap(Object filterValue) {
        Map<String, Object> data = new HashMap<>();
        for (Object key : ((Map) filterValue).keySet()) {
//            filterPropertyPath = filterPropertyPath.get(String.valueOf(key));
            Object value = ((Map) filterValue).get((String) key);
            if (value instanceof Map) {
                data = getParameterMap(value);
            } else {
                data.put(SigecuEnum.KEY_FILTER_PATH.getTipo(), key);
                data.put(SigecuEnum.VALUE_FILTER_PATH.getTipo(), value);
//                data.put(SigecuEnum.FILTER_PATH.getTipo(), filterPropertyPath);
            }
        }
        return data;
    }

    private Map<String, Object> processMap(String filters, Object value) {
        Map<String, Object> mapCurrently = new HashMap<>();
        Map<String, Object> mapFinal = new HashMap<>();
        String keyCurrently = "";
        String[] filterArray = filters.split("\\.");
        int contador = 0;
        for (String filter : filterArray) {
            if (contador == 0) {
                mapCurrently.put(filter, null);
                mapFinal = mapCurrently;
                keyCurrently = filter;
            } else {
                mapCurrently.get(keyCurrently);
                Map<String, Object> mapNew = new HashMap<>();
                mapNew.put(filter, null);

                mapCurrently.put(keyCurrently, mapNew);
                mapCurrently = mapNew;

                keyCurrently = filter;
            }
            contador++;
        }
        mapCurrently.get(keyCurrently);
        mapCurrently.put(keyCurrently, value);
        return mapFinal;
    }

    protected CriteriaBuilder getCriteriaBuilder() {
        return em.getCriteriaBuilder();
    }

    public TypedQuery<?> createQuery(CriteriaQuery<?> criteriaQuery) {
        return em.createQuery(criteriaQuery);
    }

}
