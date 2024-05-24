package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
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
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/30 15:16
 *
 * @author ketb
 */
@Service
public class ComputerListServiceImpl extends AbstractPageQueryTemplate<ComputerEntity> {


    @Autowired
    private ComputerBusinessDAO computerBusinessDAO;

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
    protected Page<ComputerEntity> find(Specification<ComputerEntity> specification, Pageable pageable) {
        if (specification == null) {
            return computerBusinessDAO.findAll(pageable);
        }
        return computerBusinessDAO.findAll(specification, pageable);
    }
}
