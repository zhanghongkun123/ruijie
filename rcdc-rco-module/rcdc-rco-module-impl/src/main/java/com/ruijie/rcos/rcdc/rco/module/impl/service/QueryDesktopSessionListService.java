package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopSessionDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewDesktopSessionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月06日
 *
 * @author wangjie9
 */
@Service
public class QueryDesktopSessionListService  extends AbstractPageQueryTemplate<ViewDesktopSessionEntity> {

    @Autowired
    private ViewDesktopSessionDAO viewDesktopSessionDAO;

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("userName", "realName", "desktopName", "terminalIp");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {

    }

    @Override
    protected Page<ViewDesktopSessionEntity> find(Specification<ViewDesktopSessionEntity> specification, Pageable pageable) {
        if (specification == null) {
            return viewDesktopSessionDAO.findAll(pageable);
        }
        return viewDesktopSessionDAO.findAll(specification, pageable);
    }
}
