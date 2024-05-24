package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.image.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.CbbCrossClusterSyncImageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.MasterImageSyncAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MasterCompleteImageSyncRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MasterImageSyncPrepareRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.image.MasterImageSyncRestServer;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: 主端镜像同步处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
@Service
public class MasterImageSyncRestServerImpl implements MasterImageSyncRestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterImageSyncRestServerImpl.class);

    @Autowired
    private MasterImageSyncAPI masterImageSyncAPI;


    @Override
    public void prepare(MasterImageSyncPrepareRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        LOGGER.info("收到主端准备镜像同步请求[{}]", JSON.toJSONString(request));
        masterImageSyncAPI.prepare(request);
    }

    @Override
    public void sync(CbbCrossClusterSyncImageRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        LOGGER.info("收到主端开始镜像同步请求[{}]", JSON.toJSONString(request));
        masterImageSyncAPI.sync(request);
    }

    @Override
    public void complete(MasterCompleteImageSyncRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        LOGGER.info("收到主端完成镜像同步请求[{}]", JSON.toJSONString(request));
        masterImageSyncAPI.complete(request);
    }
}
