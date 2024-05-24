package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbExternalStorageNotifySPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditApplyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileGlobalConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.cache.AuditFileUploadedCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.AuditFileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.service.AuditFileService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Description: 文件服务器通知SPI实现
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/7
 *
 * @author TD
 */
public class ExternalStorageNotifySPIImpl implements CbbExternalStorageNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalStorageNotifySPIImpl.class);

    @Autowired
    private AuditFileService auditFileService;
    
    @Autowired
    private AuditFileUploadedCacheManager auditFileUploadedCacheManager;
    
    @Autowired
    private AuditApplyMgmtAPI auditApplyMgmtAPI;
    
    @Override
    public void notifyExtStorageUnhealthy(UUID extStorageId, String extStorageName) {
        Assert.notNull(extStorageId, "extStorageId can be not null");
        Assert.hasText(extStorageName, "extStorageName can be not null");
        AuditFileGlobalConfigDTO auditFileGlobalConfigDTO = auditFileService.obtainAuditFileGlobalConfig();
        UUID useExtStorageId = auditFileGlobalConfigDTO.getExternalStorageId();
        if (!BooleanUtils.toBoolean(auditFileGlobalConfigDTO.getEnableExtStorage()) || Objects.isNull(useExtStorageId) 
                || !Objects.equals(extStorageId, useExtStorageId)) {
            return;
        }
        // 取消正在执行或在队列里面的文件copy任务
        Set<UUID> applySet = auditFileUploadedCacheManager.cancelAllFileUploaded();
        LOGGER.info("待取消的申请单ID集合:{}", JSON.toJSONString(applySet));
        //废弃申请单
        for (UUID applyId: applySet) {
            try {
                auditApplyMgmtAPI.discardAuditApply(applyId,
                        LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_STATE_NOT_AVAILABLE, extStorageName));
            } catch (BusinessException e) {
                LOGGER.error("废弃申请单[{}]出现异常", applyId);
            }
        }
    }
}
