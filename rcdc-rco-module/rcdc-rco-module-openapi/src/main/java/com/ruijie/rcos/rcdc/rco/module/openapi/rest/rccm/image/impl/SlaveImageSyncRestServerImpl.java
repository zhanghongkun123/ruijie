package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.image.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.SlaveImageSyncAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoImageTemplateSnapshotDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.PublishSyncingImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SlaveCompareImageSnapshotRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SlavePrepareImageSyncRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SlaveRollbackImageSyncRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.SlaveRollbackImageSyncResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.image.SlaveImageSyncRestServer;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Description: 从端镜像同步处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
@Service
public class SlaveImageSyncRestServerImpl implements SlaveImageSyncRestServer {

    @Autowired
    private SlaveImageSyncAPI slaveImageSyncAPI;

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterImageSyncRestServerImpl.class);

    @Override
    public void prepare(SlavePrepareImageSyncRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        LOGGER.info("收到从端准备镜像同步请求[{}]", JSON.toJSONString(request));
        slaveImageSyncAPI.prepare(request);
    }

    @Override
    public void publish(PublishSyncingImageTemplateDTO request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        LOGGER.info("收到从端准备镜像发布请求[{}]", JSON.toJSONString(request));
        slaveImageSyncAPI.publish(request);
    }

    @Override
    public void rollback(SlaveRollbackImageSyncRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        LOGGER.info("收到从端准备镜像异常回滚请求[{}]", JSON.toJSONString(request));
        slaveImageSyncAPI.rollback(request);
    }

    @Override
    public SlaveRollbackImageSyncResponse compare(SlaveCompareImageSnapshotRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        LOGGER.info("收到从端准备镜像比对快照请求[{}]", JSON.toJSONString(request));
        List<RcoImageTemplateSnapshotDTO> diffSnapshotListCompareWithMasterList =
                slaveImageSyncAPI.getDiffSnapshotListCompareWithMaster(request.getSnapshotList());

        return new SlaveRollbackImageSyncResponse(diffSnapshotListCompareWithMasterList);
    }
}
