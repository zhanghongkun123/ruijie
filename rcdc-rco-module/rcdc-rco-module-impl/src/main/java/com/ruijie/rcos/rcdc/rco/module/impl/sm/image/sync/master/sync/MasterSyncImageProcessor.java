package com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.master.sync;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.CbbCrossClusterSyncImageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.common.sm.AbstractTaskExecuteProcessor;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class MasterSyncImageProcessor extends AbstractTaskExecuteProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterSyncImageProcessor.class);


    @Autowired
    private MasterSyncStageContextResolver resolver;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Override
    protected StateTaskHandle.ProcessResult innerDoProcess(StateTaskHandle.StateProcessContext context, UUID taskId) throws BusinessException {

        CbbCrossClusterSyncImageRequest request = resolver.resolveDTO(context);
        request.setTaskId(taskId);
        LOGGER.info("调用跨集群同步镜像API处理，[{}]", JSON.toJSONString(request));
        cbbImageTemplateMgmtAPI.crossClusterImageSync(request);
        return StateTaskHandle.ProcessResult.next();
    }

    @Override
    protected StateTaskHandle.ProcessResult postProcess(StateTaskHandle.StateProcessContext context) throws Exception {

        CbbCrossClusterSyncImageRequest dto = resolver.resolveDTO(context);
        LOGGER.info("跨集群同步镜像[{}]处理成功", dto.getImageTemplateId());

        UUID imageId = dto.getImageTemplateId();

        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        auditLogAPI.recordLog(BusinessKey.RCDC_RCO_MASTER_SYNC_SUCCESS, imageTemplateDetail.getImageName());
        return StateTaskHandle.ProcessResult.next();
    }

    @Override
    protected String getTaskKey() {
        return MasterSyncImageProcessor.class.getName();
    }


}
