package com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.master.sync;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.CbbCrossClusterSyncImageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.base.sm2.StateTransitionRegistry;
import com.ruijie.rcos.sk.base.sm2.exception.AbstractGlobalExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static com.ruijie.rcos.rcdc.rco.module.impl.PublicTaskBusinessKey.RCDC_RCO_MASTER_IMAGE_SYNC_HANDLER;

/**
 * Description: 跨集群同步镜像 - 主端镜像完成同步状态机
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/20
 *
 * @author zhiweiHong
 */
@Service
public class MasterSyncStageStateHandler implements StateTaskHandle {

    @Autowired
    private MasterSyncStageContextResolver resolver;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI imageTemplateMgmtAPI;

    @Override
    public String getName() {
        return LocaleI18nResolver.resolve(RCDC_RCO_MASTER_IMAGE_SYNC_HANDLER);
    }

    @Override
    public void registerStateTransition(StateTransitionRegistry registry) {
        Assert.notNull(registry, "registry cannot be null");

        registry.add(MasterSyncImageProcessor.class);
    }

    @Override
    public ExceptionTranslator getDefaultDoExceptionTranslator() {
        return new AbstractGlobalExceptionTranslator() {

            @Override
            protected ProcessResult dealwithException(StateProcessContext context, Exception ex, String errorMsg) throws Exception {
                CbbCrossClusterSyncImageRequest request = resolver.resolveDTO(context);
                CbbImageTemplateDetailDTO imageTemplateDetail = imageTemplateMgmtAPI.getImageTemplateDetail(request.getImageTemplateId());
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_MASTER_SYNC_ERROR, imageTemplateDetail.getImageName(),
                        errorMsg);
                throw ex;
            }
        };
    }
}
