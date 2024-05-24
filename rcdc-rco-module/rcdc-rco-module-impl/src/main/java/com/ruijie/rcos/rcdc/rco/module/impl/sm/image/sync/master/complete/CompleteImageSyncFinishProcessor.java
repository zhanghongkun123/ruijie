package com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.master.complete;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.MasterCompleteImageSyncRequest;
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
public class CompleteImageSyncFinishProcessor implements StateTaskHandle.StateProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompleteImageSyncFinishProcessor.class);


    @Autowired
    private CompleteStageContextResolver resolver;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Override
    public StateTaskHandle.ProcessResult doProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context can not be null");
        MasterCompleteImageSyncRequest dto = resolver.resolveDTO(context);

        UUID imageId = dto.getImageTemplateId();

        LOGGER.info("更新镜像[{}]状态为[{}]", imageId, ImageTemplateState.AVAILABLE);
        cbbImageTemplateMgmtAPI.updatePointedState(imageId, ImageTemplateState.AVAILABLE);


        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        auditLogAPI.recordLog(BusinessKey.RCDC_RCO_MASTER_COMPLETE_SUCCESS, imageTemplateDetail.getImageName());
        return StateTaskHandle.ProcessResult.next();
    }


}
