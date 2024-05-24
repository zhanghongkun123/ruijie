package com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.master.prepare;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.MasterCrossClusterImageSyncDTO;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/26
 *
 * @author zhiweihong
 */
@Service
public class MasterPrepareImageSyncFinishProcessor implements StateTaskHandle.StateProcessor {

    @Autowired
    private MasterStageContextResolver resolver;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Override
    public StateTaskHandle.ProcessResult doProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context can not be null");
        MasterCrossClusterImageSyncDTO dto = resolver.resolveDTO(context);

        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(dto.getImageId());
        auditLogAPI.recordLog(BusinessKey.RCDC_RCO_MASTER_PREPARE_SUCCESS, imageTemplateDetail.getImageName());
        return StateTaskHandle.ProcessResult.next();
    }


}
