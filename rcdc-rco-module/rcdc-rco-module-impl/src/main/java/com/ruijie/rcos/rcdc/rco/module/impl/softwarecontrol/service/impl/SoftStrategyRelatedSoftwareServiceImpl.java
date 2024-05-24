package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.ViewSoftRelatedSoftStrategyDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.ViewSoftRelatedSoftStrategyEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.service.SoftStrategyRelatedSoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Description:根据软件id返回软件策略列表接口
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/1/24 17:42
 *
 * @author linrenjian
 */
@Service
public class SoftStrategyRelatedSoftwareServiceImpl extends AbstractPageQueryTemplate<ViewSoftRelatedSoftStrategyEntity>
        implements SoftStrategyRelatedSoftwareService {


    @Autowired
    private ViewSoftRelatedSoftStrategyDAO viewSoftRelatedSoftStrategyDAO;

    @Override
    public Page<ViewSoftRelatedSoftStrategyEntity> pageQuery(PageSearchRequest request) {
        Assert.notNull(request, "request is null");

        return super.pageQuery(request, ViewSoftRelatedSoftStrategyEntity.class);
    }

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("name");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("name", Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
    }

    @Override
    protected Page<ViewSoftRelatedSoftStrategyEntity>
        find(Specification<ViewSoftRelatedSoftStrategyEntity> specification, Pageable pageable) {

        return viewSoftRelatedSoftStrategyDAO.findAll(specification, pageable);
    }
}
