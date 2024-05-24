package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rcacdocking.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionTaskManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskResultDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rcacdocking.RcacDockingRestServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rcacdocking.request.RcacReportAppTerminalStatusRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rcacdocking.request.RcacReportFileDistributeResultRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rcacdocking.response.DefaultRcacRestServerResponse;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/7 16:17
 *
 * @author zhangyichi
 */
@Deprecated
public class RcacDockingRestServerImpl implements RcacDockingRestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcacDockingRestServerImpl.class);

    @Autowired
    private FileDistributionTaskManageAPI fileDistributionTaskManageAPI;

    @Override
    public DefaultRcacRestServerResponse reportFileDistributeStatus(RcacReportFileDistributeResultRestServerRequest restRequest) {
        Assert.notNull(restRequest, "restRequest cannot be null!");

        List<DistributeTaskResultDTO> resultList = restRequest.getResultList();
        if (CollectionUtils.isEmpty(resultList)) {
            LOGGER.info("RCAC反馈文件分发任务结果为空");
            return DefaultRcacRestServerResponse.success();
        }

        resultList.forEach(fileDistributionTaskManageAPI::processSubTaskResult);
        return DefaultRcacRestServerResponse.success();
    }

    @Override
    public DefaultRcacRestServerResponse reportRcaClientOnline(RcacReportAppTerminalStatusRestServerRequest restRequest) {
        Assert.notNull(restRequest, "restRequest cannot be null!");

        LOGGER.info("收到云应用客户端[{}]上线通知", restRequest.getRcaClientId());
        return DefaultRcacRestServerResponse.success();
    }
}
