package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.conf.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBAdvancedSettingMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBDeviceMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBTypeMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateAutoRedirectConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateSpecialDeviceConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateSpecialDeviceDriverConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGetAllUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUpdateCameraConfigStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUpdateUSBDeviceDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertifiedSecurityConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyTciNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.PrinterManageServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CertifiedSecurityDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.printer.dto.PrinterConfigMigrateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.printer.dto.PrinterConfigMigrateItemDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.MtoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.conf.ConfigMigrationServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.conf.dto.MigrateUSBDeviceDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.conf.dto.OfflineLoginConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.conf.dto.SpecialDeviceDriverDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.conf.dto.UsbOtherConfAutoRedirectStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.conf.dto.UsbOtherConfDeviceDriverStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.RestErrorCodeMapping;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbOfflineLoginSettingDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/6
 *
 * @author zhangsiming
 */
public class ConfigMigrationServerImpl implements ConfigMigrationServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigMigrationServerImpl.class);

    private static final String SPECIAL_DEVICE_DRIVER_CONFIG_EXIST = "23250117";

    private static final String SPECIAL_DEVICE_CONFIG_EXIST = "23250119";

    private static final String AUTO_REDIRECT_CONFIG_EXIST = "23250122";

    private static final String USB_DEVICE_EXIST = "23250110";

    private static final String USB_DEVICE_MODIFY_FORBIDDEN = "23250112";

    private static final String STATUS_ON = "ON";

    private static final String STATUS_OFF = "OFF";

    @Autowired
    private CbbTerminalOperatorAPI terminalOperatorAPI;

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private CertifiedSecurityConfigAPI certifiedSecurityConfigAPI;

    @Autowired
    private PrinterManageServiceAPI printerManageServiceAPI;

    @Autowired
    private CbbUSBAdvancedSettingMgmtAPI cbbUSBAdvancedSettingMgmtAPI;

    @Autowired
    private CbbUSBTypeMgmtAPI cbbUSBTypeMgmtAPI;

    @Autowired
    private CbbUSBDeviceMgmtAPI cbbUSBDeviceMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private DeskStrategyTciNotifyAPI deskStrategyTciNotifyAPI;

    @Override
    public void migrateOfflineConf(OfflineLoginConfigDTO offlineLoginConfigDTO) throws BusinessException {
        Assert.notNull(offlineLoginConfigDTO, "offlineLoginConfigDTO can not be null");
        CbbOfflineLoginSettingDTO cbbOfflineLoginSettingDTO = new CbbOfflineLoginSettingDTO(offlineLoginConfigDTO.getOfflineAutoLocked().getDays());
        try {
            terminalOperatorAPI.idvOfflineLoginSetting(cbbOfflineLoginSettingDTO);
            CertifiedSecurityDTO certifiedSecurityDTO = new CertifiedSecurityDTO();
            certifiedSecurityDTO.setEnableOfflineLogin(offlineLoginConfigDTO.getEnable());
            String rememberValue = rcoGlobalParameterAPI.findParameter(new FindParameterRequest(CertifiedSecurityConfigAPI.REMEMBER_KEY)).getValue();
            String changeValue = rcoGlobalParameterAPI.findParameter(new FindParameterRequest(CertifiedSecurityConfigAPI.CHANGE_KEY)).getValue();
            certifiedSecurityDTO.setChangePassword(changeValue != null ? Boolean.valueOf(changeValue) : Boolean.FALSE);
            certifiedSecurityDTO.setRememberPassWord(rememberValue != null ? Boolean.valueOf(rememberValue) : Boolean.FALSE);
            //从数据库中 设置默认值
            certifiedSecurityDTO
                    .setNeedNotifyLoginTerminalChange(certifiedSecurityConfigAPI.queryNotifyLoginTerminalChangeConfigByUnifiedLoginConfig());
            certifiedSecurityConfigAPI.updateCertifiedSecurityConfig(certifiedSecurityDTO);
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_OFFLINE_SUCCESS_LOG,
                    offlineLoginConfigDTO.getEnable() ? STATUS_ON : STATUS_OFF, offlineLoginConfigDTO.getOfflineAutoLocked().toString());
        } catch (Exception e) {
            LOGGER.error("迁移离线时常配置异常", e);
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_OFFLINE_FAIL_LOG, e, e.getMessage());
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }
    }

    @Override
    public void migratePrinterConf(PrinterConfigMigrateDTO printerConfigMigrateDTO) throws BusinessException {
        Assert.notNull(printerConfigMigrateDTO, "printerConfigMigrateDTO can not be null");
        try {
            if (printerConfigMigrateDTO.getEnable()) {
                printerManageServiceAPI.enablePrinterConfig();
            } else {
                printerManageServiceAPI.disablePrinterConfig();
            }
            deskStrategyTciNotifyAPI.notifyFetchStartParams();
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_PRINTER_TOGGLE_SUCCESS_LOG,
                    printerConfigMigrateDTO.getEnable() ? STATUS_ON : STATUS_OFF);
            if (printerConfigMigrateDTO.getEnable()) {
                if (printerConfigMigrateDTO.getPrinterList() != null) {
                    for (PrinterConfigMigrateItemDTO printerItem : printerConfigMigrateDTO.getPrinterList()) {
                        printerManageServiceAPI.migratePrinterConfig(printerItem);
                        auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_PRINTER_SUCCESS_LOG, printerItem.getConfigName());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("迁移打印机配置异常", e);
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_PRINTER_FAIL_LOG, e, e.getMessage());
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }
    }

    @Override
    public void migrateSpecialDeviceDriver(SpecialDeviceDriverDTO specialDeviceDriverDTO) throws BusinessException {
        Assert.notNull(specialDeviceDriverDTO, "specialDeviceDriverDTO can not be null");
        CbbCreateSpecialDeviceDriverConfigDTO apiRequest = new CbbCreateSpecialDeviceDriverConfigDTO();
        String firmFlag = specialDeviceDriverDTO.getFirmFlag();
        String productFlag = specialDeviceDriverDTO.getProductFlag();
        apiRequest.setFirmFlag(firmFlag);
        apiRequest.setProductFlag(productFlag);
        apiRequest.setDeviceDriverType(specialDeviceDriverDTO.getDeviceDriverType());
        try {
            cbbUSBAdvancedSettingMgmtAPI.createSpecialDeviceDriverConfig(apiRequest);
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_SPECIAL_DEVICE_DRIVER_SUCCESS_LOG, apiRequest.getFirmFlag(),
                    apiRequest.getProductFlag());
        } catch (Exception e) {
            String reason = e.getMessage();
            if (e instanceof BusinessException) {
                BusinessException businessException = (BusinessException) e;
                reason = ((BusinessException) e).getI18nMessage();
                if (SPECIAL_DEVICE_DRIVER_CONFIG_EXIST.equals(businessException.getKey())) {
                    LOGGER.info("特殊设备驱动已存在");
                    cbbUSBAdvancedSettingMgmtAPI.updateSpecialDeviceDriverConfig(apiRequest);
                    auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_SPECIAL_DEVICE_DRIVER_UPDATE_SUCCESS_LOG, e, apiRequest.getFirmFlag(),
                            apiRequest.getProductFlag());
                    return;
                }
            }
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_SPECIAL_DEVICE_DRIVER_FAIL_LOG, e, apiRequest.getFirmFlag(),
                    apiRequest.getProductFlag(), reason);

            LOGGER.error("迁移特定设备驱动配置异常", e);
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }
    }

    @Override
    public void migrateSpecialDevice(CbbCreateSpecialDeviceConfigDTO createSpecialDeviceConfigDTO) throws BusinessException {
        Assert.notNull(createSpecialDeviceConfigDTO, "CreateSpecialDeviceConfigDTO can not be null");
        try {
            cbbUSBAdvancedSettingMgmtAPI.createSpecialDeviceConfig(createSpecialDeviceConfigDTO);
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_SPECIAL_DEVICE_SUCCESS_LOG, createSpecialDeviceConfigDTO.getFirmFlag(),
                    createSpecialDeviceConfigDTO.getProductFlag());
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                BusinessException businessException = (BusinessException) e;
                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_SPECIAL_DEVICE_FAIL_LOG, e, createSpecialDeviceConfigDTO.getFirmFlag(),
                        createSpecialDeviceConfigDTO.getProductFlag(), businessException.getI18nMessage());
                if (SPECIAL_DEVICE_CONFIG_EXIST.equals(businessException.getKey())) {
                    LOGGER.info("特殊设备驱动已存在");
                    return;
                }
            }
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_SPECIAL_DEVICE_FAIL_LOG, e, createSpecialDeviceConfigDTO.getFirmFlag(),
                    createSpecialDeviceConfigDTO.getProductFlag(), e.getMessage());
            LOGGER.error("迁移特定设备配置异常", e);
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }
    }

    @Override
    public void migrateAutoRedirect(CbbCreateAutoRedirectConfigDTO createAutoRedirectConfigDTO) throws BusinessException {
        Assert.notNull(createAutoRedirectConfigDTO, "createAutoRedirectConfigDTO can not be null");
        try {
            cbbUSBAdvancedSettingMgmtAPI.createAutoRedirectConfig(createAutoRedirectConfigDTO);
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_AUTO_REDIRECT_SUCCESS_LOG, createAutoRedirectConfigDTO.getFirmFlag(),
                    createAutoRedirectConfigDTO.getProductFlag());
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                BusinessException businessException = (BusinessException) e;
                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_AUTO_REDIRECT_FAIL_LOG, e, createAutoRedirectConfigDTO.getFirmFlag(),
                        createAutoRedirectConfigDTO.getProductFlag(), businessException.getI18nMessage());
                if (AUTO_REDIRECT_CONFIG_EXIST.equals(businessException.getKey())) {
                    LOGGER.info("自动重定向设置已存在");
                    return;
                }
            }
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_AUTO_REDIRECT_FAIL_LOG, e, createAutoRedirectConfigDTO.getFirmFlag(),
                    createAutoRedirectConfigDTO.getProductFlag(), e.getMessage());
            LOGGER.error("迁移自动重定向设置异常", e);
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }
    }

    @Override
    public void migrateUsbStrategyCamera(CbbUpdateCameraConfigStrategyDTO updateCameraConfigStrategyDTO) throws BusinessException {
        Assert.notNull(updateCameraConfigStrategyDTO, "updateCameraConfigStrategyDTO can not be null");
        try {
            cbbUSBAdvancedSettingMgmtAPI.updateCameraConfigStrategy(updateCameraConfigStrategyDTO);
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_STRATEGY_CAMERA_SUCCESS_LOG);

        } catch (Exception e) {
            LOGGER.error("迁移摄像头策略异常", e);
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_STRATEGY_CAMERA_FAIL_LOG, e, e.getMessage());
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }
    }

    @Override
    public void migrateUsbStrategyDriver(UsbOtherConfDeviceDriverStrategyDTO usbOtherConfDeviceDriverStrategyDTO) throws BusinessException {
        Assert.notNull(usbOtherConfDeviceDriverStrategyDTO, "usbOtherConfDeviceDriverStrategyDTO can not be null");

        try {
            cbbUSBAdvancedSettingMgmtAPI.updateSpecialDeviceDriverConfigStrategy(usbOtherConfDeviceDriverStrategyDTO.getUsbDeviceDriverType());
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_STRATEGY_SPECIAL_DEVICE_DRIVER_SUCCESS_LOG);
        } catch (Exception e) {
            LOGGER.error("迁移特定设备驱动策略异常", e);
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_STRATEGY_SPECIAL_DEVICE_DRIVER_FAIL_LOG, e, e.getMessage());
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }
    }

    @Override
    public void migrateUsbStrategyAutoRedirect(UsbOtherConfAutoRedirectStrategyDTO usbOtherConfAutoRedirectDTO) throws BusinessException {
        Assert.notNull(usbOtherConfAutoRedirectDTO, "usbOtherConfAutoRedirectDTO can not be null");
        try {
            cbbUSBAdvancedSettingMgmtAPI.updateAutoRedirectConfigStrategy(usbOtherConfAutoRedirectDTO.getAutoRedirectType());
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_STRATEGY_AUTO_REDIRECT_SUCCESS_LOG);
        } catch (Exception e) {
            LOGGER.error("迁移自动重定向策略异常", e);
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_STRATEGY_AUTO_REDIRECT_FAIL_LOG, e, e.getMessage());
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }
    }

    @Override
    public void migrateUsbDevice(MigrateUSBDeviceDTO migrateUSBDeviceDTO) throws BusinessException {
        Assert.notNull(migrateUSBDeviceDTO, "migrateUSBDeviceDTO can not be null");

        // 需要匹配usb_type_id
        CbbUSBTypeDTO[] usbTypeDTOArr = cbbUSBTypeMgmtAPI.getAllUSBType(new CbbGetAllUSBTypeDTO());
        UUID usbTypeId = null;
        String usbTypeName = null;
        try {
            for (CbbUSBTypeDTO usbTypeDTO : usbTypeDTOArr) {
                if (usbTypeDTO.getUsbTypeName().equals(migrateUSBDeviceDTO.getLabel())) {
                    LOGGER.debug("找到正确USB类别id[{}]", usbTypeDTO.getId());
                    usbTypeName = usbTypeDTO.getUsbTypeName();
                    usbTypeId = usbTypeDTO.getId();
                    migrateUSBDeviceDTO.setUsbTypeId(usbTypeId);
                    cbbUSBDeviceMgmtAPI.createUSBDevice(migrateUSBDeviceDTO);
                    auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_USB_DEVICE_SUCCESS_LOG, usbTypeName,
                            migrateUSBDeviceDTO.getFirmFlag(), migrateUSBDeviceDTO.getProductFlag());
                    break;
                }
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            if (e instanceof BusinessException) {
                BusinessException businessException = (BusinessException) e;
                if (USB_DEVICE_EXIST.equals(businessException.getKey())) {
                    // 调用更新接口
                    migrateUpdateUsbDevice(migrateUSBDeviceDTO, usbTypeId, usbTypeName, businessException);
                    return;
                }
                msg = businessException.getI18nMessage();
            }
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_USB_DEVICE_FAIL_LOG, e, usbTypeName, migrateUSBDeviceDTO.getFirmFlag(),
                    migrateUSBDeviceDTO.getProductFlag(), msg);
            LOGGER.error("迁移USB设备异常", e);
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }
    }

    private void migrateUpdateUsbDevice(MigrateUSBDeviceDTO migrateUSBDeviceDTO, UUID usbTypeId, String usbTypeName,
            BusinessException businessException) throws BusinessException {
        String[] idArr = businessException.getArgArr();
        if (idArr != null && idArr.length > 0) {
            CbbUpdateUSBDeviceDTO cbbUpdateUSBDeviceDTO = new CbbUpdateUSBDeviceDTO();
            cbbUpdateUSBDeviceDTO.setUsbTypeId(usbTypeId);
            cbbUpdateUSBDeviceDTO.setId(UUID.fromString(idArr[0]));
            cbbUpdateUSBDeviceDTO.setNote(migrateUSBDeviceDTO.getNote());
            cbbUpdateUSBDeviceDTO.setMigrating(Boolean.TRUE);
            cbbUpdateUSBDeviceDTO.setTerminalName(migrateUSBDeviceDTO.getTerminalName());
            cbbUpdateUSBDeviceDTO.setTerminalMac(migrateUSBDeviceDTO.getTerminalMac());
            cbbUpdateUSBDeviceDTO.setFirm(migrateUSBDeviceDTO.getFirm());
            cbbUpdateUSBDeviceDTO.setProduct(migrateUSBDeviceDTO.getProduct());
            try {
                cbbUSBDeviceMgmtAPI.updateUSBDevice(cbbUpdateUSBDeviceDTO);
                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_USB_DEVICE_UPDATE_SUCCESS_LOG, usbTypeName,
                        migrateUSBDeviceDTO.getFirmFlag(), migrateUSBDeviceDTO.getProductFlag());
                LOGGER.debug("更新USBDevice[{}][{}][{}]成功", idArr[0], usbTypeId, migrateUSBDeviceDTO.getNote());
            } catch (Exception exception) {
                LOGGER.error("迁移USB设备异常", exception);
                String exceptionMsg = exception.getMessage();
                String key = null;
                if (exception instanceof BusinessException) {
                    exceptionMsg = ((BusinessException) exception).getI18nMessage();
                    key = ((BusinessException) exception).getKey();
                }
                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_CONF_USB_DEVICE_FAIL_LOG, exception, usbTypeName,
                        migrateUSBDeviceDTO.getFirmFlag(), migrateUSBDeviceDTO.getProductFlag(), exceptionMsg);
                if (USB_DEVICE_MODIFY_FORBIDDEN.equals(key)) {
                    return;
                }
                throw RestErrorCodeMapping.convert2BusinessException(exception);
            }
        }
    }
}
