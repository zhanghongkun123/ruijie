package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;
import com.ruijie.rcos.rcdc.rco.module.impl.service.specification.DesktopPageQuerySpecification;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/10
 *
 * @author Jarman
 */
@Service
public class QueryRecycleBinDesktopService extends AbstractPageQueryTemplate<ViewUserDesktopEntity> {

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("desktopName", "userName", "realName", "platformName");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("deleteTime", Sort.Direction.DESC);
    }

    @Override
    protected Page<ViewUserDesktopEntity> find(Specification<ViewUserDesktopEntity> specification, Pageable pageable) {
        @SuppressWarnings("serial")
        Specification<ViewUserDesktopEntity> recycSpec = (Specification<ViewUserDesktopEntity>) (root, query, cb) -> {
            Assert.notNull(root, "root is null");
            Assert.notNull(query, "query is null");
            Assert.notNull(cb, "CriteriaBuilder is null");
            return cb.equal(root.get("isDelete"), Boolean.TRUE);
        };
        if (specification == null) {
            specification = recycSpec;
        }
        return viewDesktopDetailDAO.findAll(specification, pageable);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        //父类实现了
    }

    @Override
    protected PageQuerySpecification<ViewUserDesktopEntity> buildSpecification(PageSearchRequest request) {
        if (StringUtils.isBlank(request.getSearchKeyword()) && ArrayUtils.isEmpty(request.getMatchEqualArr())) {
            // 允许为空
            return null;
        } else {
            return new DesktopPageQuerySpecification(request.getSearchKeyword(), getSearchColumn(), request.getMatchEqualArr(), request.getIsAnd());
        }
    }

}
