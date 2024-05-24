package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.help;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.CbbDeskStrategyUsage;
import com.ruijie.rcos.rcdc.rca.module.def.util.CommonStrategyHelper;
import com.ruijie.rcos.rcdc.rco.module.def.constants.PermissionConstants;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.CdRomMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.NetDiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.CreateDeskStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.UpdateDeskStrategyRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Description: 云桌面策略工具类
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/12
 *
 * @author TD
 */
@Service
public class DeskStrategyHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskStrategyHelper.class);

    /**
     * 字节
     */
    private static final double G_TO_M = 1 << 10;

    /**
     * 音频设备UUID
     */
    private static final UUID AUDIO_DEVICE = UUID.fromString("6be5d94e-ed5e-4acc-90d5-8ee1ad75666d");

    /**
     * 开启系统盘自动扩容时，设置的系统盘大小。
     */
    private static final int DEFAULT_SYSTEM_SIZE_WHEN_ENABLE_FULL_SYSTEM_DISK = 0;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    /**
     * 创建云桌面策略转换DTO
     * @param strategyRequest 修改请求参数
     * @return Object
     * @throws BusinessException 业务异常
     */
    public Object buildCbbCreateDeskStrategyRequest(CreateDeskStrategyRequest strategyRequest) throws BusinessException {
        Assert.notNull(strategyRequest, "strategyRequest can not be null");
        CbbStrategyType strategyType = strategyRequest.getStrategyType();
        switch (strategyType) {
            case VDI:
                return buildCbbCreateDeskStrategyVDIRequest(strategyRequest);
            case IDV:
                return buildCbbDeskStrategyIDVDTO(strategyRequest);
            case VOI:
                return buildCbbDeskStrategyVOIDTO(strategyRequest);
            case THIRD:
                return buildCbbDeskStrategyThirdPartyDTO(strategyRequest);
            default:
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_STRATEGY_TYPE_NOT_EXIST, strategyType.name());
        }
    }

    private CbbDeskStrategyThirdPartyDTO buildCbbDeskStrategyThirdPartyDTO(CreateDeskStrategyRequest strategyRequest) {
        CbbDeskStrategyThirdPartyDTO thirdPartyDTO = new CbbDeskStrategyThirdPartyDTO();
        thirdPartyDTO.setStrategyName(strategyRequest.getStrategyName());
        thirdPartyDTO.setNote(strategyRequest.getNote());
        thirdPartyDTO.setSessionType(strategyRequest.getSessionType());
        thirdPartyDTO.setClipBoardMode(strategyRequest.getClipBoardMode());
        thirdPartyDTO.setClipBoardSupportTypeArr(strategyRequest.getClipBoardSupportTypeArr());
        thirdPartyDTO.setPowerPlan(strategyRequest.getPowerPlan());
        thirdPartyDTO.setPowerPlanTime(strategyRequest.getPowerPlanTime());
        thirdPartyDTO.setEstIdleOverTime(strategyRequest.getEstIdleOverTime());
        thirdPartyDTO.setOpenDoubleScreen(strategyRequest.getEnableDoubleScreen());
        thirdPartyDTO.setForbidCatchScreen(strategyRequest.getEnableForbidCatchScreen());
        thirdPartyDTO.setEnableWebClient(strategyRequest.getEnableWebClient());
        thirdPartyDTO.setEnableUsbReadOnly(strategyRequest.getEnableUsbReadOnly());
        UUID[] usbTypeArr = buildVDIUsbTypeArr(strategyRequest.getUsbTypeIdArr());
        thirdPartyDTO.setUsbTypeIdArr(usbTypeArr);
        // 安全网关，是否使用协议代理
        thirdPartyDTO.setEnableAgreementAgency(strategyRequest.getEnableAgreementAgency());
        thirdPartyDTO.setEnableForceUseAgreementAgency(strategyRequest.getEnableForceUseAgreementAgency());
        // 由于空字符串与null 存在排序问题 统一空字符串 设置为null
        if (StringUtils.isBlank(strategyRequest.getRemark())) {
            thirdPartyDTO.setRemark(null);
        } else {
            // 设置云桌面标签备注
            thirdPartyDTO.setRemark(strategyRequest.getRemark());
        }
        // 创建者默认为：管理员Admin
        thirdPartyDTO.setCreateUserName(PermissionConstants.ADMIN_NAME);
        // 磁盘映射
        Boolean[] vdiDiskMappingArr = getVdiDiskMapArr(strategyRequest.getDiskMappingType());
        thirdPartyDTO.setEnableDiskMapping(vdiDiskMappingArr[0]);
        thirdPartyDTO.setEnableDiskMappingWriteable(vdiDiskMappingArr[1]);
        thirdPartyDTO.setEnableAgreementAgency(strategyRequest.getEnableAgreementAgency());
        // 局域网自动检测
        thirdPartyDTO.setEnableLanAutoDetection(strategyRequest.getEnableLanAutoDetection());
        // 电源计划，默认为睡眠，并且是关闭状态
        thirdPartyDTO.setPowerPlan(Optional.ofNullable(strategyRequest.getPowerPlan()).orElse(CbbPowerPlanEnum.SLEEP));
        thirdPartyDTO.setPowerPlanTime(Optional.ofNullable(strategyRequest.getPowerPlanTime()).orElse(0));
        // 网盘映射
        if (strategyRequest.getNetDiskMappingType() != null) {
            Boolean[] thirdPartyNetDiskMappingArr = getVdiNetDiskMapArr(strategyRequest.getNetDiskMappingType());
            thirdPartyDTO.setEnableNetDiskMapping(thirdPartyNetDiskMappingArr[0]);
            thirdPartyDTO.setEnableNetDiskMappingWriteable(thirdPartyNetDiskMappingArr[1]);
        }
        // CDROM映射
        if (strategyRequest.getCdRomMappingType() != null) {
            Boolean[] vdiCDRomMappingArr = getVdiCDROMDiskMapArr(strategyRequest.getCdRomMappingType());
            thirdPartyDTO.setEnableCDRomMapping(vdiCDRomMappingArr[0]);
            thirdPartyDTO.setEnableCDRomMappingWriteable(vdiCDRomMappingArr[1]);
        }
        // 允许桌面登录时间
        if (strategyRequest.getEnableOpenLoginLimit() != null && strategyRequest.getEnableOpenLoginLimit() &&
                strategyRequest.getDesktopAllowLoginTimeArr() != null) {
            CbbDeskTopAllowLoginTimeDTO[] desktopAllowLoginTimeArr = Arrays.stream(strategyRequest.getDesktopAllowLoginTimeArr()).map(s ->
                    new CbbDeskTopAllowLoginTimeDTO(s.getStartTime(), s.getEndTime(), s.getWeekArr())).toArray(CbbDeskTopAllowLoginTimeDTO[]::new);
            thirdPartyDTO.setDesktopAllowLoginTimeArr(desktopAllowLoginTimeArr);
        } else {
            thirdPartyDTO.setDesktopAllowLoginTimeArr(new CbbDeskTopAllowLoginTimeDTO[0]);
        }
        thirdPartyDTO.setIpLimitMode(Optional.ofNullable(strategyRequest.getIpLimitMode()).orElse(CbbIpLimitModeEnum.NOT_USE));
        thirdPartyDTO.setIpSegmentDTOList(strategyRequest.getIpSegmentDTOList());

        // 水印配置
        thirdPartyDTO.setEnableWatermark(strategyRequest.getEnableWatermark());
        thirdPartyDTO.setWatermarkInfo(strategyRequest.getWatermarkInfo());

        thirdPartyDTO.setNote(strategyRequest.getNote());
        // 多会话不支持文件审计 && 多会话桌面权限配置
        if (CbbDesktopSessionType.MULTIPLE == strategyRequest.getSessionType()) {
            thirdPartyDTO.setEnableAuditFile(Boolean.FALSE);
            thirdPartyDTO.setDesktopSyncLoginAccount(Boolean.TRUE);
            thirdPartyDTO.setDesktopSyncLoginAccountPermission(strategyRequest.getWindowsAccountPermission() == null ?
                    strategyRequest.getDesktopSyncLoginAccountPermission() : strategyRequest.getWindowsAccountPermission());
        } else {
            thirdPartyDTO.setEnableAuditFile(strategyRequest.getEnableAuditFile());
            thirdPartyDTO.setAuditFileInfo(strategyRequest.getAuditFileInfo());
            thirdPartyDTO.setUsbStorageDeviceMappingMode(strategyRequest.getUsbStorageDeviceMappingMode());
        }
        // usb带宽配置压缩加速
        thirdPartyDTO.setUsbStorageDeviceAcceleration(strategyRequest.getUsbStorageDeviceAcceleration());
        thirdPartyDTO.setEnableUsbCompressAcceleration(strategyRequest.getEnableUsbCompressAcceleration());
        thirdPartyDTO.setEnableUsbBandwidth(strategyRequest.getEnableUsbBandwidth());
        thirdPartyDTO.setUsbBandwidthInfo(strategyRequest.getUsbBandwidthInfo());
        // 协议配置
        thirdPartyDTO.setEstProtocolType(strategyRequest.getEstProtocolType());
        thirdPartyDTO.setAgreementInfo(CommonStrategyHelper.convertAgreementConfig(
                strategyRequest.getEstProtocolType(), strategyRequest.getAgreementInfo()));
        // 策略用途
        thirdPartyDTO.setStrategyUsage(strategyRequest.getCbbDeskStrategyUsage());
        return thirdPartyDTO;
    }

    private CbbCreateDeskStrategyVDIDTO buildCbbCreateDeskStrategyVDIRequest(CreateDeskStrategyRequest strategyRequest) {
        CbbCreateDeskStrategyVDIDTO request = new CbbCreateDeskStrategyVDIDTO();
        request.setName(strategyRequest.getStrategyName());
        request.setPattern(strategyRequest.getDesktopType());
        request.setSessionTypeEnum(strategyRequest.getSessionType());
        request.setEstIdleOverTime(strategyRequest.getEstIdleOverTime());
        // vdi设备默认音频开启
        UUID[] usbTypeArr = buildVDIUsbTypeArr(strategyRequest.getUsbTypeIdArr());
        request.setUsbTypeIdArr(usbTypeArr);
        request.setIsOpenInternet(strategyRequest.getEnableInternet());
        request.setIsOpenUsbReadOnly(strategyRequest.getEnableUsbReadOnly());
        request.setIsOpenDoubleScreen(strategyRequest.getEnableDoubleScreen());
        request.setForbidCatchScreen(strategyRequest.getEnableForbidCatchScreen() != null && strategyRequest.getEnableForbidCatchScreen());
        // 设置网页客户端接入，默认开启
        request.setEnableWebClient(strategyRequest.getEnableWebClient() == null || strategyRequest.getEnableWebClient());
        request.setClipBoardMode(strategyRequest.getClipBoardMode());
        request.setClipBoardSupportTypeArr(strategyRequest.getClipBoardSupportTypeArr());
        request.setIsOpenDesktopRedirect(strategyRequest.getEnableOpenDesktopRedirect());
        request.setDeskErrorStrategy(new CbbDeskErrorStrategyDTO());
        request.setDeskPersonalConfigStrategyType(getCbbDeskPersonalConfigStrategyType(strategyRequest.getDesktopType()));
        request.setEnableNested(strategyRequest.getEnableNested() != null);
        request.setAdOu(strategyRequest.getAdOu());
        // 由于空字符串与null 存在排序问题 统一空字符串 设置为null
        if (StringUtils.isBlank(strategyRequest.getRemark())) {
            request.setRemark(null);
        } else {
            // 设置云桌面标签备注
            request.setRemark(strategyRequest.getRemark());
        }
        // 磁盘映射
        Boolean[] vdiDiskMappingArr = getVdiDiskMapArr(strategyRequest.getDiskMappingType());
        request.setEnableDiskMapping(vdiDiskMappingArr[0]);
        request.setEnableDiskMappingWriteable(vdiDiskMappingArr[1]);
        // 局域网自动检测
        request.setEnableLanAutoDetection(Optional.ofNullable(strategyRequest.getEnableLanAutoDetection()).orElse(Boolean.FALSE));

        // 安全网关，是否使用协议代理
        request.setEnableAgreementAgency(strategyRequest.getEnableAgreementAgency());
        request.setEnableForceUseAgreementAgency(strategyRequest.getEnableForceUseAgreementAgency());

        // 请求参数中，桌面创建方式为空，则默认为链接克隆方式
        request.setDeskCreateMode(Optional.ofNullable(strategyRequest.getDeskCreateMode()).orElse(DeskCreateMode.LINK_CLONE));

        // 电源计划，默认为睡眠，并且是关闭状态
        request.setPowerPlan(Optional.ofNullable(strategyRequest.getPowerPlan()).orElse(CbbPowerPlanEnum.SLEEP));
        request.setPowerPlanTime(Optional.ofNullable(strategyRequest.getPowerPlanTime()).orElse(0));
        request.setKeyboardEmulationType(Optional.ofNullable(strategyRequest.getKeyboardEmulationType()).orElse(CbbKeyboardEmulationType.PS2));
        request.setEstIdleOverTime(Optional.ofNullable(strategyRequest.getEstIdleOverTime()).orElse(0));
        // 网盘映射
        if (strategyRequest.getNetDiskMappingType() != null) {
            Boolean[] vdiNetDiskMappingArr = getVdiNetDiskMapArr(strategyRequest.getNetDiskMappingType());
            request.setEnableNetDiskMapping(vdiNetDiskMappingArr[0]);
            request.setEnableNetDiskMappingWriteable(vdiNetDiskMappingArr[1]);
        }

        // 创建者默认为：管理员Admin
        request.setCreatorUserName(PermissionConstants.ADMIN_NAME);

        // CDROM映射
        if (strategyRequest.getCdRomMappingType() != null) {
            Boolean[] vdiCDRomMappingArr = getVdiCDROMDiskMapArr(strategyRequest.getCdRomMappingType());
            request.setEnableCDRomMapping(vdiCDRomMappingArr[0]);
            request.setEnableCDRomMappingWriteable(vdiCDRomMappingArr[1]);
        }
        // 构建允许桌面登录时间
        buildDesktopAllowLoginTime(strategyRequest, request);

        //用户自助快照开关
        request.setEnableUserSnapshot(strategyRequest.getEnableUserSnapshot() != null && strategyRequest.getEnableUserSnapshot());

        request.setDesktopOccupyDriveArr(strategyRequest.getDesktopOccupyDriveArr());
        request.setIpLimitMode(Optional.ofNullable(strategyRequest.getIpLimitMode()).orElse(CbbIpLimitModeEnum.NOT_USE));
        request.setIpSegmentDTOList(strategyRequest.getIpSegmentDTOList());
        request.setAllowLocalDisk(strategyRequest.getEnableAllowLocalDisk());

        // 水印配置
        request.setEnableWatermark(strategyRequest.getEnableWatermark());
        request.setWatermarkInfo(strategyRequest.getWatermarkInfo());

        request.setNote(strategyRequest.getNote());
        // 安全审计
        request.setEnableAuditFile(strategyRequest.getEnableAuditFile());
        request.setAuditFileInfo(strategyRequest.getAuditFileInfo());
        request.setEnableAuditPrinter(strategyRequest.getEnableAuditPrinter());
        request.setAuditPrinterInfo(strategyRequest.getAuditPrinterInfo());

        // 安全配置-桌面账号权限
        if (CbbDesktopSessionType.SINGLE == strategyRequest.getSessionType()) {
            request.setDesktopSyncLoginAccount(strategyRequest.getDesktopSyncLoginAccount());
            request.setDesktopSyncLoginAccountPermission(strategyRequest.getDesktopSyncLoginAccountPermission());
            request.setDesktopSyncLoginPassword(strategyRequest.getDesktopSyncLoginPassword());
        } else {
            request.setDesktopSyncLoginAccount(Boolean.TRUE);
            request.setDesktopSyncLoginAccountPermission(strategyRequest.getWindowsAccountPermission() == null ?
                    strategyRequest.getDesktopSyncLoginAccountPermission() : strategyRequest.getWindowsAccountPermission());
        }

        // 高可用
        request.setEnableHa(strategyRequest.getEnableHa());
        request.setHaPriority(strategyRequest.getHaPriority());
        request.setUsbStorageDeviceAcceleration(strategyRequest.getUsbStorageDeviceAcceleration());
        request.setUsbStorageDeviceMappingMode(strategyRequest.getUsbStorageDeviceMappingMode());

        //串并口重定向
        request.setEnableSerialPortRedirect(strategyRequest.getEnableSerialPortRedirect());
        request.setEnableParallelPortRedirect(strategyRequest.getEnableParallelPortRedirect());

        // Usb压缩加速
        request.setEnableUsbCompressAcceleration(strategyRequest.getEnableUsbCompressAcceleration());
        // Usb带宽控制
        request.setEnableUsbBandwidth(strategyRequest.getEnableUsbBandwidth());
        request.setUsbBandwidthInfo(strategyRequest.getUsbBandwidthInfo());
        request.setEstProtocolType(strategyRequest.getEstProtocolType());
        request.setAgreementInfo(CommonStrategyHelper.convertAgreementConfig(strategyRequest.getEstProtocolType(),
                strategyRequest.getAgreementInfo()));
        request.setEnableTransparentEncrypt(Objects.nonNull(strategyRequest.getEnableTransparentEncrypt())
                && strategyRequest.getEnableTransparentEncrypt());
        request.setTransparentEncryptInfo(strategyRequest.getTransparentEncryptInfo());
        // 策略用途
        request.setStrategyUsage(strategyRequest.getCbbDeskStrategyUsage());
        return request;
    }

    private CbbDeskStrategyIDVDTO buildCbbDeskStrategyIDVDTO(CreateDeskStrategyRequest strategyRequest) throws BusinessException {
        CbbDeskStrategyIDVDTO cbbDeskStrategyIDVDTO = new CbbDeskStrategyIDVDTO();
        cbbDeskStrategyIDVDTO.setName(strategyRequest.getStrategyName());
        cbbDeskStrategyIDVDTO.setPattern(strategyRequest.getDesktopType());
        cbbDeskStrategyIDVDTO.setOpenUsbReadOnly(strategyRequest.getEnableUsbReadOnly());
        cbbDeskStrategyIDVDTO.setUsbTypeIdArr(strategyRequest.getUsbTypeIdArr());
        cbbDeskStrategyIDVDTO.setDeskErrorStrategy(new CbbDeskErrorStrategyDTO());
        cbbDeskStrategyIDVDTO.setDeskPersonalConfigStrategyType(getCbbDeskPersonalConfigStrategyType(strategyRequest.getDesktopType()));
        cbbDeskStrategyIDVDTO.setEnableNested(BooleanUtils.isTrue(strategyRequest.getEnableNested()));
        cbbDeskStrategyIDVDTO.setKeyboardEmulationType(strategyRequest.getKeyboardEmulationType());
        cbbDeskStrategyIDVDTO.setAdOu(strategyRequest.getAdOu());
        cbbDeskStrategyIDVDTO.setDesktopOccupyDriveArr(strategyRequest.getDesktopOccupyDriveArr());
        if (Boolean.TRUE.equals(strategyRequest.getEnableFullSystemDisk())) {
            cbbDeskStrategyIDVDTO.setEnableFullSystemDisk(strategyRequest.getEnableFullSystemDisk());
            cbbDeskStrategyIDVDTO.setSystemSize(DEFAULT_SYSTEM_SIZE_WHEN_ENABLE_FULL_SYSTEM_DISK);
            cbbDeskStrategyIDVDTO.setAllowLocalDisk(Boolean.FALSE);
            cbbDeskStrategyIDVDTO.setOpenDesktopRedirect(Boolean.FALSE);
        } else {
            cbbDeskStrategyIDVDTO.setEnableFullSystemDisk(Boolean.FALSE);
            cbbDeskStrategyIDVDTO.setAllowLocalDisk(strategyRequest.getEnableAllowLocalDisk());
            cbbDeskStrategyIDVDTO.setSystemSize(strategyRequest.getSystemDisk());
            cbbDeskStrategyIDVDTO.setOpenDesktopRedirect(strategyRequest.getEnableOpenDesktopRedirect());
        }

        // 创建者默认为：管理员Admin
        cbbDeskStrategyIDVDTO.setCreatorUserName(PermissionConstants.ADMIN_NAME);

        // 水印配置
        cbbDeskStrategyIDVDTO.setEnableWatermark(strategyRequest.getEnableWatermark());
        cbbDeskStrategyIDVDTO.setWatermarkInfo(strategyRequest.getWatermarkInfo());
        cbbDeskStrategyIDVDTO.setNote(strategyRequest.getNote());
        // 策略用途
        cbbDeskStrategyIDVDTO.setStrategyUsage(strategyRequest.getCbbDeskStrategyUsage());
        cbbDeskStrategyIDVDTO.setUsbStorageDeviceAcceleration(strategyRequest.getUsbStorageDeviceAcceleration());
        return cbbDeskStrategyIDVDTO;
    }

    private CbbDeskStrategyVOIDTO buildCbbDeskStrategyVOIDTO(CreateDeskStrategyRequest strategyRequest) throws BusinessException {
        CbbDeskStrategyVOIDTO cbbDeskStrategyVOIDTO = new CbbDeskStrategyVOIDTO();
        cbbDeskStrategyVOIDTO.setName(strategyRequest.getStrategyName());
        cbbDeskStrategyVOIDTO.setPattern(strategyRequest.getDesktopType());
        // VOI 不支持 USB 存储设备只读
        cbbDeskStrategyVOIDTO.setOpenUsbReadOnly(false);
        cbbDeskStrategyVOIDTO.setUsbTypeIdArr(strategyRequest.getUsbTypeIdArr());
        cbbDeskStrategyVOIDTO.setDeskErrorStrategy(new CbbDeskErrorStrategyDTO());
        cbbDeskStrategyVOIDTO.setDeskPersonalConfigStrategyType(getCbbDeskPersonalConfigStrategyType(strategyRequest.getDesktopType()));
        cbbDeskStrategyVOIDTO.setAdOu(strategyRequest.getAdOu());
        cbbDeskStrategyVOIDTO.setDesktopOccupyDriveArr(strategyRequest.getDesktopOccupyDriveArr());
        if (Boolean.TRUE.equals(strategyRequest.getEnableFullSystemDisk())) {
            cbbDeskStrategyVOIDTO.setEnableFullSystemDisk(strategyRequest.getEnableFullSystemDisk());
            cbbDeskStrategyVOIDTO.setSystemSize(DEFAULT_SYSTEM_SIZE_WHEN_ENABLE_FULL_SYSTEM_DISK);
            cbbDeskStrategyVOIDTO.setAllowLocalDisk(Boolean.FALSE);
            cbbDeskStrategyVOIDTO.setOpenDesktopRedirect(Boolean.FALSE);
        } else {
            cbbDeskStrategyVOIDTO.setAllowLocalDisk(strategyRequest.getEnableAllowLocalDisk());
            cbbDeskStrategyVOIDTO.setSystemSize(strategyRequest.getSystemDisk());
            cbbDeskStrategyVOIDTO.setOpenDesktopRedirect(strategyRequest.getEnableOpenDesktopRedirect());
        }

        // 创建者默认为：管理员Admin
        cbbDeskStrategyVOIDTO.setCreatorUserName(PermissionConstants.ADMIN_NAME);

        // 水印配置
        cbbDeskStrategyVOIDTO.setEnableWatermark(strategyRequest.getEnableWatermark());
        cbbDeskStrategyVOIDTO.setWatermarkInfo(strategyRequest.getWatermarkInfo());
        cbbDeskStrategyVOIDTO.setNote(strategyRequest.getNote());
        // 策略用途
        cbbDeskStrategyVOIDTO.setStrategyUsage(strategyRequest.getCbbDeskStrategyUsage());
        return cbbDeskStrategyVOIDTO;
    }

    /**
     * 修改云桌面策略转换DTO
     * @param updateRequest 修改请求参数
     * @return Object
     * @throws BusinessException 业务异常
     */
    public Object convertCbbUpdateDeskStrategyDTO(UpdateDeskStrategyRequest updateRequest) throws BusinessException {
        Assert.notNull(updateRequest, "updateRequest can not be null");
        CbbDeskStrategyDTO oldDeskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(updateRequest.getId());
        CbbStrategyType strategyType = oldDeskStrategy.getStrategyType();
        switch (strategyType) {
            case VDI:
                return convertCbbUpdateDeskStrategyVDIDTO(updateRequest);
            case IDV:
                return convertCbbUpdateDeskStrategyIDVDTO(updateRequest);
            case VOI:
                return convertCbbUpdateDeskStrategyVOIDTO(updateRequest);
            case THIRD:
                return convertCbbUpdateDeskStrategyThirdPartyDTO(updateRequest);
            default:
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_STRATEGY_TYPE_NOT_EXIST, strategyType.name());
        }
    }

    private CbbUpdateDeskStrategyThirdPartyDTO convertCbbUpdateDeskStrategyThirdPartyDTO(UpdateDeskStrategyRequest updateRequest) {
        LOGGER.info("第三方策略同步参数:{}", JSON.toJSONString(updateRequest));
        Assert.notNull(updateRequest, "updateRequest must not be null");
        CbbUpdateDeskStrategyThirdPartyDTO dto = new CbbUpdateDeskStrategyThirdPartyDTO();
        dto.setId(updateRequest.getId());
        dto.setStrategyName(updateRequest.getStrategyName());
        dto.setNote(updateRequest.getNote());
        // 由于空字符串与null 存在排序问题 统一空字符串 设置为null
        if (StringUtils.isBlank(updateRequest.getRemark())) {
            dto.setRemark(null);
        } else {
            // 设置云桌面标签备注
            dto.setRemark(updateRequest.getRemark());
        }
        dto.setEnableUsbReadOnly(updateRequest.getEnableUsbReadOnly());
        dto.setEnableLanAutoDetection(updateRequest.getEnableLanAutoDetection());
        dto.setEstIdleOverTime(updateRequest.getEstIdleOverTime());
        dto.setUsbStorageDeviceMappingMode(updateRequest.getUsbStorageDeviceMappingMode());
        dto.setUsbTypeIdArr(updateRequest.getUsbTypeIdArr());
        dto.setClipBoardMode(updateRequest.getClipBoardMode());
        dto.setClipBoardSupportTypeArr(updateRequest.getClipBoardSupportTypeArr());
        dto.setEnableAgreementAgency(updateRequest.getEnableAgreementAgency());
        dto.setEnableForceUseAgreementAgency(updateRequest.getEnableForceUseAgreementAgency());
        // 磁盘映射信息
        Boolean[] vdiDiskMapArr = getVdiDiskMapArr(updateRequest.getDiskMappingType());
        dto.setEnableDiskMapping(vdiDiskMapArr[0]);
        dto.setEnableDiskMappingWriteable(vdiDiskMapArr[1]);
        // 网盘映射
        if (updateRequest.getCdRomMappingType() != null) {
            Boolean[] vdiNetDiskMappingArr = getVdiNetDiskMapArr(updateRequest.getNetDiskMappingType());
            dto.setEnableNetDiskMapping(vdiNetDiskMappingArr[0]);
            dto.setEnableNetDiskMappingWriteable(vdiNetDiskMappingArr[1]);
        }
        // 安全网关，是否使用协议代理
        dto.setEnableAgreementAgency(updateRequest.getEnableAgreementAgency());
        dto.setEnableForceUseAgreementAgency(updateRequest.getEnableForceUseAgreementAgency());

        // CDROM映射
        if (updateRequest.getCdRomMappingType() != null) {
            Boolean[] vdiCDRomMappingArr = getVdiCDROMDiskMapArr(updateRequest.getCdRomMappingType());
            dto.setEnableCDRomMapping(vdiCDRomMappingArr[0]);
            dto.setEnableCDRomMappingWriteable(vdiCDRomMappingArr[1]);
        }
        // 允许桌面登录时间
        if (updateRequest.getEnableOpenLoginLimit() != null && updateRequest.getEnableOpenLoginLimit() &&
                updateRequest.getDesktopAllowLoginTimeArr() != null) {
            CbbDeskTopAllowLoginTimeDTO[] desktopAllowLoginTimeArr = Arrays.stream(updateRequest.getDesktopAllowLoginTimeArr()).map(s ->
                    new CbbDeskTopAllowLoginTimeDTO(s.getStartTime(), s.getEndTime(), s.getWeekArr())).toArray(CbbDeskTopAllowLoginTimeDTO[]::new);
            dto.setDesktopAllowLoginTimeArr(desktopAllowLoginTimeArr);
        } else {
            dto.setDesktopAllowLoginTimeArr(new CbbDeskTopAllowLoginTimeDTO[0]);
        }
        dto.setUsbStorageDeviceMappingMode(updateRequest.getUsbStorageDeviceMappingMode());
        dto.setEnableForbidCatchScreen(updateRequest.getEnableForbidCatchScreen());
        // 电源计划，默认为睡眠，并且是关闭状态
        dto.setPowerPlan(Optional.ofNullable(updateRequest.getPowerPlan()).orElse(CbbPowerPlanEnum.SLEEP));
        dto.setPowerPlanTime(Optional.ofNullable(updateRequest.getPowerPlanTime()).orElse(0));
        dto.setEnableDoubleScreen(updateRequest.getEnableDoubleScreen());
        // 水印配置
        dto.setEnableWatermark(updateRequest.getEnableWatermark());
        dto.setWatermarkInfo(updateRequest.getWatermarkInfo());
        dto.setIpLimitMode(updateRequest.getIpLimitMode());
        dto.setIpSegmentDTOList(updateRequest.getIpSegmentDTOList());
        dto.setEnableAuditFile(updateRequest.getEnableAuditFile());
        dto.setAuditFileInfo(updateRequest.getAuditFileInfo());
        // 多会话策略桌面账号权限配置
        if (CbbDesktopSessionType.MULTIPLE == updateRequest.getSessionType()) {
            dto.setDesktopSyncLoginAccount(Boolean.TRUE);
            dto.setDesktopSyncLoginAccountPermission(updateRequest.getWindowsAccountPermission() == null ?
                    updateRequest.getDesktopSyncLoginAccountPermission() : updateRequest.getWindowsAccountPermission());
        }
        // usb压缩加速带宽配置
        dto.setUsbStorageDeviceAcceleration(updateRequest.getUsbStorageDeviceAcceleration());
        dto.setEnableUsbCompressAcceleration(updateRequest.getEnableUsbCompressAcceleration());
        dto.setEnableUsbBandwidth(updateRequest.getEnableUsbBandwidth());
        dto.setUsbBandwidthInfo(updateRequest.getUsbBandwidthInfo());
        // 协议配置
        dto.setEstProtocolType(updateRequest.getEstProtocolType());
        dto.setAgreementInfo(CommonStrategyHelper.convertAgreementConfig(
                updateRequest.getEstProtocolType(), updateRequest.getAgreementInfo()));
        return dto;
    }

    private CbbUpdateDeskStrategyVDIDTO convertCbbUpdateDeskStrategyVDIDTO(UpdateDeskStrategyRequest updateRequest) throws BusinessException {
        Assert.notNull(updateRequest, "updateRequest must not be null");
        CbbUpdateDeskStrategyVDIDTO request = new CbbUpdateDeskStrategyVDIDTO();
        request.setId(updateRequest.getId());
        request.setName(updateRequest.getStrategyName());
        // vdi设备默认音频开启
        UUID[] usbTypeArr = buildVDIUsbTypeArr(updateRequest.getUsbTypeIdArr());
        request.setUsbTypeIdArr(usbTypeArr);
        request.setIsOpenInternet(updateRequest.getEnableInternet());
        request.setIsOpenUsbReadOnly(updateRequest.getEnableUsbReadOnly());
        request.setIsOpenDoubleScreen(updateRequest.getEnableDoubleScreen());
        request.setForbidCatchScreen(updateRequest.getEnableForbidCatchScreen());
        request.setEnableWebClient(updateRequest.getEnableWebClient());
        request.setClipBoardMode(updateRequest.getClipBoardMode());
        request.setClipBoardSupportTypeArr(updateRequest.getClipBoardSupportTypeArr());
        request.setIsOpenDesktopRedirect(Boolean.TRUE.equals(updateRequest.getEnableOpenDesktopRedirect()));
        request.setDeskErrorStrategy(new CbbDeskErrorStrategyDTO());
        request.setName(updateRequest.getStrategyName());
        request.setEnableNested(updateRequest.getEnableNested() != null && updateRequest.getEnableNested());
        // 安全网关，是否使用协议代理
        request.setEnableAgreementAgency(updateRequest.getEnableAgreementAgency());
        request.setEnableForceUseAgreementAgency(updateRequest.getEnableForceUseAgreementAgency());
        // 由于空字符串与null 存在排序问题 统一空字符串 设置为null
        if (StringUtils.isBlank(updateRequest.getRemark())) {
            request.setRemark(null);
        } else {
            // 设置云桌面标签备注
            request.setRemark(updateRequest.getRemark());
        }
        request.setAdOu(updateRequest.getAdOu());
        // 磁盘映射信息
        Boolean[] vdiDiskMapArr = getVdiDiskMapArr(updateRequest.getDiskMappingType());
        request.setEnableDiskMapping(vdiDiskMapArr[0]);
        request.setEnableDiskMappingWriteable(vdiDiskMapArr[1]);
        // 局域网自动检测
        request.setEnableLanAutoDetection(Optional.ofNullable(updateRequest.getEnableLanAutoDetection()).orElse(Boolean.FALSE));
        // 电源计划，默认为睡眠，并且是关闭状态
        request.setPowerPlan(Optional.ofNullable(updateRequest.getPowerPlan()).orElse(CbbPowerPlanEnum.SLEEP));
        request.setPowerPlanTime(Optional.ofNullable(updateRequest.getPowerPlanTime()).orElse(0));
        request.setEstIdleOverTime(Optional.ofNullable(updateRequest.getEstIdleOverTime()).orElse(0));
        request.setKeyboardEmulationType(Optional.ofNullable(updateRequest.getKeyboardEmulationType()).orElse(CbbKeyboardEmulationType.PS2));
        request.setDesktopOccupyDriveArr(updateRequest.getDesktopOccupyDriveArr());

        // 网盘映射
        if (updateRequest.getCdRomMappingType() != null) {
            Boolean[] vdiNetDiskMappingArr = getVdiNetDiskMapArr(updateRequest.getNetDiskMappingType());
            request.setEnableNetDiskMapping(vdiNetDiskMappingArr[0]);
            request.setEnableNetDiskMappingWriteable(vdiNetDiskMappingArr[1]);
        }

        // CDROM映射
        if (updateRequest.getCdRomMappingType() != null) {
            Boolean[] vdiCDRomMappingArr = getVdiCDROMDiskMapArr(updateRequest.getCdRomMappingType());
            request.setEnableCDRomMapping(vdiCDRomMappingArr[0]);
            request.setEnableCDRomMappingWriteable(vdiCDRomMappingArr[1]);
        }
        // 允许桌面登录时间
        if (updateRequest.getEnableOpenLoginLimit() != null && updateRequest.getEnableOpenLoginLimit() &&
                updateRequest.getDesktopAllowLoginTimeArr() != null) {
            CbbDeskTopAllowLoginTimeDTO[] desktopAllowLoginTimeArr = Arrays.stream(updateRequest.getDesktopAllowLoginTimeArr()).map(s ->
                    new CbbDeskTopAllowLoginTimeDTO(s.getStartTime(), s.getEndTime(), s.getWeekArr())).toArray(CbbDeskTopAllowLoginTimeDTO[]::new);
            request.setDesktopAllowLoginTimeArr(desktopAllowLoginTimeArr);
        } else {
            request.setDesktopAllowLoginTimeArr(new CbbDeskTopAllowLoginTimeDTO[0]);
        }
        //用户快照开关
        request.setEnableUserSnapshot(updateRequest.getEnableUserSnapshot());

        request.setIpLimitMode(Objects.isNull(updateRequest.getIpLimitMode()) ? CbbIpLimitModeEnum.NOT_USE : updateRequest.getIpLimitMode());
        request.setIpSegmentDTOList(updateRequest.getIpSegmentDTOList());
        request.setAllowLocalDisk(updateRequest.getEnableAllowLocalDisk());

        // 水印配置
        request.setWatermarkInfo(updateRequest.getWatermarkInfo());

        request.setNote(updateRequest.getNote());
        // 安全审计
        request.setEnableAuditFile(updateRequest.getEnableAuditFile());
        request.setAuditFileInfo(updateRequest.getAuditFileInfo());
        request.setEnableAuditPrinter(updateRequest.getEnableAuditPrinter());
        request.setAuditPrinterInfo(updateRequest.getAuditPrinterInfo());

        // 安全配置-桌面账号权限
        if (CbbDesktopSessionType.SINGLE == updateRequest.getSessionType()) {
            request.setDesktopSyncLoginAccount(updateRequest.getDesktopSyncLoginAccount());
            request.setDesktopSyncLoginAccountPermission(updateRequest.getDesktopSyncLoginAccountPermission());
            request.setDesktopSyncLoginPassword(updateRequest.getDesktopSyncLoginPassword());
        } else {
            request.setDesktopSyncLoginAccount(Boolean.TRUE);
            request.setDesktopSyncLoginAccountPermission(updateRequest.getWindowsAccountPermission());
        }

        // VDI高可用
        request.setEnableHa(updateRequest.getEnableHa());
        request.setHaPriority(updateRequest.getHaPriority());
        // USB存储设备映射
        request.setUsbStorageDeviceAcceleration(updateRequest.getUsbStorageDeviceAcceleration());
        request.setUsbStorageDeviceMappingMode(updateRequest.getUsbStorageDeviceMappingMode());
        //串并口重定向
        request.setEnableSerialPortRedirect(updateRequest.getEnableSerialPortRedirect());
        request.setEnableParallelPortRedirect(updateRequest.getEnableParallelPortRedirect());
        // Usb压缩加速
        request.setEnableUsbCompressAcceleration(updateRequest.getEnableUsbCompressAcceleration());
        // Usb带宽控制
        request.setEnableUsbBandwidth(updateRequest.getEnableUsbBandwidth());
        request.setUsbBandwidthInfo(updateRequest.getUsbBandwidthInfo());
        request.setEstProtocolType(updateRequest.getEstProtocolType());
        request.setAgreementInfo(CommonStrategyHelper.convertAgreementConfig(updateRequest.getEstProtocolType(), updateRequest.getAgreementInfo()));
        request.setEnableTransparentEncrypt(Objects.nonNull(updateRequest.getEnableTransparentEncrypt())
                && updateRequest.getEnableTransparentEncrypt());
        request.setTransparentEncryptInfo(updateRequest.getTransparentEncryptInfo());
        request.setEnableForceUseAgreementAgency(updateRequest.getEnableForceUseAgreementAgency());
        request.setStrategyUsage(CbbDeskStrategyUsage.DESK);
        return request;
    }

    private CbbUpdateDeskStrategyVOIDTO convertCbbUpdateDeskStrategyVOIDTO(UpdateDeskStrategyRequest updateRequest) throws BusinessException {
        CbbUpdateDeskStrategyVOIDTO cbbUpdateDeskStrategyVOIRequest = new CbbUpdateDeskStrategyVOIDTO();
        // 支持修改云桌面类型(个性，还原) 设置云桌面类型
        cbbUpdateDeskStrategyVOIRequest.setPattern(updateRequest.getDesktopType());
        cbbUpdateDeskStrategyVOIRequest.setName(updateRequest.getStrategyName());
        cbbUpdateDeskStrategyVOIRequest.setIsAllowLocalDisk(updateRequest.getEnableAllowLocalDisk());
        cbbUpdateDeskStrategyVOIRequest.setUsbTypeIdArr(updateRequest.getUsbTypeIdArr());
        cbbUpdateDeskStrategyVOIRequest.setId(updateRequest.getId());
        cbbUpdateDeskStrategyVOIRequest.setIsOpenDesktopRedirect(updateRequest.getEnableOpenDesktopRedirect());
        cbbUpdateDeskStrategyVOIRequest.setIsOpenUsbReadOnly(false);
        cbbUpdateDeskStrategyVOIRequest.setDeskErrorStrategy(new CbbDeskErrorStrategyDTO());
        cbbUpdateDeskStrategyVOIRequest.setName(updateRequest.getStrategyName());
        cbbUpdateDeskStrategyVOIRequest.setAdOu(updateRequest.getAdOu());
        cbbUpdateDeskStrategyVOIRequest.setDesktopOccupyDriveArr(updateRequest.getDesktopOccupyDriveArr());
        cbbUpdateDeskStrategyVOIRequest.setEnableWatermark(updateRequest.getEnableWatermark());
        cbbUpdateDeskStrategyVOIRequest.setWatermarkInfo(updateRequest.getWatermarkInfo());
        cbbUpdateDeskStrategyVOIRequest.setNote(updateRequest.getNote());
        return cbbUpdateDeskStrategyVOIRequest;
    }

    private CbbUpdateDeskStrategyIDVDTO convertCbbUpdateDeskStrategyIDVDTO(UpdateDeskStrategyRequest updateRequest) throws BusinessException {
        CbbUpdateDeskStrategyIDVDTO cbbUpdateDeskStrategyIDVRequest = new CbbUpdateDeskStrategyIDVDTO();
        cbbUpdateDeskStrategyIDVRequest.setPattern(updateRequest.getDesktopType());
        cbbUpdateDeskStrategyIDVRequest.setName(updateRequest.getStrategyName());
        cbbUpdateDeskStrategyIDVRequest.setIsAllowLocalDisk(updateRequest.getEnableAllowLocalDisk());
        cbbUpdateDeskStrategyIDVRequest.setUsbTypeIdArr(updateRequest.getUsbTypeIdArr());
        cbbUpdateDeskStrategyIDVRequest.setIsOpenUsbReadOnly(updateRequest.getEnableUsbReadOnly());
        cbbUpdateDeskStrategyIDVRequest.setId(updateRequest.getId());
        cbbUpdateDeskStrategyIDVRequest.setIsOpenDesktopRedirect(updateRequest.getEnableOpenDesktopRedirect());
        cbbUpdateDeskStrategyIDVRequest.setDeskErrorStrategy(new CbbDeskErrorStrategyDTO());
        cbbUpdateDeskStrategyIDVRequest.setName(updateRequest.getStrategyName());
        cbbUpdateDeskStrategyIDVRequest.setEnableNested(BooleanUtils.isTrue(updateRequest.getEnableNested()));
        cbbUpdateDeskStrategyIDVRequest.setAdOu(updateRequest.getAdOu());
        cbbUpdateDeskStrategyIDVRequest.setDeskPersonalConfigStrategyType(getCbbDeskPersonalConfigStrategyType(updateRequest.getDesktopType()));
        cbbUpdateDeskStrategyIDVRequest.setKeyboardEmulationType(updateRequest.getKeyboardEmulationType());
        cbbUpdateDeskStrategyIDVRequest.setDesktopOccupyDriveArr(updateRequest.getDesktopOccupyDriveArr());
        cbbUpdateDeskStrategyIDVRequest.setEnableWatermark(updateRequest.getEnableWatermark());
        cbbUpdateDeskStrategyIDVRequest.setWatermarkInfo(updateRequest.getWatermarkInfo());
        cbbUpdateDeskStrategyIDVRequest.setNote(updateRequest.getNote());
        return cbbUpdateDeskStrategyIDVRequest;
    }

    private CbbDeskPersonalConfigStrategyType getCbbDeskPersonalConfigStrategyType(CbbCloudDeskPattern cbbCloudDeskPattern) {
        if (cbbCloudDeskPattern == CbbCloudDeskPattern.RECOVERABLE || cbbCloudDeskPattern == CbbCloudDeskPattern.PERSONAL) {
            return CbbDeskPersonalConfigStrategyType.USE_DATA_DISK;
        }
        return CbbDeskPersonalConfigStrategyType.NOT_USE;
    }

    /**
     * Gb 转成 Mb
     * @param size 单位 G
     * @return Mb大小
     */
    public static int gb2Mb(double size) {
        return (int)(size * G_TO_M);
    }

    /**
     * 构建VDIUSBType数组
     *
     * @param usbTypeArr usbTypeArr
     * @return UUID[]
     */
    public static UUID[] buildVDIUsbTypeArr(UUID[] usbTypeArr) {
        Assert.notNull(usbTypeArr, "usbTypeArr must not be null");
        List<UUID> usbTypeList = Lists.newArrayList();
        Collections.addAll(usbTypeList, usbTypeArr);
        if (!usbTypeList.contains(AUDIO_DEVICE)) {
            usbTypeList.add(AUDIO_DEVICE);
        }
        return usbTypeList.toArray(new UUID[] {});
    }

    /**
     * 根据网盘映射状态，获取是否开启网盘映射、是否可写
     *
     * @param netDiskMappingType 网盘映射状态
     * @return diskMapArr[0]：是否开启网盘映射，diskMapArr[1]：是否可写
     */
    public static Boolean[] getVdiNetDiskMapArr(NetDiskMappingEnum netDiskMappingType) {
        Assert.notNull(netDiskMappingType, "netDiskMappingType must not null");

        LOGGER.info("网盘映射状态为：{}", netDiskMappingType);
        switch (netDiskMappingType) {
            case CLOSED:
                return new Boolean[]{Boolean.FALSE, Boolean.FALSE};
            case READ_ONLY:
                return new Boolean[]{Boolean.TRUE, Boolean.FALSE};
            case READ_WRITE:
                return new Boolean[]{Boolean.TRUE, Boolean.TRUE};
            default:
                LOGGER.error("网盘映射类型[{0}]不存在对应设置项", JSON.toJSONString(netDiskMappingType));
                throw new IllegalArgumentException("netDiskMappingType params error");
        }
    }

    /**
     * 根据磁盘映射状态，获取是否开启磁盘映射、是否可写
     *
     * @param cdRomMappingType 网盘映射状态
     * @return diskMapArr[0]：是否开启网盘映射，diskMapArr[1]：是否可写
     */
    public static Boolean[] getVdiCDROMDiskMapArr(CdRomMappingEnum cdRomMappingType) {
        Assert.notNull(cdRomMappingType, "cdRomMappingType must not null");

        LOGGER.info("CDROM映射状态为：{}", cdRomMappingType);
        switch (cdRomMappingType) {
            case CLOSED:
                return new Boolean[]{Boolean.FALSE, Boolean.FALSE};
            case READ_ONLY:
            default:
                return new Boolean[]{Boolean.TRUE, Boolean.FALSE};
        }
    }

    private void buildDesktopAllowLoginTime(CreateDeskStrategyRequest webRequest, CbbCreateDeskStrategyVDIDTO cbbCreateRequest) {
        if (webRequest.getEnableOpenLoginLimit() != null && webRequest.getEnableOpenLoginLimit() &&
                webRequest.getDesktopAllowLoginTimeArr() != null) {
            CbbDeskTopAllowLoginTimeDTO[] desktopAllowLoginTimeArr = Arrays.stream(webRequest.getDesktopAllowLoginTimeArr()).map(s ->
                    new CbbDeskTopAllowLoginTimeDTO(s.getStartTime(), s.getEndTime(), s.getWeekArr())).toArray(CbbDeskTopAllowLoginTimeDTO[]::new);
            cbbCreateRequest.setDesktopAllowLoginTimeArr(desktopAllowLoginTimeArr);
        }
    }

    /**
     * 根据磁盘映射状态，获取是否开启磁盘映射、是否可写
     *
     * @param diskMappingType 磁盘映射状态
     * @return diskMapArr[0]：是否开启磁盘映射，diskMapArr[1]：是否可写
     */
    public static Boolean[] getVdiDiskMapArr(DiskMappingEnum diskMappingType) {
        Assert.notNull(diskMappingType, "diskMappingType must not null");

        LOGGER.info("磁盘映射状态为：{}", diskMappingType);
        switch (diskMappingType) {
            case CLOSED:
                return new Boolean[]{Boolean.FALSE, Boolean.FALSE};
            case READ_ONLY:
                return new Boolean[]{Boolean.TRUE, Boolean.FALSE};
            case READ_WRITE:
                return new Boolean[]{Boolean.TRUE, Boolean.TRUE};
            default:
                LOGGER.error("磁盘映射类型[{0}]不存在对应设置项", JSON.toJSONString(diskMappingType));
                throw new IllegalArgumentException("diskMappingType params error");
        }
    }
}
