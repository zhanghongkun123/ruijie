package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserMessageDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserMessageEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 查询用户消息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/10
 *
 * @author Jarman
 */
@Service
public class QueryUserMessageService extends AbstractPageQueryTemplate<UserMessageEntity> {

    @Autowired
    private UserMessageDAO userMessageDAO;

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("title", "content");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Direction.DESC);
    }

    @Override
    protected Page<UserMessageEntity> find(Specification<UserMessageEntity> specification, Pageable pageable) {
        if (specification == null) {
            return userMessageDAO.findAll(pageable);
        }
        return userMessageDAO.findAll(specification, pageable);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        //父类实现了
    }
}
