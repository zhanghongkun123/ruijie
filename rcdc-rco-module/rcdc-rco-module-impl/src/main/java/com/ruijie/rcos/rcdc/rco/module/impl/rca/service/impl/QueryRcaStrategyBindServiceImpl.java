package com.ruijie.rcos.rcdc.rco.module.impl.rca.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewRcaStrategyUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewRcaStrategyUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rca.service.QueryRcaStrategyBindService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.UUID;

/**
 * Description: 云应用策略绑定视图service
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/31 下午11:27
 *
 * @author yanlin
 */

@Service
public class QueryRcaStrategyBindServiceImpl extends AbstractPageQueryTemplate<ViewRcaStrategyUserEntity>
        implements QueryRcaStrategyBindService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryRcaStrategyBindServiceImpl.class);

    @Autowired
    private ViewRcaStrategyUserDAO viewRcaStrategyUserDAO;

    public static final String RCA_STRATEGY_ID = "rcaStrategyId";

    public static final String DYNAMIC_USER_RCA_STRATEGY_ID = "dynamicUserRcaStrategyId";

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("userName", "realName");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        //
    }

    @Override
    protected Page<ViewRcaStrategyUserEntity> find(Specification<ViewRcaStrategyUserEntity> specification, Pageable pageable) {
        if (specification == null) {
            return viewRcaStrategyUserDAO.findAll(pageable);
        }
        return viewRcaStrategyUserDAO.findAll(specification, pageable);
    }

    @Override
    public Page<ViewRcaStrategyUserEntity> pageQuery(UUID rcaStrategyId, PageSearchRequest request) {
        Assert.notNull(rcaStrategyId, "rcaStrategyId is null");
        Assert.notNull(request, "request is null");

        DefaultDataSort defaultDataSort = getDefaultDataSort();
        Assert.notNull(defaultDataSort, "默认排序信息不能为null");
        String defaultSortField = defaultDataSort.getSortField();
        Assert.hasText(defaultSortField, "默认排序信息不能为空");
        // 构建Pageable
        Pageable pageable = getPageRequest(request, defaultSortField, defaultDataSort.getDirection());
        return viewRcaStrategyUserDAO.findAll(new PageQuerySpecification<ViewRcaStrategyUserEntity>(request.getSearchKeyword(),
                getSearchColumn(), request.getMatchEqualArr(),
                request.getIsAnd()) {
            @Override
            public Predicate toPredicate(Root<ViewRcaStrategyUserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Assert.notNull(root, "root is null");
                Assert.notNull(query, "query is null");
                Assert.notNull(cb, "cb is null");

                Predicate predicate = super.toPredicate(root, query, cb);
                Assert.notNull(predicate, "predicate is null");
                // 添加独享应用策略条件
                Predicate rcaStrategyPredicate = cb.equal(root.get(RCA_STRATEGY_ID), rcaStrategyId);
                // 添加共享应用策略条件
                Predicate dynamicUserPredicate = cb.equal(root.get(DYNAMIC_USER_RCA_STRATEGY_ID), rcaStrategyId);

                predicate.getExpressions().add(cb.or(rcaStrategyPredicate, dynamicUserPredicate));
                return predicate;
            }

        }, pageable);
    }
}
