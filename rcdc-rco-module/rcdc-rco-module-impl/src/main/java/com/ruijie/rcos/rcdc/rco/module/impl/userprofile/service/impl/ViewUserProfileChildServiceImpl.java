package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.ViewUserProfileChildPathDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.ViewUserProfileChildPathEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.service.ViewUserProfileChildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Description: 子路径列表展示的service
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/21
 *
 * @author zwf
 */
@Service
public class ViewUserProfileChildServiceImpl extends AbstractPageQueryTemplate<ViewUserProfileChildPathEntity>
        implements ViewUserProfileChildService {
    @Autowired
    private ViewUserProfileChildPathDAO viewUserProfileChildPathDAO;

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("ip", "path", "userProfilePathId", "mode", "type");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("userProfilePathId", Sort.Direction.DESC);
    }

    @Override
    protected Page<ViewUserProfileChildPathEntity> find(Specification<ViewUserProfileChildPathEntity> specification, Pageable pageable) {
        return viewUserProfileChildPathDAO.findAll(specification, pageable);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        entityFieldMapper.mapping("userProfilePathId", "userProfilePathId");
    }

    @Override
    public Page<ViewUserProfileChildPathEntity> pageQuery(PageSearchRequest request) {
        Assert.notNull(request, "request is null");
        return super.pageQuery(request, ViewUserProfileChildPathEntity.class);
    }


}
