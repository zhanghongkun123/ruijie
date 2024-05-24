package com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbExternalStorageMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbLocalExternalStorageDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ExternalStorageHealthStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditFileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileGlobalConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.FtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.AuditFileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.service.AuditFileService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.common.service.SecurityService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * Description: 文件导出审批功能管理实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public class AuditFileMgmtAPIImpl implements AuditFileMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditFileMgmtAPIImpl.class);

    @Autowired
    private AuditFileService auditFileService;

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private CbbExternalStorageMgmtAPI cbbExternalStorageMgmtAPI;

    @Override
    public AuditFileStrategyDTO obtainAuditFileStrategy(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        return auditFileService.obtainAuditFileStrategy(deskId);
    }

    @Override
    public FtpConfigDTO obtainAuditFileEncryptFtpInfo() {
        return securityService.obtainAuditFileEncryptFtpInfo();

    }

    @Override
    public FtpConfigDTO obtainAuditFileFtpInfo() {
        return securityService.obtainAuditFileFtpInfo();
    }

    @Override
    public void editAuditFileGlobalStrategy(AuditFileGlobalConfigDTO auditFileGlobalStrategyDTO) throws BusinessException {
        Assert.notNull(auditFileGlobalStrategyDTO, "auditFileGlobalStrategyDTO must not be null");
        // 开启外置存储才需要检查外置存储状态是否可用
        if (BooleanUtils.toBoolean(auditFileGlobalStrategyDTO.getEnableExtStorage())) {
            if (Objects.isNull(auditFileGlobalStrategyDTO.getExternalStorageId())) {
                throw new BusinessException(AuditFileBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_CANNOT_BE_EMPTY_ERROR);
            }
            CbbLocalExternalStorageDTO extStorageDetail =
                    cbbExternalStorageMgmtAPI.getExternalStorageDetail(auditFileGlobalStrategyDTO.getExternalStorageId());
            // 检查外置存储状态是否可用
            ExternalStorageHealthStateEnum extStorageState = extStorageDetail.getExtStorageState();
            if (extStorageState != ExternalStorageHealthStateEnum.HEALTHY && extStorageState != ExternalStorageHealthStateEnum.WARNING) {
                throw new BusinessException(AuditFileBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_STATE_NOT_AVAILABLE, extStorageDetail.getName());
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("(编辑文件导出审批全局策略) auditFileGlobalStrategyDTO：{}", JSON.toJSONString(auditFileGlobalStrategyDTO));
        }
        auditFileService.editAuditFileGlobalStrategy(auditFileGlobalStrategyDTO);

    }

    @Override
    public AuditFileDTO findAuditFileById(UUID fileId) throws BusinessException {
        Assert.notNull(fileId, "fileId must not be null");
        return auditFileService.findAuditFileById(fileId);
    }

    @Override
    public AuditFileGlobalConfigDTO obtainAuditFileGlobalConfig() {
        return auditFileService.obtainAuditFileGlobalConfig();
    }

}
