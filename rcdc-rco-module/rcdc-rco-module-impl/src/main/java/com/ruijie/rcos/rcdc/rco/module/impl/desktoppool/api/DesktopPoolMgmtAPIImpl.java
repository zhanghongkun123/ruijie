package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbUpdateDeskSpecRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDesktopImageUpdateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbUpdateDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbCreateDeskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbLoadBalanceStrategyEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopNetworkAddressDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopNetworkDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateThirdPartyDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.EditDesktopNetworkRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.EditDesktopStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ImageTemplateConstants;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.DeskSpecDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.ExtraDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.constants.DesktopPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.user.common.UserCommonHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service.RcoAdGroupService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolAPIHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.cache.DesktopPoolCache;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.PoolModelOverviewDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.UserBindDesktopNumDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.*;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CreateDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryUserListService;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoDeskInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoDeskInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.DesktopPoolServiceTx;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 池桌面管理API接口实现类
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年12月06日
 *
 * @author linke
 */
public class DesktopPoolMgmtAPIImpl implements DesktopPoolMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolMgmtAPIImpl.class);

    private static final String DELIMITER = "，";

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private DesktopPoolService desktopPoolService;

    @Autowired
    private DesktopPoolUserService desktopPoolUserService;

    @Autowired
    private CreateDesktopService createDesktopService;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private UserDesktopPoolService userDesktopPoolService;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private DesktopPoolConfigService desktopPoolConfigService;

    @Autowired
    private QueryUserListService queryUserListService;

    @Autowired
    private RcoDeskInfoDAO rcoDeskInfoDAO;

    @Autowired
    private DesktopPoolAPIHelper desktopPoolAPIHelper;

    @Autowired
    private DesktopPoolDashboardAPI desktopPoolDashboardAPI;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    @Autowired
    private RcoAdGroupService adGroupService;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private StoragePoolAPI storagePoolAPI;

    @Autowired
    private UserCommonHelper userCommonHelper;

    @Autowired
    private DesktopPoolCache desktopPoolCache;

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private DesktopPoolServiceTx desktopPoolServiceTx;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private DesktopPoolComputerService desktopPoolComputerService;

    @Override
    public CloudDesktopDetailDTO queryDesktopPoolStrategyDetail(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(request.getId(), "id must not be null");
        DesktopPoolBasicDTO desktopPoolBasicDTO = desktopPoolService.getDesktopPoolBasicById(request.getId());
        CloudDesktopDetailDTO dto = new CloudDesktopDetailDTO();
        if (desktopPoolBasicDTO.getPoolType() == CbbDesktopPoolType.VDI) {
            CbbDeskStrategyVDIDTO deskStrategyVDIDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(desktopPoolBasicDTO.getStrategyId());
            dto.setDeskCreateMode(deskStrategyVDIDTO.getDeskCreateMode().name());
            CbbImageTemplateDetailDTO imageTemplate = cbbImageTemplateMgmtAPI.getImageTemplateDetail(desktopPoolBasicDTO.getImageTemplateId());
            dto.setCbbImageType(imageTemplate.getCbbImageType().name());
        }
        if (Objects.nonNull(desktopPoolBasicDTO.getDesktopType())) {
            dto.setDesktopType(desktopPoolBasicDTO.getDesktopType().name());
        }
        dto.setDesktopStrategyId(desktopPoolBasicDTO.getStrategyId());
        dto.setDesktopStrategyName(desktopPoolBasicDTO.getStrategyName());
        return dto;
    }

    @Override
    public DesktopNetworkDTO queryDesktopPoolNetworkDetail(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(request.getId(), "id must not be null");
        DesktopPoolBasicDTO desktopPoolBasicDTO = desktopPoolService.getDesktopPoolBasicById(request.getId());

        UUID networkId = desktopPoolBasicDTO.getNetworkId();
        DesktopNetworkDTO desktopNetworkDTO = new DesktopNetworkDTO();
        try {
            CbbDeskNetworkDetailDTO dto = cbbNetworkMgmtAPI.getDeskNetwork(networkId);
            DesktopNetworkAddressDTO addressDTO = new DesktopNetworkAddressDTO();
            addressDTO.setRow(dto);
            addressDTO.setId(networkId);
            addressDTO.setLabel(dto.getDeskNetworkName());
            desktopNetworkDTO.setAddress(addressDTO);
        } catch (BusinessException e) {
            LOGGER.error("获取桌面池[{}]网络策略失败", desktopPoolBasicDTO.getName(), e);
        }
        return desktopNetworkDTO;
    }

    @Override
    public void updateStrategyId(UUID desktopPoolId, UUID strategyId) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId must not be null");
        Assert.notNull(strategyId, "strategyId must not be null");

        CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);

        desktopPoolAPIHelper.validateEditDesktopPoolStrategy(desktopPoolDTO, strategyId);

        CbbDeskStrategyDTO deskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(strategyId);
        desktopPoolDTO.setStrategyId(deskStrategyDTO.getId());
        cbbDesktopPoolMgmtAPI.updateDesktopPool(desktopPoolDTO);
    }

    @Override
    public void updateNetworkId(UUID desktopPoolId, UUID networkId) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId must not be null");
        Assert.notNull(networkId, "networkId must not be null");

        CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);
        validateBeforeUpdate(desktopPoolDTO, desktopPoolId);

        CbbDeskNetworkDetailDTO dto = cbbNetworkMgmtAPI.getDeskNetwork(networkId);
        desktopPoolDTO.setNetworkId(dto.getId());
        cbbDesktopPoolMgmtAPI.updateDesktopPool(desktopPoolDTO);
    }

    @Override
    public void updateImageTemplateId(UUID desktopPoolId, UUID imageTemplateId) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId must not be null");
        Assert.notNull(imageTemplateId, "imageTemplateId must not be null");

        CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);
        desktopPoolAPIHelper.validateEditDesktopPoolImageTemplateId(desktopPoolDTO, imageTemplateId);

        desktopPoolDTO.setImageTemplateId(imageTemplateId);
        cbbDesktopPoolMgmtAPI.updateDesktopPool(desktopPoolDTO);
    }

    @Override
    public List<PoolDesktopInfoDTO> listNormalDeskInfoByDesktopPoolId(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId must not be null");

        return desktopPoolService.listNormalDeskInfoByDesktopPoolId(desktopPoolId);
    }

    @Override
    public List<PoolDesktopInfoDTO> listAllDeskInfoByDesktopPoolId(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId must not be null");

        return desktopPoolService.listDesktopByDesktopPoolIds(Collections.singletonList(desktopPoolId));
    }

    @Override
    public List<PoolDesktopInfoDTO> listBindUserDeskInfoByDesktopPoolId(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId must not be null");

        return desktopPoolService.listBindUserDesktopByPoolId(desktopPoolId);
    }

    @Override
    public DefaultPageResponse<DesktopPoolDTO> pageQueryByUser(UUID userId, PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(userId, "userId must not be null");

        IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserDetail(userId);
        // 访客返回空
        if (cbbUserDetailDTO.getUserType() == IacUserTypeEnum.VISITOR) {
            DefaultPageResponse<DesktopPoolDTO> resp = new DefaultPageResponse<>();
            resp.setTotal(0);
            resp.setItemArr(new DesktopPoolDTO[0]);
            return resp;
        }

        DefaultPageResponse<DesktopPoolDTO> resp = userDesktopPoolService.pageQueryUserDesktopPool(cbbUserDetailDTO, request);
        if (ArrayUtils.isEmpty(resp.getItemArr())) {
            return resp;
        }
        // 查看全部的计算集群和存储池信息
        Map<UUID, PlatformStoragePoolDTO> storagePoolAllMap =
                storagePoolAPI.queryAllStoragePool().stream().collect(Collectors.
                        toMap(PlatformStoragePoolDTO::getId, storagePoolDTO -> storagePoolDTO, (storage1, storage2) -> storage2));
        Map<UUID, ClusterInfoDTO> clusterInfoAllMap = clusterAPI.queryAllClusterInfoList()
                .stream().collect(Collectors.toMap(ClusterInfoDTO::getId, clusterInfo -> clusterInfo, (clusterInfo1, clusterInfo2) -> clusterInfo2));

        for (DesktopPoolDTO desktopPoolDTO : resp.getItemArr()) {
            fillDesktopPoolConflictDeskNum(desktopPoolDTO);
            // 设置桌面池计算集群和存储池信息
            fillDesktopPoolClusterAndStoragePool(desktopPoolDTO, clusterInfoAllMap, storagePoolAllMap);
        }
        return resp;
    }

    @Override
    public DefaultPageResponse<DesktopPoolDTO> pageQuery(PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        DefaultPageResponse<DesktopPoolBasicDTO> basicResp = desktopPoolService.pageDesktopPool(request);

        DefaultPageResponse<DesktopPoolDTO> resp = new DefaultPageResponse<>();
        resp.setTotal(basicResp.getTotal());
        if (ArrayUtils.isEmpty(basicResp.getItemArr())) {
            resp.setItemArr(new DesktopPoolDTO[0]);
            return resp;
        }
        // 查看全部的计算集群和存储池信息
        Map<UUID, PlatformStoragePoolDTO> storagePoolAllMap =
                storagePoolAPI.queryAllStoragePool().stream().collect(Collectors.
                        toMap(PlatformStoragePoolDTO::getId, storagePoolDTO -> storagePoolDTO, (storage1, storage2) -> storage2));
        Map<UUID, ClusterInfoDTO> clusterInfoAllMap = clusterAPI.queryAllClusterInfoList()
                .stream().collect(Collectors.toMap(ClusterInfoDTO::getId, clusterInfo -> clusterInfo, (clusterInfo1, clusterInfo2) -> clusterInfo2));

        List<DesktopPoolDTO> desktopPoolDTOList = new ArrayList<>();
        for (DesktopPoolBasicDTO basicDTO : basicResp.getItemArr()) {
            DesktopPoolDTO desktopPoolDTO = new DesktopPoolDTO();
            BeanUtils.copyProperties(basicDTO, desktopPoolDTO);
            // 动态池需要补充策略不一致的桌面数量
            fillDesktopPoolConflictDeskNum(desktopPoolDTO);
            // 设置桌面池计算集群和存储池信息
            fillDesktopPoolClusterAndStoragePool(desktopPoolDTO, clusterInfoAllMap, storagePoolAllMap);
            desktopPoolDTOList.add(desktopPoolDTO);
        }
        resp.setItemArr(desktopPoolDTOList.toArray(new DesktopPoolDTO[0]));
        return resp;
    }

    @Override
    public List<DesktopPoolBasicDTO> listByConditions(ConditionQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        return desktopPoolService.listByConditions(request);
    }

    @Override
    public long countByConditions(ConditionQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        return desktopPoolService.countByConditions(request);
    }

    @Override
    public List<CbbDesktopPoolDTO> listDesktopPoolByUserId(UUID userId, IacUserTypeEnum userType) throws BusinessException {
        Assert.notNull(userId, "userId must not be null");
        Assert.notNull(userType, "userType can not be null");
        // 分别查询用户及其组所关联的池
        List<UUID> ipPoolRelatedIdList = Lists.newArrayList(userId);
        UUID groupId = cbbUserAPI.getUserGroupId(userId);
        ipPoolRelatedIdList.add(groupId);
        if (userType == IacUserTypeEnum.AD) {
            List<UUID> adGroupIdList = adGroupService.getUserRelatedAdGroupList(userId);
            if (ObjectUtils.isNotEmpty(adGroupIdList)) {
                ipPoolRelatedIdList.addAll(adGroupIdList);
            }
        }
        List<UUID> poolIdList = desktopPoolUserService.listPoolIdByRelatedIdIn(ipPoolRelatedIdList);


        if (CollectionUtils.isEmpty(poolIdList)) {
            return Collections.emptyList();
        }
        // 去除重复的
        poolIdList = poolIdList.stream().distinct().collect(Collectors.toList());
        return cbbDesktopPoolMgmtAPI.listDesktopPoolByIdList(poolIdList);
    }

    @Override
    public DesktopPoolOverviewDTO getDesktopPoolOverview(@Nullable CbbDesktopPoolModel poolModel) {
        DesktopPoolOverviewDTO overviewDTO = new DesktopPoolOverviewDTO();
        List<CbbDesktopPoolDTO> cbbDesktopPoolDTOList;
        if (Objects.isNull(poolModel)) {
            cbbDesktopPoolDTOList = cbbDesktopPoolMgmtAPI.listAllDesktopPool();
        } else {
            cbbDesktopPoolDTOList = cbbDesktopPoolMgmtAPI.listDesktopPoolByPoolModel(poolModel);
        }
        if (CollectionUtils.isEmpty(cbbDesktopPoolDTOList)) {
            return overviewDTO;
        }
        overviewDTO.setPoolNum(cbbDesktopPoolDTOList.size());
        List<CbbDesktopPoolModel> poolModelList = new ArrayList<>();
        if (Objects.isNull(poolModel)) {
            poolModelList.add(CbbDesktopPoolModel.STATIC);
            poolModelList.add(CbbDesktopPoolModel.DYNAMIC);
        } else {
            poolModelList.add(poolModel);
        }
        List<PoolModelOverviewDTO> modelOverviewList = desktopPoolService.countPoolOverviewByModel(poolModelList);
        if (CollectionUtils.isEmpty(modelOverviewList)) {
            overviewDTO.setDesktopNum(0);
            overviewDTO.setFault(0);
            overviewDTO.setRunning(0);
            overviewDTO.setFree(0);
            overviewDTO.setClose(0);
            overviewDTO.setConnectedNum(0);
            return overviewDTO;
        }
        int faultNum = 0;
        int freeNum = 0;
        int runningNum = 0;
        int totalNum = 0;
        for (PoolModelOverviewDTO modelOverview : modelOverviewList) {
            totalNum += Optional.ofNullable(modelOverview.getTotalNum()).orElse(0L);
            faultNum += Optional.ofNullable(modelOverview.getFaultNum()).orElse(0L);
            freeNum += Optional.ofNullable(modelOverview.getFreeNum()).orElse(0L);
            runningNum += Optional.ofNullable(modelOverview.getRunningNum()).orElse(0L);
        }
        overviewDTO.setDesktopNum(totalNum);
        overviewDTO.setFault(faultNum);
        overviewDTO.setRunning(runningNum);
        overviewDTO.setFree(freeNum);
        overviewDTO.setClose(totalNum - runningNum);
        overviewDTO.setConnectedNum(desktopPoolDashboardAPI.getCurrentDesktopPoolInfo(null, null, null).getUsedCount());
        return overviewDTO;
    }

    @Override
    public DesktopPoolBasicDTO getDesktopPoolBasicById(UUID desktopPoolId) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId must not be null");
        return desktopPoolService.getDesktopPoolBasicById(desktopPoolId);
    }

    @Override
    public DesktopPoolDetailDTO getDesktopPoolDetail(UUID desktopPoolId) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId must not be null");
        DesktopPoolBasicDTO desktopPoolBasicDTO = desktopPoolService.getDesktopPoolBasicById(desktopPoolId);

        return convertDesktopPoolDetail(desktopPoolBasicDTO);
    }

    private DesktopPoolDetailDTO convertDesktopPoolDetail(DesktopPoolBasicDTO desktopPoolBasicDTO) throws BusinessException {
        DesktopPoolDetailDTO desktopPoolDetailDTO = new DesktopPoolDetailDTO();
        BeanUtils.copyProperties(desktopPoolBasicDTO, desktopPoolDetailDTO);
        fillDesktopPoolStrategyDetail(desktopPoolDetailDTO);
        fillDesktopPoolConflictDeskNum(desktopPoolDetailDTO);

        // 查看全部的计算集群和存储池信息
        Map<UUID, PlatformStoragePoolDTO> storagePoolAllMap = storagePoolAPI.queryAllStoragePool().stream()
                .collect(Collectors.toMap(PlatformStoragePoolDTO::getId, storagePoolDTO -> storagePoolDTO, (storage1, storage2) -> storage2));
        Map<UUID, ClusterInfoDTO> clusterInfoAllMap = clusterAPI.queryAllClusterInfoList().stream()
                .collect(Collectors.toMap(ClusterInfoDTO::getId, clusterInfo -> clusterInfo, (clusterInfo1, clusterInfo2) -> clusterInfo2));
        fillDesktopPoolClusterAndStoragePool(desktopPoolDetailDTO, clusterInfoAllMap, storagePoolAllMap);
        fillDesktopPoolExtraDiskStoragePool(desktopPoolDetailDTO, storagePoolAllMap);
        fillDesktopPoolUserAndDeskNumDetail(desktopPoolDetailDTO);
        return desktopPoolDetailDTO;
    }

    private void fillDesktopPoolStrategyDetail(DesktopPoolDetailDTO desktopPoolDetailDTO) throws BusinessException {
        CbbDeskStrategyDTO cbbDeskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(desktopPoolDetailDTO.getStrategyId());
        desktopPoolDetailDTO.setEnableOpenDesktopRedirect(cbbDeskStrategyDTO.getOpenDesktopRedirect());
    }

    private void fillDesktopPoolExtraDiskStoragePool(DesktopPoolDetailDTO desktopPoolDetailDTO, Map<UUID, PlatformStoragePoolDTO> storagePoolMap) {
        if (CollectionUtils.isEmpty(desktopPoolDetailDTO.getExtraDiskList())) {
            return;
        }
        for (ExtraDiskDTO extraDiskDTO : desktopPoolDetailDTO.getExtraDiskList()) {
            if (storagePoolMap.containsKey(extraDiskDTO.getAssignedStoragePoolId())) {
                extraDiskDTO.setExtraDiskStoragePool(getAssignStoragePool(extraDiskDTO.getAssignedStoragePoolId(), storagePoolMap));
            }
        }
        desktopPoolDetailDTO.setExtraDiskArr(desktopPoolDetailDTO.getExtraDiskList().toArray(new ExtraDiskDTO[0]));
    }

    private void fillDesktopPoolUserAndDeskNumDetail(DesktopPoolDetailDTO desktopPoolDetailDTO) {
        // 计算关联的用户数量
        desktopPoolDetailDTO.setBindUserNum(getBindUserNum(desktopPoolDetailDTO.getId()));
        List<PoolModelOverviewDTO> overviewDTOList = desktopPoolService.countPoolOverviewByIds(Lists.newArrayList(desktopPoolDetailDTO.getId()));
        if (CollectionUtils.isEmpty(overviewDTOList)) {
            return;
        }
        int faultNum = 0;
        int runningNum = 0;
        int freeNum = 0;
        for (PoolModelOverviewDTO overviewDTO : overviewDTOList) {
            faultNum += Optional.ofNullable(overviewDTO.getFaultNum()).orElse(0L);
            runningNum += Optional.ofNullable(overviewDTO.getRunningNum()).orElse(0L);
            freeNum += Optional.ofNullable(overviewDTO.getFreeNum()).orElse(0L);
        }
        desktopPoolDetailDTO.setFault(faultNum);
        desktopPoolDetailDTO.setRunning(runningNum);
        desktopPoolDetailDTO.setFree(freeNum);
        desktopPoolDetailDTO.setClose(desktopPoolDetailDTO.getDesktopNum() - runningNum);
    }

    private int getBindUserNum(UUID desktopPoolId) {
        // 计算关联的用户数量
        PageSearchRequest request = new PageSearchRequest();
        userCommonHelper.dealNonVisitorUserTypeMatch(request);
        request.setLimit(10);
        request.setPage(0);

        Page<RcoViewUserEntity> page = queryUserListService.pageUserInOrNotInDesktopPool(desktopPoolId, request, true);
        return (int) page.getTotalElements();
    }

    @Override
    public CreateDesktopResponse createDesktop(CreatePoolDesktopRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        try {
            desktopPoolCache.checkUserAndIncreaseDesktopNum(request.getPoolId(), request.getDesktopId(), null);
            UserDesktopEntity userDesktopEntity = createDesktopService.createPoolDesktop(request);
            return new CreateDesktopResponse(userDesktopEntity.getCbbDesktopId(), userDesktopEntity.getDesktopName());
        } finally {
            desktopPoolCache.reduceDesktopNum(request.getPoolId(), request.getDesktopId(), null);
        }
    }

    @Override
    public CreateDesktopResponse createThirdPartyDesktop(CreatePoolComputerDesktopRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        try {
            desktopPoolCache.checkUserAndIncreaseDesktopNum(request.getPoolId(), request.getDesktopId(), null);
            CreateThirdPartyDesktopRequest createThirdPartyDesktopRequest = new CreateThirdPartyDesktopRequest();
            createThirdPartyDesktopRequest.setSoftwareStrategyId(request.getSoftwareStrategyId());
            createThirdPartyDesktopRequest.setDesktopName(request.getDesktopName());
            createThirdPartyDesktopRequest.setStrategyId(request.getStrategyId());
            createThirdPartyDesktopRequest.setUserProfileStrategyId(request.getUserProfileStrategyId());
            createThirdPartyDesktopRequest.setPoolId(request.getPoolId());
            createThirdPartyDesktopRequest.setDeskId(request.getDesktopId());
            createThirdPartyDesktopRequest.setPoolName(request.getPoolName());
            createThirdPartyDesktopRequest.setPoolDeskType(request.getPoolDeskType());
            createThirdPartyDesktopRequest.setOsName(request.getOsName());
            createThirdPartyDesktopRequest.setAgentVersion(request.getAgentVersion());
            createDesktopService.createThirdParty(createThirdPartyDesktopRequest);
            return new CreateDesktopResponse(request.getDesktopId(), request.getDesktopName());
        } finally {
            desktopPoolCache.reduceDesktopNum(request.getPoolId(), request.getDesktopId(), null);
        }
    }

    @Override
    public void deleteDesktopPool(UUID desktopPoolId) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        cbbDesktopPoolMgmtAPI.deleteDesktopPool(desktopPoolId);

        // 删除RCCM集群缓存关系
        desktopPoolAPIHelper.deleteRccmUserByDesktopPoolId(desktopPoolId);

        // 删除池和用户的绑定关系
        desktopPoolUserService.deleteByDesktopPoolId(desktopPoolId);

        // 删除PC终端绑定关系
        desktopPoolComputerService.deleteByDesktopPoolId(desktopPoolId);
    }

    @Override
    public void updateUserProfileStrategy(UUID desktopPoolId, UUID userProfileStrategyId) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(userProfileStrategyId, "userProfileStrategyId can not be null");
        LOGGER.info("桌面池[{}]变更UPM配置策略策略，userProfileStrategyId[{}]", desktopPoolId, userProfileStrategyId);

        DesktopPoolConfigDTO desktopPoolConfigDTO = findPoolConfigByDesktopPoolId(desktopPoolId);
        desktopPoolConfigDTO.setUserProfileStrategyId(userProfileStrategyId);
        desktopPoolConfigService.saveOrUpdateDesktopPoolConfig(desktopPoolConfigDTO);
    }

    @Override
    public void unbindUserProfileStrategy(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        DesktopPoolConfigDTO desktopPoolConfigDTO = desktopPoolConfigService.queryByDesktopPoolId(desktopPoolId);
        if (Objects.isNull(desktopPoolConfigDTO) || Objects.isNull(desktopPoolConfigDTO.getUserProfileStrategyId())) {
            LOGGER.info("桌面池[{}]不存在或没有绑定UPM配置策略，无需进行删除操作", desktopPoolId);
            return;
        }
        desktopPoolConfigDTO.setUserProfileStrategyId(null);
        desktopPoolConfigService.saveOrUpdateDesktopPoolConfig(desktopPoolConfigDTO);
        LOGGER.info("桌面池[{}]删除UPM配置策略", desktopPoolId);
    }

    @Override
    public void updateSoftwareStrategy(UUID desktopPoolId, UUID softwareStrategyId) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(softwareStrategyId, "softwareStrategyId can not be null");
        LOGGER.info("桌面池[{}]变更软件管控策略，softwareStrategyId[{}]", desktopPoolId, softwareStrategyId);

        DesktopPoolConfigDTO desktopPoolConfigDTO = findPoolConfigByDesktopPoolId(desktopPoolId);
        desktopPoolConfigDTO.setSoftwareStrategyId(softwareStrategyId);
        desktopPoolConfigService.saveOrUpdateDesktopPoolConfig(desktopPoolConfigDTO);
    }

    private DesktopPoolConfigDTO findPoolConfigByDesktopPoolId(UUID desktopPoolId) throws BusinessException {
        CbbDesktopPoolDTO desktopPoolDetail = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);
        validateBeforeUpdate(desktopPoolDetail, desktopPoolId);

        DesktopPoolConfigDTO desktopPoolConfigDTO = desktopPoolConfigService.queryByDesktopPoolId(desktopPoolId);
        if (Objects.isNull(desktopPoolConfigDTO)) {
            desktopPoolConfigDTO = new DesktopPoolConfigDTO();
            desktopPoolConfigDTO.setDesktopPoolId(desktopPoolId);
        }

        return desktopPoolConfigDTO;
    }

    private void validateBeforeUpdate(CbbDesktopPoolDTO desktopPoolDetail, UUID desktopPoolId) throws BusinessException {
        if (desktopPoolDetail == null || desktopPoolDetail.getId() == null) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_NOT_EXIST, String.valueOf(desktopPoolId));
        }

        if (desktopPoolDetail.getPoolState() != CbbDesktopPoolState.AVAILABLE) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_UNAVAILABLE_UPDATE_FORBID, desktopPoolDetail.getName());
        }
    }

    @Override
    public void unbindSoftwareStrategy(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        LOGGER.info("桌面池[{}]删除软件管控策略", desktopPoolId);
        DesktopPoolConfigDTO desktopPoolConfigDTO = desktopPoolConfigService.queryByDesktopPoolId(desktopPoolId);
        if (Objects.isNull(desktopPoolConfigDTO) || Objects.isNull(desktopPoolConfigDTO.getSoftwareStrategyId())) {
            return;
        }
        desktopPoolConfigDTO.setSoftwareStrategyId(null);
        desktopPoolConfigService.saveOrUpdateDesktopPoolConfig(desktopPoolConfigDTO);
    }

    @Override
    public DesktopPoolSoftwareStrategyDTO queryDesktopPoolSoftwareStrategy(UUID desktopPoolId) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        DesktopPoolBasicDTO desktopPoolBasicDTO = desktopPoolService.getDesktopPoolBasicById(desktopPoolId);
        if (Objects.isNull(desktopPoolBasicDTO.getSoftwareStrategyId())) {
            // 为空
            return null;
        }
        CbbDesktopPoolDTO desktopPool = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);
        DesktopPoolSoftwareStrategyDTO poolSoftwareStrategyDTO = new DesktopPoolSoftwareStrategyDTO();
        poolSoftwareStrategyDTO.setSoftwareStrategyName(desktopPoolBasicDTO.getSoftwareStrategyName());
        poolSoftwareStrategyDTO.setId(desktopPoolBasicDTO.getSoftwareStrategyId());
        poolSoftwareStrategyDTO.setDesktopPoolId(desktopPoolId);
        poolSoftwareStrategyDTO.setDesktopPoolName(desktopPool.getName());
        return poolSoftwareStrategyDTO;
    }

    @Override
    public DesktopPoolUserProfileStrategyDTO queryDesktopPoolUserProfileStrategy(UUID desktopPoolId) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        DesktopPoolUserProfileStrategyDTO userProfileStrategy = new DesktopPoolUserProfileStrategyDTO();
        DesktopPoolBasicDTO desktopPoolBasicDTO = desktopPoolService.getDesktopPoolBasicById(desktopPoolId);
        if (Objects.isNull(desktopPoolBasicDTO.getUserProfileStrategyId())) {
            return userProfileStrategy;
        }

        userProfileStrategy.setDesktopPoolId(desktopPoolId);
        userProfileStrategy.setUserProfileStrategyName(desktopPoolBasicDTO.getUserProfileStrategyName());
        userProfileStrategy.setUserProfileStrategyId(desktopPoolBasicDTO.getUserProfileStrategyId());

        return userProfileStrategy;
    }

    @Override
    public DesktopPoolImageTemplateDTO queryDesktopPoolImageTemplate(UUID desktopPoolId) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        DesktopPoolBasicDTO desktopPoolBasicDTO = desktopPoolService.getDesktopPoolBasicById(desktopPoolId);

        DesktopPoolImageTemplateDTO poolImageTemplateDTO = new DesktopPoolImageTemplateDTO();
        poolImageTemplateDTO.setDesktopPoolId(desktopPoolBasicDTO.getId());
        poolImageTemplateDTO.setDesktopPoolName(desktopPoolBasicDTO.getName());
        poolImageTemplateDTO.setId(desktopPoolBasicDTO.getImageTemplateId());
        poolImageTemplateDTO.setImageTemplateName(desktopPoolBasicDTO.getImageTemplateName());
        poolImageTemplateDTO.setRootImageId(desktopPoolBasicDTO.getRootImageId());
        poolImageTemplateDTO.setRootImageName(desktopPoolBasicDTO.getRootImageName());
        return poolImageTemplateDTO;
    }

    @Override
    public int getMaxIndexNumWhenAddDesktop(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        List<PoolDesktopInfoDTO> desktopInfoList = desktopPoolService.listNormalDeskInfoByDesktopPoolId(desktopPoolId);
        if (CollectionUtils.isEmpty(desktopInfoList)) {
            return 0;
        }
        int size = desktopInfoList.size();
        Integer max = desktopInfoList.stream().map(desktop -> {
            String name = desktop.getDesktopName();
            if (StringUtils.isEmpty(name)) {
                return size;
            }
            try {
                return Integer.parseInt(name.substring(name.lastIndexOf(DesktopPoolConstants.DESKTOP_NAME_SEPARATOR) + 1));
            } catch (Exception e) {
                LOGGER.error("getMaxIndexNumWhenAddDesktop转换数字异常", e);
                return desktopInfoList.size();
            }
        }).reduce(Integer::max).orElse(size);
        LOGGER.info("池[{}]中云桌面名称最大后缀：{}", desktopPoolId, max);
        return max;
    }

    @Override
    public SyncConfigResultDTO syncStrategy(CbbDesktopPoolDTO desktopPool, CbbDeskDTO desktopInfo) {
        Assert.notNull(desktopPool, "desktopPool can not be null");
        Assert.notNull(desktopInfo, "desktopInfo can not be null");

        String desktopPoolName = desktopPool.getName();
        String desktopName = desktopInfo.getName();
        UUID strategyId = desktopPool.getStrategyId();
        try {
            // 判断云桌面的策略与池配置的策略是否一致
            if (Objects.equals(desktopInfo.getStrategyId(), desktopPool.getStrategyId())) {
                LOGGER.info("云桌面[{}]的策略[{}]和桌面池[{}]的策略[{}]一致，无需变更", desktopInfo.getName(), desktopInfo.getStrategyId(),
                        desktopPool.getName(), desktopPool.getStrategyId());
                auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_STRATEGY_SAME_LOG, desktopPoolName, desktopName);
                return SyncConfigResultDTO.success();
            }

            userDesktopMgmtAPI.configStrategy(new EditDesktopStrategyRequest(desktopInfo.getDeskId(), strategyId));
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_STRATEGY_SUC_LOG, desktopPoolName, desktopName);
            return SyncConfigResultDTO.success();
        } catch (BusinessException e) {
            LOGGER.error("应用池[{}]中云桌面[{}]的云桌面策略失败，strategy[{}]", desktopPoolName, desktopName, strategyId, e);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_STRATEGY_FAIL_LOG, e, desktopPoolName, desktopName,
                    e.getI18nMessage());
            return SyncConfigResultDTO.fail(e.getI18nMessage());
        }
    }

    @Override
    public SyncConfigResultDTO syncDeskSpec(CbbDesktopPoolDTO desktopPool, CbbDeskDTO cbbDeskDTO) {
        Assert.notNull(desktopPool, "desktopPool can not be null");
        Assert.notNull(cbbDeskDTO, "cbbDeskDTO can not be null");

        String desktopPoolName = desktopPool.getName();
        String desktopName = cbbDeskDTO.getName();
        PoolDesktopInfoDTO poolDesktopInfoDTO = new PoolDesktopInfoDTO();
        BeanUtils.copyProperties(cbbDeskDTO, poolDesktopInfoDTO);
        try {
            if (desktopPoolAPIHelper.isDesktopHardwareEqualsPoolDeskSpec(poolDesktopInfoDTO, desktopPool.getDeskSpecId())) {
                LOGGER.info("云桌面[{}]的规格和桌面池[{}]的规格一致，无需变更", desktopName, desktopPoolName);
                auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_SPEC_SAME_LOG, desktopPoolName, desktopName);
                return SyncConfigResultDTO.success();
            }
            CbbDeskSpecDTO poolDeskSpecDTO = cbbDeskSpecAPI.getById(desktopPool.getDeskSpecId());
            if (Objects.nonNull(poolDeskSpecDTO.getVgpuInfoDTO())) {
                VgpuInfoDTO vgpuInfoDTO = poolDeskSpecDTO.getVgpuInfoDTO();
                // 再检查一遍防止实际的显卡配置已经变了
                deskSpecAPI.checkAndBuildVGpuInfo(cbbDeskDTO.getClusterId(), vgpuInfoDTO.getVgpuType(), vgpuInfoDTO.getVgpuExtraInfo());
            } else {
                poolDeskSpecDTO.setVgpuInfoDTO(new VgpuInfoDTO());
            }
            CbbUpdateDeskSpecRequest cbbUpdateDeskSpecRequest = deskSpecAPI.buildUpdateDeskSpecRequest(cbbDeskDTO.getDeskId(), poolDeskSpecDTO);
            LOGGER.info("更新桌面={}，更新规格信息={}", cbbDeskDTO.getDeskId(), JSON.toJSONString(cbbUpdateDeskSpecRequest));
            cbbUpdateDeskSpecRequest.setEnableCustom(cbbDeskDTO.getEnableCustom());
            cbbVDIDeskMgmtAPI.updateDeskSpec(cbbUpdateDeskSpecRequest);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_SPEC_SUC_LOG, desktopPoolName, desktopName);
            return SyncConfigResultDTO.success();
        } catch (BusinessException e) {
            LOGGER.error("应用池[{}]中云桌面[{}]的桌面规格失败，deskSpecId[{}]", desktopPoolName, desktopName, desktopPool.getDeskSpecId(), e);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_SPEC_FAIL_LOG, e, desktopPoolName, desktopName,
                    e.getI18nMessage());
            return SyncConfigResultDTO.fail(e.getI18nMessage());
        }
    }

    @Override
    public SyncConfigResultDTO syncImageTemplate(CbbDesktopPoolDTO desktopPool, CbbDeskDTO desktopInfo, @Nullable BatchTaskItem taskItem) {
        Assert.notNull(desktopPool, "desktopPool can not be null");
        Assert.notNull(desktopInfo, "desktopInfo can not be null");

        // 判断镜像模板和模板版本是否一致
        if (Objects.equals(desktopPool.getImageTemplateId(), desktopInfo.getImageTemplateId())) {
            LOGGER.info("云桌面[{}]的镜像模板[{}]和桌面池[{}]的镜像模板[{}]一致，无需变更", desktopInfo.getName(), desktopInfo.getImageTemplateId(),
                    desktopPool.getName(), desktopPool.getImageTemplateId());
            return SyncConfigResultDTO.success();
        }
        String desktopPoolName = desktopPool.getName();
        UUID imageTemplateId = desktopPool.getImageTemplateId();
        String desktopName = desktopInfo.getName();
        CbbDesktopImageUpdateDTO cbbDesktopImageUpdateDTO = new CbbDesktopImageUpdateDTO();
        cbbDesktopImageUpdateDTO.setDesktopId(desktopInfo.getDeskId());
        cbbDesktopImageUpdateDTO.setImageId(imageTemplateId);
        cbbDesktopImageUpdateDTO.setBatchTaskItem(taskItem);
        try {
            checkImageState(desktopInfo, imageTemplateId);

            userDesktopMgmtAPI.updateDesktopImage(cbbDesktopImageUpdateDTO);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_IMAGE_SUC_LOG, desktopPoolName, desktopInfo.getName());
            return SyncConfigResultDTO.success();
        } catch (BusinessException e) {
            LOGGER.error(String.format("桌面池[%s]中的云桌面[%s]应用镜像模板失败，desktopId[%s]", desktopPoolName, desktopInfo.getName(),
                    desktopInfo.getDeskId()), e);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_IMAGE_FAIL_LOG, e, desktopPoolName, desktopName,
                    e.getI18nMessage());
            return SyncConfigResultDTO.fail(e.getI18nMessage());
        }
    }

    private void checkImageState(CbbDeskDTO cbbDeskDTO, UUID imageTemplateId) throws BusinessException {
        boolean isImageTemplateExist = cbbImageTemplateMgmtAPI.checkImageTemplateExist(imageTemplateId);
        if (!isImageTemplateExist) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_EDIT_IMAGE_NOT_EXIST, cbbDeskDTO.getName());
        }
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateId);
        if (!ImageTemplateConstants.IMAGE_CAN_UPDATE_SET.contains(imageTemplateDetail.getImageState())) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_EDIT_IMAGE_STATE_NOT_ALLOW);
        }
    }

    @Override
    public SyncConfigResultDTO syncNetworkStrategy(CbbDesktopPoolDTO desktopPool, CbbDeskDTO desktopInfo) {
        Assert.notNull(desktopPool, "desktopPool can not be null");
        Assert.notNull(desktopInfo, "desktopInfo can not be null");

        UUID networkId = desktopPool.getNetworkId();
        String desktopName = desktopInfo.getName();
        String desktopPoolName = desktopPool.getName();
        try {
            // 判断网络策略是否一致
            if (Objects.equals(desktopPool.getNetworkId(), desktopInfo.getNetworkId())) {
                LOGGER.info("云桌面[{}]的网络策略[{}]和桌面池[{}]的网络策略[{}]一致，无需变更", desktopInfo.getName(), desktopInfo.getNetworkId(),
                        desktopPool.getName(), desktopPool.getNetworkId());
                return SyncConfigResultDTO.success();
            }

            EditDesktopNetworkRequest apiRequest = new EditDesktopNetworkRequest(desktopInfo.getDeskId(), networkId);
            userDesktopMgmtAPI.configNetwork(apiRequest);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_NETWORK_SUC_LOG, desktopPoolName, desktopName);
            return SyncConfigResultDTO.success();
        } catch (BusinessException e) {
            LOGGER.error(String.format("桌面池[%s]中的云桌面[%s]应用网络策略失败，networkId[%s]", desktopPoolName, desktopInfo.getDeskId(), networkId), e);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_NETWORK_FAIL_LOG, e, desktopPoolName, desktopName,
                    e.getI18nMessage());
            return SyncConfigResultDTO.fail(e.getI18nMessage());
        }
    }

    @Override
    public SyncConfigResultDTO syncSoftwareStrategy(CbbDesktopPoolDTO desktopPool, CbbDeskDTO desktopInfo) {
        Assert.notNull(desktopPool, "desktopPool can not be null");
        Assert.notNull(desktopInfo, "desktopInfo can not be null");

        UUID desktopPoolId = desktopPool.getId();
        String desktopName = desktopInfo.getName();
        String desktopPoolName = desktopPool.getName();
        UUID softwareStrategyId = null;
        try {
            softwareStrategyId = desktopPoolConfigService.getSoftwareStrategyIdByDesktopPoolId(desktopPoolId);
            RcoDeskInfoEntity rcoDeskInfo = rcoDeskInfoDAO.findByDeskId(desktopInfo.getDeskId());

            // 判断镜像模板和模板版本是否一致
            if (Objects.equals(rcoDeskInfo.getSoftwareStrategyId(), softwareStrategyId)) {
                LOGGER.info("云桌面[{}]的软件管控策略[{}]和桌面池[{}]的软件管控策略[{}]一致，无需变更", desktopInfo.getName(),
                        rcoDeskInfo.getSoftwareStrategyId(), desktopPool.getName(), softwareStrategyId);
                return SyncConfigResultDTO.success();
            }

            userDesktopMgmtAPI.updateDesktopSoftwareStrategy(desktopInfo.getDeskId(), softwareStrategyId);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_SOFTWARE_SUC_LOG, desktopPoolName, desktopName);
            return SyncConfigResultDTO.success();
        } catch (BusinessException e) {
            LOGGER.error(String.format("桌面池[%s]中云桌面[%s]的应用软件管控策略失败，softwareStrategyId[%s]", desktopPoolName, desktopInfo.getDeskId(),
                    softwareStrategyId), e);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_SOFTWARE_FAIL_LOG, e, desktopPoolName, desktopName,
                    e.getI18nMessage());
            return SyncConfigResultDTO.fail(e.getI18nMessage());
        }
    }

    @Override
    public SyncConfigResultDTO syncUserProfileStrategy(CbbDesktopPoolDTO desktopPool, CbbDeskDTO desktopInfo) {
        Assert.notNull(desktopPool, "desktopPool can not be null");
        Assert.notNull(desktopInfo, "desktopInfo can not be null");

        UUID desktopPoolId = desktopPool.getId();
        String desktopName = desktopInfo.getName();
        String desktopPoolName = desktopPool.getName();
        UUID poolUpmStrategyId = null;
        try {
            DesktopPoolConfigDTO poolConfig = desktopPoolConfigService.queryByDesktopPoolId(desktopPoolId);
            RcoDeskInfoEntity rcoDeskInfo = rcoDeskInfoDAO.findByDeskId(desktopInfo.getDeskId());
            UUID desktopUpmId = Objects.isNull(rcoDeskInfo) ? null : rcoDeskInfo.getUserProfileStrategyId();

            poolUpmStrategyId = Objects.isNull(poolConfig) ? null : poolConfig.getUserProfileStrategyId();

            // 判断镜像模板和模板版本是否一致
            if (Objects.equals(desktopUpmId, poolUpmStrategyId)) {
                LOGGER.info("云桌面[{}]的UPM策略[{}]和桌面池[{}]的UPM策略[{}]一致，无需变更", desktopInfo.getName(), desktopUpmId,
                        desktopPool.getName(), poolUpmStrategyId);
                return SyncConfigResultDTO.success();
            }

            userDesktopMgmtAPI.updateDesktopUserProfileStrategy(desktopInfo.getDeskId(), poolUpmStrategyId);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_UPM_SUC_LOG, desktopPoolName, desktopName);
            return SyncConfigResultDTO.success();
        } catch (BusinessException e) {
            LOGGER.error(String.format("桌面池[%s]中云桌面[%s]的应用UPM策略失败，upmStrategyId[%s]", desktopPoolName, desktopInfo.getDeskId(),
                    poolUpmStrategyId), e);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_UPM_FAIL_LOG, e, desktopPoolName, desktopName,
                    e.getI18nMessage());
            return SyncConfigResultDTO.fail(e.getI18nMessage());
        }
    }

    @Override
    public void saveDesktopPoolConfig(DesktopPoolConfigDTO desktopPoolConfigDTO) {
        Assert.notNull(desktopPoolConfigDTO, "desktopPoolConfigDTO can not be null");

        desktopPoolConfigService.saveOrUpdateDesktopPoolConfig(desktopPoolConfigDTO);
    }

    @Override
    public DesktopPoolConfigDTO getDesktopPoolConfig(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        return desktopPoolConfigService.queryByDesktopPoolId(desktopPoolId);
    }

    @Override
    public void checkDesktopPoolStatusAvailable(UUID desktopPoolId) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        CbbDesktopPoolDTO cbbDesktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);
        if (cbbDesktopPoolDTO.getPoolState() != CbbDesktopPoolState.AVAILABLE && cbbDesktopPoolDTO.getPoolState() != CbbDesktopPoolState.UPDATING) {
            LOGGER.error("桌面池[{}]状态为[{}]", desktopPoolId, cbbDesktopPoolDTO.getPoolState().name());
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_STATUS_UNAVAILABLE, cbbDesktopPoolDTO.getName());
        }
    }

    @Override
    public void checkDesktopPoolMaintenanceReady(UUID desktopPoolId) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        CbbDesktopPoolDTO cbbDesktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);
        if (Boolean.TRUE.equals(cbbDesktopPoolDTO.getIsOpenMaintenance())) {
            LOGGER.error("桌面池[{}]处于维护模式", cbbDesktopPoolDTO.getName());
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CHECK_UNDER_MAINTENANCE_ERROR, cbbDesktopPoolDTO.getName());
        }
    }

    @Override
    public DefaultPageResponse<DesktopPoolDTO> pageQueryByAdGroup(UUID adGroupId, PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(adGroupId, "adGroupId must not be null");

        DefaultPageResponse<DesktopPoolDTO> resp = userDesktopPoolService.pageQueryAdGroupDesktopPool(adGroupId, request);
        if (ArrayUtils.isEmpty(resp.getItemArr())) {
            return resp;
        }
        // 查看全部的计算集群和存储池信息
        Map<UUID, PlatformStoragePoolDTO> storagePoolAllMap =
                storagePoolAPI.queryAllStoragePool().stream().collect(Collectors.
                        toMap(PlatformStoragePoolDTO::getId, storagePoolDTO -> storagePoolDTO, (storage1, storage2) -> storage2));
        Map<UUID, ClusterInfoDTO> clusterInfoAllMap = clusterAPI.queryAllClusterInfoList()
                .stream().collect(Collectors.toMap(ClusterInfoDTO::getId, clusterInfo -> clusterInfo, (clusterInfo1, clusterInfo2) -> clusterInfo2));

        for (DesktopPoolDTO desktopPoolDTO : resp.getItemArr()) {
            fillDesktopPoolConflictDeskNum(desktopPoolDTO);
            // 设置桌面池计算集群和存储池信息
            fillDesktopPoolClusterAndStoragePool(desktopPoolDTO, clusterInfoAllMap, storagePoolAllMap);
        }
        return resp;
    }

    @Override
    public boolean existUserBindMoreDesktop(List<UUID> desktopIdList, CbbDesktopPoolModel poolModel) {
        Assert.notNull(desktopIdList, "desktopIdList must not be null");
        Assert.notNull(poolModel, "poolModel must not be null");
        if (CollectionUtils.isEmpty(desktopIdList)) {
            return false;
        }
        List<UserBindDesktopNumDTO> userDeskNumList = desktopPoolService.listUserBindPoolDesktopNum(desktopIdList, poolModel);
        if (CollectionUtils.isEmpty(userDeskNumList)) {
            return false;
        }
        return userDeskNumList.stream().anyMatch(item -> item.getBindNum() > 1);
    }

    private void fillDesktopPoolConflictDeskNum(DesktopPoolDTO desktopPoolDTO) {
        desktopPoolDTO.setConflictDeskNum(0);
        // 目前只有动态池需要配置不一致的桌面数量
        if (Objects.equals(desktopPoolDTO.getPoolModel(), CbbDesktopPoolModel.DYNAMIC)) {
            desktopPoolDTO.setConflictDeskNum(cbbDesktopPoolMgmtAPI.countConflictDeskNum(desktopPoolDTO.getId()));
        }
    }

    private void fillDesktopPoolClusterAndStoragePool(DesktopPoolDTO desktopPoolDTO,
                                                      Map<UUID, ClusterInfoDTO> clusterInfoAllMap,
                                                      Map<UUID, PlatformStoragePoolDTO> storagePoolAllMap) {
        UUID clusterId = desktopPoolDTO.getClusterId();
        // 设置计算集群信息
        if (clusterInfoAllMap.containsKey(clusterId)) {
            ClusterInfoDTO clusterInfoDTO = new ClusterInfoDTO();
            BeanUtils.copyProperties(clusterInfoAllMap.get(clusterId), clusterInfoDTO);
            desktopPoolDTO.setClusterInfo(clusterInfoDTO);
        }
        // 设置存储集群信息
        UUID storagePoolId = desktopPoolDTO.getSystemDiskStoragePoolId();
        if (Objects.nonNull(storagePoolId) && storagePoolAllMap.containsKey(storagePoolId)) {
            PlatformStoragePoolDTO storagePoolDTO = new PlatformStoragePoolDTO();
            BeanUtils.copyProperties(storagePoolAllMap.get(storagePoolId), storagePoolDTO);
            desktopPoolDTO.setSystemDiskStoragePool(storagePoolDTO);
        }
        // 本地盘存储
        storagePoolId = desktopPoolDTO.getPersonDiskStoragePoolId();
        if (Objects.nonNull(storagePoolId) && storagePoolAllMap.containsKey(storagePoolId)) {
            PlatformStoragePoolDTO storagePoolDTO = new PlatformStoragePoolDTO();
            BeanUtils.copyProperties(storagePoolAllMap.get(storagePoolId), storagePoolDTO);
            desktopPoolDTO.setPersonDiskStoragePool(storagePoolDTO);
        }
    }

    @Override
    public void isExistRelateDesktopPoolByImageIdThrowEx(UUID imageTemplateId) throws BusinessException {
        Assert.notNull(imageTemplateId, "imageTemplateId must not be null");
        List<DesktopPoolBasicDTO> desktopPoolList = desktopPoolService.listByImageId(imageTemplateId);
        if (CollectionUtils.isEmpty(desktopPoolList)) {
            return;
        }
        String message = desktopPoolList.stream().map(DesktopPoolBasicDTO::getName).collect(Collectors.joining(DELIMITER));
        LOGGER.error("镜像[{}]关联桌面池，桌面池为:{}", imageTemplateId, message);
        throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_RELATE_IMAGE, message);
    }

    @Override
    public void createDesktopPool(CbbCreateDeskPoolDTO cbbCreateDeskPoolDTO) throws BusinessException {
        Assert.notNull(cbbCreateDeskPoolDTO, "cbbCreateDeskPoolDTO must not be null");
        if (cbbCreateDeskPoolDTO.getPoolType() == CbbDesktopPoolType.VDI) {
            desktopPoolAPIHelper.validateCreateDesktopPoolParam(cbbCreateDeskPoolDTO);
        } else {
            desktopPoolAPIHelper.validateCreateThirdPartyDesktopPoolParam(cbbCreateDeskPoolDTO);
        }

        LockableExecutor.executeWithTryLock(cbbCreateDeskPoolDTO.getName(), () -> {

            if (cbbCreateDeskPoolDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
                // 多会话默认初始化值
                cbbCreateDeskPoolDTO.setLoadBalanceStrategy(Optional.ofNullable(cbbCreateDeskPoolDTO.
                        getLoadBalanceStrategy()).orElse(CbbLoadBalanceStrategyEnum.SESSION_PRIORITY));
                cbbCreateDeskPoolDTO.setCpuUsage(Optional.ofNullable(cbbCreateDeskPoolDTO.getCpuUsage()).orElse(90));
                cbbCreateDeskPoolDTO.setMemoryUsage(Optional.ofNullable(cbbCreateDeskPoolDTO.getMemoryUsage()).orElse(90));
                cbbCreateDeskPoolDTO.setSystemDiskUsage(Optional.ofNullable(cbbCreateDeskPoolDTO.getSystemDiskUsage()).orElse(90));
                cbbCreateDeskPoolDTO.setMaxSession(Optional.ofNullable(cbbCreateDeskPoolDTO.getMaxSession()).orElse(0));
            }
            if (cbbCreateDeskPoolDTO.getPreStartDesktopNum() == null) {
                cbbCreateDeskPoolDTO.setPreStartDesktopNum(0);
            }
            cbbDesktopPoolMgmtAPI.createDesktopPool(cbbCreateDeskPoolDTO);
        }, 1);
    }

    @Override
    public void updateDeskSpec(EditPoolDeskSpecRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        DesktopPoolBasicDTO poolBasicDTO = getDesktopPoolBasicById(request.getId());
        desktopPoolAPIHelper.validateUpdatePoolDeskSpec(request, poolBasicDTO);

        CbbDeskSpecDTO deskSpec = request.getDeskSpec();
        CbbUpdateDeskSpecDTO updateDeskSpecDTO = new CbbUpdateDeskSpecDTO();
        BeanUtils.copyProperties(deskSpec, updateDeskSpecDTO);
        updateDeskSpecDTO.setId(poolBasicDTO.getDeskSpecId());
        VgpuInfoDTO vgpuInfoDTO = Optional.ofNullable(deskSpec.getVgpuInfoDTO()).orElse(new VgpuInfoDTO());
        updateDeskSpecDTO.setVgpuInfo(vgpuInfoDTO);
        updateDeskSpecDTO.setExtraDiskList(Optional.ofNullable(deskSpec.getExtraDiskList()).orElse(Collections.emptyList()));
        cbbDeskSpecAPI.updateDeskSpec(updateDeskSpecDTO);
    }

    @Override
    public DeskSpecDetailDTO getDesktopPoolSpecDetail(UUID id) throws BusinessException {
        Assert.notNull(id, "id must not be null");
        DesktopPoolBasicDTO poolBasicDTO = this.getDesktopPoolBasicById(id);
        DeskSpecDetailDTO specDetailDTO = new DeskSpecDetailDTO();
        specDetailDTO.setId(id);
        specDetailDTO.setCpu(poolBasicDTO.getCpu());
        specDetailDTO.setMemory(poolBasicDTO.getMemory());
        specDetailDTO.setSystemDisk(poolBasicDTO.getSystemDisk());
        specDetailDTO.setPersonalDisk(poolBasicDTO.getPersonDisk());
        specDetailDTO.setEnableHyperVisorImprove(poolBasicDTO.getEnableHyperVisorImprove());
        specDetailDTO.setVgpuType(poolBasicDTO.getVgpuType());
        specDetailDTO.setVgpuExtraInfo(poolBasicDTO.getVgpuExtraInfo());
        specDetailDTO.setSystemDiskStoragePoolId(poolBasicDTO.getSystemDiskStoragePoolId());

        Map<UUID, PlatformStoragePoolDTO> storagePoolAllMap = storagePoolAPI.queryAllStoragePool().stream()
                .collect(Collectors.toMap(PlatformStoragePoolDTO::getId, storagePoolDTO -> storagePoolDTO, (storage1, storage2) -> storage2));
        specDetailDTO.setSystemDiskStoragePool(getAssignStoragePool(poolBasicDTO.getSystemDiskStoragePoolId(), storagePoolAllMap));
        if (Objects.nonNull(poolBasicDTO.getPersonDiskStoragePoolId())) {
            specDetailDTO.setPersonDiskStoragePoolId(poolBasicDTO.getPersonDiskStoragePoolId());
            specDetailDTO.setPersonDiskStoragePool(getAssignStoragePool(poolBasicDTO.getPersonDiskStoragePoolId(), storagePoolAllMap));
        }
        if (CollectionUtils.isNotEmpty(poolBasicDTO.getExtraDiskList())) {
            specDetailDTO.setExtraDiskArr(poolBasicDTO.getExtraDiskList().stream().map(item -> {
                ExtraDiskDTO extraDiskDTO = new ExtraDiskDTO();
                extraDiskDTO.setIndex(item.getIndex());
                extraDiskDTO.setDiskId(item.getDiskId());
                extraDiskDTO.setExtraSize(item.getExtraSize());
                extraDiskDTO.setAssignedStoragePoolId(item.getAssignedStoragePoolId());
                extraDiskDTO.setExtraDiskStoragePool(getAssignStoragePool(item.getAssignedStoragePoolId(), storagePoolAllMap));
                return extraDiskDTO;
            }).toArray(ExtraDiskDTO[]::new));
        }

        return specDetailDTO;
    }

    @Override
    public CbbDeskSpecDTO getDesktopPoolCbbDeskSpec(UUID id) throws BusinessException {
        Assert.notNull(id, "id must not be null");
        CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(id);
        return cbbDeskSpecAPI.getById(desktopPoolDTO.getDeskSpecId());
    }

    @Override
    public void updateDesktopPoolWithConfig(UpdateDesktopPoolRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        DesktopPoolBasicDTO desktopPoolBasicDTO = this.getDesktopPoolBasicById(request.getCbbDesktopPoolDTO().getId());

        desktopPoolAPIHelper.validateUpdateDesktopPoolParam(request, desktopPoolBasicDTO);

        desktopPoolServiceTx.updateDesktopPool(request);
    }

    @Override
    public void updateDesktopPoolWithoutConfig(CbbDesktopPoolDTO desktopPoolDTO) throws BusinessException {
        Assert.notNull(desktopPoolDTO, "desktopPoolDTO must not be null");
        DesktopPoolBasicDTO desktopPoolBasicDTO = this.getDesktopPoolBasicById(desktopPoolDTO.getId());

        desktopPoolAPIHelper.validateUpdateDesktopPoolWithoutConfig(desktopPoolDTO, desktopPoolBasicDTO);
        cbbDesktopPoolMgmtAPI.updateDesktopPool(desktopPoolDTO);
    }

    @Override
    public void updateLoadBalance(UpdateLoadBalanceRequest updateLoadBalanceRequest, CbbDesktopPoolDTO desktopPoolDTO) throws BusinessException {
        Assert.notNull(updateLoadBalanceRequest, "updateLoadBalanceRequest must not be null");
        Assert.notNull(desktopPoolDTO, "desktopPoolBasicDTO must not be null");
        UpdateDesktopPoolRequest updateDesktopPoolRequest = new UpdateDesktopPoolRequest();
        desktopPoolDTO.setHasMaxSessionChange(updateLoadBalanceRequest.getHasMaxSessionChange());
        updateDesktopPoolRequest.setCbbDesktopPoolDTO(desktopPoolDTO);
        desktopPoolServiceTx.updateDesktopPool(updateDesktopPoolRequest);
    }

    @Override
    public void deleteDesktopPoolFromDb(UUID id) throws BusinessException {
        Assert.notNull(id, "id can not be null");

        // 删除RCCM集群缓存关系
        desktopPoolAPIHelper.deleteRccmUserByDesktopPoolId(id);

        // 删除池和用户的绑定关系
        desktopPoolUserService.deleteByDesktopPoolId(id);

        cbbDesktopPoolMgmtAPI.deleteDesktopPoolFromDb(id);

        // 删除配置信息
        desktopPoolConfigService.deleteByDesktopPoolId(id);

    }

    private IdLabelEntry getAssignStoragePool(UUID storagePoolId, Map<UUID, PlatformStoragePoolDTO> storagePoolAllMap) {
        IdLabelEntry assignStoragePool = new IdLabelEntry();
        PlatformStoragePoolDTO storagePoolDTO = storagePoolAllMap.get(storagePoolId);
        if (Objects.nonNull(storagePoolDTO)) {
            assignStoragePool.setId(storagePoolDTO.getId());
            assignStoragePool.setLabel(storagePoolDTO.getName());
        }
        return assignStoragePool;
    }
}
