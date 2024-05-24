package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Description: 封装分页查询Specification接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/10
 *
 * @param <T> 表实体类
 * @author Jarman
 */
public class PageQuerySpecification<T> implements Specification<T> {

    /**
     * 搜索关键字
     */
    private String searchKeyword;

    /**
     * 搜索的列名
     */
    private List<String> searchColumnList;

    /**
     * 条件查询，key 字段名，value 字段内容
     */
    private MatchEqual[] matchEqualArr;

    /**
     * 条件查询，key 字段名，value 字段内容
     */
    private Boolean isAnd;

    //结束时间搜索条件
    private Map<String, Date> greaterTimeInfo;//key : columnName  value : time

    //结束时间搜索条件
    private Map<String, Date> lessTimeInfo;//key : columnName  value : time

    private Class<T> entityClass;


    public PageQuerySpecification(String searchKeyword, List<String> searchColumnList, MatchEqual[] matchEqualArr, Boolean isAnd) {
        if (StringUtils.isNotBlank(searchKeyword) && CollectionUtils.isEmpty(searchColumnList)) {
            throw new IllegalArgumentException("请指定需要搜索的列名");
        }
        this.searchKeyword = searchKeyword;
        this.searchColumnList = searchColumnList;
        this.matchEqualArr = matchEqualArr;
        this.isAnd = isAnd;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Assert.notNull(root, "Root不能为null");
        Assert.notNull(query, "CriteriaQuery不能为null");
        Assert.notNull(cb, "CriteriaBuilder不能为null");
        Predicate likePredicate = buildLikePredicate(root, cb);
        Predicate exactMatchPredicate = buildExactMatchPredicate(root, cb);
        List<Predicate> pdList = new ArrayList<>();

        if (likePredicate != null) {
            pdList.add(likePredicate);
        }
        if (exactMatchPredicate != null) {
            pdList.add(exactMatchPredicate);
        }

        Predicate expandPd = expandPredicate(root, cb);
        if (expandPd != null) {
            pdList.add(expandPd);
        }

        return query.where(pdList.toArray(new Predicate[pdList.size()])).getRestriction();
    }

    private Predicate buildGreateTime(Root<T> root, CriteriaBuilder cb) {
        List<Predicate> predicateList = new ArrayList<>();

        greaterTimeInfo.forEach((columnName, value) ->
                predicateList.add(cb.greaterThanOrEqualTo(root.get(columnName), value))
        );

        return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

    }

    /**
     * 构建精确匹配条件查询
     */
    private Predicate buildExactMatchPredicate(Root<T> root, CriteriaBuilder cb) {
        if (ArrayUtils.isEmpty(matchEqualArr)) {
            //没有设置精确匹配查询条件时返回null
            return null;
        }
        List<Predicate> predicateList = new ArrayList<>(matchEqualArr.length);
        for (MatchEqual matchField : matchEqualArr) {
            Object[] fieldValueArr = matchField.getValueArr();
            List<Predicate> matchPredicateList = new ArrayList<>(fieldValueArr.length);
            Field field = null;
            Class fieldClass = null;
            try {
                if (entityClass != null) {
                    field = entityClass.getDeclaredField(matchField.getName());
                    fieldClass = field.getType();
                }
            } catch (NoSuchFieldException e) {
                LoggerFactory.getLogger(PageQuerySpecification.class).error("字段[{}]在类[{}]中找不到", matchField.getName(), entityClass);
            }
            for (Object fieldValue : fieldValueArr) {
                if (fieldValue instanceof String && fieldClass != null && !fieldClass.isInstance(fieldValue)) {
                    if (field.getType().isEnum()) {
                        matchPredicateList.add(cb.equal(root.get(matchField.getName()),
                                Enum.valueOf(fieldClass, (String) fieldValue)));
                        continue;
                    }
                    if (fieldClass.isAssignableFrom(UUID.class)) {
                        matchPredicateList.add(cb.equal(root.get(matchField.getName()),
                                UUID.fromString((String) fieldValue)));
                        continue;
                    }
                    if (fieldClass.isAssignableFrom(Boolean.class) || fieldClass.isAssignableFrom(boolean.class)) {
                        matchPredicateList.add(cb.equal(root.get(matchField.getName()),
                                Boolean.parseBoolean((String) fieldValue)));
                        continue;
                    }
                    if (fieldClass.isAssignableFrom(Integer.class) || fieldClass.isAssignableFrom(int.class)) {
                        matchPredicateList.add(cb.equal(root.get(matchField.getName()),
                                Integer.parseInt((String) fieldValue)));
                        continue;
                    }
                    if (fieldClass.isAssignableFrom(Long.class) || fieldClass.isAssignableFrom(long.class)) {
                        matchPredicateList.add(cb.equal(root.get(matchField.getName()),
                                Long.parseLong((String) fieldValue)));
                        continue;
                    }
                    if (fieldClass.isAssignableFrom(Double.class) || fieldClass.isAssignableFrom(double.class)) {
                        matchPredicateList.add(cb.equal(root.get(matchField.getName()),
                                Double.parseDouble((String) fieldValue)));
                        continue;
                    }
                }
                matchPredicateList.add(cb.equal(root.get(matchField.getName()), fieldValue));
            }
            Predicate[] matchPredicateArr = new Predicate[matchPredicateList.size()];
            matchPredicateList.toArray(matchPredicateArr);
            predicateList.add(cb.or(matchPredicateArr));
        }
        Predicate[] predicateArr = new Predicate[predicateList.size()];
        Predicate matchPredicate = null;
        //如果是采用 and查询
        if (isAnd == null || isAnd) {
            matchPredicate = cb.and(predicateList.toArray(predicateArr));
            return matchPredicate;
        }
        //如果是采用 or查询
        matchPredicate = cb.or(predicateList.toArray(predicateArr));

        return matchPredicate;
    }

    /**
     * 构建like条件查询
     */
    protected Predicate buildLikePredicate(Root<T> root, CriteriaBuilder cb) {
        if (StringUtils.isBlank(searchKeyword)) {
            //没有传搜索关键字时返回null
            return null;
        }
        List<Predicate> predicateList = new ArrayList<>(searchColumnList.size());
        searchColumnList.forEach(item -> predicateList.add(cb.like(root.get(item).as(String.class), "%" + searchKeyword + "%")));
        Predicate[] predicateArr = new Predicate[predicateList.size()];
        return cb.or(predicateList.toArray(predicateArr));
    }

    protected Predicate expandPredicate(Root<T> root, CriteriaBuilder cb) {
        //用于扩展
        return null;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public List<String> getSearchColumnList() {
        return searchColumnList;
    }

    public Boolean getAnd() {
        return isAnd;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
}
