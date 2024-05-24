package com.ruijie.rcos.rcdc.rco.module.web.service;

import java.util.*;
import java.util.stream.Collectors;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacPageResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacQueryUserListPageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppGroupAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupUserNumDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVOIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVOIDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaGroupMemberAPI;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserGroupVO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListUserGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListUserGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.ExtraDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolUserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.PoolDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UserGroupAssignedUserNumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UserGroupDisabledUserNumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolUserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.UserGroupBindDiskNumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.service.UserGroupBaseHelper;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.imagetemplate.ImageTemplateVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.strategy.DeskStrategyVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.ListUserGroupWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.UserGroupDetailWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.GroupIdvDeskCfgVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.GroupVdiDeskCfgVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.GroupVoiDeskCfgVO;
import com.ruijie.rcos.rcdc.rco.module.web.util.CapacityUnitUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/19
 *
 * @author Jarman
 */
@Service("rcoUserGroupHelper")
public class UserGroupHelper extends UserGroupBaseHelper {

    private static final int SQL_IN_MAX_NUM = 500;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private StoragePoolAPI storagePoolAPI;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    @Autowired
    private DesktopPoolUserMgmtAPI desktopPoolUserMgmtAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private DiskPoolUserAPI diskPoolUserAPI;

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    @Autowired
    private RcaGroupMemberAPI rcaGroupMemberAPI;

    @Autowired
    private RcaHostSessionAPI rcaHostSessionAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private RcaAppGroupAPI rcaAppGroupAPI;

    @Autowired
    private HostUserAPI hostUserAPI;

    public static final String USER_GROUP_ROOT_ID = "root";

    public static final String USER_GROUP_ROOT_NAME = "总览";


    private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupHelper.class);

    /**
     * 构建返回给前端的用户组数据结构
     *
     * @param userGroupDetail userGroupDetail
     * @return UserGroupDetailWebResponse
     * @throws BusinessException BusinessException
     */
    public UserGroupDetailWebResponse buildUserGroupDetailForWebResponse(IacUserGroupDetailDTO userGroupDetail) throws BusinessException {
        Assert.notNull(userGroupDetail, "userGroupDetail");
        UserGroupDetailWebResponse webResponse = new UserGroupDetailWebResponse();
        webResponse.setId(userGroupDetail.getId());
        webResponse.setGroupName(userGroupDetail.getName());
        webResponse.setPasswordRandom(Optional.ofNullable(userGroupDetail.getEnableRandomPassword()).orElse(Boolean.FALSE));
        if (userGroupDetail.getParentId() == null) {
            webResponse.setParent(new GroupIdLabelEntry(USER_GROUP_ROOT_ID, USER_GROUP_ROOT_NAME));
        } else {
            webResponse.setParent(new GroupIdLabelEntry(userGroupDetail.getParentId().toString(), userGroupDetail.getParentName()));
        }
        // 获取VDI 云桌面配置
        GroupVdiDeskCfgVO vdiConfigVO = generateGroupVdiDeskCfgVO(userGroupDetail);
        webResponse.setVdiDesktopConfig(vdiConfigVO);
        // 获取IDV 云桌面配置
        GroupIdvDeskCfgVO idvConfigVO = generateGroupIdvDeskCfgVO(userGroupDetail);
        webResponse.setIdvDesktopConfig(idvConfigVO);
        // 获取VOI 云桌面配置
        GroupVoiDeskCfgVO voiConfigVO = generateGroupVoiDeskCfgVO(userGroupDetail);
        webResponse.setVoiDesktopConfig(voiConfigVO);
        webResponse.setAccountExpireDate(userGroupDetail.getAccountExpireDate());
        webResponse.setInvalidTime(userGroupDetail.getInvalidTime());
        return webResponse;
    }

    private GroupVdiDeskCfgVO generateGroupVdiDeskCfgVO(IacUserGroupDetailDTO userGroupDetail) throws BusinessException {
        GroupVdiDeskCfgVO vdiConfigVO = new GroupVdiDeskCfgVO();
        UserGroupDesktopConfigDTO groupDesktopConfig =
                userDesktopConfigAPI.getUserGroupDesktopConfig(userGroupDetail.getId(), UserCloudDeskTypeEnum.VDI);
        if (groupDesktopConfig != null) {
            vdiConfigVO.setImage(buildImageVo(groupDesktopConfig.getImageTemplateId()));
            vdiConfigVO.setStrategy(buildVDIStrategyVo(groupDesktopConfig.getStrategyId()));
            vdiConfigVO.setNetwork(queryNetworkDto(groupDesktopConfig.getNetworkId()));
            if (Objects.nonNull(groupDesktopConfig.getSoftwareStrategyId())) {
                vdiConfigVO.setSoftwareStrategy(getSoftwareStrategyDTO(userGroupDetail.getId(), groupDesktopConfig.getSoftwareStrategyId()));
            }

            UUID userProfileStrategyId = groupDesktopConfig.getUserProfileStrategyId();
            if (Objects.nonNull(userProfileStrategyId)) {
                try {
                    vdiConfigVO.setUserProfileStrategy(userProfileMgmtAPI.findUserProfileStrategyById(userProfileStrategyId));
                } catch (BusinessException e) {
                    LOGGER.warn("用户组桌面配置: {} 对应的用户配置策略: {} 已被删除", groupDesktopConfig.getGroupId(), userProfileStrategyId);
                }
            }
            if (groupDesktopConfig.getClusterId() != null) {
                vdiConfigVO.setCluster(getCluster(groupDesktopConfig.getClusterId()));
            }
            if (groupDesktopConfig.getPlatformId() != null) {
                vdiConfigVO.setCloudPlatform(getCloudPlatform(groupDesktopConfig.getPlatformId()));
            }
            if (groupDesktopConfig.getDeskSpecId() != null) {
                CbbDeskSpecDTO cbbDeskSpecDTO = cbbDeskSpecAPI.getById(groupDesktopConfig.getDeskSpecId());
                vdiConfigVO.setCpu(cbbDeskSpecDTO.getCpu());
                vdiConfigVO.setMemory(CapacityUnitUtils.mb2Gb(cbbDeskSpecDTO.getMemory()));
                vdiConfigVO.setSystemDisk(cbbDeskSpecDTO.getSystemSize());

                Map<UUID, PlatformStoragePoolDTO> storagePoolMap = storagePoolAPI.queryAllStoragePool().stream()
                        .collect(Collectors.toMap(PlatformStoragePoolDTO::getId, storagePoolDTO -> storagePoolDTO, (storage1, storage2) -> storage2));
                if (Objects.nonNull(cbbDeskSpecDTO.getSystemDiskStoragePoolId())) {
                    vdiConfigVO.setSystemDiskStoragePool(getAssignStoragePool(cbbDeskSpecDTO.getSystemDiskStoragePoolId(), storagePoolMap));
                }
                vdiConfigVO.setPersonalDisk(cbbDeskSpecDTO.getPersonSize());
                if (Objects.nonNull(cbbDeskSpecDTO.getPersonDiskStoragePoolId())) {
                    vdiConfigVO.setPersonDiskStoragePool(getAssignStoragePool(cbbDeskSpecDTO.getPersonDiskStoragePoolId(), storagePoolMap));
                }
                vdiConfigVO.setEnableHyperVisorImprove(cbbDeskSpecDTO.getEnableHyperVisorImprove());
                if (!CollectionUtils.isEmpty(cbbDeskSpecDTO.getExtraDiskList())) {
                    vdiConfigVO.setExtraDiskArr(cbbDeskSpecDTO.getExtraDiskList().stream().map(item -> {
                        ExtraDiskDTO extraDiskDTO = new ExtraDiskDTO();
                        extraDiskDTO.setIndex(item.getIndex());
                        extraDiskDTO.setDiskId(item.getDiskId());
                        extraDiskDTO.setExtraSize(item.getExtraSize());
                        extraDiskDTO.setAssignedStoragePoolId(item.getAssignedStoragePoolId());
                        extraDiskDTO.setExtraDiskStoragePool(getAssignStoragePool(item.getAssignedStoragePoolId(), storagePoolMap));
                        return extraDiskDTO;
                    }).toArray(ExtraDiskDTO[]::new));
                }
                VgpuInfoDTO vgpuInfoDTO = Optional.ofNullable(cbbDeskSpecDTO.getVgpuInfoDTO()).orElse(new VgpuInfoDTO());
                vdiConfigVO.setVgpuType(vgpuInfoDTO.getVgpuType());
                vdiConfigVO.setVgpuExtraInfo(vgpuInfoDTO.getVgpuExtraInfo());
            }
        }
        return vdiConfigVO;
    }

    private IdLabelEntry getCluster(UUID clusterId) {

        IdLabelEntry assignStoragePool = new IdLabelEntry();

        ClusterInfoDTO clusterInfoDTO = clusterAPI.queryAvailableClusterById(clusterId);

        if (clusterInfoDTO != null) {
            assignStoragePool.setId(clusterInfoDTO.getId());
            assignStoragePool.setLabel(clusterInfoDTO.getClusterName());
        }
        return assignStoragePool;
    }

    private GroupIdvDeskCfgVO generateGroupIdvDeskCfgVO(IacUserGroupDetailDTO userGroupDetail) throws BusinessException {
        GroupIdvDeskCfgVO idvConfigVO = new GroupIdvDeskCfgVO();
        UserGroupDesktopConfigDTO groupDesktopConfig =
                userDesktopConfigAPI.getUserGroupDesktopConfig(userGroupDetail.getId(), UserCloudDeskTypeEnum.IDV);
        if (groupDesktopConfig != null) {
            idvConfigVO.setImage(buildImageVo(groupDesktopConfig.getImageTemplateId()));
            idvConfigVO.setStrategy(buildIDVStrategyVo(groupDesktopConfig.getStrategyId()));
            if (Objects.nonNull(groupDesktopConfig.getSoftwareStrategyId())) {
                idvConfigVO.setSoftwareStrategy(getSoftwareStrategyDTO(userGroupDetail.getId(), groupDesktopConfig.getSoftwareStrategyId()));
            }
            UUID userProfileStrategyId = groupDesktopConfig.getUserProfileStrategyId();
            if (Objects.nonNull(userProfileStrategyId)) {
                try {
                    idvConfigVO.setUserProfileStrategy(userProfileMgmtAPI.findUserProfileStrategyById(userProfileStrategyId));
                } catch (BusinessException e) {
                    LOGGER.warn("用户组桌面配置: {} 对应的用户配置策略: {} 已被删除", groupDesktopConfig.getGroupId(), userProfileStrategyId);
                }
            }
        }
        return idvConfigVO;
    }

    private IdLabelEntry getAssignStoragePool(UUID storagePoolId, Map<UUID, PlatformStoragePoolDTO> storagePoolMap) {
        IdLabelEntry assignStoragePool = new IdLabelEntry();
        PlatformStoragePoolDTO storagePoolDTO = storagePoolMap.get(storagePoolId);
        if (Objects.nonNull(storagePoolDTO)) {
            assignStoragePool.setId(storagePoolDTO.getId());
            assignStoragePool.setLabel(storagePoolDTO.getName());
        }
        return assignStoragePool;
    }

    private IdLabelEntry getCloudPlatform(UUID platformId) {
        IdLabelEntry cloudPlatform = new IdLabelEntry();

        CloudPlatformDTO cloudPlatformDTO = null;
        try {
            cloudPlatformDTO = cloudPlatformManageAPI.getInfoById(platformId);
        } catch (BusinessException e) {
            LOGGER.error("获取云平台[{}]信息有出错", platformId, e);
        }

        if (Objects.nonNull(cloudPlatformDTO)) {
            cloudPlatform.setId(cloudPlatformDTO.getId());
            cloudPlatform.setLabel(cloudPlatformDTO.getName());
        }
        return cloudPlatform;
    }

    /**
     * 构造用户组 VOI云桌面配置
     *
     * @param userGroupDetail
     * @return
     * @throws BusinessException
     */
    private GroupVoiDeskCfgVO generateGroupVoiDeskCfgVO(IacUserGroupDetailDTO userGroupDetail) throws BusinessException {
        GroupVoiDeskCfgVO voiConfigVO = new GroupVoiDeskCfgVO();
        UserGroupDesktopConfigDTO groupDesktopConfig =
                userDesktopConfigAPI.getUserGroupDesktopConfig(userGroupDetail.getId(), UserCloudDeskTypeEnum.VOI);
        if (groupDesktopConfig != null) {
            voiConfigVO.setImage(buildImageVo(groupDesktopConfig.getImageTemplateId()));
            voiConfigVO.setStrategy(buildVOIStrategyVo(groupDesktopConfig.getStrategyId()));
            if (Objects.nonNull(groupDesktopConfig.getSoftwareStrategyId())) {
                voiConfigVO.setSoftwareStrategy(getSoftwareStrategyDTO(userGroupDetail.getId(), groupDesktopConfig.getSoftwareStrategyId()));
            }
            voiConfigVO.setImage(buildImageVo(groupDesktopConfig.getImageTemplateId()));
            voiConfigVO.setStrategy(buildVOIStrategyVo(groupDesktopConfig.getStrategyId()));
            UUID userProfileStrategyId = groupDesktopConfig.getUserProfileStrategyId();
            if (Objects.nonNull(userProfileStrategyId)) {
                try {
                    voiConfigVO.setUserProfileStrategy(userProfileMgmtAPI.findUserProfileStrategyById(userProfileStrategyId));
                } catch (BusinessException e) {
                    LOGGER.warn("用户组桌面配置: {} 对应的用户配置策略: {} 已被删除", groupDesktopConfig.getGroupId(), userProfileStrategyId);
                }
            }

        }
        return voiConfigVO;
    }

    /**
     * @param softwareStrategyId
     * @return
     */
    private SoftwareStrategyDTO getSoftwareStrategyDTO(UUID groupId, UUID softwareStrategyId) {
        SoftwareStrategyDTO softwareStrategyDTO = null;
        try {
            softwareStrategyDTO = softwareControlMgmtAPI.findSoftwareStrategyById(softwareStrategyId);
        } catch (BusinessException e) {
            LOGGER.warn("用户组桌面配置: {} 对应的软控策略: {} 已被删除", groupId, softwareStrategyId);
        }
        return softwareStrategyDTO;
    }

    private ImageTemplateVO buildImageVo(UUID imageId) throws BusinessException {
        CbbImageTemplateDetailDTO dto = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        Assert.notNull(dto, "dto can not be null");

        ImageTemplateVO vo = new ImageTemplateVO();
        BeanUtils.copyProperties(dto, vo);
        vo.setImageOsName(dto.getOsType().name());

        return vo;
    }

    private DeskStrategyVO buildVDIStrategyVo(UUID vdiStrategyId) throws BusinessException {
        CbbDeskStrategyVDIDTO dto = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(vdiStrategyId);
        Assert.notNull(dto, "dto can not be null");

        DeskStrategyVO vo = new DeskStrategyVO();
        vo.setId(dto.getId());
        vo.setStrategyName(dto.getName());
        vo.setDesktopType(dto.getPattern());
        vo.setUsbTypeIdArr(dto.getUsbTypeIdArr());
        vo.setEnableInternet(dto.getOpenInternet());
        vo.setEnableUsbReadOnly(dto.getOpenUsbReadOnly());
        vo.setEnableOpenDesktopRedirect(dto.getOpenDesktopRedirect());
        return vo;
    }

    private DeskStrategyVO buildIDVStrategyVo(UUID idvStrategyId) throws BusinessException {
        CbbDeskStrategyIDVDTO dto = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(idvStrategyId);
        Assert.notNull(dto, "dto can not be null");
        DeskStrategyVO vo = new DeskStrategyVO();
        vo.setId(dto.getId());
        vo.setStrategyName(dto.getName());
        vo.setDesktopType(dto.getPattern());
        vo.setSystemDisk(dto.getSystemSize());
        vo.setUsbTypeIdArr(dto.getUsbTypeIdArr());
        vo.setEnableAllowLocalDisk(dto.getAllowLocalDisk());
        vo.setEnableUsbReadOnly(dto.getOpenUsbReadOnly());
        vo.setEnableOpenDesktopRedirect(dto.getOpenDesktopRedirect());
        return vo;
    }

    private DeskStrategyVO buildVOIStrategyVo(UUID idvStrategyId) throws BusinessException {
        CbbDeskStrategyVOIDTO dto = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOI(idvStrategyId);
        Assert.notNull(dto, "dto can not be null");
        DeskStrategyVO vo = new DeskStrategyVO();
        vo.setId(dto.getId());
        vo.setStrategyName(dto.getName());
        vo.setDesktopType(dto.getPattern());
        vo.setSystemDisk(dto.getSystemSize());
        vo.setUsbTypeIdArr(dto.getUsbTypeIdArr());
        vo.setEnableAllowLocalDisk(dto.getAllowLocalDisk());
        vo.setEnableUsbReadOnly(dto.getOpenUsbReadOnly());
        vo.setEnableOpenDesktopRedirect(dto.getOpenDesktopRedirect());
        return vo;
    }


    private CbbDeskNetworkDetailDTO queryNetworkDto(UUID networkId) throws BusinessException {
        CbbDeskNetworkDetailDTO deskNetwork = new CbbDeskNetworkDetailDTO();
        try {
            deskNetwork = cbbNetworkMgmtAPI.getDeskNetwork(networkId);
            return deskNetwork;
        } catch (BusinessException ex) {
            LOGGER.error("获取网络策略[{}]异常", networkId);
        }
        return deskNetwork;
    }

    /**
     * 获取用户组下未绑定vdi桌面的用户id列表（排除掉禁用状态用户、访客用户）
     *
     * @param groupId  用户组id
     * @param deskType 获取用户未绑定的桌面类型
     * @return 返回用户id列表
     * @throws BusinessException getUserDetail异常
     */
    public UUID[] getUnBindDesktopUserListByGroupId(@Nullable UUID groupId, UserCloudDeskTypeEnum deskType) throws BusinessException {
        Assert.notNull(deskType, "deskType can not be null");

        List<UUID> userIdList = new ArrayList<>();
        // 组下用户可能会超过1000，分批查询
        IacPageResponse<IacUserDetailDTO> pageResult = pageQueryByGroupId(groupId, 0);
        // 总页数
        int totalPage = (int) Math.ceil((double) pageResult.getTotal() / Constants.MAX_QUERY_LIST_SIZE);
        for (int page = 0; page < totalPage; page++) {
            // 前面已查过，不重复查
            if (page == 0) {
                getUnbindDeskUserId(groupId, deskType, pageResult.getItemArr(), userIdList);
                continue;
            }
            pageResult = pageQueryByGroupId(groupId, page);
            if (pageResult.getTotal() == 0) {
                break;
            }
            getUnbindDeskUserId(groupId, deskType, pageResult.getItemArr(), userIdList);
        }

        UUID[] idArr = new UUID[userIdList.size()];
        userIdList.toArray(idArr);
        return idArr;
    }

    private IacPageResponse<IacUserDetailDTO> pageQueryByGroupId(UUID groupId, Integer page) throws BusinessException {
        IacQueryUserListPageDTO pageDTO = new IacQueryUserListPageDTO();
        pageDTO.setGroupId(groupId);
        pageDTO.setPage(page);
        pageDTO.setLimit(Constants.MAX_QUERY_LIST_SIZE);
        return cbbUserAPI.pageQueryUserListByGroupId(pageDTO);
    }

    private void getUnbindDeskUserId(UUID groupId, UserCloudDeskTypeEnum deskType, IacUserDetailDTO[] resultArr, List<UUID> uuidList) {
        switch (deskType) {
            case VDI:
                List<CloudDesktopDTO> cloudDesktopList = userDesktopMgmtAPI.listUserDesktopByGroupId(groupId, deskType);
                // 过滤掉池桌面
                Set<UUID> deskUserIdSet = cloudDesktopList.stream().filter(item -> item.getDesktopPoolId() == null).map(CloudDesktopDTO::getUserId)
                        .collect(Collectors.toSet());
                Arrays.asList(resultArr).stream()
                        .filter(userDTO -> userDTO.getUserState() != IacUserStateEnum.DISABLE && userDTO.getUserType() != IacUserTypeEnum.VISITOR)
                        .forEach(userDTO -> {
                            // 用户未配置VDI云桌面
                            if (!deskUserIdSet.contains(userDTO.getId())) {
                                // 获取用户创建中的云桌面个数
                                int creatingDesktopNum = userDesktopMgmtAPI.getCreatingDesktopNum(userDTO.getId());
                                if (creatingDesktopNum == 0) {
                                    uuidList.add(userDTO.getId());
                                }
                            }
                        });
                break;
            case IDV:
            case VOI:
                Arrays.asList(resultArr).stream()
                        .filter(userDTO -> userDTO.getUserState() != IacUserStateEnum.DISABLE && userDTO.getUserType() != IacUserTypeEnum.VISITOR)
                        .forEach(userDTO -> {
                            UserDesktopConfigDTO userDesktopConfigDTO = userDesktopConfigAPI.getUserDesktopConfig(userDTO.getId(), deskType);
                            // 找到云桌面配置并镜像ID不为空，说明用户已经配置不需要变更
                            if (userDesktopConfigDTO == null || userDesktopConfigDTO.getImageTemplateId() == null) {
                                uuidList.add(userDTO.getId());
                            }
                        });
                break;
            default:
        }
    }


    /**
     * 获取子节点
     *
     * @param parentGroupDTOList parentGroupDTOList
     * @param deleteGroupIdList  deleteGroupIdList
     * @param userGroupList      terminalGroupDTOList
     */
    public void getChildrenTreeNode(List<IacUserGroupDetailDTO> parentGroupDTOList, List<UUID> deleteGroupIdList,
                                    List<IacUserGroupDetailDTO> userGroupList) {

        Assert.notNull(parentGroupDTOList, "parentGroupDTOList cannot null");
        Assert.notNull(deleteGroupIdList, "deleteGroupIdList cannot null");
        Assert.notNull(userGroupList, "terminalGroupDTOList cannot null");

        List<IacUserGroupDetailDTO> childrenDTOList = new ArrayList<>();
        // 遍历父节点
        for (IacUserGroupDetailDTO parentGroupDTO : parentGroupDTOList) {
            deleteGroupIdList.add(parentGroupDTO.getId());
            userGroupList.forEach(childGroupDTO -> {
                if (parentGroupDTO.getId().equals(childGroupDTO.getParentId())) {
                    childrenDTOList.add(childGroupDTO);
                }
            });
        }
        if (CollectionUtils.isEmpty(childrenDTOList)) {
            return;
        }
        getChildrenTreeNode(childrenDTOList, deleteGroupIdList, userGroupList);
    }

    /**
     * @param groupId          用户组组编码
     * @param userGroupDTOList 用户组列表
     * @param localGroupName   用户组名称
     * @param sessionContext   会话信息
     * @throws BusinessException 业务异常
     */
    public void checkPermission(UUID groupId, List<IacUserGroupDetailDTO> userGroupDTOList, String localGroupName, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(groupId, "groupId cannot null");
        Assert.notNull(userGroupDTOList, "terminalGroupDTOList cannot null");
        Assert.notNull(localGroupName, "localGroupName cannot null");
        Assert.notNull(sessionContext, "sessionContext cannot null");

        List<UUID> deleteGroupIdList = new ArrayList<>();
        StringBuilder tip = new StringBuilder();
        for (IacUserGroupDetailDTO dto : userGroupDTOList) {
            if (dto.getId().equals(groupId)) {
                List<IacUserGroupDetailDTO> parentGroupDTOList = new ArrayList<>();
                parentGroupDTOList.add(dto);
                getChildrenTreeNode(parentGroupDTOList, deleteGroupIdList, userGroupDTOList);
                break;
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(" 管理员ID:{}将删除的用户组集合为{}", sessionContext.getUserId(), JSON.toJSONString(deleteGroupIdList));
        }

        // 获取拥有权限的用户组
        ListUserGroupIdRequest listUserGroupIdRequest = new ListUserGroupIdRequest();
        listUserGroupIdRequest.setAdminId(sessionContext.getUserId());
        ListUserGroupIdResponse listUserGroupIdResponse = adminDataPermissionAPI.listUserGroupIdByAdminId(listUserGroupIdRequest);
        if (listUserGroupIdResponse == null) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_ADMIN_NOT_HAS_USER_GROUP_PERMISSION_FOR_MANAGE, sessionContext.getUserName(),
                    localGroupName);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("管理员ID:{}拥有的用户组集合为{}", sessionContext.getUserId(), JSON.toJSONString(listUserGroupIdResponse.getUserGroupIdList()));
        }
        for (UUID id : deleteGroupIdList) {
            if (!listUserGroupIdResponse.getUserGroupIdList().contains(id.toString())) {
                tip.append("," + getUserGroupName(id));
            }
        }
        String resultTip = tip.toString();
        if (StringUtils.isNotBlank(resultTip)) {
            String checkPermissionTip = StringUtils.substring(resultTip, 1);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_ADMIN_NOT_HAS_BELONG_USER_GROUP_PERMISSION_FOR_MANAGE, sessionContext.getUserName(),
                    localGroupName, checkPermissionTip);
        }

    }

    /**
     * 获取用户组名称
     *
     * @param id uuid
     * @return 名称
     */
    public String getUserGroupName(UUID id) {
        Assert.notNull(id, "id cannot null");

        String localGroupName = id.toString();
        try {
            IacUserGroupDetailDTO groupDTO = cbbUserGroupAPI.getUserGroupDetail(id);
            localGroupName = groupDTO.getName();
        } catch (Exception e) {
            LOGGER.error("get terminal group label", e);
        }
        return localGroupName;
    }

    /**
     * 添加池的分配信息
     *
     * @param request         参数
     * @param userGroupVOList userGroupVOList
     * @throws BusinessException 业务异常
     */
    public void addPoolAssignInfo(ListUserGroupWebRequest request, List<UserGroupVO> userGroupVOList) throws BusinessException {
        Assert.notNull(request, "request is null");
        Assert.notNull(userGroupVOList, "userGroupVOList is null");
        if (Objects.nonNull(request.getDesktopPoolId())) {
            // 如果有桌面池ID参数，需要添加桌面池的分配信息
            this.addDesktopPoolAssignInfo(request.getDesktopPoolId(), userGroupVOList);
            return;
        }
        if (Objects.nonNull(request.getDiskPoolId())) {
            this.addDiskPoolAssignInfo(request.getDiskPoolId(), userGroupVOList);
            return;
        }
        if (Objects.nonNull(request.getAppGroupId())) {
            this.addAppGroupAssignInfo(request.getAppGroupId(), userGroupVOList);
            return;
        }

        if (Boolean.TRUE.equals(request.getEnableCountGroupUser())) {
            this.addGroupUserCount(userGroupVOList);
        }
    }

    /**
     * 添加对应桌面池的分配信息
     *
     * @param desktopPoolId   桌面池ID
     * @param userGroupVOList userGroupVOList
     * @throws BusinessException 业务异常
     */
    public void addDesktopPoolAssignInfo(UUID desktopPoolId, List<UserGroupVO> userGroupVOList) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId is null");
        Assert.notNull(userGroupVOList, "userGroupVOList is null");
        if (CollectionUtils.isEmpty(userGroupVOList)) {
            return;
        }
        // 添加用户组下用户数量属性
        Map<String, Integer> groupUserNumMap = getGroupUserNumMap();

        // 已经分配到桌面池的组集合
        List<DesktopPoolUserDTO> assignedGroupList = desktopPoolUserMgmtAPI.listDesktopPoolUser(desktopPoolId, IacConfigRelatedType.USERGROUP);
        Set<String> assignedGroupSet = assignedGroupList.stream().map(item -> item.getRelatedId().toString()).collect(Collectors.toSet());

        // 各个用户组下已分配的数量
        List<UserGroupAssignedUserNumDTO> groupAssignedUserNumList = desktopPoolUserMgmtAPI.countAssignedUserNumInGroupByDesktopPoolId(desktopPoolId);
        Map<String, Integer> groupAssignedUserNumMap = groupAssignedUserNumList.stream().filter(item -> Objects.nonNull(item.getGroupId()))
                .collect(Collectors.toMap(item -> item.getGroupId().toString(), UserGroupAssignedUserNumDTO::getAssignedUserNum));

        // 用户组下所有用户分别绑定了多少个桌面
        DesktopPoolBasicDTO desktopPoolBasicDTO = desktopPoolMgmtAPI.getDesktopPoolBasicById(desktopPoolId);
        Map<String, Long> bindUserDeskNumMap;
        int total = 0;
        if (desktopPoolBasicDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            List<HostUserGroupBindDeskNumDTO> bindDeskNumList = hostUserAPI.countGroupDeskNumByDesktopPoolId(desktopPoolId);
            bindUserDeskNumMap = new HashMap<>();
            for (HostUserGroupBindDeskNumDTO bindDeskNumDTO : bindDeskNumList) {
                if (Objects.isNull(bindDeskNumDTO.getGroupId())) {
                    continue;
                }
                long num = Optional.ofNullable(bindDeskNumDTO.getDeskNum()).orElse(0L);
                bindUserDeskNumMap.put(bindDeskNumDTO.getGroupId().toString(), num);
                total += num;
            }
        } else {
            List<PoolDesktopInfoDTO> bindUserDeskList = desktopPoolMgmtAPI.listBindUserDeskInfoByDesktopPoolId(desktopPoolId);
            total = bindUserDeskList.size();
            bindUserDeskNumMap = bindUserDeskList.stream().filter(item -> Objects.nonNull(item.getUserGroupId()))
                    .collect(Collectors.groupingBy(item -> item.getUserGroupId().toString(), Collectors.counting()));
        }

        int totalAssignedNum = 0;
        int allGroupTotalUserNum = 0;
        UserGroupVO rootGroup = null;
        for (UserGroupVO groupVO : userGroupVOList) {
            groupVO.setTotalUserNum(Optional.ofNullable(groupUserNumMap.get(groupVO.getId())).orElse(0));
            groupVO.setAssignedUserNum(Optional.ofNullable(groupAssignedUserNumMap.get(groupVO.getId())).orElse(0));
            groupVO.setIsAssigned(assignedGroupSet.contains(groupVO.getId()));
            totalAssignedNum += groupVO.getAssignedUserNum();
            allGroupTotalUserNum += groupVO.getTotalUserNum();
            if (Objects.equals(USER_GROUP_ROOT_ID, groupVO.getId())) {
                rootGroup = groupVO;
            }
            Long bindNum = bindUserDeskNumMap.get(groupVO.getId());
            groupVO.setBindDesktopNum(Objects.nonNull(bindNum) ? bindNum.intValue() : 0);
        }
        if (Objects.nonNull(rootGroup)) {
            rootGroup.setTotalUserNum(allGroupTotalUserNum);
            rootGroup.setAssignedUserNum(totalAssignedNum);
            rootGroup.setBindDesktopNum(total);
        }
    }

    /**
     * 添加对应磁盘池的分配信息
     *
     * @param diskPoolId      磁盘池ID
     * @param userGroupVOList userGroupVOList
     * @throws BusinessException 业务异常
     */
    public void addDiskPoolAssignInfo(UUID diskPoolId, List<UserGroupVO> userGroupVOList) throws BusinessException {
        Assert.notNull(diskPoolId, "diskPoolId is null");
        Assert.notNull(userGroupVOList, "userGroupVOList is null");
        if (CollectionUtils.isEmpty(userGroupVOList)) {
            return;
        }
        // 1.添加用户组下用户数量属性
        Map<String, Integer> groupUserNumMap = getGroupUserNumMap();

        // 2.已经分配到池的组集合
        List<DiskPoolUserDTO> allAssignedGroupList = diskPoolUserAPI.listDiskPoolUserByRelatedType(IacConfigRelatedType.USERGROUP);

        List<DiskPoolUserDTO> assignedGroupList =
                allAssignedGroupList.stream().filter(userDTO -> Objects.equals(diskPoolId, userDTO.getDiskPoolId())).collect(Collectors.toList());
        Set<String> assignedGroupSet = assignedGroupList.stream().map(item -> item.getRelatedId().toString()).collect(Collectors.toSet());

        // 3.各个用户组下已分配的数量
        List<UserGroupAssignedUserNumDTO> groupAssignedUserNumList = diskPoolUserAPI.countAssignedUserNumInGroupByDiskPoolId(diskPoolId);
        Map<String, Integer> groupAssignedUserNumMap = groupAssignedUserNumList.stream().filter(item -> Objects.nonNull(item.getGroupId()))
                .collect(Collectors.toMap(item -> item.getGroupId().toString(), UserGroupAssignedUserNumDTO::getAssignedUserNum));

        // 4.添加不能选择的组disabled
        List<DiskPoolUserDTO> disableGroupList =
                allAssignedGroupList.stream().filter(userDTO -> !Objects.equals(diskPoolId, userDTO.getDiskPoolId())).collect(Collectors.toList());
        Set<String> disableGroupSet = disableGroupList.stream().map(item -> item.getRelatedId().toString()).collect(Collectors.toSet());

        // 获取各个组内不能选的用户数量
        List<UserGroupDisabledUserNumDTO> groupDisabledUserNumList = diskPoolUserAPI.countDisabledUserNumInGroupByDiskPoolId(diskPoolId);
        Map<String, Integer> groupDisabledUserNumMap = groupDisabledUserNumList.stream().filter(item -> Objects.nonNull(item.getGroupId()))
                .collect(Collectors.toMap(item -> item.getGroupId().toString(), UserGroupDisabledUserNumDTO::getDisabledUserNum));
        // 获取该池内已绑定磁盘的数量
        Map<String, Integer> groupBindDiskNumMap = userDiskMgmtAPI.countGroupBindDiskNumByDiskPoolId(diskPoolId).stream()
                .collect(Collectors.toMap(item -> item.getGroupId().toString(), UserGroupBindDiskNumDTO::getBindDiskNum));
        int totalAssignedNum = 0;
        int allGroupTotalUserNum = 0;
        int totalDisableUserNum = 0;
        int totalBindDiskNum = 0;
        UserGroupVO rootGroup = null;
        for (UserGroupVO groupVO : userGroupVOList) {
            groupVO.setDisableUserNum(Math.max(disableGroupSet.contains(groupVO.getId())
                    ? groupUserNumMap.get(groupVO.getId()) - Optional.ofNullable(groupAssignedUserNumMap.get(groupVO.getId())).orElse(0)
                    : Optional.ofNullable(groupDisabledUserNumMap.get(groupVO.getId())).orElse(0), 0));
            // totalUserNum需要减去disabled用户数量
            groupVO.setTotalUserNum(Math.max(Optional.ofNullable(groupUserNumMap.get(groupVO.getId())).orElse(0) - groupVO.getDisableUserNum(), 0));
            groupVO.setAssignedUserNum(Optional.ofNullable(groupAssignedUserNumMap.get(groupVO.getId())).orElse(0));
            groupVO.setIsAssigned(assignedGroupSet.contains(groupVO.getId()));
            groupVO.setDisabled(disableGroupSet.contains(groupVO.getId()) || groupVO.isDisabled());
            groupVO.setBindDiskNum(Optional.ofNullable(groupBindDiskNumMap.get(groupVO.getId())).orElse(0));
            totalAssignedNum += groupVO.getAssignedUserNum();
            allGroupTotalUserNum += groupVO.getTotalUserNum();
            totalDisableUserNum += groupVO.getDisableUserNum();
            totalBindDiskNum += groupVO.getBindDiskNum();
            if (Objects.equals(USER_GROUP_ROOT_ID, groupVO.getId())) {
                rootGroup = groupVO;
            }
        }
        if (Objects.nonNull(rootGroup)) {
            rootGroup.setTotalUserNum(allGroupTotalUserNum);
            rootGroup.setAssignedUserNum(totalAssignedNum);
            rootGroup.setDisableUserNum(totalDisableUserNum);
            rootGroup.setBindDiskNum(totalBindDiskNum);
        }
    }

    /**
     * 添加对应应用分组的分配信息
     *
     * @param appGroupId      应用分组id
     * @param userGroupVOList userGroupVOList
     * @throws BusinessException businessException
     */
    public void addAppGroupAssignInfo(UUID appGroupId, List<UserGroupVO> userGroupVOList) throws BusinessException {
        Assert.notNull(appGroupId, "appGroupId is null");
        Assert.notNull(userGroupVOList, "userGroupVOList is null");
        if (CollectionUtils.isEmpty(userGroupVOList)) {
            return;
        }
        // 1.添加用户组下用户数量属性
        Map<String, Integer> groupUserNumMap = getGroupUserNumMap();

        // 2.已经分配到应用分组的用户组集合
        List<RcaGroupMemberDTO> groupMemberDTOList = rcaGroupMemberAPI.listGroupMember(appGroupId, RcaEnum.GroupMemberType.USER_GROUP);
        Set<String> assignedGroupSet = groupMemberDTOList.stream().map(item -> item.getMemberId().toString()).collect(Collectors.toSet());

        // 3.各个用户组下已分配的数量
        List<RcaGroupAssignedUserNumDTO> groupAssignedUserNumDTOList = rcaGroupMemberAPI.countAssignedUserNumInGroupByAppGroupId(appGroupId);
        Map<String, Integer> groupAssignedUserNumMap = groupAssignedUserNumDTOList.stream().filter(item -> Objects.nonNull(item.getUserGroupId()))
                .collect(Collectors.toMap(item -> item.getUserGroupId().toString(), RcaGroupAssignedUserNumDTO::getAssignedUserNum));

        Map<UUID, Set<UUID>> userGroupIdWithUserIdMap = getUserGroupHostMap(appGroupId);

        int totalAssignedNum = 0;
        int allGroupTotalUserNum = 0;
        UserGroupVO rootGroup = null;
        for (UserGroupVO groupVO : userGroupVOList) {
            groupVO.setTotalUserNum(Optional.ofNullable(groupUserNumMap.get(groupVO.getId())).orElse(0));
            groupVO.setAssignedUserNum(Optional.ofNullable(groupAssignedUserNumMap.get(groupVO.getId())).orElse(0));
            groupVO.setIsAssigned(assignedGroupSet.contains(groupVO.getId()));
            totalAssignedNum += groupVO.getAssignedUserNum();
            allGroupTotalUserNum += groupVO.getTotalUserNum();
            if (Objects.equals(USER_GROUP_ROOT_ID, groupVO.getId())) {
                rootGroup = groupVO;
            } else {
                Set<UUID> hostIdSet = userGroupIdWithUserIdMap.get(UUID.fromString(groupVO.getId()));
                groupVO.setBindHostNum(CollectionUtils.isEmpty(hostIdSet) ? 0 : hostIdSet.size());
            }
            groupVO.setBindDesktopNum(0);
        }
        if (Objects.nonNull(rootGroup)) {
            rootGroup.setTotalUserNum(allGroupTotalUserNum);
            rootGroup.setAssignedUserNum(totalAssignedNum);
            rootGroup.setBindDesktopNum(0);

            Set<UUID> hostIdSet = new HashSet<>();
            for (Set<UUID> groupHostIdSet : userGroupIdWithUserIdMap.values()) {
                hostIdSet.addAll(groupHostIdSet);
            }
            rootGroup.setBindHostNum(CollectionUtils.isEmpty(hostIdSet) ? 0 : hostIdSet.size());
        }
    }

    private Map<UUID, Set<UUID>> getUserGroupHostMap(UUID appGroupId) throws BusinessException {
        List<RcaHostSessionDTO> rcaHostSessionDTOList = new ArrayList<>();
        RcaGroupDTO groupDTO = rcaAppGroupAPI.getGroupDTO(appGroupId);
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(groupDTO.getPoolId());
        if (RcaEnum.PoolType.STATIC == appPoolBaseDTO.getPoolType()) {
            rcaHostSessionDTOList = rcaHostSessionAPI.listByPoolId(appPoolBaseDTO.getId());
        }

        Map<UUID, Set<UUID>> userWithHostIdMap = new HashMap<>();
        Map<UUID, Set<UUID>> userGroupIdWithUserIdMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(rcaHostSessionDTOList)) {
            List<UUID> userIdList = rcaHostSessionDTOList.stream().map(RcaHostSessionDTO::getUserId).collect(Collectors.toList());
            for (RcaHostSessionDTO rcaHostSessionDTO : rcaHostSessionDTOList) {
                Set<UUID> hostIdSet = userWithHostIdMap.getOrDefault(rcaHostSessionDTO.getUserId(), new HashSet<>());
                hostIdSet.add(rcaHostSessionDTO.getHostId());
                userWithHostIdMap.put(rcaHostSessionDTO.getUserId(), hostIdSet);
            }

            List<IacUserDetailDTO> iacUserDetailDTOList = new ArrayList<>();
            List<List<UUID>> tempIdList = Lists.partition(userIdList, SQL_IN_MAX_NUM);
            for (List<UUID> idList : tempIdList) {
                iacUserDetailDTOList.addAll(cbbUserAPI.listUserByUserIds(idList));
            }
            for (IacUserDetailDTO iacUserDetailDTO : iacUserDetailDTOList) {
                Set<UUID> userIdSet = userGroupIdWithUserIdMap.getOrDefault(iacUserDetailDTO.getGroupId(), new HashSet<>());
                userIdSet.add(iacUserDetailDTO.getId());
                userGroupIdWithUserIdMap.put(iacUserDetailDTO.getGroupId(), userIdSet);
            }
        }

        Map<UUID, Set<UUID>> userGroupIdWithHostMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(userGroupIdWithUserIdMap)) {
            for (Map.Entry<UUID, Set<UUID>> entry : userGroupIdWithUserIdMap.entrySet()) {
                Set<UUID> userIdList = entry.getValue();
                Set<UUID> groupHostIdList = new HashSet<>();
                for (UUID userId : userIdList) {
                    groupHostIdList.addAll(userWithHostIdMap.get(userId));
                }
                userGroupIdWithHostMap.put(entry.getKey(), groupHostIdList);
            }
        }
        return userGroupIdWithHostMap;
    }

    /**
     * 添加用户总数信息
     *
     * @param userGroupVOList userGroupVOList
     * @throws BusinessException businessException
     */
    public void addGroupUserCount(List<UserGroupVO> userGroupVOList) throws BusinessException {
        Assert.notNull(userGroupVOList, "userGroupVOList is null");
        if (CollectionUtils.isEmpty(userGroupVOList)) {
            return;
        }
        // 1.添加用户组下用户数量属性
        Map<String, Integer> groupUserNumMap = getGroupUserNumMap();

        int allGroupTotalUserNum = 0;
        UserGroupVO rootGroup = null;
        for (UserGroupVO groupVO : userGroupVOList) {
            groupVO.setTotalUserNum(Optional.ofNullable(groupUserNumMap.get(groupVO.getId())).orElse(0));
            allGroupTotalUserNum += groupVO.getTotalUserNum();
            if (Objects.equals(USER_GROUP_ROOT_ID, groupVO.getId())) {
                rootGroup = groupVO;
            }
        }
        if (Objects.nonNull(rootGroup)) {
            rootGroup.setTotalUserNum(allGroupTotalUserNum);
        }
    }

    private Map<String, Integer> getGroupUserNumMap() throws BusinessException {
        List<String> userTypeList = Lists.newArrayList(IacUserTypeEnum.NORMAL.name(), IacUserTypeEnum.AD.name(),
                IacUserTypeEnum.LDAP.name(), IacUserTypeEnum.THIRD_PARTY.name());
        List<IacUserGroupUserNumDTO> groupUserNumList = cbbUserGroupAPI.countAllGroupUserNumByUserType(userTypeList);
        return groupUserNumList.stream().collect(Collectors.toMap(item -> item.getGroupId().toString(), IacUserGroupUserNumDTO::getUserNum));
    }

    /**
     * 过滤池未关联的用户组
     *
     * @param request         请求
     * @param userGroupVOList 用户ID
     * @return 过滤后的用户组
     * @throws BusinessException businessException
     */
    public List<UserGroupVO> filterPoolNotRelaGroupVOArr(ListUserGroupWebRequest request, List<UserGroupVO> userGroupVOList)
            throws BusinessException {
        Assert.notNull(request, "filterDiskPollNotGroupVOArr request is null");
        Assert.notNull(userGroupVOList, "filterDiskPollNotGroupVOArr userGroupVOList is null");
        // 关联的用户组
        Set<String> relationUserGroup;
        if (Objects.nonNull(request.getDiskPoolId())) {
            relationUserGroup = diskPoolUserAPI.getDiskPoolRelationUserGroup(request.getDiskPoolId());
        } else if (Objects.nonNull(request.getDesktopPoolId())) {
            relationUserGroup = desktopPoolUserMgmtAPI.getDesktopPoolRelationUserGroup(request.getDesktopPoolId());
        } else {
            relationUserGroup = rcaGroupMemberAPI.getAppGroupRelationUserGroup(request.getAppGroupId());
        }
        List<UserGroupVO> hasPermissionDTOList = new ArrayList<>();
        for (UserGroupVO userGroupVO : userGroupVOList) {
            if (relationUserGroup.contains(userGroupVO.getId())) {
                userGroupVO.setDisabled(false);
                hasPermissionDTOList.add(userGroupVO);
            }
        }
        return hasPermissionDTOList;
    }
}
