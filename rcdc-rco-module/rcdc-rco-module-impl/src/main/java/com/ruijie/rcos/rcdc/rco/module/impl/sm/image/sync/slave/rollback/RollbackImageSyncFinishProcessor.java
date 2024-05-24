package com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.slave.rollback;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.SlaveRollbackImageSyncDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
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
public class RollbackImageSyncFinishProcessor implements StateTaskHandle.StateProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RollbackImageSyncFinishProcessor.class);


    @Autowired
    private RollbackStageContextResolver resolver;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Override
    public StateTaskHandle.ProcessResult doProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context can not be null");
        SlaveRollbackImageSyncDTO dto = resolver.resolveDTO(context);

        UUID imageId = dto.getImageId();
        List<CbbImageTemplateDetailDTO> versionList = cbbImageTemplateMgmtAPI.listImageTemplateVersionByRootImageId(imageId);

        ImageTemplateState exceptState = obtainState(versionList);
        LOGGER.info("更新镜像[{}]状态为[{}]", imageId, exceptState);
        cbbImageTemplateMgmtAPI.updatePointedState(imageId, exceptState);

        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(dto.getImageId());
        auditLogAPI.recordLog(BusinessKey.RCDC_RCO_SLAVE_ROLLBACK_SUCCESS, imageTemplateDetail.getImageName());
        return StateTaskHandle.ProcessResult.next();
    }

    private ImageTemplateState obtainState(List<CbbImageTemplateDetailDTO> versionList) {
        if (CollectionUtils.isEmpty(versionList)) {
            return ImageTemplateState.ERROR;
        }
        return ImageTemplateState.AVAILABLE;
    }

}
