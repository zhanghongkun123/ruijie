package com.ruijie.rcos.rcdc.rco.module.impl.rca.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewRcaDynamicUserInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewRcaDynamicUserInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rca.service.QueryDynamicUserGroupUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
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
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 24/10/2022 下午 3:33
 *
 * @author gaoxueyuan
 */
@Service
public class QueryDynamicUserGroupUserServiceImpl  extends AbstractPageQueryTemplate<ViewRcaDynamicUserInfoEntity>
        implements QueryDynamicUserGroupUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryDynamicUserGroupUserServiceImpl.class);

    @Autowired
    private ViewRcaDynamicUserInfoDAO viewRcaDynamicUserInfoDAO;

    @Override
    public Page<ViewRcaDynamicUserInfoEntity> pageQuery(PageSearchRequest request) {
        Assert.notNull(request, "request cannot be null");
        return super.pageQuery(request, ViewRcaDynamicUserInfoEntity.class);
    }

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

    }

    @Override
    protected Page<ViewRcaDynamicUserInfoEntity> find(Specification<ViewRcaDynamicUserInfoEntity> specification, Pageable pageable) {
        return viewRcaDynamicUserInfoDAO.findAll(specification, pageable);
    }
}
