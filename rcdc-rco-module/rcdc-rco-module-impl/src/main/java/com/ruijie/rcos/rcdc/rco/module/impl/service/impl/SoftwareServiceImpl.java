package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.RcoGlobalParameterEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.SoftwareService;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoSoftwareDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoSoftwareEntity;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/03/09 10:07
 *
 * @author chenli
 */
@Service
public class SoftwareServiceImpl extends AbstractPageQueryTemplate<RcoSoftwareEntity> implements SoftwareService {

    @Autowired
    private RcoSoftwareDAO rcoSoftwareDAO;

    @Autowired
    private GlobalParameterService globalParameterService;

    private final static String NAME_SEARCH_FIELD_NAME = "nameSearch";

    private final static String NAME_FIELD_NAME = "name";

    /**
     * 搜索对应的表列 对应前端的keyword
     */
    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of(NAME_SEARCH_FIELD_NAME);
    }

    /**
     * 获取默认排序
     */
    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort(NAME_FIELD_NAME, Sort.Direction.DESC);
    }

    /**
     * 前端查询字段映射到数据库字段,根据需实现映射代码
     *
     * @param entityFieldMapper
     */
    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {

    }

    /**
     * 分页查询
     *
     * @param specification
     * @param pageable
     * @return
     */
    @Override
    protected Page<RcoSoftwareEntity> find(Specification<RcoSoftwareEntity> specification, Pageable pageable) {
        return rcoSoftwareDAO.findAll(specification, pageable);
    }

    /**
     * @param request search request
     * @return view page
     */
    @Override
    public Page<RcoSoftwareEntity> pageQuery(PageSearchRequest request) {
        Assert.notNull(request, "request is null");
        return super.pageQuery(request, RcoSoftwareEntity.class);
    }
}
