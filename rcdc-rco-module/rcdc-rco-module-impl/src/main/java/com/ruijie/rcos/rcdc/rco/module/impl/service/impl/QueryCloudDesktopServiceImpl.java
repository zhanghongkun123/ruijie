package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamDeliveryGroupDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVOIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVOIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.VgpuUtil;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfoSupport;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaHostSessionDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.common.query.DefaultConditionQueryRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.api.AppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareControlMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.StoragePoolAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcaHostDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.StrategyHardwareDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.common.DeskStrategyHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolConfigService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewRcaHostDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PublicBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoDeskInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoDeskInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.util.CapacityUnitUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbNetworkCardEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_RCO_USER_ACCOUNT_EXPIRE;
import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID;

/**
 * Description: 产品业务组件云桌面服务接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 *
 * @author chenzj
 */
@Service
public class QueryCloudDesktopServiceImpl implements QueryCloudDesktopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryCloudDesktopServiceImpl.class);

    private static final String DATE_FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";

    public static final long EXPIRE_DATE_ZERO = 0L;

    private static final String IMAGE_TEMPLATE_ID = "imageTemplateId";

    private static final String ID = "id";

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    private RcoDeskInfoDAO rcoDeskInfoDAO;

    @Autowired
    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private DesktopPoolConfigService desktopPoolConfigService;

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    @Autowired
    private AppDeliveryMgmtAPI appDeliveryMgmtAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    @Autowired
    private RcaHostSessionAPI rcaHostSessionAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private StoragePoolAPI storagePoolAPI;

    @Override
    public UserDesktopEntity checkAndFindById(UUID cbbDesktopId) throws BusinessException {
        Assert.notNull(cbbDesktopId, "Param [cbbDesktopId] must not be null");
        UserDesktopEntity userDesktopEntity = userDesktopDAO.findByCbbDesktopId(cbbDesktopId);
        if (userDesktopEntity == null) {
            throw new BusinessException(RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, String.valueOf(cbbDesktopId));
        }
        return userDesktopEntity;
    }

    @Override
    public ViewUserDesktopEntity checkDesktopExistInDeskViewById(UUID cbbDesktopId) throws BusinessException {
        Assert.notNull(cbbDesktopId, "Param [cbbDesktopId] must not be null");

        ViewUserDesktopEntity viewUserDesktopEntity = viewDesktopDetailDAO.findByCbbDesktopId(cbbDesktopId);
        if (viewUserDesktopEntity == null) {
            throw new BusinessException(RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, cbbDesktopId.toString());
        }

        return viewUserDesktopEntity;
    }

    @Override
    public CloudDesktopDetailDTO queryDeskDetail(UUID cbbDesktopId) throws BusinessException {
        Assert.notNull(cbbDesktopId, "Param [id] must not be null");

        ViewUserDesktopEntity viewUserDesktopEntity = viewDesktopDetailDAO.findByCbbDesktopId(cbbDesktopId);
        if (viewUserDesktopEntity == null) {
            LOGGER.error("桌面[{}]不存在", cbbDesktopId);
            throw new BusinessException(RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, cbbDesktopId.toString());
        }
        CloudDesktopDetailDTO dto = new CloudDesktopDetailDTO();
        dto.setId(viewUserDesktopEntity.getCbbDesktopId());
        dto.setDeskType(viewUserDesktopEntity.getDeskType());
        // IDV公用终端桌面名称设置为公用桌面
        if (CbbCloudDeskType.IDV.name().equals(viewUserDesktopEntity.getDeskType()) && StringUtils.isBlank(viewUserDesktopEntity.getDesktopName())) {
            dto.setDesktopName(PublicBusinessKey.DEFAULT_PUBLIC_IDV_DESK_NAME);
        } else {
            dto.setDesktopName(viewUserDesktopEntity.getDesktopName());
        }

        dto.setSessionType(viewUserDesktopEntity.getSessionType());
        dto.setDesktopState(viewUserDesktopEntity.getDeskState());
        // 前端属性和后端定义不一致，前端desktoptype:个性/还原,而用的是pattern, 前端修订量大，暂时不改
        dto.setDesktopType(viewUserDesktopEntity.getPattern());
        dto.setDesktopCategory(viewUserDesktopEntity.getDesktopType());
        dto.setCpu(viewUserDesktopEntity.getCpu());
        if (!ObjectUtils.isEmpty(viewUserDesktopEntity.getMemory())) {
            dto.setMemory(CapacityUnitUtils.mb2GbMaintainOneFraction(viewUserDesktopEntity.getMemory()));
        }
        dto.setSystemDisk(viewUserDesktopEntity.getSystemSize());
        dto.setPersonDisk(viewUserDesktopEntity.getPersonSize());

        dto.setDesktopImageId(viewUserDesktopEntity.getImageTemplateId());
        dto.setRootImageId(viewUserDesktopEntity.getRootImageId());
        dto.setImageRoleType(viewUserDesktopEntity.getImageRoleType());
        dto.setDesktopStrategyId(viewUserDesktopEntity.getCbbStrategyId());
        dto.setDesktopStrategyName(viewUserDesktopEntity.getStrategyName());
        dto.setDesktopNetworkId(viewUserDesktopEntity.getCbbNetworkId());
        dto.setUserId(viewUserDesktopEntity.getUserId());
        dto.setDesktopPoolName(viewUserDesktopEntity.getDesktopPoolName());

        RcoDeskInfoEntity rcoDeskInfo = rcoDeskInfoDAO.findByDeskId(cbbDesktopId);
        if (rcoDeskInfo != null && rcoDeskInfo.getSoftwareStrategyId() != null) {
            try {
                SoftwareStrategyDTO softwareStrategyDTO = softwareControlMgmtAPI.findSoftwareStrategyById(rcoDeskInfo.getSoftwareStrategyId());
                dto.setDesktopSoftwareStrategyId(softwareStrategyDTO.getId());
                dto.setDesktopSoftwareStrategyName(softwareStrategyDTO.getName());
            } catch (BusinessException e) {
                LOGGER.warn("云桌面: {} 对应的软控策略: {} 已被删除", rcoDeskInfo.getDeskId(), rcoDeskInfo.getSoftwareStrategyId());
            }
        }

        if (rcoDeskInfo != null && rcoDeskInfo.getUserProfileStrategyId() != null) {
            try {
                UserProfileStrategyDTO userProfileStrategyDTO =
                        userProfileMgmtAPI.findUserProfileStrategyById(rcoDeskInfo.getUserProfileStrategyId());
                dto.setUserProfileStrategyId(userProfileStrategyDTO.getId());
                dto.setUserProfileStrategyName(userProfileStrategyDTO.getName());
            } catch (BusinessException e) {
                LOGGER.warn("云桌面: {} 对应的用户配置策略: {} 已被删除", rcoDeskInfo.getDeskId(), rcoDeskInfo.getUserProfileStrategyId());
            }
        }

        // 无用户名情况，如公用终端，名称设置为--
        if (StringUtils.isBlank(viewUserDesktopEntity.getUserName())) {
            dto.setUserName(PublicBusinessKey.DEFAULT_EMPTY_USERNAME);
        } else {
            dto.setUserName(viewUserDesktopEntity.getUserName());
        }
        dto.setUserRealName(viewUserDesktopEntity.getRealName());
        dto.setUserGroupId(viewUserDesktopEntity.getUserGroupId());
        dto.setUserGroupName(viewUserDesktopEntity.getUserGroupName());
        dto.setUserCreateTime(viewUserDesktopEntity.getUserCreateTime());
        dto.setDesktopRole(DesktopRole.valueOf(viewUserDesktopEntity.getDesktopRole()));
        dto.setServerName(viewUserDesktopEntity.getHostName());
        dto.setPhysicalServerIp(viewUserDesktopEntity.getPhysicalServerIp());
        if (viewUserDesktopEntity.getUserId() != null) {
            String[] userGroupNameArr = userService.getUserGroupNameArr(viewUserDesktopEntity.getUserId());
            dto.setUserGroupNameArr(userGroupNameArr);
        }

        dto.setTerminalId(viewUserDesktopEntity.getTerminalId());
        dto.setTerminalName(viewUserDesktopEntity.getTerminalName());
        dto.setTerminalPlatform(viewUserDesktopEntity.getTerminalPlatform());
        dto.setTerminalGroupName(viewUserDesktopEntity.getTerminalGroupName());
        if (viewUserDesktopEntity.getTerminalGroupId() != null) {
            String[] terminalGroupNameArr = getTerminalGroupNameArr(viewUserDesktopEntity.getTerminalGroupId());
            dto.setTerminalGroupNameArr(terminalGroupNameArr);
        }
        dto.setTerminalIp(viewUserDesktopEntity.getTerminalIp());
        dto.setTerminalMask(viewUserDesktopEntity.getTerminalMask());
        dto.setLastOnlineTime(viewUserDesktopEntity.getLastOnlineTime());
        dto.setCreateTime(viewUserDesktopEntity.getCreateTime());
        dto.setUserType(viewUserDesktopEntity.getUserType());
        dto.setConfigIp(viewUserDesktopEntity.getConfigIp());
        dto.setLatestLoginTime(viewUserDesktopEntity.getLatestLoginTime());
        // 云桌面是否激活，可能存在是否激活信息为空的情况
        Boolean isWindowsOsActive =
                viewUserDesktopEntity.getIsWindowsOsActive() != null ? viewUserDesktopEntity.getIsWindowsOsActive() : Boolean.FALSE;
        dto.setIsWindowsOsActive(isWindowsOsActive);
        dto.setOsActiveBySystem(Optional.ofNullable(viewUserDesktopEntity.getOsActiveBySystem()).orElse(Boolean.FALSE));
        dto.setTerminalMac(viewUserDesktopEntity.getTerminalMac());
        if (!CbbDesktopPoolType.THIRD.name().equals(viewUserDesktopEntity.getDeskType())) {
            fillDesktopNetworkName(viewUserDesktopEntity, dto);
            // 填充镜像类型和镜像名称字段
            fillImageTypeAndImageName(viewUserDesktopEntity, dto);
        } else {
            if (StringUtils.isNotBlank(viewUserDesktopEntity.getOsType())) {
                dto.setDesktopImageType(CbbOsType.valueOf(viewUserDesktopEntity.getOsType()));
            }
        }
        fillDesktopStrategyName(viewUserDesktopEntity, dto);

        dto.setIdvTerminalModel(viewUserDesktopEntity.getIdvTerminalModel());
        dto.setComputerName(viewUserDesktopEntity.getComputerName());
        dto.setEnableCustom(viewUserDesktopEntity.getEnableCustom());
        // 新增镜像类型
        dto.setCbbImageType(viewUserDesktopEntity.getCbbImageType());
        dto.setStrategyType(viewUserDesktopEntity.getStrategyType());
        if (CbbCloudDeskType.VDI.name().equals(viewUserDesktopEntity.getDeskType())
                || CbbCloudDeskType.THIRD.name().equals(viewUserDesktopEntity.getDeskType())) {
            dto.setDesktopIp(viewUserDesktopEntity.getIp());
            dto.setDesktopMac(viewUserDesktopEntity.getDeskMac());
        }
        Consumer<CbbDeskNetworkInfoDTO> wireNetworkConfigConsumer = networkInfoDTO -> {
            dto.setWirelessIp(networkInfoDTO.getIp());
            dto.setWirelessMacAddr(networkInfoDTO.getMac());
        };
        Consumer<CbbDeskNetworkInfoDTO> networkConfigConsumer = networkInfoDTO -> {
            dto.setDesktopIp(networkInfoDTO.getIp());
            dto.setDesktopMac(networkInfoDTO.getMac());
        };
        setNetWorkInfo(dto.getId(), networkConfigConsumer, wireNetworkConfigConsumer, dto::setNetworkAccessMode);
        // 云桌面标签
        dto.setRemark(viewUserDesktopEntity.getRemark());
        dto.setDeskCreateMode(viewUserDesktopEntity.getDeskCreateMode());
        // 桌面池
        dto.setDesktopPoolType(viewUserDesktopEntity.getDesktopPoolType());
        dto.setDesktopPoolId(viewUserDesktopEntity.getDesktopPoolId());
        // 设置显卡信息
        VgpuExtraInfoSupport vgpuExtraInfoSupport =
                VgpuUtil.deserializeVgpuExtraInfoByType(viewUserDesktopEntity.getVgpuType(), viewUserDesktopEntity.getVgpuExtraInfo());
        dto.setVgpuType(viewUserDesktopEntity.getVgpuType());
        if (vgpuExtraInfoSupport instanceof VgpuExtraInfo) {
            VgpuExtraInfo vgpuExtraInfo = (VgpuExtraInfo) vgpuExtraInfoSupport;
            dto.setVgpuModel(deskSpecAPI.buildDefaultAmdModel(vgpuExtraInfo));
            dto.setVgpuItem(vgpuExtraInfo.getModel());
        }
        // 镜像下载信息
        dto.setDownloadState(viewUserDesktopEntity.getDownloadState());
        dto.setDownloadFinishTime(viewUserDesktopEntity.getDownloadFinishTime());
        dto.setDownloadPromptMessage(DownloadStateEnum.getDownloadPromptMessage(viewUserDesktopEntity.getFailCode()));
        // 代理协议
        dto.setEnableAgreementAgency(viewUserDesktopEntity.getEnableAgreementAgency());
        dto.setEnableWebClient(viewUserDesktopEntity.getEnableWebClient());

        // 设置集群名称，IDV/TCI桌面没有该值
        if (Objects.nonNull(viewUserDesktopEntity.getClusterId())) {
            ClusterInfoDTO clusterInfoDTO = clusterAPI.queryAvailableClusterById(viewUserDesktopEntity.getClusterId());
            if (clusterInfoDTO != null) {
                dto.setClusterName(clusterInfoDTO.getClusterName());
                dto.setClusterInfo(clusterInfoDTO);
            }
        }
        dto.setIsOpenDeskMaintenance(viewUserDesktopEntity.getIsOpenDeskMaintenance());
        // 关联交付组名称
        try {
            List<CbbUamDeliveryGroupDTO> cbbUamDeliveryGroupDTOList =
                    cbbAppDeliveryMgmtAPI.listByCloudDesktopId(viewUserDesktopEntity.getCbbDesktopId());
            List<String> deliveryGroupNameList =
                    cbbUamDeliveryGroupDTOList.stream().map(CbbUamDeliveryGroupDTO::getDeliveryGroupName).collect(Collectors.toList());

            List<UUID> deliveryGroupIdList =
                    cbbUamDeliveryGroupDTOList.stream().map(CbbUamDeliveryGroupDTO::getId).collect(Collectors.toList());
            dto.setDeliveryGroupName(StringUtils.joinWith(Constants.COMMA_SEPARATION_CHARACTER, deliveryGroupNameList.toArray(new String[0])));

            List<String> appNameList = appDeliveryMgmtAPI.findAppNameListByGroupId(deliveryGroupIdList);
            dto.setDeliveryGroupAppArrName(StringUtils.joinWith(Constants.COMMA_SEPARATION_CHARACTER, appNameList.toArray(new String[0])));

        } catch (Exception e) {
            LOGGER.debug("云桌面未被交付组使用，则忽略,错误信息：{}", e.getMessage());
        }
        if (StringUtils.equals(CbbImageType.IDV.name(), viewUserDesktopEntity.getCbbImageType())
                || StringUtils.equals(CbbImageType.VOI.name(), viewUserDesktopEntity.getCbbImageType())) {
            dto.setImageDiskList(cbbImageTemplateMgmtAPI.getAvailableImageDiskInfoList(viewUserDesktopEntity.getImageTemplateId()));
        }
        dto.setOsType(viewUserDesktopEntity.getOsType());
        dto.setOsVersion(viewUserDesktopEntity.getOsVersion());
        dto.setShowRootPwd(viewUserDesktopEntity.getShowRootPwd());
        dto.setEstProtocolType(viewUserDesktopEntity.getEstProtocolType());
        dto.setStrategyEnableWatermark(viewUserDesktopEntity.getStrategyEnableWatermark());
        // 云平台相关
        dto.setPlatformId(viewUserDesktopEntity.getPlatformId());
        dto.setPlatformName(viewUserDesktopEntity.getPlatformName());
        dto.setPlatformStatus(viewUserDesktopEntity.getPlatformStatus());
        dto.setPlatformType(viewUserDesktopEntity.getPlatformType());
        dto.setCloudPlatformId(viewUserDesktopEntity.getCloudPlatformId());

        // 云应用-镜像类型
        dto.setImageUsage(viewUserDesktopEntity.getImageUsage());
        dto.setEnableAgreementAgency(viewUserDesktopEntity.getEnableAgreementAgency());
        dto.setEnableForceUseAgreementAgency(viewUserDesktopEntity.getEnableForceUseAgreementAgency());

        // 存储池
        fillDesktopDiskStorage(dto);
        dto.setRegisterState(viewUserDesktopEntity.getRegisterState());
        dto.setRegisterMessage(viewUserDesktopEntity.getRegisterMessage());
        dto.setGuestToolVersion(viewUserDesktopEntity.getGuestToolVersion());
        return dto;
    }

    private void fillDesktopDiskStorage(CloudDesktopDetailDTO dto) {
        if (!Objects.equals(CbbCloudDeskType.VDI.name(), dto.getDeskType())) {
            return;
        }
        List<CbbDeskDiskDTO> diskList = cbbVDIDeskDiskAPI.listDeskDisk(dto.getId());
        if (CollectionUtils.isEmpty(diskList)) {
            return;
        }
        Optional<CbbDeskDiskDTO> optionalDisk = diskList.stream().filter(diskDTO -> diskDTO.getType() == CbbDiskType.SYSTEM).findFirst();
        if (optionalDisk.isPresent() && StringUtils.isNotBlank(optionalDisk.get().getAssignStoragePoolIds())) {
            PlatformStoragePoolDTO storagePool = storagePoolAPI.getStoragePoolDetail(UUID.fromString(optionalDisk.get().getAssignStoragePoolIds()));
            dto.setSystemDiskStoragePoolName(storagePool.getName());
            dto.setSystemDiskStoragePool(storagePool);
        }
        optionalDisk = diskList.stream().filter(diskDTO -> diskDTO.getType() == CbbDiskType.PERSONAL).findFirst();
        if (optionalDisk.isPresent() && StringUtils.isNotBlank(optionalDisk.get().getAssignStoragePoolIds())) {
            PlatformStoragePoolDTO storagePool = storagePoolAPI.getStoragePoolDetail(UUID.fromString(optionalDisk.get().getAssignStoragePoolIds()));
            dto.setPersonDiskStoragePoolName(storagePool.getName());
            dto.setPersonDiskStoragePool(storagePool);
        }
    }

    private void fillImageTypeAndImageName(ViewUserDesktopEntity viewUserDesktopEntity, CloudDesktopDetailDTO dto) {
        DeskCreateMode deskCreateMode = DeskCreateMode.valueOf(viewUserDesktopEntity.getDeskCreateMode());
        if (deskCreateMode == DeskCreateMode.FULL_CLONE) {
            dto.setDesktopImageType(CbbOsType.valueOf(viewUserDesktopEntity.getOsType()));
        } else {
            CbbImageTemplateDetailDTO imageDto = cbbImageTemplateMgmtAPI.getImageTemplateDetail(viewUserDesktopEntity.getImageTemplateId());
            dto.setDesktopImageName(imageDto.getImageName());
            dto.setDesktopImageType(imageDto.getOsType());
            dto.setRootImageName(imageDto.getRootImageName());
        }
    }

    private void fillDesktopNetworkName(ViewUserDesktopEntity viewUserDesktopEntity, CloudDesktopDetailDTO dto) {

        if (CbbCloudDeskType.IDV.name().equals(dto.getDesktopCategory())) {
            LOGGER.debug("IDV桌面无网络策略");
            return;
        }

        try {
            CbbDeskNetworkDetailDTO netDto = cbbNetworkMgmtAPI.getDeskNetwork(viewUserDesktopEntity.getCbbNetworkId());
            dto.setDesktopNetworkName(netDto.getDeskNetworkName());
        } catch (BusinessException e) {
            LOGGER.error("query cbb network info fail. id=" + viewUserDesktopEntity.getCbbDesktopId() + ", cbb network id="
                    + viewUserDesktopEntity.getCbbNetworkId(), e);
        }
    }

    /**
     * 根据桌面类型进行填充桌面策略
     *
     * @param viewUserDesktopEntity ViewUserDesktopEntity
     * @param dto                   CloudDesktopDetailDTO
     * @throws BusinessException 业务异常
     */
    private void fillDesktopStrategyName(ViewUserDesktopEntity viewUserDesktopEntity, CloudDesktopDetailDTO dto) throws BusinessException {
        fillIDVDesktopStrategyName(viewUserDesktopEntity, dto);
        fillVDIDesktopStrategyName(viewUserDesktopEntity, dto);
    }

    private void fillIDVDesktopStrategyName(ViewUserDesktopEntity viewUserDesktopEntity, CloudDesktopDetailDTO dto) {
        CbbCloudDeskType cbbCloudDeskType = CbbCloudDeskType.valueOf(dto.getDesktopCategory());
        if (cbbCloudDeskType != CbbCloudDeskType.IDV) {
            return;
        }
        try {
            // 云桌面类型IDV没有区分VOI，所以要再次判断
            if (CbbImageType.VOI.toString().equals(viewUserDesktopEntity.getCbbImageType())) {
                CbbDeskStrategyVOIDTO strategyVOIDTO = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOI(viewUserDesktopEntity.getCbbStrategyId());
                dto.setDesktopStrategyName(strategyVOIDTO.getName());
            } else {
                CbbDeskStrategyIDVDTO strategyIDVDTO = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(viewUserDesktopEntity.getCbbStrategyId());
                dto.setDesktopStrategyName(strategyIDVDTO.getName());
            }
        } catch (BusinessException e) {
            LOGGER.error("query cbb strategy info fail. id=" + viewUserDesktopEntity.getCbbDesktopId() + ", cbb strategy id="
                    + viewUserDesktopEntity.getCbbStrategyId(), e);
        }
    }

    private void fillVDIDesktopStrategyName(ViewUserDesktopEntity viewUserDesktopEntity, CloudDesktopDetailDTO dto) {
        CbbCloudDeskType cbbCloudDeskType = CbbCloudDeskType.valueOf(dto.getDesktopCategory());
        if (cbbCloudDeskType != CbbCloudDeskType.VDI) {
            return;
        }
        try {
            CbbDeskStrategyVDIDTO strategyDto = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(viewUserDesktopEntity.getCbbStrategyId());
            dto.setDesktopStrategyName(strategyDto.getName());
        } catch (BusinessException e) {
            LOGGER.error("query cbb strategy info fail. id=" + viewUserDesktopEntity.getCbbDesktopId() + ", cbb strategy id="
                    + viewUserDesktopEntity.getCbbStrategyId(), e);
        }
    }

    private String[] getTerminalGroupNameArr(UUID groupId) throws BusinessException {
        CbbTerminalGroupDetailDTO groupDTO = getTerminalGroup(groupId);
        List<String> groupNameList = new ArrayList<>();
        groupNameList.add(groupDTO.getGroupName());
        UUID parentId = groupDTO.getParentGroupId();
        while (parentId != null) {
            groupDTO = getTerminalGroup(parentId);
            groupNameList.add(groupDTO.getGroupName());
            parentId = groupDTO.getParentGroupId();
        }
        String[] groupNameArr = new String[groupNameList.size()];
        Collections.reverse(groupNameList);
        groupNameList.toArray(groupNameArr);
        return groupNameArr;
    }

    private CbbTerminalGroupDetailDTO getTerminalGroup(UUID groupId) throws BusinessException {

        return cbbTerminalGroupMgmtAPI.loadById(groupId);
    }

    @Override
    public DefaultPageResponse<CloudDesktopDTO> convertPageInfoAndQuery(Page<ViewUserDesktopEntity> page) throws BusinessException {
        Assert.notNull(page, "Param [page] must not be null");
        if (page.getTotalElements() == 0) {
            DefaultPageResponse<CloudDesktopDTO> response = new DefaultPageResponse<>();
            response.setTotal(0);
            response.setItemArr(new CloudDesktopDTO[0]);
            return response;
        }
        DefaultPageResponse<CloudDesktopDTO> resp = new DefaultPageResponse<>();
        List<ViewUserDesktopEntity> viewList = page.getContent();
        List<CloudDesktopDTO> cloudDesktopList = convertCloudDesktopForRecovery(viewList);
        CloudDesktopDTO[] desktopArr = cloudDesktopList.toArray(new CloudDesktopDTO[cloudDesktopList.size()]);
        resp.setItemArr(desktopArr);
        resp.setTotal(page.getTotalElements());
        return resp;
    }

    @Override
    public DefaultPageResponse<RcaHostDesktopDTO> convertRcaHostPageInfoList(Page<ViewRcaHostDesktopEntity> page) throws BusinessException {
        Assert.notNull(page, "Param [page] must not be null");
        if (page.getTotalElements() == 0) {
            DefaultPageResponse<RcaHostDesktopDTO> response = new DefaultPageResponse<>();
            response.setTotal(0);
            response.setItemArr(new RcaHostDesktopDTO[0]);
            return response;
        }

        DefaultPageResponse<RcaHostDesktopDTO> resp = new DefaultPageResponse<>();
        List<ViewRcaHostDesktopEntity> rcaHostViewList = page.getContent();
        // 将rca的entity列表转为rco的list
        List<ViewUserDesktopEntity> viewList = convertToCloudDesktopList(rcaHostViewList);
        List<CloudDesktopDTO> cloudDesktopList = convertCloudDesktopForRecovery(viewList);
        // 将rco的dto列表转为rca的dto列表
        List<RcaHostDesktopDTO> rcaHostDesktopDTOList =
                convertToRcaHostDesktopList(cloudDesktopList, rcaHostViewList);

        RcaHostDesktopDTO[] rcaDesktopArr = rcaHostDesktopDTOList.toArray(new RcaHostDesktopDTO[rcaHostDesktopDTOList.size()]);
        resp.setItemArr(rcaDesktopArr);
        resp.setTotal(page.getTotalElements());
        return resp;
    }

    /**
     * @param viewList viewList
     * @return CloudDesktopDTO
     * @throws BusinessException BusinessException
     */
    @Override
    public List<CloudDesktopDTO> convertCloudDesktop(List<ViewUserDesktopEntity> viewList) throws BusinessException {
        Assert.notNull(viewList, "viewList can not be null");

        List<CloudDesktopDTO> resultList = new ArrayList<>();
        Map<UUID, CbbDesktopPoolDTO> desktopPoolMap = new HashMap<>();
        Map<UUID, UUID> poolSoftStrategyIdMap = new HashMap<>();
        // 获取全部计算集群信息
        Map<UUID, ClusterInfoDTO> clusterInfoAllMap = clusterAPI.queryAllClusterInfoList().stream()
                .collect(Collectors.toMap(ClusterInfoDTO::getId, clusterInfoDTO -> clusterInfoDTO));
        for (ViewUserDesktopEntity userDesktopEntity : viewList) {
            CloudDesktopDTO cloudDesktop = ViewUserDesktopEntity.convertEntityToDTO(userDesktopEntity);
            cloudDesktop.setNeedRefreshStrategy(checkNeedRefreshStrategy(userDesktopEntity));
            // 添加GPU大小值
            cloudDesktop.setVgpuModel(convertDesktopVgpu(userDesktopEntity.getVgpuType(), userDesktopEntity.getVgpuExtraInfo()));

            // 设置集群名称
            if (Objects.nonNull(userDesktopEntity.getClusterId())) {
                ClusterInfoDTO clusterInfoDTO = clusterInfoAllMap.get(userDesktopEntity.getClusterId());
                if (clusterInfoDTO != null) {
                    cloudDesktop.setClusterName(clusterInfoDTO.getClusterName());
                }
            }

            cloudDesktop.setPoolStrategyError(checkDesktopPoolStrategyError(desktopPoolMap, poolSoftStrategyIdMap, userDesktopEntity));
            Consumer<CbbDeskNetworkInfoDTO> wireNetworkConfigConsumer = networkInfoDTO -> {
                cloudDesktop.setWirelessIp(networkInfoDTO.getIp());
                cloudDesktop.setWirelessMacAddr(networkInfoDTO.getMac());
                cloudDesktop.setAutoWirelessDhcp(networkInfoDTO.getAutoDhcp());
                cloudDesktop.setAutoWirelessDns(networkInfoDTO.getAutoDns());
                cloudDesktop.setWirelessGateway(networkInfoDTO.getGateWay());
                cloudDesktop.setWirelessMask(networkInfoDTO.getMask());
                cloudDesktop.setWirelessDnsPrimary(networkInfoDTO.getDnsPrimary());
                cloudDesktop.setWirelessSecondDnsPrimary(networkInfoDTO.getDnsSecondary());
            };
            Consumer<CbbDeskNetworkInfoDTO> networkConfigConsumer = networkInfoDTO -> {
                cloudDesktop.setDesktopIp(networkInfoDTO.getIp());
                cloudDesktop.setDesktopMac(networkInfoDTO.getMac());
                cloudDesktop.setAutoDeskDhcp(networkInfoDTO.getAutoDhcp());
                cloudDesktop.setAutoDeskDns(networkInfoDTO.getAutoDns());
                cloudDesktop.setDeskGateway(networkInfoDTO.getGateWay());
                cloudDesktop.setDeskMask(networkInfoDTO.getMask());
                cloudDesktop.setDeskDnsPrimary(networkInfoDTO.getDnsPrimary());
                cloudDesktop.setDeskSecondDnsPrimary(networkInfoDTO.getDnsSecondary());
            };
            setNetWorkInfo(cloudDesktop.getId(), networkConfigConsumer, wireNetworkConfigConsumer, cloudDesktop::setNetworkAccessMode);
            resultList.add(cloudDesktop);
        }
        return resultList;
    }

    /**
     * 设置网络信息
     *
     * @param deskId                  桌面Id
     * @param wiredCallback           有线回调
     * @param wirelessCallback        无线回调
     * @param setNetworkModelCallback 网络类型设置回调
     * @throws BusinessException
     */
    private void setNetWorkInfo(UUID deskId, Consumer<CbbDeskNetworkInfoDTO> wiredCallback, Consumer<CbbDeskNetworkInfoDTO> wirelessCallback,
                                Consumer<CbbNetworkAccessModeEnums> setNetworkModelCallback) throws BusinessException {
        CbbDeskDTO deskDTO = cbbIDVDeskMgmtAPI.getDeskIDV(deskId);
        if (Objects.isNull(deskDTO) || Objects.isNull(deskDTO.getNetworkInfoArr())) {
            return;
        }
        CbbDeskNetworkInfoDTO[] networkInfoArr = deskDTO.getNetworkInfoArr();
        if (Objects.isNull(networkInfoArr)) {
            return;
        }
        boolean isWired = false;
        boolean isWireless = false;
        boolean hasSetMainCardInfo = false;
        for (CbbDeskNetworkInfoDTO networkInfoDTO : networkInfoArr) {
            CbbNetworkAccessModeEnums networkAccessMode = networkInfoDTO.getNetworkAccessMode();
            switch (networkAccessMode) {
                case WIRED:
                    if (!hasSetMainCardInfo) {
                        hasSetMainCardInfo = (networkInfoDTO.getBusinessCard() == CbbNetworkCardEnums.MAIN_NETCARD);
                        wiredCallback.accept(networkInfoDTO);
                        isWired = true;
                    }
                    break;
                case WIRELESS:
                    wirelessCallback.accept(networkInfoDTO);
                    isWireless = true;
                    break;
                default:
            }
        }
        CbbNetworkAccessModeEnums currentNetworkAccessMode = null;
        if (Boolean.TRUE.equals(isWired)) {
            // 只有连接了有线，均认为当前网络连接模式为有线连接
            currentNetworkAccessMode = CbbNetworkAccessModeEnums.WIRED;
        }
        if (Boolean.FALSE.equals(isWired) && Boolean.TRUE.equals(isWireless)) {
            // 只有当有线断开无线连接时，才认为连接模式为无线连接
            currentNetworkAccessMode = CbbNetworkAccessModeEnums.WIRELESS;
        }
        setNetworkModelCallback.accept(currentNetworkAccessMode);
    }

    @Override
    public CbbCloudDeskState queryState(UUID cbbDesktopId) throws BusinessException {
        Assert.notNull(cbbDesktopId, "id must not be null.");

        CbbDeskDTO deskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(cbbDesktopId);
        if (deskDTO == null) {
            throw new BusinessException(RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, cbbDesktopId.toString());
        }
        return deskDTO.getDeskState();
    }

    @Override
    public CbbDeskStrategyVDIDTO checkAndGetVDIStrategyById(UUID id) throws BusinessException {
        Assert.notNull(id, "id can not null");
        CbbDeskStrategyVDIDTO dto = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(id);
        if (dto == null) {
            LOGGER.warn("不存在vdi云桌面策略id：[" + id + "]");
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_VDI_NOT_FOUND, id.toString());
        }
        return dto;
    }

    @Override
    public CbbDeskStrategyIDVDTO checkAndGetIDVStrategyById(UUID id) throws BusinessException {
        Assert.notNull(id, "id can not null");
        CbbDeskStrategyIDVDTO dto = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(id);
        if (dto == null) {
            LOGGER.warn("不存在idv云桌面策略id：[" + id + "]");
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_STRATEGY_IDV_NOT_FOUND, id.toString());
        }
        return dto;
    }

    @Override
    public CbbImageTemplateDetailDTO checkAndGetImageByIdAndImageType(UUID id, CbbImageType imageType) throws BusinessException {
        Assert.notNull(id, "id can not null");
        Assert.notNull(imageType, "imageType can not be null");
        CbbImageTemplateDetailDTO imageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(id);
        if (imageTemplateDetailDTO == null) {
            LOGGER.error("不存在镜像模板id：[" + id + "]");
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_NOT_FOUND, id.toString());
        }
        if (imageTemplateDetailDTO.getCbbImageType() != imageType) {
            LOGGER.error("镜像模板类型错误, 镜像模板id：[" + id + "]不是" + imageType.toString() + "类型");
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_TYPE_UNMATCH, id.toString(), imageType.toString());
        }
        return imageTemplateDetailDTO;
    }

    @Override
    public CbbDeskNetworkDetailDTO checkAndGetNetworkById(UUID id) throws BusinessException {
        Assert.notNull(id, "id can not null");
        CbbDeskNetworkDetailDTO deskNetworkDTO = cbbNetworkMgmtAPI.getDeskNetwork(id);
        if (deskNetworkDTO == null) {
            LOGGER.warn("不存在网络策略id：[" + id + "]");
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_NETWORK_STRATEGY_NOT_FOUND, id.toString());
        }
        return deskNetworkDTO;
    }

    /**
     * 查询桌面信息
     *
     * @param cbbDesktopId 桌面id
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @Override
    public CbbDeskDTO getDeskInfo(UUID cbbDesktopId) throws BusinessException {
        Assert.notNull(cbbDesktopId, "cbbDesktopId can not null");

        CbbDeskDTO deskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(cbbDesktopId);
        if (deskDTO == null) {
            throw new BusinessException(RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, cbbDesktopId.toString());
        }
        return deskDTO;
    }

    @Override
    public List<RcaHostDesktopDTO> convertRcaHostPageInfoList(List<ViewRcaHostDesktopEntity> desktopEntityList) throws BusinessException {
        Assert.notEmpty(desktopEntityList, "desktopEntityList can not null");

        // 将rca的entity列表转为rco的list
        List<ViewUserDesktopEntity> viewList = convertToCloudDesktopList(desktopEntityList);
        List<CloudDesktopDTO> cloudDesktopList = convertCloudDesktopForRecovery(viewList);
        // 将rco的dto列表转为rca的dto列表
        return convertToRcaHostDesktopList(cloudDesktopList, desktopEntityList);
    }

    @Override
    public List<RcaHostDesktopDTO> convertToRcaHostDesktopList(List<CloudDesktopDTO> cloudDesktopList,
                                                               List<ViewRcaHostDesktopEntity> rcaHostViewList) {
        Assert.notNull(cloudDesktopList, "cloudDesktopList can not be null");
        Assert.notNull(rcaHostViewList, "rcaHostViewList can not be null");
        List<RcaHostDesktopDTO> rcaHostDesktopDTOList = new ArrayList<>();
        // cloudDesktopList 只有云桌面的信息，rcaHostViewList有rca特性信息，需要聚合到RcaHostDesktopDTO里面
        cloudDesktopList.forEach(cloudDesktopDTO -> {
            ViewRcaHostDesktopEntity viewRcaHostDesktopEntity = rcaHostViewList.stream()
                    .filter(rcaHostView -> rcaHostView.getCbbDesktopId().equals(cloudDesktopDTO.getCbbId())).findFirst().orElse(null);
            if (viewRcaHostDesktopEntity != null) {
                // 先将云桌面信息放到临时dto里，再单独设置rca特性信息
                RcaHostDesktopDTO tempRcaHostDesktopDTO = new RcaHostDesktopDTO();
                BeanUtils.copyProperties(cloudDesktopDTO, tempRcaHostDesktopDTO);
                tempRcaHostDesktopDTO.setRcaHostId(viewRcaHostDesktopEntity.getRcaHostId());
                tempRcaHostDesktopDTO.setRcaHostSessionType(viewRcaHostDesktopEntity.getRcaHostSessionType());
                tempRcaHostDesktopDTO.setRcaPoolType(viewRcaHostDesktopEntity.getRcaPoolType());
                tempRcaHostDesktopDTO.setRcaPoolName(viewRcaHostDesktopEntity.getRcaPoolName());
                tempRcaHostDesktopDTO.setRcaMaxSessionCount(viewRcaHostDesktopEntity.getRcaMaxSessionCount());
                tempRcaHostDesktopDTO.setRcaSessionUsage(rcaHostSessionAPI.countByHostId(viewRcaHostDesktopEntity.getRcaHostId()));
                tempRcaHostDesktopDTO.setRcaPreStartHostNum(viewRcaHostDesktopEntity.getRcaPreStartHostNum());
                tempRcaHostDesktopDTO.setRcaSessionHoldTime(viewRcaHostDesktopEntity.getRcaSessionHoldTime());
                tempRcaHostDesktopDTO.setRcaLoadBalanceMode(viewRcaHostDesktopEntity.getRcaLoadBalanceMode());
                tempRcaHostDesktopDTO.setRcaSessionHoldConfigMode(viewRcaHostDesktopEntity.getRcaSessionHoldConfigMode());
                tempRcaHostDesktopDTO.setIsExpire(viewRcaHostDesktopEntity.getIsExpire());
                tempRcaHostDesktopDTO.setImageTemplateId(cloudDesktopDTO.getImageId());
                tempRcaHostDesktopDTO.setVgpuModel(cloudDesktopDTO.getVgpuModel());
                tempRcaHostDesktopDTO.setOneAgentVersion(viewRcaHostDesktopEntity.getOneAgentVersion());
                tempRcaHostDesktopDTO.setDifferentImageWithRcaPool(getIsDiffImageWithRcaPool(viewRcaHostDesktopEntity.getRcaPoolId(), cloudDesktopDTO));
                // 若是单会话则获取用户信息
                if (viewRcaHostDesktopEntity.getRcaHostSessionType() == RcaEnum.HostSessionType.SINGLE) {
                    createRcaHostSingleSessionUserInfo(tempRcaHostDesktopDTO);
                }
                tempRcaHostDesktopDTO.setRegisterState(viewRcaHostDesktopEntity.getRegisterState());
                tempRcaHostDesktopDTO.setRegisterMessage(viewRcaHostDesktopEntity.getRegisterMessage());
                tempRcaHostDesktopDTO.setEnableHyperVisorImprove(viewRcaHostDesktopEntity.getEnableHyperVisorImprove());
                // 686096-配合前端修订，派生主机列表增加池镜像名称的返回
                fillRcaPoolImageInfo(tempRcaHostDesktopDTO, viewRcaHostDesktopEntity.getRcaPoolId());
                rcaHostDesktopDTOList.add(tempRcaHostDesktopDTO);
            }
        });
        return rcaHostDesktopDTOList;
    }

    private Boolean getIsDiffImageWithRcaPool(UUID poolId, CloudDesktopDTO cloudDesktopDTO) {
        if (Objects.isNull(poolId)) {
            return Boolean.FALSE;
        }
        if (StringUtils.isNotEmpty(cloudDesktopDTO.getDesktopState())) {
            CbbCloudDeskState cbbCloudDeskState = CbbCloudDeskState.valueOf(cloudDesktopDTO.getDesktopState());
            if (CbbCloudDeskState.UPDATING == cbbCloudDeskState) {
                return Boolean.FALSE;
            }
        }

        UUID imageId = cloudDesktopDTO.getImageId();
        ConditionQueryRequestBuilder deskPoolBuilder = new DefaultConditionQueryRequestBuilder();
        deskPoolBuilder.eq(IMAGE_TEMPLATE_ID, imageId);
        deskPoolBuilder.eq(ID, poolId);
        try {
            return rcaAppPoolAPI.countByConditions(deskPoolBuilder.build()) <= 0;
        } catch (BusinessException e) {
            LOGGER.error("根据池id={}，和镜像id={}，获取数量异常，e={}", poolId, imageId, e);
            return Boolean.FALSE;
        }
    }

    private void fillRcaPoolImageInfo(RcaHostDesktopDTO tempRcaHostDesktopDTO, UUID rcaPoolId) {
        try {
            if (Boolean.TRUE.equals(tempRcaHostDesktopDTO.getDifferentImageWithRcaPool()) && Objects.nonNull(rcaPoolId)) {
                RcaAppPoolBaseDTO rcaAppPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(rcaPoolId);
                if (Objects.isNull(rcaAppPoolBaseDTO.getImageTemplateId())) {
                    return;
                }
                CbbImageTemplateDTO imageTemplateDTO = cbbImageTemplateMgmtAPI.getCbbImageTemplateDTO(rcaAppPoolBaseDTO.getImageTemplateId());
                tempRcaHostDesktopDTO.setPoolImageTemplateName(imageTemplateDTO.getImageTemplateName());
                if (imageTemplateDTO.getImageRoleType() == ImageRoleType.VERSION) {
                    tempRcaHostDesktopDTO.setPoolRootImageName(imageTemplateDTO.getRootImageName());
                }
            }
        } catch (BusinessException e) {
            LOGGER.error("根据池id[{}]获取池信息失败, e={}", rcaPoolId, e);
        }
    }

    /**
     * @param viewList viewList
     * @return CloudDesktopDTO
     * @throws BusinessException BusinessException
     */
    private List<CloudDesktopDTO> convertCloudDesktopForRecovery(List<ViewUserDesktopEntity> viewList) throws BusinessException {
        Assert.notNull(viewList, "viewList can not be null");

        List<CloudDesktopDTO> resultList = new ArrayList<>();
        Map<UUID, CbbDesktopPoolDTO> desktopPoolMap = new HashMap<>();
        Map<UUID, UUID> poolSoftStrategyIdMap = new HashMap<>();
        Map<UUID, ClusterInfoDTO> clusterInfoAllMap = clusterAPI.queryAllClusterInfoList().stream()
                .collect(Collectors.toMap(ClusterInfoDTO::getId, clusterInfoDTO -> clusterInfoDTO));
        // 获取全部计算集群信息
        for (ViewUserDesktopEntity userDesktopEntity : viewList) {
            CloudDesktopDTO cloudDesktop = ViewUserDesktopEntity.convertEntityToDTO(userDesktopEntity);
            // 是否需要刷新云桌面策略的标识
            cloudDesktop.setNeedRefreshStrategy(checkNeedRefreshStrategy(userDesktopEntity));
            // 添加GPU大小值
            cloudDesktop.setVgpuModel(convertDesktopVgpu(userDesktopEntity.getVgpuType(), userDesktopEntity.getVgpuExtraInfo()));
            cloudDesktop.setPoolStrategyError(checkDesktopPoolStrategyError(desktopPoolMap, poolSoftStrategyIdMap, userDesktopEntity));
            // 设置集群名称
            if (Objects.nonNull(userDesktopEntity.getClusterId())) {
                ClusterInfoDTO clusterInfoDTO = clusterInfoAllMap.get(userDesktopEntity.getClusterId());
                if (clusterInfoDTO != null) {
                    cloudDesktop.setClusterName(clusterInfoDTO.getClusterName());
                    cloudDesktop.setClusterId(userDesktopEntity.getClusterId());
                }
            }
            // 过期时间
            cloudDesktop.setUserAccountExpireDate(expireDateFormat(userDesktopEntity));
            try {
                Consumer<CbbDeskNetworkInfoDTO> wireNetworkConfigConsumer = networkInfoDTO -> {
                    cloudDesktop.setWirelessIp(networkInfoDTO.getIp());
                    cloudDesktop.setWirelessMacAddr(networkInfoDTO.getMac());
                    cloudDesktop.setAutoWirelessDhcp(networkInfoDTO.getAutoDhcp());
                    cloudDesktop.setAutoWirelessDns(networkInfoDTO.getAutoDns());
                    cloudDesktop.setWirelessGateway(networkInfoDTO.getGateWay());
                    cloudDesktop.setWirelessMask(networkInfoDTO.getMask());
                    cloudDesktop.setWirelessDnsPrimary(networkInfoDTO.getDnsPrimary());
                    cloudDesktop.setWirelessSecondDnsPrimary(networkInfoDTO.getDnsSecondary());
                };
                Consumer<CbbDeskNetworkInfoDTO> networkConfigConsumer = networkInfoDTO -> {
                    cloudDesktop.setDesktopIp(networkInfoDTO.getIp());
                    cloudDesktop.setDesktopMac(networkInfoDTO.getMac());
                    cloudDesktop.setAutoDeskDhcp(networkInfoDTO.getAutoDhcp());
                    cloudDesktop.setAutoDeskDns(networkInfoDTO.getAutoDns());
                    cloudDesktop.setDeskGateway(networkInfoDTO.getGateWay());
                    cloudDesktop.setDeskMask(networkInfoDTO.getMask());
                    cloudDesktop.setDeskDnsPrimary(networkInfoDTO.getDnsPrimary());
                    cloudDesktop.setDeskSecondDnsPrimary(networkInfoDTO.getDnsSecondary());
                };
                setNetWorkInfo(cloudDesktop.getId(), networkConfigConsumer, wireNetworkConfigConsumer, cloudDesktop::setNetworkAccessMode);
                resultList.add(cloudDesktop);
            } catch (Exception e) {
                LOGGER.error("云桌面列表构造异常，临时解决方案是捕获不让列表报错，但是会导致数据不对", e);
            }
        }
        return resultList;
    }

    private static String expireDateFormat(ViewUserDesktopEntity entity) {
        Assert.notNull(entity, "entity is not null");
        if (Objects.isNull(entity.getUserAccountExpireDate())) {
            // null
            return null;
        }
        if (entity.getUserAccountExpireDate() == EXPIRE_DATE_ZERO) {
            return LocaleI18nResolver.resolve(RCDC_RCO_USER_ACCOUNT_EXPIRE);
        }
        Date accountExpireDate;
        if (Objects.equals(entity.getUserType(), IacUserTypeEnum.AD.name())) {
            accountExpireDate = DateUtil.adDomainTimestampToDate(entity.getUserAccountExpireDate());
        } else {
            accountExpireDate = new Date(entity.getUserAccountExpireDate());
        }
        return new SimpleDateFormat(DATE_FORMAT_TIME).format(accountExpireDate);
    }

    private String convertDesktopVgpu(VgpuType vgpuType, String extraInfoStr) {
        VgpuExtraInfo extraInfo = DeskStrategyHelper.convert2VgpuExtraInfo(vgpuType, extraInfoStr);
        if (Objects.isNull(extraInfo.getGraphicsMemorySize()) || extraInfo.getGraphicsMemorySize() == 0) {
            return "";
        }
        return deskSpecAPI.buildDefaultAmdModel(extraInfo);
    }

    private Boolean checkNeedRefreshStrategy(ViewUserDesktopEntity desktop) {
        if (!CbbCloudDeskType.VDI.name().equals(desktop.getDesktopType())) {
            return Boolean.FALSE;
        }
        desktop.setPersonSize(Objects.nonNull(desktop.getPersonSize()) ? desktop.getPersonSize() : 0);
        StrategyHardwareDTO strategyHardwareDTO = new StrategyHardwareDTO();
        strategyHardwareDTO.setCpu(desktop.getCpu());
        strategyHardwareDTO.setMemory(desktop.getMemory());
        strategyHardwareDTO.setPersonSize(desktop.getPersonSize());
        strategyHardwareDTO.setSystemSize(desktop.getSystemSize());
        strategyHardwareDTO.setEnableHyperVisorImprove(desktop.getEnableHyperVisorImprove());
        VgpuExtraInfoSupport extraInfo = VgpuUtil.deserializeVgpuExtraInfoByType(desktop.getVgpuType(), desktop.getVgpuExtraInfo());
        strategyHardwareDTO.setVgpuInfoDTO(new VgpuInfoDTO(desktop.getVgpuType(), extraInfo));

        if (Objects.nonNull(desktop.getDeskSpecId())) {
            CbbDeskSpecDTO cbbDeskSpecDTO;
            try {
                cbbDeskSpecDTO = cbbDeskSpecAPI.getById(desktop.getDeskSpecId());
            } catch (Exception e) {
                LOGGER.error("获取桌面[{}]规格信息异常, deskSpecId[{}]", desktop.getCbbDesktopId(), desktop.getDeskSpecId(), e);
                return true;
            }
            return !deskSpecAPI.specHardwareEquals(strategyHardwareDTO, cbbDeskSpecDTO);
        }
        return true;
    }

    /**
     * 池桌面的配置与池配置是否一致
     *
     * @param desktopPoolMap    池集合
     * @param userDesktopEntity 桌面
     * @return true 策略错误的存在不一致，false 策略已经一致没错误
     */
    private boolean checkDesktopPoolStrategyError(Map<UUID, CbbDesktopPoolDTO> desktopPoolMap, Map<UUID, UUID> poolSoftStrategyIdMap,
                                                  ViewUserDesktopEntity userDesktopEntity) {
        if (Objects.isNull(userDesktopEntity.getDesktopPoolId())) {
            return false;
        }
        CbbDesktopPoolDTO cbbDesktopPoolDTO = desktopPoolMap.get(userDesktopEntity.getDesktopPoolId());
        if (Objects.isNull(cbbDesktopPoolDTO)) {
            try {
                cbbDesktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(userDesktopEntity.getDesktopPoolId());
            } catch (BusinessException e) {
                LOGGER.error("查询桌面[{}]所属的池[{}]详情异常", userDesktopEntity.getCbbDesktopId(), userDesktopEntity.getDesktopPoolId(), e);
                return false;
            }
            desktopPoolMap.put(userDesktopEntity.getDesktopPoolId(), cbbDesktopPoolDTO);
        }

        UUID poolSoftwareStrategyId;
        if (!poolSoftStrategyIdMap.containsKey(userDesktopEntity.getDesktopPoolId())) {
            poolSoftwareStrategyId = desktopPoolConfigService.getSoftwareStrategyIdByDesktopPoolId(cbbDesktopPoolDTO.getId());
            poolSoftStrategyIdMap.put(userDesktopEntity.getDesktopPoolId(), poolSoftwareStrategyId);
        } else {
            poolSoftwareStrategyId = poolSoftStrategyIdMap.get(userDesktopEntity.getDesktopPoolId());
        }

        return !(Objects.equals(userDesktopEntity.getCbbNetworkId(), cbbDesktopPoolDTO.getNetworkId())
                && Objects.equals(userDesktopEntity.getCbbStrategyId(), cbbDesktopPoolDTO.getStrategyId())
                && Objects.equals(userDesktopEntity.getImageTemplateId(), cbbDesktopPoolDTO.getImageTemplateId())
                && Objects.equals(userDesktopEntity.getSoftwareStrategyId(), poolSoftwareStrategyId));
    }

    private List<ViewUserDesktopEntity> convertToCloudDesktopList(List<ViewRcaHostDesktopEntity> rcaHostViewList) {
        List<ViewUserDesktopEntity> userDesktopEntityList = new ArrayList<>();
        rcaHostViewList.forEach(viewRcaHostDesktopEntity -> {
            ViewUserDesktopEntity userDesktopEntity = new ViewUserDesktopEntity();
            BeanUtils.copyProperties(viewRcaHostDesktopEntity, userDesktopEntity);
            userDesktopEntityList.add(userDesktopEntity);
        });
        return userDesktopEntityList;
    }

    private void createRcaHostSingleSessionUserInfo(RcaHostDesktopDTO tempRcaHostDesktopDTO) {
        try {
            // 多会话由前端增加用户入口，点击用户信息后请求主机对应的用户列表
            List<UUID> hostIdList = new ArrayList<>();
            hostIdList.add(tempRcaHostDesktopDTO.getRcaHostId());
            List<RcaHostSessionDTO> sessionList = rcaHostSessionAPI.listByHostIdIn(hostIdList);

            // 单会话的话只会有一个用户信息
            if (CollectionUtils.isEmpty(sessionList)) {
                // 无绑定的用户会话，不做用户信息的返回
                return;
            }

            RcaHostSessionDTO sessionEntity = sessionList.get(0);
            LOGGER.debug("rca_host-单会话的应用主机[{}]的会话信息sessionId={}, userId={}"
                    , tempRcaHostDesktopDTO.getRcaHostId(), sessionEntity.getId(), sessionEntity.getUserId());
            if (Objects.isNull(sessionEntity.getUserId())) {
                return;
            }
            IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserDetail(sessionEntity.getUserId());
            tempRcaHostDesktopDTO.setRcaUserDetailDTO(userDetailDTO);
            tempRcaHostDesktopDTO.setUserId(userDetailDTO.getId());
            tempRcaHostDesktopDTO.setUserName(userDetailDTO.getUserName());
            tempRcaHostDesktopDTO.setUserType(userDetailDTO.getUserType().name());
            tempRcaHostDesktopDTO.setRealName(userDetailDTO.getRealName());
            return;
        } catch (BusinessException e) {
            LOGGER.error("rca_host-获取应用主机[{}]会话对应的用户信息失败，不做处理, e={}", tempRcaHostDesktopDTO.getRcaHostId(), e);
        }

    }
}
