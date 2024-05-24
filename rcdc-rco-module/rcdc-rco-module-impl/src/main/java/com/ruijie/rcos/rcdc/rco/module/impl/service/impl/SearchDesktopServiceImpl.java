package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchHitContent;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopSearchDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewDesktopSearchEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractSearchTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/4
 *
 * @author Jarman
 */
@Service
public class SearchDesktopServiceImpl extends AbstractSearchTemplate<ViewDesktopSearchEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchDesktopServiceImpl.class);

    @Autowired
    private ViewDesktopSearchDAO desktopSearchDAO;

    @Override
    protected SearchHitContent matchHitContent(ViewDesktopSearchEntity viewDesktopSearchEntity, String keyword) {
        UUID id = viewDesktopSearchEntity.getDeskId();
        String name = viewDesktopSearchEntity.getName();
        if (StringUtils.isNotBlank(name) && name.contains(keyword)) {
            return new SearchHitContent(id, name);
        }
        String ip = viewDesktopSearchEntity.getIp();
        if (StringUtils.isNotBlank(ip) && ip.contains(keyword)) {
            return new SearchHitContent(id, ip + "(" + name + ")");
        }
        String deskIp = viewDesktopSearchEntity.getDeskIp();
        if (StringUtils.isNotBlank(deskIp) && deskIp.contains(keyword)) {
            return new SearchHitContent(id, deskIp + "(" + name + ")");
        }

        String terminalName = viewDesktopSearchEntity.getTerminalName();
        if (StringUtils.isNotBlank(terminalName) && terminalName.contains(keyword)) {
            return new SearchHitContent(id, terminalName + "(" + name + ")");
        }

        LOGGER.warn("没有找到包含[{}]的桌面信息", keyword);
        // 没有找到匹配的内容返回null
        return null;
    }

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("ip", "deskIp", "terminalName");
    }

    @Override
    protected Page<ViewDesktopSearchEntity> find(Specification<ViewDesktopSearchEntity> specification, Pageable pageable) {
        if (specification == null) {
            return desktopSearchDAO.findAll(pageable);
        }
        return desktopSearchDAO.findAll(specification, pageable);
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("timestamp", Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        //父类实现了
    }
}
