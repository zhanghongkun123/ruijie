package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.prepare.impl;

import com.ruijie.rcos.base.sysmanage.module.def.api.MaintenanceModeMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.SambaServiceAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.enums.SystemMaintenanceState;
import com.ruijie.rcos.base.sysmanage.module.def.dto.SambaConfigDTO;
import com.ruijie.rcos.base.sysmanage.module.def.enums.SambaMountState;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbBackupAPI;
import com.ruijie.rcos.rcdc.backup.module.def.enums.BackupType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.license.module.def.api.CbbLicenseCenterAPI;
import com.ruijie.rcos.rcdc.license.module.def.dto.CbbAuthInfoDTO;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseCategoryEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.ConfigurationWizardAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageTemplateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AllowCreateImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetConfigurationWizardResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.prepare.MigrationPrepareCheckServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.prepare.dto.IDVLicenseRemainCheckResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.RestErrorCodeMapping;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLicenseMgmtAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/7
 *
 * @author zhangsiming
 */
@Service
public class MigrationPrepareCheckServerImpl implements MigrationPrepareCheckServer {

    public static final String QCOW2_PATH_SMB_SHARE_NAME = "qcow2";

    @Autowired
    private SambaServiceAPI sambaServiceAPI;

    @Autowired
    private MaintenanceModeMgmtAPI maintenanceModeMgmtAPI;

    @Autowired
    private CbbBackupAPI cbbBackupAPI;

    @Autowired
    private ImageTemplateAPI imageTemplateAPI;

    @Autowired
    private CbbTerminalLicenseMgmtAPI cbbTerminalLicenseMgmtAPI;

    @Autowired
    private ConfigurationWizardAPI configurationWizardAPI;

    @Autowired
    private CbbLicenseCenterAPI cbbLicenseCenterAPI;

    @Autowired
    private CloudPlatformManageAPI  cloudPlatformManageAPI;


    @Override
    public void checkSambaMount() throws BusinessException {
        // 1）判断 Docker Samba 盘挂载是否 OK。
        try {
            SambaConfigDTO sambaConfigDTO = sambaServiceAPI.getSambaConfig(QCOW2_PATH_SMB_SHARE_NAME);
            if (sambaConfigDTO.getState() == SambaMountState.UNMOUNT) {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_CHECK_SAMBA_UMOUNT);
            }
        } catch (Exception e) {
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }
    }

    @Override
    public void checkMaintenanceMode() throws BusinessException {
        // 2）判断是否进入维护模式，升级则不支持迁移导入。
        try {
            SystemMaintenanceState maintenanceMode = maintenanceModeMgmtAPI.getMaintenanceMode();
            if (maintenanceMode != SystemMaintenanceState.NORMAL) {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_CHECK_IN_MAINTENANCE_MODE);
            }
        } catch (Exception e) {
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }
    }

    @Override
    public void checkBackup() throws BusinessException {
        // 3）判断是否正在操作服务器数据备份，备份则不支持迁移导入。
        try {
            for (BackupType backupType: BackupType.values()) {
                if (cbbBackupAPI.isBackupRunning(backupType)) {
                    throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_CHECK_IN_BACKUP_RUNNING);
                }
            }

        } catch (Exception e) {
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }
    }

    @Override
    public void checkImageCreate() throws BusinessException {
        CloudPlatformDTO defaultCloudPlatform = cloudPlatformManageAPI.getDefaultCloudPlatform();
        // 4）是否在创建镜像中，仅支持同时创建同一个镜像，创建则不支持。
        try {
            AllowCreateImageTemplateDTO allowCreateImageTemplateDTO = imageTemplateAPI.checkIsAllowCreate(null, defaultCloudPlatform.getId());
            if (!allowCreateImageTemplateDTO.getEnableCreate()) {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_CHECK_IN_CREATING_IMAGE);
            }
        } catch (Exception e) {
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }
    }

    @Override
    public void checkConfigWizard() throws BusinessException {
        // 5）新平台配置向导还未完成
        try {
            GetConfigurationWizardResponse response = configurationWizardAPI.getConfigurationWizardResponse();
            if (response.getShow()) {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_CHECK_IN_CONFIG_WIZARD_SHOWING);
            }
        } catch (Exception e) {
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }
    }

    @Override
    public IDVLicenseRemainCheckResponse checkIDVRemainLicense() throws BusinessException {

        try {
            List<CbbAuthInfoDTO> authInfoDTOList = new ArrayList<>();
            cbbLicenseCenterAPI.appendAuthInfo(authInfoDTOList, CbbLicenseCategoryEnum.IDV.name());
            int totalNum = 0;
            int usedNum = 0;
            for (CbbAuthInfoDTO authInfoDTO : authInfoDTOList) {
                if (authInfoDTO.getTotal() == -1) {
                    totalNum = -1;
                } else {
                    if (totalNum >= 0) {
                        totalNum += authInfoDTO.getTotal();
                    }
                }
                usedNum += authInfoDTO.getUsed();
            }
            int remainLicenseCount = -1;
            if (totalNum > 0) {
                remainLicenseCount = totalNum - usedNum;
            }
            return new IDVLicenseRemainCheckResponse("0", remainLicenseCount);
        } catch (Exception e) {
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }

    }
}
