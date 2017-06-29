/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.dao;

import com.megagitel.sigecu.util.QueryData;
import com.megagitel.sigecu.util.QuerySortOrder;
import java.util.ArrayList;
import java.util.Date;
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
        if (filters != null) {
            for (String filterProperty : filters.keySet()) {
                Object filterValue = filters.get(filterProperty);
                if (filterValue instanceof Map) {
                    predicates = new ArrayList<>();
                    for (Object key : ((Map) filterValue).keySet()) {
                        Object value = ((Map) filterValue).get((String) key);
                        if (value instanceof Date) {
                            Path<Date> filterPropertyPath = root.<Date>get(filterProperty);
                            ParameterExpression<Date> pexpStart = cb.parameter(Date.class, "start");
                            ParameterExpression<Date> pexpEnd = cb.parameter(Date.class, "end");
                            Predicate predicate = cb.between(filterPropertyPath, pexpStart, pexpEnd);
                            criteria.add(predicate);
                            break;
                        } else if (value instanceof String) {
                            Path<String> filterPropertyPath = root.<String>get((String) key);
                            ParameterExpression<String> pexp = cb.parameter(String.class,
                                    (String) key);
                            Predicate predicate = cb.like(cb.lower(filterPropertyPath), pexp);
                            predicates.add(predicate);
                        }
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
                    ParameterExpression<String> pexp = cb.parameter(String.class, filterProperty);
                    Predicate predicate = cb.like(cb.lower(root.<String>get(filterProperty)), pexp);
                    criteria.add(predicate);
                } else {
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
                if (filterValue instanceof Map) {
                    for (Object key : ((Map) filterValue).keySet()) {
                        Object value = ((Map) filterValue).get((String) key);
                        //Verify data content for build
                        if (value instanceof Date) {
                            q.setParameter(q.getParameter((String) key, Date.class), (Date) value, TemporalType.TIMESTAMP);
                            countquery.setParameter(q.getParameter((String) key, Date.class), (Date) value, TemporalType.TIMESTAMP);
                        } else {
                            String _filterValue = "%" + (String) value + "%";
                            q.setParameter(q.getParameter((String) key, String.class), _filterValue);
                            countquery.setParameter(q.getParameter((String) key, String.class), _filterValue);
                        }
                    }
                } else if (filterValue instanceof List) {
                    q.setParameter(filterProperty, filterValue);
                    countquery.setParameter(filterProperty, filterValue);
                } else if (filterValue instanceof String) {
                    filterValue = "%" + filterValue + "%";
                    q.setParameter(filterProperty, filterValue);
                    countquery.setParameter(filterProperty, filterValue);
                } else if (filterValue != null) {
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

    protected CriteriaBuilder getCriteriaBuilder() {
        return em.getCriteriaBuilder();
    }

    public TypedQuery<?> createQuery(CriteriaQuery<?> criteriaQuery) {
        return em.createQuery(criteriaQuery);
    }

}
