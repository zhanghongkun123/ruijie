package com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.master.prepare;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.SlaveRollbackImageSyncDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.slave.rollback.RollbackStageContextResolver;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.base.sm2.StateTransitionRegistry;
import com.ruijie.rcos.sk.base.sm2.exception.AbstractGlobalExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static com.ruijie.rcos.rcdc.rco.module.impl.PublicTaskBusinessKey.RCDC_RCO_MASTER_IMAGE_SYNC_PREPARE_HANDLER;

/**
 * Description: 跨集群同步镜像 - 从集群准备阶段状态机
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/20
 *
 * @author zhiweiHong
 */
@Service
public class MasterClusterPrepareStageStateHandler implements StateTaskHandle {

    @Autowired
    private RollbackStageContextResolver resolver;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI imageTemplateMgmtAPI;

    @Override
    public String getName() {
        return LocaleI18nResolver.resolve(RCDC_RCO_MASTER_IMAGE_SYNC_PREPARE_HANDLER);
    }

    @Override
    public void registerStateTransition(StateTransitionRegistry registry) {
        Assert.notNull(registry, "registry cannot be null");

        registry.add(MasterLockImageProcessor.class);
        registry.add(MasterSaveUnifiedImageMangeDataIfNullableProcessor.class);
        registry.add(MasterSaveUnifiedImageSnapshotMangeDataIfNullableProcessor.class);
        registry.add(MasterPrepareImageSyncFinishProcessor.class);
    }


    @Override
    public ExceptionTranslator getDefaultDoExceptionTranslator() {
        return new AbstractGlobalExceptionTranslator() {

            @Override
            protected ProcessResult dealwithException(StateProcessContext context, Exception ex, String errorMsg) throws Exception {
                final SlaveRollbackImageSyncDTO dto = resolver.resolveDTO(context);
                CbbImageTemplateDetailDTO imageTemplateDetail = imageTemplateMgmtAPI.getImageTemplateDetail(dto.getImageId());
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_MASTER_PREPARE_ERROR, imageTemplateDetail.getImageName(),
                        errorMsg);
                throw ex;
            }
        };
    }
}