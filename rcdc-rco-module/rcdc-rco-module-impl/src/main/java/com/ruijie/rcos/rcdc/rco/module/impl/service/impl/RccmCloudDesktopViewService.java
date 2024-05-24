package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: RCCM搜索云桌面列表service
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/18
 *
 * @author WuShengQiang
 */
@Service
public class RccmCloudDesktopViewService extends AbstractPageQueryTemplate<ViewUserDesktopEntity> {

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("desktopName", "userName");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("latestLoginTime", Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        entityFieldMapper.mapping("desktopIp", "ip");
    }

    @Override
    protected Page<ViewUserDesktopEntity> find(Specification<ViewUserDesktopEntity> specification, Pageable pageable) {
        return viewDesktopDetailDAO.findAll(specification, pageable);
    }
}
