package com.ruijie.rcos.rcdc.rco.module.openapi.service.terminal.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGlobalStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskErrorStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.CloudDeskComputerNameConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SystemBusinessMappingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WifiWhitelistAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SystemBusinessMappingDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvCreateTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.IdvTerminalGroupDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.dto.WifiWhitelistDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.request.SaveWifiWhitelistRequest;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.spi.AdminActionMsgSPI;
import com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto.SyncUpgradeConsts;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.MtoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.service.BusinessCommonService;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportDesktopConfig;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportTerminalGroupInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportTerminalInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportTerminalRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.RestErrorCodeMapping;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.terminal.ImportTerminalService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbModifyTerminalDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbShineTerminalBasicInfo;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.11
 *
 * @author linhj
 */
@Service
public class ImportTerminalServiceImpl implements ImportTerminalService {

    private final Logger logger = LoggerFactory.getLogger(ImportTerminalServiceImpl.class);

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private SystemBusinessMappingAPI systemBusinessMappingAPI;

    @Autowired
    private CloudDeskComputerNameConfigAPI cloudDeskComputerNameConfigAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private UserTerminalGroupMgmtAPI userTerminalGroupMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private IacUserMgmtAPI userAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private BusinessCommonService businessCommonService;

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Autowired
    private WifiWhitelistAPI wifiWhitelistAPI;

    @Autowired
    private CbbGlobalStrategyMgmtAPI cbbGlobalStrategyMgmtAPI;

    @Autowired
    private AdminActionMsgSPI adminActionMsgSPI;

    // 计算机名称前缀
    private final static String PREFIX_COMPUTER_NAME = "PC";

    // 管理员账号
    private final static String ADMIN_NAME = "admin";

    // 默认未分组旧平台标识
    public final static String DEFAULT_OLD_UNGROUP_ID = "0";

    @Override
    public ImportTerminalInfoDTO validateAndConvertTerminalRequest(ImportTerminalRequest request) throws BusinessException {

        Assert.notNull(request, "request is not null");
        ImportTerminalInfoDTO infoDTO = new ImportTerminalInfoDTO();

        if (request.getIdvTerminalMode() == IdvTerminalModeEnums.UNKNOWN) {
            throw new BusinessException(RestErrorCode.RCDC_CODE_TERMINAL_MODE_ERROR);
        }

        // 终端组判断是否存在
        SystemBusinessMappingDTO terminalGroupMapping = systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                SyncUpgradeConsts.BUSINESS_TYPE_TERMINAL_GROUP, request.getTerminalGroupId().toString());
        if (null == terminalGroupMapping) {
            throw new BusinessException(RestErrorCode.RCDC_CODE_TERMINAL_NOT_EXISTS);
        }

        infoDTO.setOldTerminalGroupId(request.getTerminalGroupId());
        infoDTO.setNewTerminalGroupId(UUID.fromString(terminalGroupMapping.getDestId()));

        // 验证终端组参数
        IdvTerminalGroupDetailResponse terminalGroupDetail;
        try {
            terminalGroupDetail =
                    userTerminalGroupMgmtAPI.idvTerminalGroupDetail(infoDTO.getNewTerminalGroupId());
        } catch (BusinessException ex) {
            throw new BusinessException(RestErrorCode.RCDC_CODE_TERMINAL_NOT_EXISTS, ex);
        }

        // 个性终端
        if (request.getIdvTerminalMode() == IdvTerminalModeEnums.PERSONAL && request.getUserId() != null) {

            if (StringUtils.isEmpty(request.getUserName())) {
                throw new BusinessException(RestErrorCode.RCDC_CODE_TERMINAL_BIND_USER_NOT_FOUND);
            }
            SystemBusinessMappingDTO userMapping = systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                    SyncUpgradeConsts.BUSINESS_TYPE_USER, request.getUserId().toString());
            if (null == userMapping) {
                throw new BusinessException(RestErrorCode.RCDC_CODE_TERMINAL_BIND_USER_NOT_EXISTS);
            }

            // 校验用户属性
            IacUserDetailDTO cbbUserInfoDTO;
            try {
                cbbUserInfoDTO = userAPI.getUserDetail(UUID.fromString(userMapping.getDestId()));
            } catch (BusinessException ex) {
                throw new BusinessException(RestErrorCode.RCDC_RCO_TERMINAL_USER_NOT_FOUND, ex);
            }
            if (cbbUserInfoDTO.getUserState() == IacUserStateEnum.DISABLE) {
                throw new BusinessException(RestErrorCode.RCDC_RCO_TERMINAL_USER_DISABLE);
            }

            infoDTO.setOldUserId(request.getUserId());
            infoDTO.setNewUserId(UUID.fromString(userMapping.getDestId()));

            // 获取用户特性
            UserDesktopConfigDTO userDesktopConfig = userDesktopConfigAPI.getUserDesktopConfig(infoDTO.getNewUserId(), UserCloudDeskTypeEnum.IDV);
            infoDTO.setNewStrategyId(Optional.ofNullable(userDesktopConfig).map(UserDesktopConfigDTO::getStrategyId).orElse(null));
            infoDTO.setNewImageId(Optional.ofNullable(userDesktopConfig).map(UserDesktopConfigDTO::getImageTemplateId).orElse(null));

            // 判断是否绑定过
            TerminalDTO terminalDTO = userTerminalMgmtAPI.findByTerminalIdAndBindUserId(request.getTerminalMac(), cbbUserInfoDTO.getId());
            if (null != terminalDTO) {
                throw new BusinessException(RestErrorCode.RCDC_RCO_TERMINAL_USER_BINDED);
            }
        }

        // 公共终端
        if (request.getIdvTerminalMode() == IdvTerminalModeEnums.PUBLIC) {

            infoDTO.setNewStrategyId(terminalGroupDetail.getIdvDesktopStrategyId());
            infoDTO.setNewImageId(terminalGroupDetail.getIdvDesktopImageId());

            // 判断是否绑定过
            TerminalDTO terminalDTO = userTerminalMgmtAPI.findByTerminalId(request.getTerminalMac());
            if (null != terminalDTO && null != terminalDTO.getBindDeskId()) {
                throw new BusinessException(RestErrorCode.RCDC_RCO_TERMINAL_EXISTS);
            }
        }

        return infoDTO;
    }

    @Override
    public ImportTerminalGroupInfoDTO validateAndConvertTerminalGroupRequest(ImportTerminalGroupRequest request) throws BusinessException {

        Assert.notNull(request, "request is not null");

        // 验证 wifi 白名单
        if (request.getSsidArr() != null) {
            if (!CollectionUtils.isEmpty(Arrays.asList(request.getSsidArr())) && request.getSsidArr().length > 3) {
                throw new BusinessException(BusinessKey.RCDC_RCO_WIFI_GT_FOUR);
            }
            if (!CollectionUtils.isEmpty(Arrays.asList(request.getSsidArr()))) {
                int distinctSize = (int) Arrays.stream(request.getSsidArr()).distinct().count();
                if (distinctSize != request.getSsidArr().length) {
                    throw new BusinessException(BusinessKey.RCDC_RCO_WIFI_DUPLICATE);
                }
            }
        }

        if (request.getParentGroup() != null) {
            if (Objects.equals(request.getParentGroup(), request.getId())) {
                throw new BusinessException(RestErrorCode.RCDC_CODE_TERMINAL_NOT_EXISTS);
            }
        }

        // 封装参数
        SystemBusinessMappingDTO systemBusinessMappingDTO = systemBusinessMappingAPI.findSystemBusinessMapping(
                SyncUpgradeConsts.SYSTEM_TYPE_MTOOL, SyncUpgradeConsts.BUSINESS_TYPE_TERMINAL_GROUP, request.getId().toString());

        // 未分组则直接使用默认标识，并且不支持修改名称，不支持修改父分组
        if (ImportTerminalServiceImpl.DEFAULT_OLD_UNGROUP_ID.equals(request.getId().toString())) {
            CbbTerminalGroupDetailDTO terminalGroupDTO = cbbTerminalGroupMgmtAPI.loadById(CbbTerminalGroupMgmtAPI.DEFAULT_TERMINAL_GROUP_ID);
            request.setName(terminalGroupDTO.getGroupName());
            request.setParentGroup(null);
            return new ImportTerminalGroupInfoDTO(systemBusinessMappingDTO, CbbTerminalGroupMgmtAPI.DEFAULT_TERMINAL_GROUP_ID);
        }

        // 未获取到关联数据则表示尚未导入过
        ImportTerminalGroupInfoDTO importTerminalGroupInfoDTO = new ImportTerminalGroupInfoDTO(systemBusinessMappingDTO);
        if (systemBusinessMappingDTO == null || StringUtils.isEmpty(systemBusinessMappingDTO.getDestId())) {
            // 处理迁移数据范围的重入
            dealDefaultImported(request, importTerminalGroupInfoDTO);
            return importTerminalGroupInfoDTO;
        }

        if (logger.isInfoEnabled()) {
            logger.info("判断是否存在已导入数据：{}", JSON.toJSONString(systemBusinessMappingDTO));
        }

        // 验证终端组参数
        IdvTerminalGroupDetailResponse terminalGroupDetail;
        try {
            terminalGroupDetail =
                    userTerminalGroupMgmtAPI.idvTerminalGroupDetail(UUID.fromString(systemBusinessMappingDTO.getDestId()));
            importTerminalGroupInfoDTO.setTerminalGroupId(terminalGroupDetail.getId());
            return importTerminalGroupInfoDTO;
        } catch (BusinessException ignore) {
            // 处理迁移数据范围的重入（已经导入一次，之后删除 WEB 数据却未删除映射数据导致的重入）
            dealDefaultImported(request, importTerminalGroupInfoDTO);
            // 未查询到现有配置则表示被删除过，支持继续导入
            return importTerminalGroupInfoDTO;
        }
    }

    // 未导入未建立关联关系的场景下，判断新平台是否已存在映射记录，存在则提示重复
    private void dealDefaultImported(ImportTerminalGroupRequest request, ImportTerminalGroupInfoDTO importTerminalGroupInfoDTO)
            throws BusinessException {

        UUID parentGroupId = null;
        if (request.getParentGroup() != null) {
            SystemBusinessMappingDTO mappingDTO = systemBusinessMappingAPI.findSystemBusinessMapping(
                    SyncUpgradeConsts.SYSTEM_TYPE_MTOOL, SyncUpgradeConsts.BUSINESS_TYPE_TERMINAL_GROUP, request.getParentGroup().toString());
            if (mappingDTO == null) {
                throw new BusinessException(RestErrorCodeMapping.convert(RestErrorCode.RCDC_CODE_TERMINAL_GROUP_NOT_EXISTS));
            }

            parentGroupId = UUID.fromString(mappingDTO.getDestId());
        }

        // 判断是否存在名称重复
        logger.info("查询是否存在相同层级不同标识同名称场景下的数据，{}，{}", parentGroupId, request.getName());
        CbbTerminalGroupDetailDTO groupDetailDTO = cbbTerminalGroupMgmtAPI.getByName(new CbbTerminalGroupDTO(request.getName(), parentGroupId));

        if (groupDetailDTO != null) {
            // 判断当前同名是否属于迁移数据
            List<SystemBusinessMappingDTO> mappingDTOList = systemBusinessMappingAPI.findMappingByDestId(
                    SyncUpgradeConsts.SYSTEM_TYPE_MTOOL, SyncUpgradeConsts.BUSINESS_TYPE_TERMINAL_GROUP, groupDetailDTO.getId().toString());

            // 找到迁移数据中相同名称的分组
            UUID terminalGroupId = mappingDTOList.stream().findFirst().map(mappingDTO -> UUID.fromString(mappingDTO.getDestId())).orElse(null);
            importTerminalGroupInfoDTO.setTerminalGroupId(terminalGroupId);
            logger.info("迁移数据存量终端组标识：{}", terminalGroupId);
            // importTerminalGroupInfoDTO.setSystemBusinessMappingDTO(null);
        }
    }

    // 拼接接口请求参数
    @Override
    public IdvCreateTerminalGroupRequest buildTerminalGroupRequest(ImportTerminalGroupRequest request) throws BusinessException {

        Assert.notNull(request, "request is not null");

        IdvCreateTerminalGroupRequest createTerminalGroupRequest = new IdvCreateTerminalGroupRequest();
        createTerminalGroupRequest.setGroupName(request.getName());

        // 终端组转换
        if (request.getParentGroup() != null && !request.getParentGroup().toString().equals(DEFAULT_OLD_UNGROUP_ID)) {
            SystemBusinessMappingDTO systemBusinessMappingDTO = systemBusinessMappingAPI.findSystemBusinessMapping(
                    SyncUpgradeConsts.SYSTEM_TYPE_MTOOL, SyncUpgradeConsts.BUSINESS_TYPE_TERMINAL_GROUP, request.getParentGroup().toString());
            if (systemBusinessMappingDTO == null) {
                throw new BusinessException(RestErrorCodeMapping.convert(RestErrorCode.RCDC_CODE_TERMINAL_GROUP_NOT_EXISTS));
            }
            createTerminalGroupRequest.setParentGroupId(UUID.fromString(systemBusinessMappingDTO.getDestId()));
        }

        // 终端分组 WIFI 白名单转换
        if (request.getSsidArr() != null && request.getSsidArr().length > 0) {
            List<WifiWhitelistDTO> wifiWhitelistDTOList = new ArrayList<>();
            for (int index = 0; index < request.getSsidArr().length; index++) {
                wifiWhitelistDTOList.add(new WifiWhitelistDTO(request.getSsidArr()[index], index));
            }
            createTerminalGroupRequest.setWifiWhitelistDTOList(wifiWhitelistDTOList);
        }

        // 只要特性存在一个为空，都认为无需开启特性
        boolean enableGroupFeature = request.getImageTemplateId() != null && request.getDesktopConfig() != null;
        logger.info("终端组 [{}] 是否需要开启终端组的特性，enableGroupFeature：{}", request.getId(), enableGroupFeature);

        // 关联镜像
        if (enableGroupFeature && request.getImageTemplateId() != null) {
            SystemBusinessMappingDTO systemBusinessMapping = systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                    SyncUpgradeConsts.BUSINESS_TYPE_IMAGE_TEMPLATE, request.getImageTemplateId().toString());
            if (systemBusinessMapping == null || StringUtils.isEmpty(systemBusinessMapping.getDestId())) {
                throw new BusinessException(RestErrorCodeMapping.convert(RestErrorCode.RCDC_CODE_IMAGE_TEMPLATE_NOT_FOUND));
            }

            UUID imageTemplateId = UUID.fromString(systemBusinessMapping.getDestId());

            CbbImageTemplateDetailDTO imageTemplateDetail;
            try {
                imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateId);
            } catch (Exception ex) {
                throw new BusinessException(RestErrorCodeMapping.convert(RestErrorCode.RCDC_CODE_IMAGE_TEMPLATE_NOT_FOUND), ex);
            }
            if (imageTemplateDetail.getLastRecoveryPointId() == null) {
                throw new BusinessException(RestErrorCodeMapping.convert(RestErrorCode.RCDC_CODE_IMAGE_TEMPLATE_STATUS_ERROR));
            }
            createTerminalGroupRequest.setCbbIdvDesktopImageId(imageTemplateId);
        }

        // 创建 & 关联云桌面策略
        if (enableGroupFeature && request.getDesktopConfig() != null) {
            UUID desktopStrategy = createDesktopStrategy(request);
            createTerminalGroupRequest.setCbbIdvDesktopStrategyId(desktopStrategy);
        }

        return createTerminalGroupRequest;
    }

    @Override
    public void saveTerminalInfo(ImportTerminalRequest request) throws BusinessException {

        Assert.notNull(request, "request is not null");

        CbbTerminalBasicInfoDTO cbbTerminalBasicInfoDTO = null;
        try {
            cbbTerminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalMac());
        } catch (BusinessException ex) {
            logger.debug("查询终端信息异常", ex);
        }

        // 导入公共终端实体
        CbbShineTerminalBasicInfo cbbShineTerminalBasicInfo = new CbbShineTerminalBasicInfo();

        if (cbbTerminalBasicInfoDTO != null) {
            BeanUtils.copyProperties(cbbTerminalBasicInfoDTO, cbbShineTerminalBasicInfo);
        } else {
            cbbShineTerminalBasicInfo.setProductType(request.getProductType());
            cbbShineTerminalBasicInfo.setProductId(request.getProductId());
            cbbShineTerminalBasicInfo.setSerialNumber(request.getSerialNumber());
            cbbShineTerminalBasicInfo.setTerminalOsType("Linux");
            cbbShineTerminalBasicInfo.setAuthMode(CbbTerminalPlatformEnums.IDV);
        }

        cbbShineTerminalBasicInfo.setTerminalId(request.getTerminalMac());
        cbbShineTerminalBasicInfo.setTerminalName(request.getTerminalName());
        cbbShineTerminalBasicInfo.setMacAddr(request.getTerminalMac());
        cbbShineTerminalBasicInfo.setIp(request.getTerminalIp());
        cbbShineTerminalBasicInfo.setCpuType(request.getCpuType());
        cbbShineTerminalBasicInfo.setPlatform(CbbTerminalPlatformEnums.IDV);
        cbbTerminalOperatorAPI.importTerminalInfo(cbbShineTerminalBasicInfo);

        // 导入产品终端实体
        userTerminalMgmtAPI.importUserTerminal(cbbShineTerminalBasicInfo);
    }

    @Override
    public void modifyTerminalGroup(String terminalId, String terminalName, UUID groupId) throws BusinessException {

        Assert.notNull(terminalId, "terminalId is not null");
        Assert.notNull(terminalName, "terminalName is not null");
        Assert.notNull(groupId, "groupId is not null");

        // 修改终端所在分组
        CbbModifyTerminalDTO terminalRequest = new CbbModifyTerminalDTO();
        terminalRequest.setCbbTerminalId(terminalId);
        terminalRequest.setGroupId(groupId);
        terminalRequest.setTerminalName(terminalName);

        logger.info("修改终端所在分组，terminalRequest={}", JSONObject.toJSONString(terminalRequest));
        cbbTerminalOperatorAPI.modifyTerminal(terminalRequest);
    }

    /**
     * 更新白名单的应用配置
     *
     * @param terminalGroupId     终端标识
     * @param needApplyToSubgroup 是否应用子组
     * @param wifiWhiteList       白名单列表
     */
    public void dealWifiWriteList(UUID terminalGroupId, @Nullable Boolean needApplyToSubgroup, @Nullable List<WifiWhitelistDTO> wifiWhiteList) {

        Assert.notNull(terminalGroupId, "terminalGroupId is not null");

        if (!CollectionUtils.isEmpty(wifiWhiteList)) {
            SaveWifiWhitelistRequest saveWifiWhitelistRequest = new SaveWifiWhitelistRequest();
            saveWifiWhitelistRequest.setNeedApplyToSubgroup(Optional.ofNullable(needApplyToSubgroup).orElse(false));
            saveWifiWhitelistRequest.setTerminalGroupId(terminalGroupId);
            saveWifiWhitelistRequest.setWifiWhiteList(wifiWhiteList);
            wifiWhitelistAPI.updateWifiWhitelist(saveWifiWhitelistRequest);
        }
    }

    /**
     * 创建云桌面策略
     *
     * @param request 终端导入 DTO
     * @return 唯一标识
     * @throws BusinessException 业务异常
     */
    private UUID createDesktopStrategy(ImportTerminalGroupRequest request) throws BusinessException {

        if (request.getDesktopConfig() == null) {
            // 理论不会走到
            return null;
        }

        final String strategyNamePrefix = "迁移终端组策略_";
        final String strategyName = strategyNamePrefix + systemBusinessMappingAPI.obtainMappingSequenceVal();

        try {
            ImportDesktopConfig desktopConfig = request.getDesktopConfig();
            CbbDeskStrategyIDVDTO cbbDeskStrategyIDVDTO = new CbbDeskStrategyIDVDTO();
            cbbDeskStrategyIDVDTO.setName(strategyName);
            cbbDeskStrategyIDVDTO.setSystemSize(desktopConfig.getSystemDiskSize());
            cbbDeskStrategyIDVDTO.setPattern(desktopConfig.getPattern());
            cbbDeskStrategyIDVDTO.setAllowLocalDisk(desktopConfig.getAllowLocalDisk());
            cbbDeskStrategyIDVDTO.setAdOu(org.apache.commons.lang3.StringUtils.EMPTY);
            cbbDeskStrategyIDVDTO.setOpenUsbReadOnly(false);
            cbbDeskStrategyIDVDTO.setEnableNested(false);
            // 本地盘和云桌面重定向功能必须同时开启或关闭
            cbbDeskStrategyIDVDTO.setOpenDesktopRedirect(desktopConfig.getOpenDesktopRedirect());
            cbbDeskStrategyIDVDTO.setDeskErrorStrategy(new CbbDeskErrorStrategyDTO());
            cbbDeskStrategyIDVDTO.setDeskPersonalConfigStrategyType(
                    businessCommonService.getCbbDeskPersonalConfigStrategyType(cbbDeskStrategyIDVDTO.getPattern()));

            // 云桌面策略关联外设策略
            if (desktopConfig.getUsbTypeIdArr() != null) {
                UUID[] usbIdArr = Arrays.stream(desktopConfig.getUsbTypeIdArr()).map(ImportDesktopConfig.USBType::getId).toArray(UUID[]::new);
                cbbDeskStrategyIDVDTO.setUsbTypeIdArr(usbIdArr);
            }

            // 创建者登录账号
            cbbDeskStrategyIDVDTO.setCreatorUserName(ADMIN_NAME);

            // 策略重复性判断
            UUID deskStrategyId = businessCommonService.getRepeatDeskStrategy(cbbDeskStrategyIDVDTO, strategyNamePrefix);
            if (deskStrategyId != null) {
                logger.info("终端组 {} 导入，发现已存在相同策略 {}", request.getId(), deskStrategyId);
                return deskStrategyId;
            }

            deskStrategyId = cbbIDVDeskStrategyMgmtAPI.createDeskStrategyIDV(cbbDeskStrategyIDVDTO);
            cloudDeskComputerNameConfigAPI.createCloudDeskComputerNameConfig(PREFIX_COMPUTER_NAME, deskStrategyId);

            // 如果存在应用分发桌面且当前应用分发是隐藏的则去更新应用分发隐藏策略
            if (CbbCloudDeskPattern.APP_LAYER == cbbDeskStrategyIDVDTO.getPattern()
                    && cbbGlobalStrategyMgmtAPI.getAppLayerHidden()) {
                cbbGlobalStrategyMgmtAPI.updateAppLayerHidden();
                // 通知所有管理员退出登录(因为全局策略修订，前端需要刷新)
                adminActionMsgSPI.notifyAllAdminLogout();
            }

            // 记录审计日志
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_STRATEGY_CREATE_SUCCESS_LOG, strategyName);
            return deskStrategyId;

        } catch (BusinessException ex) {
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_STRATEGY_CREATE_FAIL_LOG, ex, strategyName, ex.getI18nMessage());
            throw ex;
        }
    }
}
