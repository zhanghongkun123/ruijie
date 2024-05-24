package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CabinetDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CabinetEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * Description: 机柜分页查询
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author BaiGuoliang
 */
@Service
public class QueryCabinetServiceImpl extends AbstractPageQueryTemplate<CabinetEntity> {

    @Autowired
    private CabinetDAO cabinetDAO;

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("name");
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
    protected Page<CabinetEntity> find(Specification<CabinetEntity> specification, Pageable pageable) {
        if (specification == null) {
            return cabinetDAO.findAll(pageable);
        }
        return cabinetDAO.findAll(specification, pageable);
    }
}