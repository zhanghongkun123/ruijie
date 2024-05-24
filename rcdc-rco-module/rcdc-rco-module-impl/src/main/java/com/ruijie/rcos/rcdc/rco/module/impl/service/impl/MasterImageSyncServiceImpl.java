package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.CbbCrossClusterSyncImageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.MasterCrossClusterImageSyncDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MasterCompleteImageSyncRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MasterImageSyncPrepareRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.MasterImageSyncService;
import com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.master.complete.MasterCompleteStageStateHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.master.prepare.MasterClusterPrepareStageStateHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.master.prepare.MasterStageContextResolver;
import com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.master.sync.MasterSyncStageStateHandler;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 主端准备服务实现
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
@Service
public class MasterImageSyncServiceImpl extends AbstractImageSyncServiceImpl implements MasterImageSyncService {

    private static Logger LOGGER = LoggerFactory.getLogger(MasterImageSyncServiceImpl.class);

    @Autowired
    private StateMachineFactory stateFactory;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private RccmManageService rccmManageService;

    @Override
    public void prepareStage(MasterImageSyncPrepareRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        UUID imageId = request.getImageTemplateId();
        prepareValid(imageId);

        MasterCrossClusterImageSyncDTO clusterImageSyncDto = new MasterCrossClusterImageSyncDTO();
        clusterImageSyncDto.setImageId(imageId);
        clusterImageSyncDto.setUnifiedManageDataId(UUID.randomUUID());


        stateFactory.newBuilder(request.getTaskId(), MasterClusterPrepareStageStateHandler.class)
                .initArg(MasterStageContextResolver.DTO, clusterImageSyncDto)
                .lockResources(request.getImageTemplateId().toString())
                .start();

    }

    @Override
    public void sync(CbbCrossClusterSyncImageRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        notAllowRoleExecuteCheck(rccmManageService.isSlave());

        isImageExist(request.getImageTemplateId());

        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(request.getImageTemplateId());
        isImageStateNotInSync(imageTemplateDetail, BusinessKey.RCDC_RCO_IMAGE_NOT_SYNCING_NOT_ALLOW_SYNC);


        stateFactory.newBuilder(request.getTaskId(), MasterSyncStageStateHandler.class)
                .initArg(MasterStageContextResolver.DTO, request)
                .lockResources(request.getTargetDiskId().toString())
                .start();

    }

    @Override
    public void complete(MasterCompleteImageSyncRequest request) throws BusinessException {

        Assert.notNull(request, "request can not be null");

        notAllowRoleExecuteCheck(rccmManageService.isSlave());

        isImageExist(request.getImageTemplateId());

        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(request.getImageTemplateId());
        isImageStateNotInSync(imageTemplateDetail, BusinessKey.RCDC_RCO_IMAGE_NOT_SYNCING_NOT_ALLOW_COMPLETE);

        stateFactory.newBuilder(request.getTaskId(), MasterCompleteStageStateHandler.class)
                .initArg(MasterStageContextResolver.DTO, request)
                .lockResources(request.getImageTemplateId().toString())
                .start();

    }

    private void prepareValid(UUID imageId) throws BusinessException {

        notAllowRoleExecuteCheck(rccmManageService.isSlave());

        isImageExist(imageId);

        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);

        isImageStateInSync(imageTemplateDetail);

        isImageStateNotInAvailable(imageTemplateDetail);
    }
}
