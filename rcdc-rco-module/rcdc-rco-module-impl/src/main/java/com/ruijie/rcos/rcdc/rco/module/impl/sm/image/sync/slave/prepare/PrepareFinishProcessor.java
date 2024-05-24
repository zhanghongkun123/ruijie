package com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.slave.prepare;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.SlavePrepareImageSyncDTO;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: 配置镜像模版
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/20
 *
 * @author zhiweiHong
 */
@Service
public class PrepareFinishProcessor implements StateTaskHandle.StateProcessor {

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private PrepareStageContextResolver resolver;

    @Autowired
    private CbbImageTemplateMgmtAPI imageTemplateMgmtAPI;


    @Override
    public StateTaskHandle.ProcessResult doProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context must not be null");

        SlavePrepareImageSyncDTO dto = resolver.resolveDTO(context);
        CbbImageTemplateDetailDTO imageTemplateDetail = imageTemplateMgmtAPI.getImageTemplateDetail(dto.getImageId());
        auditLogAPI.recordLog(BusinessKey.RCDC_RCO_SLAVE_PREPARE_SUCCESS, imageTemplateDetail.getImageName());
        return StateTaskHandle.ProcessResult.next();
    }
}
