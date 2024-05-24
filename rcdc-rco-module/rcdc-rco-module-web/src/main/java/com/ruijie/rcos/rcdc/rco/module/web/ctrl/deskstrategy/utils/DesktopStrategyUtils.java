package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbClusterGpuInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkDisplayConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbIpLimitModeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbKeyboardEmulationType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbPowerPlanEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.CbbDeskStrategyUsage;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskTopAllowLoginTimeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DeskStrategyCheckDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.VgpuExtraInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.CdRomMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.NetDiskMappingEnum;
import com.ruijie.rcos.rcdc.rca.module.def.util.CommonStrategyHelper;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.WatermarkDisplayConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.WatermarkDisplayContentDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy.EditWatermarkWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy.GetWatermarkConfigResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.DeskStrategyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.EditAuditFileWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.EditAuditPrinterWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.UpdateThirdPartyDeskStrategyWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.UpdateVDIDeskStrategyWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nullable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Description: 云桌面策略工具类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-03-02
 *
 * @author linke
 */
public class DesktopStrategyUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopStrategyUtils.class);

    private static final int WEEK_MIN = 1;

    private static final int WEEK_MAX = 7;

    /**
     * 时间格式
     */
    private static final String TIME_PATTERN = "HH:mm:ss";

    /**
     * 允许登录时间配置最大数量
     */
    private static final int ALLOW_LOGIN_TIME_MAX_NUM = 50;

    private DesktopStrategyUtils() {
        throw new IllegalStateException("Utility class");
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
        if (!usbTypeList.contains(Constants.AUDIO_DEVICE)) {
            usbTypeList.add(Constants.AUDIO_DEVICE);
        }
        return usbTypeList.toArray(new UUID[] {});
    }

    /**
     * 转换为 CbbUpdateDeskStrategyVDIDTO
     *
     * @param webRequest UpdateVDIDeskStrategyWebRequest
     * @param oldDeskStrategyVDIDTO oldDeskStrategyVDIDTO
     * @throws BusinessException 业务异常
     * @return CbbUpdateDeskStrategyVDIDTO
     */
    public static CbbUpdateDeskStrategyVDIDTO convert2CbbUpdateDeskStrategyVDIDTO(UpdateVDIDeskStrategyWebRequest webRequest,
                                                                                  CbbDeskStrategyVDIDTO oldDeskStrategyVDIDTO)
            throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");
        Assert.notNull(oldDeskStrategyVDIDTO, "oldDeskStrategyVDIDTO must not be null");
        CbbUpdateDeskStrategyVDIDTO request = new CbbUpdateDeskStrategyVDIDTO();
        request.setId(webRequest.getId());
        request.setName(webRequest.getStrategyName());
        // vdi设备默认音频开启
        UUID[] usbTypeArr = buildVDIUsbTypeArr(webRequest.getUsbTypeIdArr());
        request.setUsbTypeIdArr(usbTypeArr);
        request.setEnableSharedPrinting(webRequest.getEnableSharedPrinting());
        request.setIsOpenInternet(webRequest.getEnableInternet());
        request.setIsOpenUsbReadOnly(webRequest.getEnableUsbReadOnly());
        request.setIsOpenDoubleScreen(webRequest.getEnableDoubleScreen());
        request.setForbidCatchScreen(webRequest.getEnableForbidCatchScreen());
        request.setEnableWebClient(webRequest.getEnableWebClient());
        request.setClipBoardMode(webRequest.getClipBoardMode());
        request.setClipBoardSupportTypeArr(webRequest.getClipBoardSupportTypeArr());
        request.setIsOpenDesktopRedirect(Boolean.TRUE.equals(webRequest.getEnableOpenDesktopRedirect()));
        request.setDeskErrorStrategy(new CbbDeskErrorStrategyDTO());
        request.setName(webRequest.getStrategyName());
        request.setEnableNested(webRequest.getEnableNested() != null && webRequest.getEnableNested());
        request.setEnableSharedPrinting(webRequest.getEnableSharedPrinting());
        // 由于空字符串与null 存在排序问题 统一空字符串 设置为null
        if (StringUtils.isBlank(webRequest.getRemark())) {
            request.setRemark(null);
        } else {
            // 设置云桌面标签备注
            request.setRemark(webRequest.getRemark());
        }
        request.setAdOu(webRequest.getAdOu());
        // 磁盘映射信息
        DiskMappingEnum diskMappingType = webRequest.getDiskMappingType();
        request.setEnableDiskMapping(diskMappingType.getEnableDiskMapping());
        request.setEnableDiskMappingWriteable(diskMappingType.getEnableDiskMappingWriteable());
        // 局域网自动检测
        request.setEnableLanAutoDetection(webRequest.getEnableLanAutoDetection());
        // 电源计划，默认为睡眠，并且是关闭状态
        request.setPowerPlan(Optional.ofNullable(webRequest.getPowerPlan()).orElse(CbbPowerPlanEnum.SLEEP));
        request.setPowerPlanTime(Optional.ofNullable(webRequest.getPowerPlanTime()).orElse(0));
        request.setEstIdleOverTime(Optional.ofNullable(request.getEstIdleOverTime()).orElse(0));
        request.setKeyboardEmulationType(Optional.ofNullable(webRequest.getKeyboardEmulationType()).orElse(CbbKeyboardEmulationType.PS2));
        request.setDesktopOccupyDriveArr(webRequest.getDesktopOccupyDriveArr());

        // 网盘映射
        NetDiskMappingEnum netDiskMappingType = webRequest.getNetDiskMappingType();
        if (netDiskMappingType != null) {
            request.setEnableNetDiskMapping(netDiskMappingType.getEnableNetDiskMapping());
            request.setEnableNetDiskMappingWriteable(netDiskMappingType.getEnableNetDiskMappingWriteable());
        }

        // CDROM映射
        CdRomMappingEnum cdRomMappingType = webRequest.getCdRomMappingType();
        if (cdRomMappingType != null) {
            request.setEnableCDRomMapping(cdRomMappingType.getEnableCdRomMapping());
            request.setEnableCDRomMappingWriteable(cdRomMappingType.getEnableCDRomMappingWriteable());
        }
        // 允许桌面登录时间
        if (webRequest.getEnableOpenLoginLimit() != null && webRequest.getEnableOpenLoginLimit() &&
                webRequest.getDesktopAllowLoginTimeArr() != null) {
            verifyDesktopAllowLoginTime(webRequest.getDesktopAllowLoginTimeArr());
            CbbDeskTopAllowLoginTimeDTO[] desktopAllowLoginTimeArr = Arrays.stream(webRequest.getDesktopAllowLoginTimeArr()).map(s ->
                    new CbbDeskTopAllowLoginTimeDTO(s.getStartTime(), s.getEndTime(), s.getWeekArr())).toArray(CbbDeskTopAllowLoginTimeDTO[]::new);
            request.setDesktopAllowLoginTimeArr(desktopAllowLoginTimeArr);
        } else {
            request.setDesktopAllowLoginTimeArr(new CbbDeskTopAllowLoginTimeDTO[0]);
        }
        //用户快照数量
        request.setEnableUserSnapshot(webRequest.getEnableUserSnapshot() != null && webRequest.getEnableUserSnapshot());

        request.setIpLimitMode(Objects.isNull(webRequest.getIpLimitMode()) ? CbbIpLimitModeEnum.NOT_USE : webRequest.getIpLimitMode());
        request.setIpSegmentDTOList(webRequest.getIpSegmentDTOList());
        // 水印配置
        dealWatermarkInfo(webRequest.getWatermarkInfo(), request::setEnableWatermark, request::setWatermarkInfo);
        request.setNote(webRequest.getNote());
        // 安全审计
        convert2AuditConfig(webRequest, request, oldDeskStrategyVDIDTO);
        // 账号同步
        request.setDesktopSyncLoginAccount(webRequest.getDesktopSyncLoginAccount());
        request.setDesktopSyncLoginPassword(webRequest.getDesktopSyncLoginPassword());
        request.setDesktopSyncLoginAccountPermission(webRequest.getDesktopSyncLoginAccountPermission());
        // 安全配置-桌面账号权限
        if (CbbDesktopSessionType.SINGLE == oldDeskStrategyVDIDTO.getSessionTypeEnum()) {
            request.setDesktopSyncLoginAccount(webRequest.getDesktopSyncLoginAccount());
            request.setDesktopSyncLoginAccountPermission(webRequest.getDesktopSyncLoginAccountPermission());
            request.setDesktopSyncLoginPassword(webRequest.getDesktopSyncLoginPassword());
        } else {
            request.setDesktopSyncLoginAccount(Boolean.TRUE);
            request.setDesktopSyncLoginAccountPermission(webRequest.getWindowsAccountPermission());
        }
        //开启/禁止高可用与设置优先级
        request.setEnableHa(webRequest.getEnableHa());
        request.setHaPriority(webRequest.getHaPriority());
        request.setEstIdleOverTime(webRequest.getEstIdleOverTime());

        // cbb设置了非空约束，前端又没传，目前看办公没有使用到该字段
        request.setAllowLocalDisk(false);

        // Usb存储设备映射
        request.setUsbStorageDeviceMappingMode(webRequest.getUsbStorageDeviceMappingMode());

        // EST串并口重定向配置
        request.setEnableSerialPortRedirect(Optional.ofNullable(webRequest.getEnableSerialPortRedirect()).orElse(Boolean.FALSE));
        request.setEnableParallelPortRedirect(Optional.ofNullable(webRequest.getEnableParallelPortRedirect()).orElse(Boolean.FALSE));

        request.setUsbStorageDeviceAcceleration(webRequest.getUsbStorageDeviceAcceleration());
        request.setEstProtocolType(webRequest.getEstProtocolType());
        request.setAgreementInfo(CommonStrategyHelper.convertAgreementConfig(webRequest.getEstProtocolType(), webRequest.getAgreementInfo()));
        request.setEnableTransparentEncrypt(Objects.nonNull(webRequest.getEnableTransparentEncrypt()) && webRequest.getEnableTransparentEncrypt());
        if (Boolean.TRUE.equals(webRequest.getEnableTransparentEncrypt())) {
            CbbTransparentEncryptDTO encryptInfo = webRequest.getTransparentEncryptInfo();
            if (Objects.nonNull(encryptInfo)) {
                encryptInfo.setCustomAddressVersion(System.currentTimeMillis());
            }
            request.setTransparentEncryptInfo(encryptInfo);
        }

        // Usb压缩加速
        request.setEnableUsbCompressAcceleration(webRequest.getEnableUsbCompressAcceleration());
        // Usb带宽控制
        request.setEnableUsbBandwidth(webRequest.getEnableUsbBandwidth());
        request.setUsbBandwidthInfo(webRequest.getUsbBandwidthInfo());
        request.setEnableAgreementAgency(webRequest.getEnableAgreementAgency());
        request.setEnableForceUseAgreementAgency(webRequest.getEnableForceUseAgreementAgency());

        request.setStrategyUsage(CbbDeskStrategyUsage.DESK);
        return request;
    }

    private static void convert2AuditConfig(UpdateVDIDeskStrategyWebRequest webRequest, CbbUpdateDeskStrategyVDIDTO request,
                                            CbbDeskStrategyVDIDTO oldDeskStrategyVDIDTO) {
        // 多会话不支持文件审批
        if (CbbDesktopSessionType.MULTIPLE == oldDeskStrategyVDIDTO.getSessionTypeEnum()) {
            request.setEnableAuditFile(false);
        } else {
            boolean enableAuditFile = Boolean.TRUE.equals(webRequest.getEnableAuditFile());
            request.setEnableAuditFile(enableAuditFile);
            EditAuditFileWebRequest auditFileInfo = webRequest.getAuditFileInfo();
            if (enableAuditFile && Objects.nonNull(auditFileInfo)) {
                CbbAuditFileConfigDTO cbbAuditFileConfigDTO = new CbbAuditFileConfigDTO();
                BeanUtils.copyProperties(auditFileInfo, cbbAuditFileConfigDTO);
                request.setAuditFileInfo(cbbAuditFileConfigDTO);
            }
        }

        boolean enableAuditPrinter = Boolean.TRUE.equals(webRequest.getEnableAuditPrinter());
        request.setEnableAuditPrinter(enableAuditPrinter);
        EditAuditPrinterWebRequest auditPrinterInfo = webRequest.getAuditPrinterInfo();
        if (enableAuditPrinter && Objects.nonNull(auditPrinterInfo)) {
            CbbAuditPrinterConfigDTO cbbAuditPrinterConfigDTO = new CbbAuditPrinterConfigDTO();
            BeanUtils.copyProperties(auditPrinterInfo, cbbAuditPrinterConfigDTO);
            request.setAuditPrinterInfo(cbbAuditPrinterConfigDTO);
        }
    }

    /**
     * 转换为CbbUpdateDeskStrategyThirdPartyDTO
     * @param webRequest UpdateThirdPartyDeskStrategyWebRequest
     * @param deskStrategyThirdParty deskStrategyThirdParty
     * @return CbbUpdateDeskStrategyThirdPartyDTO
     * @throws BusinessException 业务异常
     */
    public static CbbUpdateDeskStrategyThirdPartyDTO convert2CbbUpdateDeskStrategyThirdPartyDTO(
            UpdateThirdPartyDeskStrategyWebRequest webRequest, CbbDeskStrategyThirdPartyDTO deskStrategyThirdParty) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not be null");
        Assert.notNull(deskStrategyThirdParty, "deskStrategyThirdParty is not be null");
        CbbUpdateDeskStrategyThirdPartyDTO dto = new CbbUpdateDeskStrategyThirdPartyDTO();
        BeanUtils.copyProperties(webRequest, dto);
        // 剪切板
        dto.setClipBoardMode(dto.getClipBoardMode());
        dto.setClipBoardSupportTypeArr(dto.getClipBoardSupportTypeArr());
        dto.setEnableSharedPrinting(webRequest.getEnableSharedPrinting());
        // 电源计划，默认为睡眠，并且是关闭状态
        dto.setPowerPlan(Optional.ofNullable(webRequest.getPowerPlan()).orElse(CbbPowerPlanEnum.SLEEP));
        dto.setPowerPlanTime(Optional.ofNullable(webRequest.getPowerPlanTime()).orElse(0));
        dto.setEstIdleOverTime(Optional.ofNullable(webRequest.getEstIdleOverTime()).orElse(0));
        // 磁盘映射信息
        DiskMappingEnum diskMappingType = webRequest.getDiskMappingType();
        dto.setEnableDiskMapping(diskMappingType.getEnableDiskMapping());
        dto.setEnableDiskMappingWriteable(diskMappingType.getEnableDiskMappingWriteable());
        // 局域网自动检测
        dto.setEnableLanAutoDetection(webRequest.getEnableLanAutoDetection());
        // 网盘映射
        NetDiskMappingEnum netDiskMappingType = webRequest.getNetDiskMappingType();
        if (netDiskMappingType != null) {
            dto.setEnableNetDiskMapping(netDiskMappingType.getEnableNetDiskMapping());
            dto.setEnableNetDiskMappingWriteable(netDiskMappingType.getEnableNetDiskMappingWriteable());
        }
        // CDROM映射
        CdRomMappingEnum cdRomMappingType = webRequest.getCdRomMappingType();
        if (cdRomMappingType != null) {
            dto.setEnableCDRomMapping(cdRomMappingType.getEnableCdRomMapping());
            dto.setEnableCDRomMappingWriteable(cdRomMappingType.getEnableCDRomMappingWriteable());
        }
        // 允许桌面登录时间
        if (webRequest.getEnableOpenLoginLimit() != null && webRequest.getEnableOpenLoginLimit() &&
                webRequest.getDesktopAllowLoginTimeArr() != null) {
            verifyDesktopAllowLoginTime(webRequest.getDesktopAllowLoginTimeArr());
            CbbDeskTopAllowLoginTimeDTO[] desktopAllowLoginTimeArr = Arrays.stream(webRequest.getDesktopAllowLoginTimeArr()).map(s ->
                    new CbbDeskTopAllowLoginTimeDTO(s.getStartTime(), s.getEndTime(), s.getWeekArr())).toArray(CbbDeskTopAllowLoginTimeDTO[]::new);
            dto.setDesktopAllowLoginTimeArr(desktopAllowLoginTimeArr);
        } else {
            dto.setDesktopAllowLoginTimeArr(new CbbDeskTopAllowLoginTimeDTO[0]);
        }
        dto.setIpLimitMode(Objects.isNull(webRequest.getIpLimitMode()) ? CbbIpLimitModeEnum.NOT_USE : webRequest.getIpLimitMode());
        dto.setIpSegmentDTOList(webRequest.getIpSegmentDTOList());
        // 水印配置
        dealWatermarkInfo(webRequest.getWatermarkInfo(), dto::setEnableWatermark, dto::setWatermarkInfo);
        // 多会话不支持文件审计 && 多会话桌面登录账号权限配置
        if (CbbDesktopSessionType.MULTIPLE == deskStrategyThirdParty.getSessionType()) {
            dto.setEnableAuditFile(Boolean.FALSE);
            dto.setDesktopSyncLoginAccount(Boolean.TRUE);
            dto.setDesktopSyncLoginAccountPermission(webRequest.getWindowsAccountPermission());
        } else {
            boolean enableAuditFile = Boolean.TRUE.equals(webRequest.getEnableAuditFile());
            dto.setEnableAuditFile(enableAuditFile);
            EditAuditFileWebRequest auditFileInfo = webRequest.getAuditFileInfo();
            if (enableAuditFile && Objects.nonNull(auditFileInfo)) {
                CbbAuditFileConfigDTO cbbAuditFileConfigDTO = new CbbAuditFileConfigDTO();
                BeanUtils.copyProperties(auditFileInfo, cbbAuditFileConfigDTO);
                dto.setAuditFileInfo(cbbAuditFileConfigDTO);
            }
        }
        // EST协议
        dto.setEstProtocolType(webRequest.getEstProtocolType());
        // 协议配置
        dto.setAgreementInfo(CommonStrategyHelper.convertAgreementConfig(webRequest.getEstProtocolType(), webRequest.getAgreementInfo()));
        // USB带宽配置压缩加速
        dto.setUsbStorageDeviceAcceleration(webRequest.getUsbStorageDeviceAcceleration());
        dto.setEnableUsbCompressAcceleration(webRequest.getEnableUsbCompressAcceleration());
        dto.setEnableUsbBandwidth(webRequest.getEnableUsbBandwidth());
        dto.setUsbBandwidthInfo(webRequest.getUsbBandwidthInfo());
        return dto;
    }

    /**
     * 验证时间
     *
     * @param desktopAllowLoginTimeArr 云桌面允许登录时间
     * @throws BusinessException 业务异常
     */
    public static void verifyDesktopAllowLoginTime(DeskTopAllowLoginTimeDTO[] desktopAllowLoginTimeArr) throws BusinessException {
        Assert.notNull(desktopAllowLoginTimeArr, "desktopAllowLoginTimeArr must not be null");

        if (desktopAllowLoginTimeArr.length == 0) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_LOGIN_TIME_NOT_NULL);
        }
        if (desktopAllowLoginTimeArr.length > ALLOW_LOGIN_TIME_MAX_NUM) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_LOGIN_TIME_OVER_LIMIT_NUM);
        }
        for (DeskTopAllowLoginTimeDTO deskTopAllowLoginTimeDTO : desktopAllowLoginTimeArr) {
            verifyTime(deskTopAllowLoginTimeDTO);
        }

    }

    private static void verifyTime(DeskTopAllowLoginTimeDTO deskTopAllowLoginTimeDTO) throws BusinessException {
        Integer[] weekArr = deskTopAllowLoginTimeDTO.getWeekArr();
        String startTime = deskTopAllowLoginTimeDTO.getStartTime();
        String endTime = deskTopAllowLoginTimeDTO.getEndTime();
        if (weekArr == null || weekArr.length == 0) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_LOGIN_WEEK_NOT_NULL);
        }
        if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_LOGIN_START_TIME_OR_END_TIME_NOT_NULL);
        }
        LocalTime start;
        LocalTime end;
        try {
            start = localTimeParse(startTime);
            end = localTimeParse(endTime);
        } catch (Exception ex) {
            throw new BusinessException(BusinessKey.RCDC_RCO_TIME_FORMAT_ERROR, ex);
        }
        //结束时间小于开始时间
        if (end.compareTo(start) <= 0) {
            throw new BusinessException(BusinessKey.RCDC_RCO_START_TIME_LATER_THAN_END_TIME);
        }
        for (Integer week : weekArr) {
            if (week < WEEK_MIN || week > WEEK_MAX) {
                throw new BusinessException(BusinessKey.RCDC_RCO_TIME_FORMAT_ERROR);
            }
        }
    }

    /**
     * 根据时间字符串转时间类型
     *
     * @param localTimeStr 时间字符串
     * @return 时间
     */
    private static LocalTime localTimeParse(String localTimeStr) {
        Assert.hasText(localTimeStr, "localTimeStr must has text");
        return LocalTime.parse(localTimeStr, DateTimeFormatter.ofPattern(TIME_PATTERN));
    }

    /**
     * 校验并获取VGPU信息
     *
     * @param extraInfoDTO 显卡配置
     * @param allGpuInfoList 服务器配置的显卡列表
     * @return CbbClusterGpuInfoDTO
     * @throws BusinessException 业务异常
     */
    public static CbbClusterGpuInfoDTO getMatchVgpuInfo(VgpuExtraInfoDTO extraInfoDTO, List<CbbClusterGpuInfoDTO> allGpuInfoList)
            throws BusinessException {
        Assert.notNull(extraInfoDTO, "extraInfoDTO must not be null");
        Assert.notNull(allGpuInfoList, "allGpuInfoList must not be null");

        String model = extraInfoDTO.getModel();
        Map<String, List<CbbClusterGpuInfoDTO>> gpuMap = allGpuInfoList.stream().collect(Collectors.groupingBy(CbbClusterGpuInfoDTO::getModel));
        List<CbbClusterGpuInfoDTO> gpuInfoDTOList = gpuMap.get(model);
        if (!CollectionUtils.isEmpty(gpuInfoDTOList)) {
            // 获取前端要展示的内容保存起来
            CbbClusterGpuInfoDTO gpuInfoDTO = gpuInfoDTOList.get(0);
            extraInfoDTO.setVgpuModelType(gpuInfoDTO.getVgpuModelType());
            extraInfoDTO.setGraphicsMemorySize(gpuInfoDTO.getGraphicsMemorySize());
            extraInfoDTO.setParentGpuModel(gpuInfoDTO.getParentGpuModel());
            extraInfoDTO.setModel(extraInfoDTO.getModel());
            return gpuInfoDTO;
        }
        LOGGER.error("服务器不存在此vGPU配置选项[{}]", model);
        throw new BusinessException(DeskStrategyBusinessKey.RCO_DESK_STRATEGY_VGPU_OPTIONS_NOT_EXIST, model);
    }

    /**
     * 根据磁盘映射状态，获取是否开启磁盘映射、是否可写
     *
     * @param diskMappingType 磁盘映射状态
     * @return diskMapArr[0]：是否开启磁盘映射，diskMapArr[1]：是否可写
     * @throws BusinessException 业务异常
     */
    public static Boolean[] getVdiDiskMapArr(DiskMappingEnum diskMappingType) throws BusinessException {
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
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_DISK_MAPPING_TYPE_NOT_EXIST,
                        JSON.toJSONString(diskMappingType));
        }
    }

    /**
     * 根据是否开启磁盘映射、是否可写，获取磁盘映射状态
     *
     * @param enableDiskMapping 是否开启磁盘映射
     * @param enableDiskMappingWriteable 是否可写
     * @return 磁盘映射状态
     */
    public static DiskMappingEnum getDiskMappingEnum(Boolean enableDiskMapping, Boolean enableDiskMappingWriteable) {
        Assert.notNull(enableDiskMapping, "enableDiskMapping must not null");
        Assert.notNull(enableDiskMappingWriteable, "enableDiskMappingWriteable must not null");

        LOGGER.info("是否开启磁盘映射[{}]，是否可写[{}]", enableDiskMapping, enableDiskMappingWriteable);
        return DiskMappingEnum.getNetDiskMappingEnum(enableDiskMapping, enableDiskMappingWriteable) //
                .orElse(DiskMappingEnum.CLOSED);
    }



    /**
     * 根据是否开启网盘映射、是否可写，获取网盘映射状态
     *
     * @param enableNetDiskMapping 是否开启网盘映射
     * @param enableNetDiskMappingWriteable 是否可写
     * @return 网盘映射状态
     */
    public static NetDiskMappingEnum getNetDiskMappingEnum(Boolean enableNetDiskMapping, Boolean enableNetDiskMappingWriteable) {
        Assert.notNull(enableNetDiskMapping, "enableNetDiskMapping must not null");
        Assert.notNull(enableNetDiskMappingWriteable, "enableNetDiskMappingWriteable must not null");

        LOGGER.info("是否开启网盘映射[{}]，是否可写[{}]", enableNetDiskMapping, enableNetDiskMappingWriteable);
        return NetDiskMappingEnum.getNetDiskMappingEnum(enableNetDiskMapping, enableNetDiskMappingWriteable) //
                .orElse(NetDiskMappingEnum.CLOSED);

    }


    /**
     * 根据是否开启CDROM映射、是否可写，获取CDROM映射状态
     *
     * @param enableCDRomMapping 是否开启CDROM映射
     * @param enableCDRomWriteable 是否可写
     * @return CDROM映射状态
     */
    public static CdRomMappingEnum getCDRomMappingEnum(Boolean enableCDRomMapping, Boolean enableCDRomWriteable) {
        Assert.notNull(enableCDRomMapping, "enableCDRomMapping must not null");
        Assert.notNull(enableCDRomWriteable, "enableCDRomWriteable must not null");

        LOGGER.info("是否开启CDROM映射[{}]，是否可写[{}]", enableCDRomMapping, enableCDRomWriteable);
        return CdRomMappingEnum.getCdRomMappingEnum(enableCDRomMapping, enableCDRomWriteable) //
                .orElse(CdRomMappingEnum.CLOSED);
    }

    /**
     * 处理水印相关参数
     *
     * @param watermark 水印配置信息
     * @param setEnable 设置是否开启水印的set方法
     * @param setInfo   设置配置信息的set方法
     */
    public static void dealWatermarkInfo(@Nullable EditWatermarkWebRequest watermark,
                                         Consumer<Boolean> setEnable, Consumer<CbbWatermarkConfigDTO> setInfo) {
        Assert.notNull(setEnable, "setEnable must not null");
        Assert.notNull(setInfo, "setInfo must not null");
        if (Objects.isNull(watermark)) {
            setEnable.accept(false);
            return;
        }
        setEnable.accept(watermark.getEnable());

        CbbWatermarkConfigDTO watermarkConfigDTO = new CbbWatermarkConfigDTO();
        watermarkConfigDTO.setEnable(watermark.getEnable());
        watermarkConfigDTO.setEnableDarkWatermark(watermark.getEnableDarkWatermark());
        watermarkConfigDTO.setDisplayContent(JSON.toJSONString(watermark.getDisplayContent()));

        CbbWatermarkDisplayConfigDTO cbbDisplayConfigDTO = new CbbWatermarkDisplayConfigDTO();
        BeanUtils.copyProperties(watermark.getDisplayConfig(), cbbDisplayConfigDTO);
        watermarkConfigDTO.setDisplayConfig(cbbDisplayConfigDTO);
        setInfo.accept(watermarkConfigDTO);
    }

    /**
     * 处理水印相关参数
     *
     * @param watermarkConfigDTO 水印配置信息
     * @return GetWatermarkConfigResponse 水印配置信息
     */
    public static GetWatermarkConfigResponse convertWatermarkConfig(@Nullable CbbWatermarkConfigDTO watermarkConfigDTO) {

        if (Objects.isNull(watermarkConfigDTO)) {
            // 返回null
            return null;
        }
        GetWatermarkConfigResponse watermarkConfigResponse = new GetWatermarkConfigResponse();
        watermarkConfigResponse.setEnable(watermarkConfigDTO.getEnable());
        watermarkConfigResponse.setEnableDarkWatermark(watermarkConfigDTO.getEnableDarkWatermark());
        WatermarkDisplayContentDTO displayContentDTO = JSON.parseObject(watermarkConfigDTO.getDisplayContent(), WatermarkDisplayContentDTO.class);
        watermarkConfigResponse.setDisplayContent(displayContentDTO);
        WatermarkDisplayConfigDTO displayConfig = new WatermarkDisplayConfigDTO();
        BeanUtils.copyProperties(watermarkConfigDTO.getDisplayConfig(), displayConfig);
        watermarkConfigResponse.setDisplayConfig(displayConfig);

        return watermarkConfigResponse;
    }

    /**
     * 处理安全审计相关参数
     *
     * @param auditFileInfo    文件审计配置
     * @param auditPrinterInfo 打印审计配置
     * @param strategyCheckDTO 策略校验DTO
     */
    public static void dealAuditInfo(@Nullable EditAuditFileWebRequest auditFileInfo, @Nullable EditAuditPrinterWebRequest auditPrinterInfo,
                                     DeskStrategyCheckDTO strategyCheckDTO) {
        Assert.notNull(strategyCheckDTO, "strategyCheckDTO must not null");
        if (Objects.nonNull(auditFileInfo)) {
            CbbAuditFileConfigDTO cbbAuditFileConfigDTO = new CbbAuditFileConfigDTO();
            BeanUtils.copyProperties(auditFileInfo, cbbAuditFileConfigDTO);
            strategyCheckDTO.setAuditFileInfo(cbbAuditFileConfigDTO);
        }

        if (Objects.nonNull(auditPrinterInfo)) {
            CbbAuditPrinterConfigDTO cbbAuditPrinterConfigDTO = new CbbAuditPrinterConfigDTO();
            BeanUtils.copyProperties(auditPrinterInfo, cbbAuditPrinterConfigDTO);
            strategyCheckDTO.setAuditPrinterInfo(cbbAuditPrinterConfigDTO);
        }
    }
}
