package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewSoftTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewSoftTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;
import com.ruijie.rcos.rcdc.rco.module.impl.service.specification.SoftTerminalPageQuerySpecification;

/**
 * Description: 终端分页列表
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月21日
 *
 * @author nt
 */
@Service
public class SoftTerminalListServiceImpl extends AbstractPageQueryTemplate<ViewSoftTerminalEntity> {

    @Autowired
    private ViewSoftTerminalDAO viewTerminalDAO;

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of(Constants.TERMINAL_NAME, Constants.IP, Constants.MAC_ADDR, Constants.BIND_USER_NAME,
                Constants.NETWORK_INFOS, Constants.USER_NAME);
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        //父类实现了
    }

    @Override
    protected Page<ViewSoftTerminalEntity> find(Specification<ViewSoftTerminalEntity> specification, Pageable pageable) {
        return viewTerminalDAO.findAll(specification, pageable);
    }

    @Override
    protected PageQuerySpecification<ViewSoftTerminalEntity> buildSpecification(PageSearchRequest request) {
        if (StringUtils.isBlank(request.getSearchKeyword()) && ArrayUtils.isEmpty(request.getMatchEqualArr())) {
            // 允许为空
            return null;
        } else {
            return new SoftTerminalPageQuerySpecification(request.getSearchKeyword(), getSearchColumn(),
                    request.getMatchEqualArr(), request.getIsAnd());
        }
    }
}
