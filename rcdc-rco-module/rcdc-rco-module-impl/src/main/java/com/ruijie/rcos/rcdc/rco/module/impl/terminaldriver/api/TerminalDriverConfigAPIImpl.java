package com.ruijie.rcos.rcdc.rco.module.impl.terminaldriver.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalDriverConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.terminaldriver.response.TerminalDriverConfigResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.terminaldriver.dao.TerminalDriverConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.terminaldriver.entity.TerminalDriverConfigEntity;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: DashboardStatisticsAPIImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public class TerminalDriverConfigAPIImpl implements TerminalDriverConfigAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalDriverConfigAPIImpl.class);

    @Autowired
    private TerminalDriverConfigDAO terminalDriverConfigDAO;

    @Override
    public TerminalDriverConfigResponse findTerminalDriverConfigByProductId(String productId) {
        Assert.hasText(productId, "productId can not null");
        TerminalDriverConfigEntity terminalDriverConfigEntity = terminalDriverConfigDAO.findByProductId(productId);
        if (terminalDriverConfigEntity == null) {
            LOGGER.debug("未获取终端驱动配置信息。productId = {}", productId);
            // 当TerminalDriverConfigEntity为null时返回空对象
            return null;
        }
        return buildTerminalDriverConfigResponse(terminalDriverConfigEntity);
    }

    @Override
    public List<TerminalDriverConfigResponse> findAllTerminalDriverConfig() {
        List<TerminalDriverConfigEntity> terminalDriverConfigEntityList = terminalDriverConfigDAO.findAll();
        if (terminalDriverConfigEntityList.isEmpty()) {
            LOGGER.debug("没有终端驱动配置信息");
            // 未获取到驱动配置信息是返回空对象
            return Collections.emptyList();
        }
        List<TerminalDriverConfigResponse> terminalDriverConfigResponseList = new ArrayList<>();
        for (TerminalDriverConfigEntity entity : terminalDriverConfigEntityList) {
            terminalDriverConfigResponseList.add(buildTerminalDriverConfigResponse(entity));
        }
        return terminalDriverConfigResponseList;
    }

    private TerminalDriverConfigResponse buildTerminalDriverConfigResponse(TerminalDriverConfigEntity entity) {
        TerminalDriverConfigResponse terminalDriverConfigResponse = new TerminalDriverConfigResponse();
        terminalDriverConfigResponse.setId(entity.getId());
        terminalDriverConfigResponse.setDriverType(entity.getDriverType());
        terminalDriverConfigResponse.setProductId(entity.getProductId());
        return terminalDriverConfigResponse;
    }
}
