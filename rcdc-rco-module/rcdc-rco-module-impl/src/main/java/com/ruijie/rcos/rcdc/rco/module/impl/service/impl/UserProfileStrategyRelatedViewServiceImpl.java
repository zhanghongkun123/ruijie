package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserProfileStrategyRelatedViewService;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.ViewRcoUserProfileStrategyRelatedDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.ViewRcoUserProfileStrategyRelatedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Description: 用户配置策略关联信息详情视图实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/18
 *
 * @author WuShengQiang
 */
@Service
public class UserProfileStrategyRelatedViewServiceImpl extends
        AbstractPageQueryTemplate<ViewRcoUserProfileStrategyRelatedEntity>
        implements UserProfileStrategyRelatedViewService {

    private final static String NAME_FIELD_NAME = "name";

    @Autowired
    private ViewRcoUserProfileStrategyRelatedDAO viewRcoUserProfileStrategyRelatedDAO;

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of(NAME_FIELD_NAME);
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {

    }

    @Override
    protected Page<ViewRcoUserProfileStrategyRelatedEntity> find(Specification<ViewRcoUserProfileStrategyRelatedEntity> specification,
                                                                 Pageable pageable) {
        return viewRcoUserProfileStrategyRelatedDAO.findAll(specification, pageable);
    }

    @Override
    public Page<ViewRcoUserProfileStrategyRelatedEntity> pageQuery(PageSearchRequest request) {
        Assert.notNull(request, "request is null");
        return super.pageQuery(request, ViewRcoUserProfileStrategyRelatedEntity.class);
    }
}