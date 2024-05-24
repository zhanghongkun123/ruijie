package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchHitContent;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractSearchTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月29日
 *
 * @author zhanghongkun
 */
@Service
public class SearchComputerImpl extends AbstractSearchTemplate<ComputerEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchComputerImpl.class);

    @Autowired
    ComputerBusinessDAO computerBusinessDAO;

    private static final String COLUMN_IP = "ip";

    private static final String COLUMN_NAME = "name";

    @Override
    protected SearchHitContent matchHitContent(ComputerEntity computerEntity, String keyword) {
        UUID id = computerEntity.getId();
        String ip = computerEntity.getIp();
        String name = computerEntity.getName();
        if (StringUtils.isNotBlank(ip) && ip.contains(keyword)) {
            return new SearchHitContent(id, ip + "(" + name + ")");
        }

        if (StringUtils.isNotBlank(name) && name.contains(keyword)) {
            return new SearchHitContent(id, name + "(" + ip + ")");
        }

        LOGGER.warn("没有找到包含[{}]的PC信息", keyword);
        // 没有找到匹配的内容返回null
        return null;
    }

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of(COLUMN_IP, COLUMN_NAME);
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        // 父类实现了
    }

    @Override
    protected Page<ComputerEntity> find(Specification<ComputerEntity> specification, Pageable pageable) {
        if (specification == null) {
            return computerBusinessDAO.findAll(pageable);
        }
        return computerBusinessDAO.findAll(specification, pageable);
    }
}
