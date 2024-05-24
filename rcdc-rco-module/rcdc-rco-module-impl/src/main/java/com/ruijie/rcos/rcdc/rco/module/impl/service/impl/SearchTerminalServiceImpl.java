package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchHitContent;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewTerminalSearchDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalSearchEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractSearchTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalNetworkInfoDTO;
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

/**
 * Description: 搜索用户
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/3
 *
 * @author Jarman
 */
@Service
public class SearchTerminalServiceImpl extends AbstractSearchTemplate<ViewTerminalSearchEntity> {

    @Autowired
    private ViewTerminalSearchDAO viewTerminalSearchDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchTerminalServiceImpl.class);

    @Override
    protected SearchHitContent matchHitContent(ViewTerminalSearchEntity viewTerminalSearchEntity, String keyword) {
        String terminalId = viewTerminalSearchEntity.getTerminalId();
        String name = viewTerminalSearchEntity.getTerminalName();
        String ip = viewTerminalSearchEntity.getIp();
        String networkInfos = viewTerminalSearchEntity.getNetworkInfos();
        if (StringUtils.isNotBlank(name) && name.contains(keyword)) {
            return new SearchHitContent(terminalId, name + "(" + ip + ")");
        }

        if (StringUtils.isNotBlank(networkInfos)) {
            List<CbbTerminalNetworkInfoDTO> networkInfoDTOList = JSON.parseArray(networkInfos, CbbTerminalNetworkInfoDTO.class);
            for (CbbTerminalNetworkInfoDTO networkInfoDTO : networkInfoDTOList) {
                if (StringUtils.isNotBlank(networkInfoDTO.getIp()) && networkInfoDTO.getIp().contains(keyword)) {
                    return new SearchHitContent(terminalId, name + "(" + networkInfoDTO.getIp() + ")");
                }
            }
        }

        LOGGER.warn("没有找到包含[{}]的终端信息", keyword);
        // 找不到匹配则返回null
        return null;
    }

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("terminalName", "ip", "networkInfos");
    }

    @Override
    protected Page<ViewTerminalSearchEntity> find(Specification<ViewTerminalSearchEntity> specification, Pageable pageable) {
        if (specification == null) {
            return viewTerminalSearchDAO.findAll(pageable);
        }
        return viewTerminalSearchDAO.findAll(specification, pageable);
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        //父类实现了
    }
}
