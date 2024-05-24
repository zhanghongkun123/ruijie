package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao.ViewDesktopPoolComputerDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.ViewDesktopPoolComputerEntity;
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

import java.util.List;

/**
 * Description: 桌面池与PC终端/PC终端组关联
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/30
 *
 * @author zqj
 */

@Service
public class DesktopPoolComputerListServiceImpl extends AbstractPageQueryTemplate<ViewDesktopPoolComputerEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolComputerListServiceImpl.class);



    @Autowired
    private ViewDesktopPoolComputerDAO viewDesktopPoolComputerDAO;


    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("name", "ip", "mac", "alias");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("state", Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        //父类实现了
    }

    @Override
    protected Page<ViewDesktopPoolComputerEntity> find(Specification<ViewDesktopPoolComputerEntity> specification, Pageable pageable) {
        if (specification == null) {
            return viewDesktopPoolComputerDAO.findAll(pageable);
        }
        return viewDesktopPoolComputerDAO.findAll(specification, pageable);
    }
}
