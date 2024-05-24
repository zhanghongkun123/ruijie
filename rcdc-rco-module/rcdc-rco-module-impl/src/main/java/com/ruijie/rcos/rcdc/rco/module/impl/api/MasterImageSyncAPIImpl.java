package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.CbbCrossClusterSyncImageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.MasterImageSyncAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.MasterCrossClusterImageSyncDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MasterCompleteImageSyncRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MasterImageSyncPrepareRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.service.MasterImageSyncService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/26
 *
 * @author zhiweihong
 */
public class MasterImageSyncAPIImpl implements MasterImageSyncAPI {

    @Autowired
    private MasterImageSyncService masterImageSyncService;

    @Override
    public void prepare(MasterImageSyncPrepareRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        MasterCrossClusterImageSyncDTO clusterImageSyncDto = new MasterCrossClusterImageSyncDTO();
        clusterImageSyncDto.setImageId(request.getImageTemplateId());
        clusterImageSyncDto.setUnifiedManageDataId(UUID.randomUUID());
        masterImageSyncService.prepareStage(request);
    }

    @Override
    public void sync(CbbCrossClusterSyncImageRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        masterImageSyncService.sync(request);
    }

    @Override
    public void complete(MasterCompleteImageSyncRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        masterImageSyncService.complete(request);
    }
}
