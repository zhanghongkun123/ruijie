package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;


import static com.ruijie.rcos.rcdc.rco.module.def.constants.Constants.COMMA;

import java.util.*;
import java.util.stream.Collectors;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaMainStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaStrategyRelationshipDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacDomainConfigDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbDeskAppDiskRestoreAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTargetAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.DeskDiskExpectDetailDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppResourceTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.VgpuUtil;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.common.enums.HostTypeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.AutoLogonAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.PrinterManageServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.autologon.constant.AutoLogonConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.UpmPolicyTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AcpiService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoDeskInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoDeskInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.StartVmAcpiInfo;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;


/**
 * Description: AcpiUtils工具类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/6
 *
 * @author zhiweiHong
 */
@Service
public class AcpiServiceImpl implements AcpiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudDesktopOperateServiceImpl.class);

    private static final String PRINTER_OPEN = "1";

    private static final String PRINTER_CLOSE = "0";

    @Autowired
    private GlobalParameterAPI globalParameterAPI;


    @Autowired
    CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private CbbDeskAppDiskRestoreAPI cbbDeskAppDiskRestoreAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private PrinterManageServiceAPI printerManageServiceAPI;

    @Autowired
    private AutoLogonAPI autoLogonAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private RcoDeskInfoDAO rcoDeskInfoDAO;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbAppSoftwarePackageMgmtAPI appSoftPackageMgmtAPI;

    @Autowired
    private CbbUamAppTestTargetAPI uamAppTestTargetAPI;

    @Autowired
    private RcaHostSessionAPI rcaHostSessionAPI;

    @Autowired
    private UserService userService;

    @Autowired
    private IacAdMgmtAPI iacAdMgmtAPI;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Autowired
    private RcaMainStrategyAPI rcaMainStrategyAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    /**
     * 获取ACPI
     *
     * @param userEntity userEntity
     * @param userDesktopEntity userDesktopEntity
     * @param deskVDI VDI桌面信息
     * @return acpi
     * @throws BusinessException BusinessException
     */
    public String genAcpiPara(@Nullable RcoViewUserEntity userEntity, UserDesktopEntity userDesktopEntity, CbbDeskDTO deskVDI)
            throws BusinessException {
        Assert.notNull(userDesktopEntity, "userDesktopEntity can not be null");
        Assert.notNull(deskVDI, "deskVDI can not be null");

        StartVmAcpiInfo startVmAcpiInfo = new StartVmAcpiInfo();
        // AD域用户和普通用户、LDAP用户传userName和password
        // 池桌面可无用户情况下启动
        // 应用主机，没有和桌面有强绑定关系，需要通过会话信息进行查询，仅为静态单会话主机，才能够在启动时携带自动登录信息
        UUID hostSingleStaticBindUser =
                rcaHostSessionAPI.getHostSingleBindUser(userDesktopEntity.getCbbDesktopId(), RcaEnum.HostSessionBindMode.STATIC);
        if (hostSingleStaticBindUser != null) {
            userEntity = userService.getUserInfoById(hostSingleStaticBindUser);
        }

        IacUserTypeEnum userType;
        userType = null == userEntity ? IacUserTypeEnum.VISITOR : userEntity.getUserType();
        startVmAcpiInfo.setUsertype(userType.name());
        startVmAcpiInfo.setDomainUserType(userType);
        if (userType != IacUserTypeEnum.VISITOR) {
            LOGGER.info("acpi param username : {}", userEntity.getUserName());
            startVmAcpiInfo.setUsername(userEntity.getUserName());
            startVmAcpiInfo.setPassword(userEntity.getPassword());
        }
        CbbDeskDTO deskInfo = queryCloudDesktopService.getDeskInfo(userDesktopEntity.getCbbDesktopId());
        if (userDesktopEntity.getDesktopType() == CbbCloudDeskType.IDV) {
            CbbTerminalBasicInfoDTO terminalResponse = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(userDesktopEntity.getTerminalId());
            startVmAcpiInfo.setTerminalSerialNumber(terminalResponse.getSerialNumber());
            startVmAcpiInfo.setTerminalName(terminalResponse.getTerminalName());
            startVmAcpiInfo.setTerminalIp(terminalResponse.getIp());
            startVmAcpiInfo.setTerminalMode(Constants.ACPI_IDV_TERMINAL_MODE_PERSONAL);
        }

        // 默认关闭自动登录设置
        startVmAcpiInfo.setWinUsername("");
        startVmAcpiInfo.setWinPassword("");
        startVmAcpiInfo.setSetWinName(AutoLogonConstants.WINDOWS_AUTO_SET_NAME_OFF);
        startVmAcpiInfo.setAutoLogon(AutoLogonConstants.WINDOWS_AUTO_LOGON_OFF);
        startVmAcpiInfo.setWinUserGroup(CbbSyncLoginAccountPermissionEnums.DEFAULT.getValue());

        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(userDesktopEntity.getCbbDesktopId());
        if (userDesktopEntity.getDesktopType() == CbbCloudDeskType.VDI) {
            String imageTemplateId = deskInfo.getImageTemplateId().toString();
            CbbDeskStrategyVDIDTO deskStrategyVDI = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(deskInfo.getStrategyId());
            CbbCloudDeskPattern cbbCloudDeskPattern = deskStrategyVDI.getPattern();
            if (cbbCloudDeskPattern == CbbCloudDeskPattern.RECOVERABLE || cbbCloudDeskPattern == CbbCloudDeskPattern.PERSONAL) {
                // 全量克隆,使用版本ID
                String personalConfigDir = imageTemplateId;
                if (deskInfo.getDeskCreateMode() == DeskCreateMode.LINK_CLONE) {
                    CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(deskInfo.getImageTemplateId());
                    // 链接克隆并且是多版本,使用RootImageId
                    if (Objects.nonNull(imageTemplateDetail.getRootImageId())) {
                        personalConfigDir = imageTemplateDetail.getRootImageId().toString();
                    }
                }
                startVmAcpiInfo.setPersonalConfigDir(personalConfigDir);
            }
            String terminalPassword = cbbTerminalOperatorAPI.queryPassword();
            startVmAcpiInfo.setTermPassword(terminalPassword);
            // 设置镜像ID
            startVmAcpiInfo.setImageTemplateId(imageTemplateId);

            CbbOsType osType = null;
            // 全量克隆存在镜像被删除的情况
            if (deskInfo.getDeskCreateMode() == DeskCreateMode.FULL_CLONE) {
                osType = deskInfo.getOsType();
                // 云应用没有全量克隆
                startVmAcpiInfo.setIsRca(CbbBooleanEnums.N.name());
            } else {
                CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(deskInfo.getImageTemplateId());
                osType = imageTemplateDetail.getOsType();
                // 设置应用主机标记
                boolean isAppImageTemplate = imageTemplateDetail.getImageUsage() == ImageUsageTypeEnum.APP;
                startVmAcpiInfo.setIsRca(isAppImageTemplate ? CbbBooleanEnums.Y.name() : CbbBooleanEnums.N.name());
            }

            // 自动登录设置，用户类型增加到除访客和AD之外均支持
            if ((userEntity == null || (IacUserTypeEnum.VISITOR != userType && IacUserTypeEnum.AD != userType))
                    && CbbOsType.isDesktopSyncAccountOS(osType)
                    && CbbDesktopSessionType.SINGLE.equals(cbbDeskDTO.getSessionType())) {
                if (Boolean.TRUE.equals(deskStrategyVDI.getDesktopSyncLoginAccount())) {
                    startVmAcpiInfo.setWinUsername(userEntity == null ? "" : userEntity.getUserName());
                    startVmAcpiInfo.setSetWinName(AutoLogonConstants.WINDOWS_AUTO_SET_NAME_ON);
                }

                if (Boolean.TRUE.equals(deskStrategyVDI.getDesktopSyncLoginPassword())) {
                    startVmAcpiInfo.setWinPassword(userEntity == null ? "" : userEntity.getPassword());
                    startVmAcpiInfo.setAutoLogon(AutoLogonConstants.WINDOWS_AUTO_LOGON_ON);
                }

                startVmAcpiInfo.setWinUserGroup(
                        deskStrategyVDI.getDesktopSyncLoginAccountPermission() == null ? CbbSyncLoginAccountPermissionEnums.DEFAULT.getValue()
                                : deskStrategyVDI.getDesktopSyncLoginAccountPermission().getValue());
            }
            startVmAcpiInfo.setHostBusinessType(HostTypeEnums.CLOUD_DESK.name());
            // 带内带外
            if (deskVDI.getEstProtocolType() != null) {
                startVmAcpiInfo.setConnectType(deskVDI.getEstProtocolType());
            } else {
                startVmAcpiInfo.setConnectType(deskStrategyVDI.getEstProtocolType());
            }
        }

        if (Boolean.parseBoolean(printerManageServiceAPI.getPrinterConfigStatus())) {
            startVmAcpiInfo.setPrinterManager(PRINTER_OPEN);
        } else {
            startVmAcpiInfo.setPrinterManager(PRINTER_CLOSE);
        }
        // AD域用户是否自动登录
        Boolean hasBindDomainPassword =
                userEntity == null || (userEntity.getHasBindDomainPassword() == null ? Boolean.FALSE : userEntity.getHasBindDomainPassword());
        IacDomainConfigDetailDTO iacDomainConfigDetailDTO = iacAdMgmtAPI.getAdConfig();
        Boolean enableAdAutoLogon = iacDomainConfigDetailDTO == null ? Boolean.FALSE : iacDomainConfigDetailDTO.getAdAutoLogon();
        boolean isOpenADAutoLogin = hasBindDomainPassword && enableAdAutoLogon;
        RcaHostDTO rcaHostDTO = getRcaHostDesktop(userDesktopEntity.getCbbDesktopId());
        if (rcaHostDTO != null && rcaHostDTO.getPoolId() != null) {
            try {
                RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(rcaHostDTO.getPoolId());
                if (RcaEnum.HostSessionType.MULTIPLE.equals(appPoolBaseDTO.getSessionType())) {
                    LOGGER.info("桌面[{}]为应用池主机，且为多会话，不执行加域自动登录动作", userDesktopEntity.getCbbDesktopId());
                    isOpenADAutoLogin = false;
                }
            } catch (Exception ex) {
                LOGGER.error("桌面[{}]为应用池主机，获取应用池发生异常", userDesktopEntity.getCbbDesktopId(), ex);
            }
        }
        startVmAcpiInfo.setAdAutoLogon(isOpenADAutoLogin ? AutoLogonConstants.WINDOWS_AUTO_LOGON_ON : AutoLogonConstants.WINDOWS_AUTO_LOGON_OFF);

        // 显卡参数
        appendGpuAcpiParamIfNeeded(deskVDI, startVmAcpiInfo);

        boolean isImageAd = cbbDeskMgmtAPI.imageTemplateRestorePointJoinAd(cbbDeskDTO.getRestorePointId());
        startVmAcpiInfo.setImageAd(isImageAd ? CbbBooleanEnums.Y.name() : CbbBooleanEnums.N.name());
        startVmAcpiInfo.setDesktopPoolType(cbbDeskDTO.getDesktopPoolType());
        if (rcaHostDTO != null) {
            DesktopPoolType hostPoolType = RcaEnum.PoolType.STATIC.equals(rcaHostDTO.getPoolType()) ?
                    DesktopPoolType.STATIC : DesktopPoolType.DYNAMIC;
            LOGGER.info("桌面[{}]为应用池主机，且主机所在池类型为[{}]，更新桌面池类型", userDesktopEntity.getCbbDesktopId(), hostPoolType);
            startVmAcpiInfo.setDesktopPoolType(hostPoolType);
        }
        startVmAcpiInfo.setSessionType(cbbDeskDTO.getSessionType().name());

        // 用户配置策略
        // 如果是云应用主机，使用云应用策略的值
        if (Objects.nonNull(rcaHostDTO) && Objects.nonNull(rcaHostDTO.getPoolId())) {
            RcaMainStrategyDTO rcaMainStrategyDTO = getRcaMainStrategyDTO(rcaHostDTO.getPoolId());
            if (Objects.nonNull(rcaMainStrategyDTO) && RcaEnum.HostSourceType.VDI == rcaMainStrategyDTO.getHostSourceType()) {
                startVmAcpiInfo.setRcaHostPersonalDataRetention(rcaMainStrategyDTO.getPersonalDataRetention());
            }
        }
        RcoDeskInfoEntity rcoDeskInfoEntity = rcoDeskInfoDAO.findByDeskId(userDesktopEntity.getCbbDesktopId());
        Integer upmPolicyEnable = getUpmPolicyEnable(rcoDeskInfoEntity, cbbDeskDTO.getRestorePointId(), userType, cbbDeskDTO.getDesktopPoolType());
        startVmAcpiInfo.setUpmPolicyEnable(upmPolicyEnable);
        DeskDiskExpectDetailDTO deskDiskExpectDetailDTO = appSoftPackageMgmtAPI.getDeskDiskExpectDetail(deskVDI.getDeskId());

        List<String> appDiskList = Optional.ofNullable(deskDiskExpectDetailDTO.getShouldAttachDiskList()).orElse(Collections.emptyList()).stream()
                .map(CbbDeskDiskDTO::getDiskSn).collect(Collectors.toList());
        startVmAcpiInfo.setUamVmMode(obtainUamMode(appDiskList, deskVDI.getDeskId()));
        startVmAcpiInfo.setAppDiskIdArr(String.join(COMMA, appDiskList));
        boolean isDeskAppDiskRestore = cbbDeskAppDiskRestoreAPI.cleanDeskRestoreSign(deskVDI.getDeskId());
        if (isDeskAppDiskRestore) {
            startVmAcpiInfo.setAppDiskRestore(UUID.randomUUID());
        }



        return JSON.toJSONString(startVmAcpiInfo);
    }

    @Override
    public String genAcpiPara(@Nullable IacUserDetailDTO userEntity, UserDesktopEntity userDesktopEntity, CbbDeskDTO deskVDI,
            @Nullable DeskDiskExpectDetailDTO deskDiskExpectDetailDTO) throws BusinessException {
        Assert.notNull(userDesktopEntity, "userDesktopEntity can not be null");
        Assert.notNull(deskVDI, "deskVDI can not be null");

        StartVmAcpiInfo startVmAcpiInfo = new StartVmAcpiInfo();
        // AD域用户和普通用户、LDAP用户传userName和password
        // 池桌面可无用户情况下启动
        // 应用主机，没有和桌面有强绑定关系，需要通过会话信息进行查询，仅为静态单会话主机，才能够在启动时携带自动登录信息(通过web,通过oc启动时会带上用户信息)
        if (userEntity == null) {
            UUID hostSingleStaticBindUser =
                    rcaHostSessionAPI.getHostSingleBindUser(userDesktopEntity.getCbbDesktopId(), RcaEnum.HostSessionBindMode.STATIC);
            if (hostSingleStaticBindUser != null) {
                userEntity = iacUserMgmtAPI.getUserDetail(hostSingleStaticBindUser);
            }
        }


        IacUserTypeEnum userType;
        userType = null == userEntity ? IacUserTypeEnum.VISITOR : userEntity.getUserType();
        startVmAcpiInfo.setUsertype(userType.name());
        startVmAcpiInfo.setDomainUserType(userType);
        if (userType != IacUserTypeEnum.VISITOR) {
            LOGGER.info("acpi param username : {}", userEntity.getUserName());
            startVmAcpiInfo.setUsername(userEntity.getUserName());
            startVmAcpiInfo.setPassword(userEntity.getPassword());
        }
        if (userDesktopEntity.getDesktopType() == CbbCloudDeskType.IDV) {
            CbbTerminalBasicInfoDTO terminalResponse = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(userDesktopEntity.getTerminalId());
            startVmAcpiInfo.setTerminalSerialNumber(terminalResponse.getSerialNumber());
            startVmAcpiInfo.setTerminalName(terminalResponse.getTerminalName());
            startVmAcpiInfo.setTerminalIp(terminalResponse.getIp());
            startVmAcpiInfo.setTerminalMode(Constants.ACPI_IDV_TERMINAL_MODE_PERSONAL);
        }

        // 默认关闭自动登录设置
        startVmAcpiInfo.setWinUsername("");
        startVmAcpiInfo.setWinPassword("");
        startVmAcpiInfo.setSetWinName(AutoLogonConstants.WINDOWS_AUTO_SET_NAME_OFF);
        startVmAcpiInfo.setAutoLogon(AutoLogonConstants.WINDOWS_AUTO_LOGON_OFF);
        startVmAcpiInfo.setWinUserGroup(CbbSyncLoginAccountPermissionEnums.DEFAULT.getValue());

        if (userDesktopEntity.getDesktopType() == CbbCloudDeskType.VDI) {
            String imageTemplateId = deskVDI.getImageTemplateId().toString();
            CbbDeskStrategyVDIDTO deskStrategyVDI = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(deskVDI.getStrategyId());
            CbbCloudDeskPattern cbbCloudDeskPattern = deskStrategyVDI.getPattern();

            CbbOsType osType;
            if (deskVDI.getDeskCreateMode() == DeskCreateMode.LINK_CLONE) {
                CbbImageTemplateDTO cbbImageTemplateDTO = cbbImageTemplateMgmtAPI.getCbbImageTemplateDTO(deskVDI.getImageTemplateId());
                if (cbbCloudDeskPattern == CbbCloudDeskPattern.RECOVERABLE || cbbCloudDeskPattern == CbbCloudDeskPattern.PERSONAL) {
                    String personalConfigDir = imageTemplateId;
                    // 链接克隆并且是多版本,使用RootImageId
                    if (Objects.nonNull(cbbImageTemplateDTO.getRootImageId())) {
                        personalConfigDir = cbbImageTemplateDTO.getRootImageId().toString();
                    }
                    startVmAcpiInfo.setPersonalConfigDir(personalConfigDir);
                }
                osType = cbbImageTemplateDTO.getOsType();
                boolean isAppImageTemplate = cbbImageTemplateDTO.getImageUsage() == ImageUsageTypeEnum.APP;
                startVmAcpiInfo.setIsRca(isAppImageTemplate ? CbbBooleanEnums.Y.name() : CbbBooleanEnums.N.name());
            } else {
                if (cbbCloudDeskPattern == CbbCloudDeskPattern.RECOVERABLE || cbbCloudDeskPattern == CbbCloudDeskPattern.PERSONAL) {
                    // 全量克隆,使用版本ID
                    startVmAcpiInfo.setPersonalConfigDir(imageTemplateId);
                }
                startVmAcpiInfo.setIsRca(CbbBooleanEnums.N.name());
                osType = deskVDI.getOsType();
            }


            String terminalPassword = cbbTerminalOperatorAPI.queryPassword();
            startVmAcpiInfo.setTermPassword(terminalPassword);
            // 设置镜像ID
            startVmAcpiInfo.setImageTemplateId(imageTemplateId);


            // 自动登录设置，用户类型增加到除访客和AD之外均支持
            if ((userEntity == null || (IacUserTypeEnum.VISITOR != userType && IacUserTypeEnum.AD != userType))
                    && CbbOsType.isDesktopSyncAccountOS(osType)
                    && CbbDesktopSessionType.SINGLE.equals(deskVDI.getSessionType())) {
                if (Boolean.TRUE.equals(deskStrategyVDI.getDesktopSyncLoginAccount())) {
                    startVmAcpiInfo.setWinUsername(userEntity == null ? "" : userEntity.getUserName());
                    startVmAcpiInfo.setSetWinName(AutoLogonConstants.WINDOWS_AUTO_SET_NAME_ON);
                }

                if (Boolean.TRUE.equals(deskStrategyVDI.getDesktopSyncLoginPassword())) {
                    startVmAcpiInfo.setWinPassword(userEntity == null ? "" : userEntity.getPassword());
                    startVmAcpiInfo.setAutoLogon(AutoLogonConstants.WINDOWS_AUTO_LOGON_ON);
                }

                startVmAcpiInfo.setWinUserGroup(
                        deskStrategyVDI.getDesktopSyncLoginAccountPermission() == null ? CbbSyncLoginAccountPermissionEnums.DEFAULT.getValue()
                                : deskStrategyVDI.getDesktopSyncLoginAccountPermission().getValue());
            }
            startVmAcpiInfo.setHostBusinessType(HostTypeEnums.CLOUD_DESK.name());
            // 带内带外
            if (deskVDI.getEstProtocolType() != null) {
                startVmAcpiInfo.setConnectType(deskVDI.getEstProtocolType());
            } else {
                startVmAcpiInfo.setConnectType(deskStrategyVDI.getEstProtocolType());
            }
        }

        if (Boolean.parseBoolean(printerManageServiceAPI.getPrinterConfigStatus())) {
            startVmAcpiInfo.setPrinterManager(PRINTER_OPEN);
        } else {
            startVmAcpiInfo.setPrinterManager(PRINTER_CLOSE);
        }
        // AD域用户是否自动登录
        Boolean hasBindDomainPassword =
                userEntity == null || (userEntity.getHasBindDomainPassword() == null ? Boolean.FALSE : userEntity.getHasBindDomainPassword());
        IacDomainConfigDetailDTO iacDomainConfigDetailDTO = iacAdMgmtAPI.getAdConfig();
        Boolean enableAdAutoLogon = iacDomainConfigDetailDTO == null ? Boolean.FALSE : iacDomainConfigDetailDTO.getAdAutoLogon();
        boolean isOpenADAutoLogin = hasBindDomainPassword && enableAdAutoLogon;
        RcaHostDTO rcaHostDTO = getRcaHostDesktop(userDesktopEntity.getCbbDesktopId());
        if (rcaHostDTO != null && rcaHostDTO.getPoolId() != null) {
            try {
                RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(rcaHostDTO.getPoolId());
                if (RcaEnum.HostSessionType.MULTIPLE.equals(appPoolBaseDTO.getSessionType())) {
                    LOGGER.info("桌面[{}]为应用池主机，且为多会话，不执行加域自动登录动作", userDesktopEntity.getCbbDesktopId());
                    isOpenADAutoLogin = false;
                }
            } catch (Exception ex) {
                LOGGER.error("桌面[{}]为应用池主机，获取应用池发生异常", userDesktopEntity.getCbbDesktopId(), ex);
            }
        }
        startVmAcpiInfo.setAdAutoLogon(isOpenADAutoLogin ? AutoLogonConstants.WINDOWS_AUTO_LOGON_ON : AutoLogonConstants.WINDOWS_AUTO_LOGON_OFF);

        // 显卡参数
        appendGpuAcpiParamIfNeeded(deskVDI, startVmAcpiInfo);

        boolean isImageAd = cbbDeskMgmtAPI.imageTemplateRestorePointJoinAd(deskVDI.getRestorePointId());
        startVmAcpiInfo.setImageAd(isImageAd ? CbbBooleanEnums.Y.name() : CbbBooleanEnums.N.name());
        startVmAcpiInfo.setDesktopPoolType(deskVDI.getDesktopPoolType());
        if (rcaHostDTO != null) {
            DesktopPoolType hostPoolType = RcaEnum.PoolType.STATIC.equals(rcaHostDTO.getPoolType()) ?
                    DesktopPoolType.STATIC : DesktopPoolType.DYNAMIC;
            LOGGER.info("桌面[{}]为应用池主机，且主机所在池类型为[{}]，更新桌面池类型", userDesktopEntity.getCbbDesktopId(), hostPoolType);
            startVmAcpiInfo.setDesktopPoolType(hostPoolType);
        }
        startVmAcpiInfo.setSessionType(deskVDI.getSessionType().name());

        // 用户配置策略
        // 如果是云应用主机，使用云应用策略的值
        if (Objects.nonNull(rcaHostDTO) && Objects.nonNull(rcaHostDTO.getPoolId())) {
            RcaMainStrategyDTO rcaMainStrategyDTO = getRcaMainStrategyDTO(rcaHostDTO.getPoolId());
            if (Objects.nonNull(rcaMainStrategyDTO) && RcaEnum.HostSourceType.VDI == rcaMainStrategyDTO.getHostSourceType()) {
                startVmAcpiInfo.setRcaHostPersonalDataRetention(rcaMainStrategyDTO.getPersonalDataRetention());
            }
        }
        RcoDeskInfoEntity rcoDeskInfoEntity = rcoDeskInfoDAO.findByDeskId(userDesktopEntity.getCbbDesktopId());
        Integer upmPolicyEnable =
                getUpmPolicyEnable(rcoDeskInfoEntity, deskVDI.getRestorePointId(), userType, deskVDI.getDesktopPoolType(), isImageAd);
        startVmAcpiInfo.setUpmPolicyEnable(upmPolicyEnable);
        if (deskDiskExpectDetailDTO == null) {
            deskDiskExpectDetailDTO = appSoftPackageMgmtAPI.getDeskDiskExpectDetail(deskVDI.getDeskId());
        }

        List<String> appDiskList = Optional.ofNullable(deskDiskExpectDetailDTO.getShouldAttachDiskList()).orElse(Collections.emptyList()).stream()
                .map(CbbDeskDiskDTO::getDiskSn).collect(Collectors.toList());
        startVmAcpiInfo.setUamVmMode(obtainUamMode(appDiskList, deskVDI.getDeskId()));
        startVmAcpiInfo.setAppDiskIdArr(String.join(COMMA, appDiskList));
        boolean isDeskAppDiskRestore = cbbDeskAppDiskRestoreAPI.cleanDeskRestoreSign(deskVDI.getDeskId());
        if (isDeskAppDiskRestore) {
            startVmAcpiInfo.setAppDiskRestore(UUID.randomUUID());
        }
        return JSON.toJSONString(startVmAcpiInfo);
    }


    private String obtainUamMode(List<String> appDiskList, UUID deskId) {
        boolean isInTest = uamAppTestTargetAPI.existResourceInTest(AppResourceTypeEnum.CLOUD_DESKTOP, deskId);
        if (isInTest) {
            // 测试模式下关机开机，使用3
            return Constants.TEST_UAM_MODE;
        }
        // 没有应用软件盘 默认使用0 有应用软件盘 默认使用1
        return CollectionUtils.isEmpty(appDiskList) ? Constants.NONE_UAM_MODE : Constants.NORMAL_UAM_MODE;
    }

    @Override
    public Integer getUpmPolicyEnable(@Nullable RcoDeskInfoEntity rcoDeskInfoEntity, UUID restorePointId, IacUserTypeEnum userType,
            @Nullable DesktopPoolType desktopPoolType) {
        Assert.notNull(restorePointId, "restorePointId must not be null");
        Assert.notNull(userType, "userType must not be null");
        boolean isImageAd = cbbDeskMgmtAPI.imageTemplateRestorePointJoinAd(restorePointId);
        return getUpmPolicyEnable(rcoDeskInfoEntity, restorePointId, userType, desktopPoolType, isImageAd);
    }


    private Integer getUpmPolicyEnable(@Nullable RcoDeskInfoEntity rcoDeskInfoEntity, UUID restorePointId, IacUserTypeEnum userType,
                                      @Nullable DesktopPoolType desktopPoolType, boolean isImageAd) {
        Assert.notNull(restorePointId, "restorePointId must not be null");
        Assert.notNull(userType, "userType must not be null");

        if (rcoDeskInfoEntity == null || rcoDeskInfoEntity.getUserProfileStrategyId() == null) {
            // 无绑定UPM策略
            return UpmPolicyTypeEnum.UPM_UNABLE_START.getCode();
        }

        if (isImageAd) {
            // 镜像加域
            return UpmPolicyTypeEnum.UPM_MANUAL_START.getCode();
        }

        if (DesktopPoolType.isPoolDesktop(desktopPoolType)) {
            // 池桌面加域
            return UpmPolicyTypeEnum.UPM_MANUAL_START.getCode();
        }

        if (IacUserTypeEnum.AD == userType) {
            // 用户AD域
            return UpmPolicyTypeEnum.UPM_MANUAL_START.getCode();
        }

        return UpmPolicyTypeEnum.UPM_AUTO_START.getCode();
    }

    private void appendGpuAcpiParamIfNeeded(CbbDeskDTO cbbDeskDTO, StartVmAcpiInfo startVmAcpiInfo) throws BusinessException {
        VgpuInfoDTO vgpuInfoDTO = cbbDeskDTO.getVgpuInfoDTO();
        // 参考镜像模块 XQL不设置
        if (!VgpuUtil.isStandardGpuType(vgpuInfoDTO.getVgpuType())) {
            startVmAcpiInfo.setGpu(VgpuUtil.getVgpuTypeDriverType(vgpuInfoDTO));
        }
    }

    private RcaHostDTO getRcaHostDesktop(UUID cbbDeskId) {
        RcaHostDTO rcaHostDTO = null;
        try {
            rcaHostDTO = rcaHostAPI.getById(cbbDeskId);
        } catch (BusinessException e) {
            if (e.getKey().equals(RcaBusinessKey.RCDC_RCA_HOST_NOT_EXISTS)) {
                LOGGER.info("云桌面{}不是派生桌面，不需要处理", cbbDeskId);
            }
        }
        return rcaHostDTO;
    }

    private RcaMainStrategyDTO getRcaMainStrategyDTO(UUID poolId) {
        RcaStrategyRelationshipDTO rcaStrategyRelationshipDTO = new RcaStrategyRelationshipDTO();
        rcaStrategyRelationshipDTO.setPoolId(poolId);
        RcaMainStrategyDTO rcaMainStrategyDTO = null;
        try {
            rcaMainStrategyDTO = rcaMainStrategyAPI.getStrategyConfigDetail(rcaStrategyRelationshipDTO);
        } catch (BusinessException e) {
            LOGGER.warn("派生桌面所属应用池{}的云应用策略获取异常", poolId);
        }
        return rcaMainStrategyDTO;
    }
}
