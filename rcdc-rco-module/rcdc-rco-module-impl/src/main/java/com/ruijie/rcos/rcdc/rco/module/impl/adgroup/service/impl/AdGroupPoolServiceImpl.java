package com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.PageQueryAppGroupDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.common.query.AdditionalSpecification;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdGroupEntityDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdGroupAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.dao.AdGroupPoolDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.entity.ViewAdGroupEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service.AdGroupPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.PageQueryPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserQueryHelper;

/**
 * Description: 安全组与池分配关系
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/9/27
 *
 * @author TD
 */
@Service
public class AdGroupPoolServiceImpl extends AbstractPageQueryTemplate<ViewAdGroupEntity> implements AdGroupPoolService {

    private static final String ID = "id";

    @Autowired
    private IacAdGroupAPI adGroupAPI;

    @Autowired
    private AdGroupPoolDAO adGroupPoolDAO;

    @Override
    public Page<ViewAdGroupEntity> pageAdGroupInOrNotInPool(PageQueryPoolDTO pageQuery) {
        Assert.notNull(pageQuery, "pageQuery can be not null");
        com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest request = pageQuery.getRequest();
        Assert.notNull(request, "request can be not null");
        Pageable pageable = buildPageable(request);

        return adGroupPoolDAO.findAll(new PageQuerySpecification<ViewAdGroupEntity>(request.getSearchKeyword(),
                getSearchColumn(), request.getMatchEqualArr(), request.getIsAnd()) {
            @Override
            public Predicate toPredicate(Root<ViewAdGroupEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Assert.notNull(root, "root is null");
                Assert.notNull(query, "query is null");
                Assert.notNull(cb, "cb is null");

                Predicate predicate = super.toPredicate(root, query, cb);
                Assert.notNull(predicate, "predicate is null");

                pageQuery.setQuery(query);
                pageQuery.setBuilder(cb);

                // 安全组id条件
                Predicate subQueryPredicate = buildUserSubQueryPredicate(pageQuery, root);
                predicate.getExpressions().add(subQueryPredicate);
                return predicate;
            }
        }, pageable);
    }


    @Override
    public Page<ViewAdGroupEntity> pageAdGroupInOrNotInAppGroup(PageQueryAppGroupDTO pageQuery) {
        Assert.notNull(pageQuery, "pageQuery can be not null");
        com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest request = pageQuery.getRequest();
        Assert.notNull(request, "request can be not null");
        Pageable pageable = buildPageable(request);

        return adGroupPoolDAO.findAll(new PageQuerySpecification<ViewAdGroupEntity>(request.getSearchKeyword(),
                getSearchColumn(), request.getMatchEqualArr(), request.getIsAnd()) {
            @Override
            public Predicate toPredicate(Root<ViewAdGroupEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Assert.notNull(root, "root is null");
                Assert.notNull(query, "query is null");
                Assert.notNull(cb, "cb is null");

                Predicate predicate = super.toPredicate(root, query, cb);
                Assert.notNull(predicate, "predicate is null");

                pageQuery.setQuery(query);
                pageQuery.setBuilder(cb);

                // 安全组id条件
                Predicate subQueryPredicate = buildAppGroupUserSubQueryPredicate(pageQuery, root);
                predicate.getExpressions().add(subQueryPredicate);
                return predicate;
            }
        }, pageable);
    }

    @Override
    public List<IacAdGroupEntityDTO> listAdGroupByIds(List<UUID> adGroupIdList) throws BusinessException {
        Assert.notNull(adGroupIdList, "adGroupIdList can be not null");
        if (CollectionUtils.isEmpty(adGroupIdList)) {
            return Collections.emptyList();
        }
        List<IacAdGroupEntityDTO> adGroupList = adGroupAPI.getAdGroupIds(adGroupIdList);

        return adGroupList;
    }

    @Override
    public boolean checkAllAdGroupExistByIds(List<UUID> adGroupIdList) throws BusinessException {
        Assert.notNull(adGroupIdList, "adGroupIdList can be not null");
        if (CollectionUtils.isEmpty(adGroupIdList)) {
            return true;
        }
        return listAdGroupByIds(adGroupIdList).size() == adGroupIdList.size();
    }

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("name");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        //父类实现了
    }

    @Override
    protected Page<ViewAdGroupEntity> find(Specification<ViewAdGroupEntity> specification, Pageable pageable) {
        if (specification == null) {
            return adGroupPoolDAO.findAll(pageable);
        }
        return adGroupPoolDAO.findAll(specification, pageable);
    }

    @Override
    protected Pageable getPageRequest(com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest request, String defaultSortField,
            Sort.Direction defaultDirection) {
        Assert.notNull(defaultSortField, "请指定默认排序字段");
        Assert.notNull(defaultDirection, "请指定默认排序方式");
        int page = request.getPage();
        int limit = request.getLimit();
        List<Sort.Order> orderList = new ArrayList<>();
        // 传入没有指定排序字段，使用id和默认字段进行排序
        if (ArrayUtils.isEmpty(request.getSortArr())) {
            orderList.add(new Sort.Order(defaultDirection, defaultSortField));
            // 加入ID作为排序条件，防止分页数据重复
            orderList.add(new Sort.Order(Sort.Direction.DESC, ID));
            return PageRequest.of(page, limit, Sort.by(orderList));
        }
        // 有指定排序字段和排方式，需要加上id作为排序条件防止分页重复
        Arrays.stream(Objects.requireNonNull(request.getSortArr())).forEach(sortVO -> {
            Sort.Direction directionEnum = Sort.Direction.fromString(sortVO.getDirection().name());
            Sort.Order order = new Sort.Order(directionEnum, sortVO.getSortField());
            orderList.add(order);
        });
        orderList.add(new Sort.Order(Sort.Direction.DESC, ID));
        Sort sort = Sort.by(orderList);
        return PageRequest.of(page, limit, sort);
    }

    private Predicate buildUserSubQueryPredicate(PageQueryPoolDTO pageQuery, Root<ViewAdGroupEntity> root) {
        Subquery<UUID> subQuery = UserQueryHelper.buildPoolUserSubQuery(pageQuery, IacConfigRelatedType.AD_GROUP);
        Expression<String> userIdExpression = root.get(UserQueryHelper.ID);
        return pageQuery.isIn() ? userIdExpression.in(subQuery) : userIdExpression.in(subQuery).not();
    }

    @Override
    public Page<ViewAdGroupEntity> pageQueryAdGroupInObjectGuidList(
            com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest request, List<String> objectGuidList) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(objectGuidList, "objectGuidList can not be null");
        AdditionalSpecification<ViewAdGroupEntity> additionalSpecification = new AdditionalSpecification<ViewAdGroupEntity>(
                ((Specification<ViewAdGroupEntity>) (root, query, criteriaBuilder) ->
                        query.where(criteriaBuilder.in(root.get("objectGuid")).value(objectGuidList)).getRestriction()),
                AdditionalSpecification.AdditionalPredicate.AND);
        return super.pageQuery(request, ViewAdGroupEntity.class, additionalSpecification);
    }

    @Override
    public Page<ViewAdGroupEntity> pageQueryAdGroupInAdGroupIdList(
            com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest request, List<UUID> adGroupIdList) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(adGroupIdList, "adGroupIdList can not be null");
        AdditionalSpecification<ViewAdGroupEntity> additionalSpecification = new AdditionalSpecification<ViewAdGroupEntity>(
                ((Specification<ViewAdGroupEntity>) (root, query, criteriaBuilder) ->
                        query.where(criteriaBuilder.in(root.get("id")).value(adGroupIdList)).getRestriction()),
                AdditionalSpecification.AdditionalPredicate.AND);
        return super.pageQuery(request, ViewAdGroupEntity.class, additionalSpecification);
    }

    private Predicate buildAppGroupUserSubQueryPredicate(PageQueryAppGroupDTO pageQuery, Root<ViewAdGroupEntity> root) {
        Subquery<UUID> subQuery = UserQueryHelper.buildAppGroupUserSubQuery(pageQuery, RcaEnum.GroupMemberType.AD_SAFETY_GROUP);
        Expression<String> userIdExpression = root.get(UserQueryHelper.ID);
        return pageQuery.isIn() ? userIdExpression.in(subQuery) : userIdExpression.in(subQuery).not();
    }

}
