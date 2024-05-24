package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchHitContent;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoViewUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractSearchTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Description: 搜索用户
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/3
 *
 * @author Jarman
 */
@Service
public class SearchUserServiceImpl extends AbstractSearchTemplate<RcoViewUserEntity> {

    @Autowired
    private RcoViewUserDAO rcoViewUserDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchUserServiceImpl.class);

    @Override
    protected SearchHitContent matchHitContent(RcoViewUserEntity viewUserEntity, String keyword) {
        UUID id = viewUserEntity.getId();
        String name = viewUserEntity.getUserName();
        if (StringUtils.isNotBlank(name) && name.contains(keyword)) {
            return new SearchHitContent(id, name);
        }
        LOGGER.warn("没有找到包含[{}]的用户信息", keyword);
        // 找不到匹配则返回null
        return null;
    }

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("userName");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Sort.Direction.DESC);
    }

    @Override
    protected Page<RcoViewUserEntity> find(Specification<RcoViewUserEntity> specification, Pageable pageable) {
        if (specification == null) {
            return rcoViewUserDAO.findAll(pageable);
        }
        return rcoViewUserDAO.findAll(specification, pageable);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        //父类实现了
    }
}
