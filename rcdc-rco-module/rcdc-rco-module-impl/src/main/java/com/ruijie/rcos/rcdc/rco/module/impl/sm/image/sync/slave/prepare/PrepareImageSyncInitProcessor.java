package com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.slave.prepare;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.SlavePrepareImageSyncDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.UnifiedManageDataRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.UnifiedManageDataService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
@Service
public class PrepareImageSyncInitProcessor implements StateTaskHandle.StateProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrepareImageSyncInitProcessor.class);

    @Autowired
    private PrepareStageContextResolver resolver;

    @Autowired
    private UnifiedManageDataService unifiedManageDataService;

    @Autowired
    private CbbImageTemplateMgmtAPI imageTemplateMgmtAPI;

    @Override
    public StateTaskHandle.ProcessResult doProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context can not be null");
        SlavePrepareImageSyncDTO dto = resolver.resolveDTO(context);

        UUID imageId = dto.getImageId();

        UnifiedManageDataRequest request = new UnifiedManageDataRequest();
        request.setRelatedId(imageId);
        request.setRelatedType(UnifiedManageFunctionKeyEnum.IMAGE_TEMPLATE);
        request.setUnifiedManageDataId(dto.getUnifiedManageDataId());
        unifiedManageDataService.saveUnifiedManage(request);

        boolean isExistsImageTemplate = imageTemplateMgmtAPI.existsImageTemplate(dto.getImageId());
        if (isExistsImageTemplate) {
            imageTemplateMgmtAPI.updatePointedState(imageId, ImageTemplateState.SYNCING);
        }

        return StateTaskHandle.ProcessResult.next();
    }


    @Override
    public StateTaskHandle.ProcessResult undoProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context can not be null");
        SlavePrepareImageSyncDTO dto = resolver.resolveDTO(context);

        boolean isExistsImageTemplate = imageTemplateMgmtAPI.existsImageTemplate(dto.getImageId());
        if (!isExistsImageTemplate) {
            UnifiedManageDataRequest request = new UnifiedManageDataRequest();
            request.setUnifiedManageDataId(dto.getUnifiedManageDataId());
            request.setRelatedId(dto.getImageId());
            request.setRelatedType(UnifiedManageFunctionKeyEnum.IMAGE_TEMPLATE);
            LOGGER.warn("从端镜像[{}]首次准备异常,删除统一管理数据", dto.getImageId());
            unifiedManageDataService.deleteUnifiedManage(request);
        }

        return StateTaskHandle.ProcessResult.next();
    }
}
