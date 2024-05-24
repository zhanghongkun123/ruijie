package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewRcaHostDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewRcaHostDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RcaHostDesktopViewService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.specification.RcaHostDesktopPageQuerySpecification;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * desktop view service
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月11日
 * 
 * @author liuwc
 */
@Service
public class RcaHostDesktopViewServiceImpl extends AbstractPageQueryTemplate<ViewRcaHostDesktopEntity> implements RcaHostDesktopViewService {

    @Autowired
    private ViewRcaHostDesktopDetailDAO viewRcaHostDesktopDetailDAO;

    @Override
    public Page<ViewRcaHostDesktopEntity> pageQuery(PageSearchRequest request) {
        Assert.notNull(request, "request is null");

        if (request instanceof DeskPageSearchRequest) {
            DeskPageSearchRequest searchRequest = (DeskPageSearchRequest) request;
            RcaHostDesktopPageQuerySpecification specification =
                    new RcaHostDesktopPageQuerySpecification(request.getSearchKeyword(), getSearchColumn(),
                    request.getMatchEqualArr(), request.getIsAnd());
            specification.setScheduleTypeCode(searchRequest.getScheduleTypeCode());
            specification.setEntityClass(ViewRcaHostDesktopEntity.class);
            return viewRcaHostDesktopDetailDAO.findAll(specification, buildPageable(request));
        }

        return super.pageQuery(request, ViewRcaHostDesktopEntity.class);
    }

    @Override
    public List<ViewRcaHostDesktopEntity> findAllByPoolId(UUID poolId) {
        Assert.notNull(poolId, "poolId can not be null");
        return viewRcaHostDesktopDetailDAO.findAllByRcaPoolId(poolId);
    }

    @Override
    public List<ViewRcaHostDesktopEntity> listByHostIdIn(List<UUID> hostIdList) throws BusinessException {
        Assert.notEmpty(hostIdList, "hostIdList is not empty");

        return viewRcaHostDesktopDetailDAO.findAllByRcaHostIdIn(hostIdList);
    }

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("ip", "desktopName", "imageTemplateName", "physicalServerIp", "rcaPoolName", "platformName");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("latestLoginTime", Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        entityFieldMapper.mapping("desktopIp", "ip");
    }

    @Override
    protected Page<ViewRcaHostDesktopEntity> find(Specification<ViewRcaHostDesktopEntity> specification, Pageable pageable) {
        return viewRcaHostDesktopDetailDAO.findAll(specification, pageable);
    }
}
