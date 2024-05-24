package com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.common.helper;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbExternalStorageMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ExternalStorageHealthStateEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.auditfile.AuditFileBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Description: 审批单WEB协助类
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/20
 *
 * @author TD
 */
@Service
public class AuditApplyWebHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditApplyWebHelper.class);

    @Autowired
    private CbbExternalStorageMgmtAPI externalStorageMgmtAPI;

    /**
     * 检查文件服务器健康与文件是否存在
     * @param externalStorageId 文件服务器ID
     * @param file 文件
     * @throws BusinessException 业务异常
     */
    public void checkExternalStorageAndFileExists(UUID externalStorageId, File file) throws BusinessException {
        Assert.notNull(externalStorageId, "externalStorageId can be not null");
        Assert.notNull(file, "externalStorageId can be not null");
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            try {
                ExternalStorageHealthStateEnum extStorageState =
                        externalStorageMgmtAPI.getExternalStorageDetail(externalStorageId).getExtStorageState();
                return (extStorageState == ExternalStorageHealthStateEnum.HEALTHY || extStorageState == ExternalStorageHealthStateEnum.WARNING)
                        && file.exists();
            } catch (Exception e) {
                LOGGER.error("检查文件服务器[{}]出现异常", externalStorageId, e);
                return false;
            }
        });
        try {
            if (Boolean.FALSE.equals(future.get(2, TimeUnit.SECONDS))) {
                throw new BusinessException(AuditFileBusinessKey.RCDC_AUDIT_FILE_DOWNLOAD_PATH_NOT_EXIST);
            }
        } catch (Exception e) {
            LOGGER.error("检查文件[{}]路径出现异常", file.toString(), e);
            throw new BusinessException(AuditFileBusinessKey.RCDC_AUDIT_FILE_DOWNLOAD_PATH_NOT_EXIST, e);
        }
    }
    
}
