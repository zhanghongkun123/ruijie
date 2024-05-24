package com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ImageTemplateSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbStrategyType;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementConfigRequestDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.CloudDeskComputerNameConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskTopAllowLoginTimeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.RccmServerConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.QueryPlatformTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.RccmDeskStrategyClusterRestRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.UnifiedManageDataRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.UnifiedManageMasterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.CdRomMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.NetDiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.RccmManageBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.dao.UnifiedManageDataDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.entity.UnifiedManageDataEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.UnifiedManageDataService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DesktopStrategyUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.ws.rs.HttpMethod;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 统一管理实现类
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/17
 *
 * @author TD
 */
@Service
public class UnifiedManageDataServiceImpl implements UnifiedManageDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnifiedManageDataServiceImpl.class);

    @Autowired
    private UnifiedManageDataDAO unifiedManageDataDAO;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CloudDeskComputerNameConfigAPI cloudDeskComputerNameConfigAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    private CbbThirdPartyDeskStrategyMgmtAPI cbbThirdPartyDeskStrategyMgmtAPI;

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbImageDiskSnapshotMgmtAPI cbbImageDiskSnapshotMgmtAPI;

    @Autowired
    private CbbUSBTypeMgmtAPI cbbUSBTypeMgmtAPI;

    private static final double G_TO_M = 1 << 10; // 字节

    private static final String SYNC_DESK_STRATEGY_URL = "/v1/desk_strategy/sync";

    private static final String DELETE_DESK_STRATEGY_URL = "/v1/desk_strategy/delete/%s";

    @Override
    public void saveUnifiedManage(UnifiedManageDataRequest request) {
        Assert.notNull(request, "saveUnifiedManage request is not null");
        UUID unifiedManageDataId = request.getUnifiedManageDataId();
        UUID relatedId = request.getRelatedId();
        UnifiedManageDataEntity manageDataEntity
                = unifiedManageDataDAO.findByRelatedIdAndRelatedType(relatedId, request.getRelatedType());
        if (Objects.isNull(manageDataEntity)) {
            manageDataEntity = new UnifiedManageDataEntity();
            manageDataEntity.setId(UUID.randomUUID());
            manageDataEntity.setUnifiedManageDataId(unifiedManageDataId);
            manageDataEntity.setRelatedId(relatedId);
            manageDataEntity.setRelatedType(request.getRelatedType());
            manageDataEntity.setCreateTime(new Date());
            unifiedManageDataDAO.save(manageDataEntity);
        }
    }

    @Override
    public UnifiedManageDataEntity findByRelatedIdAndRelatedType(UnifiedManageDataRequest request) {
        Assert.notNull(request, "findByRelatedIdAndRelatedType request is not null");
        return unifiedManageDataDAO.findByRelatedIdAndRelatedType(request.getRelatedId(), request.getRelatedType());
    }

    @Override
    public List<UnifiedManageDataEntity> findByRelatedType(UnifiedManageFunctionKeyEnum relatedType) {
        Assert.notNull(relatedType, "relatedType request is not null");
        return unifiedManageDataDAO.findByRelatedType(relatedType);
    }

    @Override
    public UnifiedManageDataEntity findByUnifiedManageDataId(UUID unifiedManageDataId) {
        Assert.notNull(unifiedManageDataId, "unifiedManageDataId is not null");
        return unifiedManageDataDAO.findByUnifiedManageDataId(unifiedManageDataId);
    }

    @Override
    public List<UnifiedManageDataEntity> findByUnifiedManageDataIdIn(List<UUID> unifiedManageDataIdList) {
        Assert.notNull(unifiedManageDataIdList, "unifiedManageDataId is not null");
        return unifiedManageDataDAO.findByUnifiedManageDataIdIn(unifiedManageDataIdList);
    }

    @Override
    public void deleteUnifiedManage(UnifiedManageDataRequest request) {
        Assert.notNull(request, "deleteUnifiedManage request is not null");
        UnifiedManageDataEntity manageDataEntity
                = unifiedManageDataDAO.findByRelatedIdAndRelatedType(request.getRelatedId(), request.getRelatedType());
        if (Objects.nonNull(manageDataEntity)) {
            unifiedManageDataDAO.deleteById(manageDataEntity.getId());
        }
    }

    @Override
    public void pushSyncUnifiedManage(UUID deskStrategyId) throws BusinessException {
        Assert.notNull(deskStrategyId, "pushUpdateUnifiedManage deskStrategyId can not be null");
        RccmDeskStrategyClusterRestRequest strategyRequestContent = buildCreateDeskStrategyClusterRestRequest(deskStrategyId);
        UnifiedManageMasterRequest<RccmDeskStrategyClusterRestRequest> masterRequest =
                buildUnifiedManageRequest(strategyRequestContent, SYNC_DESK_STRATEGY_URL, HttpMethod.PUT,
                        strategyRequestContent.getUnifiedManageDataId(), strategyRequestContent.getStrategyName());
        rccmManageService.pushDeskStrategyToRccm(masterRequest);
    }

    @Override
    public void pushDeleteUnifiedManage(UUID deskStrategyId, String deskStrategyName, UUID unifiedManageDataId) throws BusinessException {
        Assert.notNull(deskStrategyId, "deskStrategyId can not be null");
        Assert.hasText(deskStrategyName, "deskStrategyName can not be null");
        Assert.notNull(unifiedManageDataId, "unifiedManageDataId can not be null");
        UnifiedManageDataRequest request = new UnifiedManageDataRequest(
                deskStrategyId, UnifiedManageFunctionKeyEnum.DESK_STRATEGY, unifiedManageDataId);
        UnifiedManageMasterRequest<UnifiedManageDataRequest> masterRequest = buildUnifiedManageRequest(request,
                String.format(DELETE_DESK_STRATEGY_URL, deskStrategyId), HttpMethod.DELETE, unifiedManageDataId, deskStrategyName);
        rccmManageService.pushDeskStrategyToRccm(masterRequest);
    }

    @Override
    public void createNotify(UUID deskStrategyId) throws BusinessException {
        Assert.notNull(deskStrategyId, "deskStrategyId can not be null");
        if (isNeedSendDeskStrategy(deskStrategyId)) {
            // 数据保存
            UnifiedManageDataRequest unifiedManageDataRequest = new UnifiedManageDataRequest(deskStrategyId,
                    UnifiedManageFunctionKeyEnum.DESK_STRATEGY);
            saveUnifiedManage(unifiedManageDataRequest);
            try {
                // 数据推送
                pushSyncUnifiedManage(deskStrategyId);
            } catch (BusinessException e) {
                LOGGER.error("推送创建的云桌面策略[{}]数据失败", deskStrategyId, e);
                auditLogAPI.recordLog(RccmManageBusinessKey.RCDC_RCO_DESKTOP_STRATEGY_CHANGE_SYNC_RCCM_ERROR, e,
                        cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(deskStrategyId).getName(), e.getI18nMessage());
            }
        }
    }

    @Override
    public void updateNotify(UUID deskStrategyId) throws BusinessException {
        Assert.notNull(deskStrategyId, "deskStrategyId can not be null");
        if (isNeedSendDeskStrategy(deskStrategyId)) {
            UnifiedManageDataRequest unifiedManageDataRequest = new UnifiedManageDataRequest(deskStrategyId,
                    UnifiedManageFunctionKeyEnum.DESK_STRATEGY);
            UnifiedManageDataEntity unifiedManageDataEntity = findByRelatedIdAndRelatedType(unifiedManageDataRequest);
            if (Objects.isNull(unifiedManageDataEntity)) {
                LOGGER.debug("云桌面策略关联集群管理数据为空，无需进行策略数据推送，deskStrategyId:{}", deskStrategyId);
                return;
            }
            try {
                // 数据推送
                pushSyncUnifiedManage(deskStrategyId);
            } catch (BusinessException e) {
                LOGGER.error("推送已更新云桌面策略[{}]数据失败", deskStrategyId, e);
                auditLogAPI.recordLog(RccmManageBusinessKey.RCDC_RCO_DESKTOP_STRATEGY_CHANGE_SYNC_RCCM_ERROR, e,
                        cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(deskStrategyId).getName(), e.getI18nMessage());
            }
        }
    }

    @Override
    public void deleteNotify(UUID deskStrategyId, String deskStrategyName) throws BusinessException {
        Assert.notNull(deskStrategyId, "deleteNotify deskStrategyId can not be null");
        Assert.hasText(deskStrategyName, "deleteNotify deskStrategyName can not be null");
        UnifiedManageDataRequest unifiedManageDataRequest = new UnifiedManageDataRequest(deskStrategyId, UnifiedManageFunctionKeyEnum.DESK_STRATEGY);
        UnifiedManageDataEntity unifiedManageDataEntity = findByRelatedIdAndRelatedType(unifiedManageDataRequest);
        if (Objects.isNull(unifiedManageDataEntity)) {
            LOGGER.debug("云桌面策略关联集群管理数据为空，无需进行策略数据推送，deskStrategyId:{}", deskStrategyId);
            return;
        }
        deleteUnifiedManage(unifiedManageDataRequest);
        if (isNeedSendDeskStrategy(deskStrategyId)) {
            try {
                // 数据推送
                pushDeleteUnifiedManage(deskStrategyId, deskStrategyName, unifiedManageDataEntity.getUnifiedManageDataId());
            } catch (BusinessException e) {
                LOGGER.error("推送主集群已删除云桌面策略[{}]数据失败", deskStrategyId, e);
                auditLogAPI.recordLog(RccmManageBusinessKey.RCDC_RCO_DESKTOP_STRATEGY_CHANGE_SYNC_RCCM_ERROR, e, deskStrategyName,
                        e.getI18nMessage());
            }
        }
    }

    private RccmDeskStrategyClusterRestRequest buildCreateDeskStrategyClusterRestRequest(UUID deskStrategyId) throws BusinessException {
        CbbDeskStrategyDTO deskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(deskStrategyId);
        RccmDeskStrategyClusterRestRequest restRequest;
        switch (deskStrategy.getStrategyType()) {
            case VDI:
                restRequest = getVDIDeskStrategyRequest(deskStrategyId);
                break;
            case IDV:
                restRequest = getIDVDeskStrategyRequest(deskStrategyId);
                break;
            case VOI:
                restRequest = getVOIDeskStrategyRequest(deskStrategyId);
                break;
            case THIRD:
                restRequest = getTHIRDDeskStrategyRequest(deskStrategyId);
                break;
            default:
                throw new IllegalArgumentException("StrategyType params error");
        }
        return restRequest;
    }

    private RccmDeskStrategyClusterRestRequest getTHIRDDeskStrategyRequest(UUID deskStrategyId) throws BusinessException {
        RccmDeskStrategyClusterRestRequest request = new RccmDeskStrategyClusterRestRequest();
        CbbDeskStrategyThirdPartyDTO dto = cbbThirdPartyDeskStrategyMgmtAPI.getDeskStrategyThirdParty(deskStrategyId);
        request.setId(deskStrategyId);
        request.setStrategyName(dto.getStrategyName());
        request.setSessionType(dto.getSessionType());
        request.setUsbTypeIdArr(dto.getUsbTypeIdArr());
        request.setUsbTypeNameList(convertToUSBTypeName(dto.getUsbTypeIdArr()));
        request.setEnableUsbReadOnly(dto.getEnableUsbReadOnly());
        request.setUsbStorageDeviceMappingMode(dto.getUsbStorageDeviceMappingMode());
        request.setEnableDoubleScreen(dto.getOpenDoubleScreen());
        request.setEnableForbidCatchScreen(dto.getForbidCatchScreen());
        request.setEnableWebClient(dto.getEnableWebClient());
        request.setDeskCreateMode(dto.getDeskCreateMode());
        request.setClipBoardMode(dto.getClipBoardMode());
        request.setClipBoardSupportTypeArr(ArrayUtils.isEmpty(dto.getClipBoardSupportTypeArr()) ? null : dto.getClipBoardSupportTypeArr());
        request.setStrategyType(dto.getStrategyType());
        // 协议代理开关
        request.setEnableAgreementAgency(dto.getEnableAgreementAgency());
        request.setEnableForceUseAgreementAgency(dto.getEnableForceUseAgreementAgency());
        // 共享打印开关
        request.setEnableSharedPrinting(dto.getEnableSharedPrinting());

        // 设置云桌面标签
        request.setRemark(dto.getRemark());
        // 磁盘映射
        DiskMappingEnum diskMappingEnum = DesktopStrategyUtils.getDiskMappingEnum(dto.getEnableDiskMapping(), dto.getEnableDiskMappingWriteable());
        request.setDiskMappingType(diskMappingEnum);
        // 是否开启局域网自动检测
        request.setEnableLanAutoDetection(dto.getEnableLanAutoDetection());
        // 电源计划
        request.setPowerPlan(dto.getPowerPlan());
        request.setPowerPlanTime(dto.getPowerPlanTime());
        request.setEstIdleOverTime(dto.getEstIdleOverTime());

        // 网盘映射
        NetDiskMappingEnum netDiskMappingEnum = DesktopStrategyUtils.getNetDiskMappingEnum(dto.getEnableNetDiskMapping(),
                dto.getEnableNetDiskMappingWriteable());
        request.setNetDiskMappingType(netDiskMappingEnum);

        // CDROM映射
        CdRomMappingEnum cdRomMappingEnum = DesktopStrategyUtils.getCDRomMappingEnum(dto.getEnableCDRomMapping(),
                dto.getEnableCDRomMappingWriteable());
        request.setCdRomMappingType(cdRomMappingEnum);
        request.setEnableOpenLoginLimit(dto.getEnableOpenLoginLimit());
        // 桌面登录时间范围
        if (dto.getDesktopAllowLoginTimeInfo() != null) {
            List<DeskTopAllowLoginTimeDTO> deskTopAllowLoginTimeList = JSON.parseArray(dto.getDesktopAllowLoginTimeInfo()
                    , DeskTopAllowLoginTimeDTO.class);
            if (CollectionUtils.isNotEmpty(deskTopAllowLoginTimeList)) {
                request.setEnableOpenLoginLimit(true);
                request.setDesktopAllowLoginTimeArr(deskTopAllowLoginTimeList.toArray(new DeskTopAllowLoginTimeDTO[0]));
            }
        }
        request.setIpLimitMode(dto.getIpLimitMode());
        request.setIpSegmentDTOList(CollectionUtils.isEmpty(dto.getIpSegmentDTOList()) ? null : dto.getIpSegmentDTOList());
        // 水印配置
        request.setWatermarkInfo(dto.getWatermarkInfo());
        request.setEnableWatermark(dto.getEnableWatermark());
        // 集群信息
        UnifiedManageDataEntity manageDataEntity =
                unifiedManageDataDAO.findByRelatedIdAndRelatedType(deskStrategyId, UnifiedManageFunctionKeyEnum.DESK_STRATEGY);
        if (Objects.nonNull(manageDataEntity)) {
            request.setUnifiedManageDataId(manageDataEntity.getUnifiedManageDataId());
        }

        request.setNote(dto.getNote());
        // 文件审计
        request.setEnableAuditFile(dto.getEnableAuditFile());
        request.setAuditFileInfo(dto.getAuditFileInfo());
        // 协议配置
        CbbAgreementDTO cbbAgreementDTO = dto.getAgreementInfo();
        request.setEstProtocolType(dto.getEstProtocolType());
        if (Objects.nonNull(cbbAgreementDTO)) {
            AgreementDTO agreement = new AgreementDTO();
            agreement.setLanEstConfig(buildEstConfig(cbbAgreementDTO.getLanEstConfig()));
            agreement.setWanEstConfig(buildEstConfig(cbbAgreementDTO.getWanEstConfig()));
            request.setAgreementInfo(agreement);
        }
        // 登录权限
        request.setDesktopSyncLoginAccount(dto.getDesktopSyncLoginAccount());
        request.setDesktopSyncLoginAccountPermission(dto.getDesktopSyncLoginAccountPermission());

        // USB带宽控制与压缩加速
        request.setUsbStorageDeviceAcceleration(dto.getUsbStorageDeviceAcceleration());
        request.setEnableUsbCompressAcceleration(dto.getEnableUsbCompressAcceleration());
        request.setEnableUsbBandwidth(dto.getEnableUsbBandwidth());
        request.setUsbBandwidthInfo(dto.getUsbBandwidthInfo());
        return request;
    }

    private RccmDeskStrategyClusterRestRequest getVDIDeskStrategyRequest(UUID deskStrategyId) throws BusinessException {
        RccmDeskStrategyClusterRestRequest request = new RccmDeskStrategyClusterRestRequest();
        CbbDeskStrategyVDIDTO dto = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(deskStrategyId);
        request.setId(deskStrategyId);
        request.setStrategyName(dto.getName());
        request.setSessionType(dto.getSessionTypeEnum());
        request.setDesktopType(dto.getPattern());
        request.setUsbTypeIdArr(dto.getUsbTypeIdArr());
        request.setUsbTypeNameList(convertToUSBTypeName(dto.getUsbTypeIdArr()));
        request.setEnableInternet(dto.getOpenInternet());
        request.setEnableUsbReadOnly(dto.getOpenUsbReadOnly());
        request.setEnableDoubleScreen(dto.getOpenDoubleScreen());
        request.setEnableForbidCatchScreen(dto.getForbidCatchScreen());
        request.setEnableWebClient(dto.getEnableWebClient());
        request.setEnableNested(dto.getEnableNested());
        request.setDeskCreateMode(dto.getDeskCreateMode());
        request.setClipBoardMode(dto.getClipBoardMode());
        request.setClipBoardSupportTypeArr(ArrayUtils.isEmpty(dto.getClipBoardSupportTypeArr()) ? null : dto.getClipBoardSupportTypeArr());
        request.setEnableOpenDesktopRedirect(Boolean.TRUE.equals(dto.getOpenDesktopRedirect()));
        request.setStrategyType(CbbStrategyType.VDI);
        request.setComputerName(obtainComputerName(dto.getId()));
        // 协议代理开关
        request.setEnableAgreementAgency(dto.getEnableAgreementAgency());
        request.setEnableForceUseAgreementAgency(dto.getEnableForceUseAgreementAgency());
        // 共享打印开关
        request.setEnableSharedPrinting(dto.getEnableSharedPrinting());

        // 设置云桌面标签
        request.setRemark(dto.getRemark());
        request.setAdOu(dto.getAdOu());
        // 磁盘映射
        DiskMappingEnum diskMappingEnum = DesktopStrategyUtils.getDiskMappingEnum(dto.getEnableDiskMapping(), dto.getEnableDiskMappingWriteable());
        request.setDiskMappingType(diskMappingEnum);
        // 是否开启局域网自动检测
        request.setEnableLanAutoDetection(dto.getEnableLanAutoDetection());
        // 电源计划
        request.setPowerPlan(dto.getPowerPlan());
        request.setPowerPlanTime(dto.getPowerPlanTime());
        request.setEstIdleOverTime(dto.getEstIdleOverTime());
        request.setKeyboardEmulationType(dto.getKeyboardEmulationType());

        // 网盘映射
        NetDiskMappingEnum netDiskMappingEnum = DesktopStrategyUtils.getNetDiskMappingEnum(dto.getEnableNetDiskMapping(),
                dto.getEnableNetDiskMappingWriteable());
        request.setNetDiskMappingType(netDiskMappingEnum);

        // CDROM映射
        CdRomMappingEnum cdRomMappingEnum = DesktopStrategyUtils.getCDRomMappingEnum(dto.getEnableCDRomMapping(),
                dto.getEnableCDRomMappingWriteable());
        request.setCdRomMappingType(cdRomMappingEnum);
        request.setEnableOpenLoginLimit(false);
        // 桌面登录时间范围
        if (dto.getDesktopAllowLoginTimeInfo() != null) {
            List<DeskTopAllowLoginTimeDTO> deskTopAllowLoginTimeList = JSON.parseArray(dto.getDesktopAllowLoginTimeInfo()
                    , DeskTopAllowLoginTimeDTO.class);
            if (CollectionUtils.isNotEmpty(deskTopAllowLoginTimeList)) {
                request.setEnableOpenLoginLimit(true);
                request.setDesktopAllowLoginTimeArr(deskTopAllowLoginTimeList.toArray(new DeskTopAllowLoginTimeDTO[0]));
            }
        }
        request.setEnableUserSnapshot(dto.getEnableUserSnapshot() != null && dto.getEnableUserSnapshot());
        request.setIpLimitMode(dto.getIpLimitMode());
        request.setIpSegmentDTOList(CollectionUtils.isEmpty(dto.getIpSegmentDTOList()) ? null : dto.getIpSegmentDTOList());
        request.setEnableAllowLocalDisk(dto.getAllowLocalDisk());
        request.setDesktopOccupyDriveArr(dto.getDesktopOccupyDriveArr());

        // 水印配置
        request.setWatermarkInfo(dto.getWatermarkInfo());
        request.setEnableWatermark(dto.getEnableWatermark());
        // 集群信息
        UnifiedManageDataEntity manageDataEntity =
                unifiedManageDataDAO.findByRelatedIdAndRelatedType(deskStrategyId, UnifiedManageFunctionKeyEnum.DESK_STRATEGY);
        if (Objects.nonNull(manageDataEntity)) {
            request.setUnifiedManageDataId(manageDataEntity.getUnifiedManageDataId());
        }

        request.setNote(dto.getNote());
        // 安全审计
        request.setEnableAuditFile(dto.getEnableAuditFile());
        request.setAuditFileInfo(dto.getAuditFileInfo());
        request.setEnableAuditPrinter(dto.getEnableAuditPrinter());
        request.setAuditPrinterInfo(dto.getAuditPrinterInfo());

        // 桌面登录账号同步
        request.setDesktopSyncLoginAccount(dto.getDesktopSyncLoginAccount());
        request.setDesktopSyncLoginAccountPermission(dto.getDesktopSyncLoginAccountPermission());
        request.setDesktopSyncLoginPassword(dto.getDesktopSyncLoginPassword());

        // 高可用
        request.setEnableHa(dto.getEnableHa());
        request.setHaPriority(dto.getHaPriority());
        // Usb存储设备映射
        request.setUsbStorageDeviceMappingMode(dto.getUsbStorageDeviceMappingMode());
        request.setUsbStorageDeviceAcceleration(dto.getUsbStorageDeviceAcceleration());
        //串并口重定向
        request.setEnableSerialPortRedirect(dto.getEnableSerialPortRedirect());
        request.setEnableParallelPortRedirect(dto.getEnableParallelPortRedirect());
        // Usb压缩加速
        request.setEnableUsbCompressAcceleration(dto.getEnableUsbCompressAcceleration());
        // Usb带宽控制
        request.setEnableUsbBandwidth(dto.getEnableUsbBandwidth());
        request.setUsbBandwidthInfo(dto.getUsbBandwidthInfo());
        request.setEstProtocolType(dto.getEstProtocolType());
        CbbAgreementDTO cbbAgreementDTO = dto.getAgreementInfo();
        if (Objects.nonNull(cbbAgreementDTO)) {
            AgreementDTO agreement = new AgreementDTO();
            agreement.setLanEstConfig(buildEstConfig(cbbAgreementDTO.getLanEstConfig()));
            agreement.setWanEstConfig(buildEstConfig(cbbAgreementDTO.getWanEstConfig()));
            request.setAgreementInfo(agreement);
        }
        request.setEnableTransparentEncrypt(dto.getEnableTransparentEncrypt());
        request.setTransparentEncryptInfo(dto.getTransparentEncryptInfo());
        return request;
    }

    private AgreementConfigRequestDTO buildEstConfig(CbbHestConfigDTO estConfig) {
        AgreementConfigRequestDTO agreementConfig = new AgreementConfigRequestDTO();
        BeanUtils.copyProperties(estConfig, agreementConfig);
        if (estConfig.getWebAdvanceSettingInfo() != null) {
            agreementConfig.setWebAdvanceSettingInfo(JSON.toJSONString(estConfig.getWebAdvanceSettingInfo()));
        }
        return agreementConfig;
    }

    private RccmDeskStrategyClusterRestRequest getIDVDeskStrategyRequest(UUID deskStrategyId) throws BusinessException {
        CbbDeskStrategyIDVDTO dto = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(deskStrategyId);
        RccmDeskStrategyClusterRestRequest request = new RccmDeskStrategyClusterRestRequest();
        request.setId(deskStrategyId);
        request.setStrategyName(dto.getName());
        request.setDesktopType(dto.getPattern());
        request.setSystemDisk(dto.getSystemSize());
        request.setUsbTypeIdArr(dto.getUsbTypeIdArr());
        request.setUsbTypeNameList(convertToUSBTypeName(dto.getUsbTypeIdArr()));
        request.setEnableUsbReadOnly(dto.getOpenUsbReadOnly());
        request.setEnableOpenDesktopRedirect(dto.getOpenDesktopRedirect());
        request.setEnableAllowLocalDisk(dto.getAllowLocalDisk());
        request.setStrategyType(CbbStrategyType.IDV);
        request.setComputerName(obtainComputerName(dto.getId()));
        request.setAdOu(dto.getAdOu());
        request.setEnableNested(dto.getEnableNested());
        // 系统盘自动扩容
        request.setEnableFullSystemDisk(dto.getEnableFullSystemDisk());
        request.setKeyboardEmulationType(dto.getKeyboardEmulationType());
        request.setDesktopOccupyDriveArr(dto.getDesktopOccupyDriveArr());
        // 水印配置
        request.setWatermarkInfo(dto.getWatermarkInfo());
        request.setEnableWatermark(dto.getEnableWatermark());
        // 集群信息
        UnifiedManageDataEntity manageDataEntity =
                unifiedManageDataDAO.findByRelatedIdAndRelatedType(deskStrategyId, UnifiedManageFunctionKeyEnum.DESK_STRATEGY);
        if (Objects.nonNull(manageDataEntity)) {
            request.setUnifiedManageDataId(manageDataEntity.getUnifiedManageDataId());
        }
        request.setNote(dto.getNote());
        request.setUsbStorageDeviceAcceleration(dto.getUsbStorageDeviceAcceleration());
        return request;
    }

    private RccmDeskStrategyClusterRestRequest getVOIDeskStrategyRequest(UUID deskStrategyId) throws BusinessException {
        CbbDeskStrategyVOIDTO dto = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOI(deskStrategyId);
        RccmDeskStrategyClusterRestRequest request = new RccmDeskStrategyClusterRestRequest();
        request.setId(deskStrategyId);
        request.setStrategyName(dto.getName());
        request.setDesktopType(dto.getPattern());
        request.setSystemDisk(dto.getSystemSize());
        request.setUsbTypeIdArr(dto.getUsbTypeIdArr());
        request.setUsbTypeNameList(convertToUSBTypeName(dto.getUsbTypeIdArr()));
        request.setEnableUsbReadOnly(dto.getOpenUsbReadOnly());
        request.setEnableOpenDesktopRedirect(dto.getOpenDesktopRedirect());
        request.setEnableAllowLocalDisk(dto.getAllowLocalDisk());
        request.setStrategyType(CbbStrategyType.VOI);
        request.setComputerName(obtainComputerName(dto.getId()));
        request.setEnableNested(dto.getEnableNested());
        request.setAdOu(dto.getAdOu());

        // 系统盘自动扩容
        request.setEnableFullSystemDisk(dto.getEnableFullSystemDisk());
        request.setDesktopOccupyDriveArr(dto.getDesktopOccupyDriveArr());
        // 水印配置
        request.setWatermarkInfo(dto.getWatermarkInfo());
        request.setEnableWatermark(dto.getEnableWatermark());
        // 集群信息
        UnifiedManageDataEntity manageDataEntity =
                unifiedManageDataDAO.findByRelatedIdAndRelatedType(deskStrategyId, UnifiedManageFunctionKeyEnum.DESK_STRATEGY);
        if (Objects.nonNull(manageDataEntity)) {
            request.setUnifiedManageDataId(manageDataEntity.getUnifiedManageDataId());
        }
        request.setNote(dto.getNote());
        return request;
    }

    private <T> UnifiedManageMasterRequest<T> buildUnifiedManageRequest(T reqContent, String url, String action,
                                           UUID unifiedManageId, String unifiedManageName) throws BusinessException {
        UnifiedManageMasterRequest<T> masterRequest = new UnifiedManageMasterRequest<>();
        RccmServerConfigDTO rccmServerConfig = rccmManageService.getRccmServerConfig();
        masterRequest.setClusterId(rccmServerConfig.getClusterId());
        masterRequest.setFunctionSystem(QueryPlatformTypeEnum.RCDC.name());
        masterRequest.setFunctionKey(UnifiedManageFunctionKeyEnum.DESK_STRATEGY.name());
        masterRequest.setFunctionVersion(rccmManageAPI.getVersionInfo().getVersion());
        masterRequest.setUrl(url);
        masterRequest.setAction(action);
        masterRequest.setRequestBody(reqContent);
        masterRequest.setUnifiedManageId(unifiedManageId);
        masterRequest.setUnifiedManageName(unifiedManageName);
        return masterRequest;
    }

    private String obtainComputerName(UUID deskStrategyId) {
        try {
            return cloudDeskComputerNameConfigAPI.findCloudDeskComputerName(deskStrategyId);
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面策略[{}]对应计算机名数据失败", deskStrategyId, e);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 是否需要推送
     *
     * @param deskStrategyId 云桌面策略I的
     * @return true:需要  false:不需要
     */
    private boolean isNeedSendDeskStrategy(UUID deskStrategyId) {
        RccmServerConfigDTO rccmServerConfig = rccmManageService.getRccmServerConfig();
        boolean isNeed = rccmServerConfig != null && rccmServerConfig.hasHealth()
                && rccmManageAPI.isClusterUnifiedManageNeedRefresh(UnifiedManageFunctionKeyEnum.DESK_STRATEGY);
        if (!isNeed && LOGGER.isDebugEnabled()) {
            LOGGER.debug("当前CDC未被RCCM纳管或不是主节点，无需进行策略数据推送，deskStrategyId:{}", deskStrategyId);
        }
        return isNeed;
    }

    @Override
    public UUID getUnifiedManageDataId(UUID relatedId, UnifiedManageFunctionKeyEnum relatedType) {
        Assert.notNull(relatedId, "relatedId not be null");
        Assert.notNull(relatedType, "relatedType not be null");

        return unifiedManageDataDAO.getUnifiedManageDataId(relatedId, relatedType);
    }

    @Override
    public void deleteImageAndRelateData(UUID imageId) {
        Assert.notNull(imageId, "imageId not be null");

        // 删除镜像信息
        UnifiedManageDataRequest deleteImageRequest = new UnifiedManageDataRequest();
        deleteImageRequest.setRelatedId(imageId);
        deleteImageRequest.setRelatedType(UnifiedManageFunctionKeyEnum.IMAGE_TEMPLATE);
        LOGGER.info("删除镜像[{}]统一管理表信息", imageId);
        this.deleteUnifiedManage(deleteImageRequest);

        // 删除镜像快照信息
        List<ImageTemplateSnapshotDTO> imageSnapshotList = cbbImageDiskSnapshotMgmtAPI.findAllSnapshot(imageId);
        List<UUID> snapshotIdList = imageSnapshotList.stream().map(ImageTemplateSnapshotDTO::getId).collect(Collectors.toList());
        for (UUID snapshotId : snapshotIdList) {
            // 删除镜像信息
            UnifiedManageDataRequest deleteSnapshotRequest = new UnifiedManageDataRequest();
            deleteSnapshotRequest.setRelatedId(snapshotId);
            deleteSnapshotRequest.setRelatedType(UnifiedManageFunctionKeyEnum.IMAGE_SNAPSHOT);
            LOGGER.info("删除快照[{}]统一管理表信息", snapshotId);
            this.deleteUnifiedManage(deleteSnapshotRequest);
        }
    }

    @Override
    public boolean existsUnifiedData(UUID relatedId, UnifiedManageFunctionKeyEnum relatedType) {
        Assert.notNull(relatedId, "relatedId not be null");
        Assert.notNull(relatedType, "relatedType not be null");

        return unifiedManageDataDAO.findByRelatedIdAndRelatedType(relatedId, relatedType) != null;
    }


    @Override
    public List<UnifiedManageDataEntity> findByRelatedTypeAndRelatedIdIn(UnifiedManageFunctionKeyEnum relatedType, List<UUID> relatedIdList) {
        Assert.notNull(relatedType, "relatedType is not null");
        Assert.notNull(relatedIdList, "relatedIdList is not null");

        return unifiedManageDataDAO.findByRelatedTypeAndRelatedIdIn(relatedType, relatedIdList);
    }

    private List<String> convertToUSBTypeName(UUID[] idArr) {
        List<CbbUSBTypeDTO> usbTypeDTOList = cbbUSBTypeMgmtAPI.findByIdIn(Arrays.asList(idArr));
        return usbTypeDTOList.stream().map(CbbUSBTypeDTO::getUsbTypeName).collect(Collectors.toList());
    }
}
