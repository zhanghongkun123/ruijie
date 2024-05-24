package com.ruijie.rcos.rcdc.rco.module.impl.diskpool.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbDiskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbPlatformDiskPoolDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.DiskStatus;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.StoragePoolAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.constants.DiskPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolOverviewDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolStatisticDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolUserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.UserDiskDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.common.UserCommonHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.common.AccountLastLoginUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.common.RcoInvalidTimeHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.DiskPoolUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.ViewUserDiskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.DiskPoolUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.UserDiskPoolEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.ViewUserDiskEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.service.UserDiskPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.PageQueryPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryUserListService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserQueryHelper;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description: 磁盘池管理API接口实现
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/14
 *
 * @author TD
 */
public class DiskPoolMgmtAPIImpl implements DiskPoolMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiskPoolMgmtAPIImpl.class);

    private static final String IN_USE_NUM = "in_use_num";

    private static final String ASSIGN_NUM = "assign_num";

    private static final String ALL_NUM = "all_num";

    private static final Long TIME_CONVERSION_UNIT = 24 * 60 * 60 * 1000L;

    private static final Long EXPIRE_TIME_ZERO = 0L;

    @Autowired
    private DiskPoolUserDAO diskPoolUserDAO;

    @Autowired
    private ViewUserDiskDAO viewUserDiskDAO;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI;

    @Autowired
    private QueryUserListService userListService;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UserDiskPoolService userDiskPoolService;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private StoragePoolAPI storagePoolAPI;

    @Autowired
    private UserCommonHelper userCommonHelper;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private RcoInvalidTimeHelper invalidTimeUtil;

    @Override
    public PageQueryResponse<DiskPoolStatisticDTO> pageDiskPool(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "pageDiskPool request can not be null");
        DefaultPageResponse<CbbPlatformDiskPoolDTO> pageResponse = cbbDiskPoolMgmtAPI.pageDiskPool(request);
        // 查询使用中的磁盘数量
        List<Map<String, Object>> inUseDiskNumAllList = viewUserDiskDAO.countInUseDiskNumAll();
        // 查询已分配的磁盘数量
        List<Map<String, Object>> assignedDiskNumAllList = viewUserDiskDAO.countAssignedDiskNumAll();
        // 查询全部的磁盘数量
        List<Map<String, Object>> diskNumAllList = viewUserDiskDAO.countAllDiskNum();
        // 查看全部的计算集群和存储池信息
        Map<UUID, PlatformStoragePoolDTO> storagePoolAllMap =
                storagePoolAPI.queryAllStoragePool().stream().collect(Collectors.
                        toMap(PlatformStoragePoolDTO::getId, storagePoolDTO -> storagePoolDTO, (storage1, storage2) -> storage2));
        Map<UUID, ClusterInfoDTO> clusterInfoAllMap = clusterAPI.queryAllClusterInfoList()
                .stream().collect(Collectors.toMap(ClusterInfoDTO::getId, clusterInfo -> clusterInfo, (clusterInfo1, clusterInfo2) -> clusterInfo2));
        return new PageQueryResponse<>(Arrays.stream(pageResponse.getItemArr()).map(cbbDiskPoolDTO -> {
            DiskPoolStatisticDTO diskPoolDTO = new DiskPoolStatisticDTO();
            BeanUtils.copyProperties(cbbDiskPoolDTO, diskPoolDTO);
            // 统计使用中，已分配的磁盘数量
            statisticDiskPool(diskPoolDTO, diskNumAllList, inUseDiskNumAllList, assignedDiskNumAllList);
            // 设置磁盘池计算集群和存储池信息
            setClusterAndStoragePool(cbbDiskPoolDTO.getClusterId(),
                    cbbDiskPoolDTO.getStoragePoolId(), diskPoolDTO, clusterInfoAllMap, storagePoolAllMap);
            return diskPoolDTO;
        }).toArray(DiskPoolStatisticDTO[]::new), pageResponse.getTotal());
    }

    @Override
    public DefaultPageResponse<DiskPoolStatisticDTO> pageDiskPoolByUserId(UUID userId, PageSearchRequest request) throws BusinessException {
        Assert.notNull(userId, "pageDiskPoolByUserId userId can be not null");
        Assert.notNull(request, "pageDiskPoolByUserId request can be not null");
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
        DefaultPageResponse<UserDiskPoolEntity> pageResponse =
                userDiskPoolService.pageQueryUserDiskPool(userDetail, request, CollectionUtils.isEmpty(diskPoolUserDAO.findByRelatedId(userId)));
        // 查询使用中的磁盘数量
        List<Map<String, Object>> inUseDiskNumAllList = viewUserDiskDAO.countInUseDiskNumAll();
        // 查询已分配的磁盘数量
        List<Map<String, Object>> assignedDiskNumAllList = viewUserDiskDAO.countAssignedDiskNumAll();
        // 查询全部的磁盘数量
        List<Map<String, Object>> diskNumAllList = viewUserDiskDAO.countAllDiskNum();
        // 查看全部的计算集群和存储池信息
        Map<UUID, PlatformStoragePoolDTO> storagePoolAllMap =
                storagePoolAPI.queryAllStoragePool().stream().collect(Collectors.
                        toMap(PlatformStoragePoolDTO::getId, storagePoolDTO -> storagePoolDTO, (storage1, storage2) -> storage2));
        Map<UUID, ClusterInfoDTO> clusterInfoAllMap = clusterAPI.queryAllClusterInfoList()
                .stream().collect(Collectors.toMap(ClusterInfoDTO::getId, clusterInfo -> clusterInfo, (clusterInfo1, clusterInfo2) -> clusterInfo2));
        return DefaultPageResponse.Builder.success(request.getLimit(), (int) pageResponse.getTotal(),
                Arrays.stream(pageResponse.getItemArr()).map(userDiskPool -> {
                    DiskPoolStatisticDTO diskPoolDTO = new DiskPoolStatisticDTO();
                    BeanUtils.copyProperties(userDiskPool, diskPoolDTO);
                    // 统计使用中，已分配的磁盘数量
                    statisticDiskPool(diskPoolDTO, diskNumAllList, inUseDiskNumAllList, assignedDiskNumAllList);
                    // 设置磁盘池计算集群和存储池信息
                    setClusterAndStoragePool(userDiskPool.getClusterId(),
                            userDiskPool.getStoragePoolId(), diskPoolDTO, clusterInfoAllMap, storagePoolAllMap);
                    return diskPoolDTO;
                }).toArray(DiskPoolStatisticDTO[]::new));
    }


    @Override
    public DiskPoolStatisticDTO diskPoolStatistic(UUID diskPoolId) throws BusinessException {
        Assert.notNull(diskPoolId, "diskPoolDetail diskPoolId can not be null");
        CbbPlatformDiskPoolDTO diskPoolDetail = cbbDiskPoolMgmtAPI.getDiskPoolDetail(diskPoolId);
        DiskPoolStatisticDTO diskPoolDTO = new DiskPoolStatisticDTO();
        BeanUtils.copyProperties(diskPoolDetail, diskPoolDTO);
        diskPoolDTO.setUserNum(getBindUserNum(diskPoolId));
        // 使用中磁盘总数
        diskPoolDTO.setInUseNum(viewUserDiskDAO.countByDiskPoolIdAndDiskState(diskPoolId, DiskStatus.IN_USE));
        // 已分配磁盘总数
        diskPoolDTO.setAssignedNum(viewUserDiskDAO.countByDiskPoolIdAndUserIdIsNotNull(diskPoolId));
        // 磁盘总数
        diskPoolDTO.setDiskNum(viewUserDiskDAO.findAllByDiskPoolIdIn(Collections.singletonList(diskPoolId)).size());
        // 设置计算集群和存储位置信息
        diskPoolDTO.setClusterInfo(clusterAPI.queryAvailableClusterById(diskPoolDetail.getClusterId()));
        diskPoolDTO.setStoragePool(storagePoolAPI.getStoragePoolDetail(diskPoolDetail.getStoragePoolId()));
        return diskPoolDTO;
    }

    @Override
    public DiskPoolOverviewDTO getDiskPoolOverview() {
        DiskPoolOverviewDTO diskPoolOverviewDTO = new DiskPoolOverviewDTO();
        List<CbbDiskPoolDTO> diskPoolList = cbbDiskPoolMgmtAPI.listAllDiskPool();
        if (CollectionUtils.isEmpty(diskPoolList)) {
            return diskPoolOverviewDTO;
        }
        // 磁盘总数
        diskPoolOverviewDTO.setPoolNum(diskPoolList.size());
        List<ViewUserDiskEntity> diskList =
                viewUserDiskDAO.findAllByDiskPoolIdIn(diskPoolList.stream().map(CbbDiskPoolDTO::getId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(diskList)) {
            return diskPoolOverviewDTO;
        }
        // 磁盘总数
        diskPoolOverviewDTO.setDiskNum(diskList.size());
        for (ViewUserDiskEntity userDiskEntity : diskList) {
            if (userDiskEntity.getDiskState() == DiskStatus.IN_USE) {
                diskPoolOverviewDTO.inUseNumIncrement();
            }
            if (Objects.nonNull(userDiskEntity.getUserId())) {
                diskPoolOverviewDTO.assignedNumIncrement();
            }
        }
        return diskPoolOverviewDTO;
    }

    @Override
    public void deleteDiskPool(UUID diskPoolId) throws BusinessException {
        Assert.notNull(diskPoolId, "diskPoolId can not be null");
        // 删除磁盘池信息以及磁盘
        cbbDiskPoolMgmtAPI.deleteDiskPool(diskPoolId);
        // 删除磁盘池与用户分配关系
        diskPoolUserDAO.deleteByDiskPoolId(diskPoolId);
    }

    @Override
    public List<DiskPoolUserDTO> listDiskPoolUser(UUID diskPoolId, @Nullable IacConfigRelatedType relatedType) {
        Assert.notNull(diskPoolId, "listDiskPoolUser diskPoolId can not be null");
        if (Objects.isNull(relatedType)) {
            return convert2PoolUserList(diskPoolUserDAO.findByDiskPoolId(diskPoolId));
        }
        return convert2PoolUserList(diskPoolUserDAO.findAllByDiskPoolIdAndRelatedType(diskPoolId, relatedType));
    }

    @Override
    public int getMaxIndexNumWhenAddDisk(UUID diskPoolId) {
        Assert.notNull(diskPoolId, "getMaxIndexNumWhenAddDisk diskPoolId can bot be null");
        List<CbbDeskDiskDTO> diskList = cbbVDIDeskDiskAPI.listDiskInfoByDiskPoolId(diskPoolId);
        if (diskList.isEmpty()) {
            return 0;
        }
        int diskPoolSize = diskList.size();
        return diskList.stream().map(disk -> {
            String name = disk.getName();
            if (StringUtils.isEmpty(name)) {
                return diskPoolSize;
            }
            try {
                return Integer.parseInt(name.substring(name.lastIndexOf(DiskPoolConstants.DISK_NAME_SEPARATOR) + 1));
            } catch (Exception e) {
                LOGGER.error("getMaxIndexNumWhenAddDisk转换数字异常", e);
                return diskPoolSize;
            }
        }).reduce(Integer::max).orElse(diskPoolSize);
    }

    @Override
    public PageQueryResponse<UserDiskDetailDTO> pagePoolDiskUser(PageQueryRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "pagePoolDiskUser request can not be null");
        PageQueryResponse<ViewUserDiskEntity> queryResponse = viewUserDiskDAO.pageQuery(pageQueryRequest);
        PageQueryResponse<UserDiskDetailDTO> pageQueryResponse = new PageQueryResponse<>();
        if (ArrayUtils.isEmpty(queryResponse.getItemArr())) {
            pageQueryResponse.setItemArr(new UserDiskDetailDTO[0]);
            return pageQueryResponse;
        }
        // 查看全部的存储池信息
        Map<UUID, PlatformStoragePoolDTO> storagePoolAllMap =
                storagePoolAPI.queryAllStoragePool().stream().collect(Collectors.
                        toMap(PlatformStoragePoolDTO::getId, storagePoolDTO -> storagePoolDTO, (storage1, storage2) -> storage2));
        pageQueryResponse.setTotal(queryResponse.getTotal());
        pageQueryResponse.setItemArr(Arrays.stream(queryResponse.getItemArr()).map(viewUserDiskEntity -> {
            UserDiskDetailDTO userDiskDTO = new UserDiskDetailDTO();
            BeanUtils.copyProperties(viewUserDiskEntity, userDiskDTO);
            userDiskDTO.setId(viewUserDiskEntity.getDiskId());
            if (Objects.nonNull(viewUserDiskEntity.getAssignStoragePoolIds())) {
                UUID storagePoolId = UUID.fromString(viewUserDiskEntity.getAssignStoragePoolIds());
                // 设置存储集群信息
                if (storagePoolAllMap.containsKey(storagePoolId)) {
                    userDiskDTO.setStoragePoolName(storagePoolAllMap.get(storagePoolId).getName());
                }
            }
            try {
                if (ObjectUtils.isNotEmpty(viewUserDiskEntity.getUserId())) {
                    IacUserDetailDTO dto = cbbUserAPI.getUserDetail(viewUserDiskEntity.getUserId());
                    userDiskDTO.setInvalid(viewUserDiskEntity.getInvalid());
                    userDiskDTO.setInvalidDescription(invalidTimeUtil.obtainInvalidDescription(dto));
                    userDiskDTO.setAccountExpireDate(invalidTimeUtil.expireDateFormat(dto));
                }
            } catch (BusinessException e) {
                LOGGER.error("查询用户[{}]详情出错", viewUserDiskEntity.getUserId());
                throw new RuntimeException(e);
            }
            userDiskDTO.setUserDescription(viewUserDiskEntity.getUserDescription());
            return userDiskDTO;
        }).toArray(UserDiskDetailDTO[]::new));
        return pageQueryResponse;
    }

    private String dealInvalidDescription(ViewUserDiskEntity entity) {

        Integer invalidTime = entity.getInvalidTime();
        if (ObjectUtils.isEmpty(invalidTime)) {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_FOREVER_INVALID);
        }

        Date invalidRecoverTime = entity.getInvalidRecoverTime();
        List<CloudDesktopDTO> dtoList = userDesktopMgmtAPI.findByUserId(entity.getUserId());
        //恢复失效时间不为空则以恢复时间为准
        if (ObjectUtils.isNotEmpty(invalidRecoverTime)) {
            return AccountLastLoginUtil.setInvalidDescription(invalidRecoverTime.getTime(), invalidTime);
        } else {
            //以最后一次登录时间为准
            Long lastLoginTime = AccountLastLoginUtil.offerLastLoginTime(entity.getUserId(), dtoList);
            return AccountLastLoginUtil.setInvalidDescription(lastLoginTime, invalidTime);
        }
    }

    @Override
    public List<UserDiskDetailDTO> queryAssignableDiskByDiskPoolId(UUID diskPoolId) {
        Assert.notNull(diskPoolId, "queryAssignableDiskByDiskPoolId diskPoolId can not be null");
        List<ViewUserDiskEntity> diskList = viewUserDiskDAO.findAllByDiskPoolIdIn(Collections.singletonList(diskPoolId));
        if (CollectionUtils.isEmpty(diskList)) {
            return Collections.emptyList();
        }
        return diskList.stream().filter(entity -> entity.getDiskState() == DiskStatus.ACTIVE && Objects.isNull(entity.getUserId())).map(entity -> {
            UserDiskDetailDTO userDiskDTO = new UserDiskDetailDTO();
            BeanUtils.copyProperties(entity, userDiskDTO);
            return userDiskDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserDiskDetailDTO> querySnapshotCapableDiskByDiskPoolId(UUID diskPoolId) {
        Assert.notNull(diskPoolId, "queryAssignableDiskByDiskPoolId diskPoolId can not be null");
        List<ViewUserDiskEntity> diskList = viewUserDiskDAO.findAllByDiskPoolIdIn(Arrays.asList(diskPoolId));
        if (CollectionUtils.isEmpty(diskList)) {
            return Arrays.asList();
        }
        return diskList.stream().filter(this::isDiskSnapshotCapable).map(entity -> {
            UserDiskDetailDTO userDiskDTO = new UserDiskDetailDTO();
            BeanUtils.copyProperties(entity, userDiskDTO);
            return userDiskDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public DefaultPageResponse<DiskPoolStatisticDTO> pageDiskPoolByAdGroupId(UUID adGroupId, PageSearchRequest request) throws BusinessException {
        Assert.notNull(adGroupId, "pageDiskPoolByUserId userId can be not null");
        Assert.notNull(request, "pageDiskPoolByUserId request can be not null");

        DefaultPageResponse<UserDiskPoolEntity> pageResponse =
                userDiskPoolService.pageQueryAdGroupDiskPool(adGroupId, request);
        // 查询使用中的磁盘数量
        List<Map<String, Object>> inUseDiskNumAllList = viewUserDiskDAO.countInUseDiskNumAll();
        // 查询已分配的磁盘数量
        List<Map<String, Object>> assignedDiskNumAllList = viewUserDiskDAO.countAssignedDiskNumAll();
        // 查询全部的磁盘数量
        List<Map<String, Object>> diskNumAllList = viewUserDiskDAO.countAllDiskNum();
        // 查看全部的计算集群和存储池信息
        Map<UUID, PlatformStoragePoolDTO> storagePoolAllMap =
                storagePoolAPI.queryAllStoragePool().stream().collect(Collectors.
                        toMap(PlatformStoragePoolDTO::getId, storagePoolDTO -> storagePoolDTO, (storage1, storage2) -> storage2));
        Map<UUID, ClusterInfoDTO> clusterInfoAllMap = clusterAPI.queryAllClusterInfoList()
                .stream().collect(Collectors.toMap(ClusterInfoDTO::getId, clusterInfo -> clusterInfo, (clusterInfo1, clusterInfo2) -> clusterInfo2));
        return DefaultPageResponse.Builder.success(request.getLimit(), (int) pageResponse.getTotal(),
                Arrays.stream(pageResponse.getItemArr()).map(userDiskPool -> {
                    DiskPoolStatisticDTO diskPoolDTO = new DiskPoolStatisticDTO();
                    BeanUtils.copyProperties(userDiskPool, diskPoolDTO);
                    // 统计使用中，已分配的磁盘数量
                    statisticDiskPool(diskPoolDTO, diskNumAllList, inUseDiskNumAllList, assignedDiskNumAllList);
                    // 设置磁盘池计算集群和存储池信息
                    setClusterAndStoragePool(userDiskPool.getClusterId(), 
                            userDiskPool.getStoragePoolId(), diskPoolDTO, clusterInfoAllMap, storagePoolAllMap);
                    return diskPoolDTO;
                }).toArray(DiskPoolStatisticDTO[]::new));
    }
    
    private void setClusterAndStoragePool(UUID clusterId, UUID storagePoolId, DiskPoolStatisticDTO diskPoolDTO,
                        Map<UUID, ClusterInfoDTO> clusterInfoAllMap, Map<UUID, PlatformStoragePoolDTO> storagePoolAllMap) {
        // 设置计算集群信息
        if (clusterInfoAllMap.containsKey(clusterId)) {
            ClusterInfoDTO clusterInfoDTO = new ClusterInfoDTO();
            BeanUtils.copyProperties(clusterInfoAllMap.get(clusterId), clusterInfoDTO);
            diskPoolDTO.setClusterInfo(clusterInfoDTO);
        }
        // 设置存储集群信息
        if (storagePoolAllMap.containsKey(storagePoolId)) {
            PlatformStoragePoolDTO storagePoolDTO = new PlatformStoragePoolDTO();
            BeanUtils.copyProperties(storagePoolAllMap.get(storagePoolId), storagePoolDTO);
            diskPoolDTO.setStoragePool(storagePoolDTO);
        }
    }

    private boolean isDiskSnapshotCapable(ViewUserDiskEntity entity) {
        return entity.getDiskState() == DiskStatus.ACTIVE || entity.getDiskState() == DiskStatus.IN_USE
                || entity.getDiskState() == DiskStatus.DISABLE;
    }

    private List<DiskPoolUserDTO> convert2PoolUserList(List<DiskPoolUserEntity> entityList) {
        List<DiskPoolUserDTO> poolUserList = new ArrayList<>();
        if (CollectionUtils.isEmpty(entityList)) {
            return poolUserList;
        }
        for (DiskPoolUserEntity entity : entityList) {
            DiskPoolUserDTO poolUser = new DiskPoolUserDTO();
            BeanUtils.copyProperties(entity, poolUser);
            poolUserList.add(poolUser);
        }
        return poolUserList;
    }

    private void statisticDiskPool(DiskPoolStatisticDTO diskPoolDTO, List<Map<String, Object>> diskNumAllList,
                                   List<Map<String, Object>> inUseDiskList, List<Map<String, Object>> assignedDiskList) {
        String diskPoolId = diskPoolDTO.getId().toString();
        // 重置磁盘池磁盘数量
        diskPoolDTO.setDiskNum(0);
        // 设置磁盘池总的磁盘数量
        diskNumAllList.forEach(item -> {
            if (item.containsValue(diskPoolId)) {
                diskPoolDTO.setDiskNum(Integer.parseInt(item.get(ALL_NUM).toString()));
            }
        });
        // 设置使用中的磁盘
        inUseDiskList.forEach(item -> {
            if (item.containsValue(diskPoolId)) {
                diskPoolDTO.setInUseNum(Integer.parseInt(item.get(IN_USE_NUM).toString()));
            }
        });
        // 设置已分配的磁盘数量
        assignedDiskList.forEach(item -> {
            if (item.containsValue(diskPoolId)) {
                diskPoolDTO.setAssignedNum(Integer.parseInt(item.get(ASSIGN_NUM).toString()));
            }
        });
    }

    private long getBindUserNum(UUID diskPoolId) {
        // 计算池关联的用户数量
        PageSearchRequest request = new PageSearchRequest();
        userCommonHelper.dealNonVisitorUserTypeMatch(request);
        request.setLimit(10);
        request.setPage(0);

        PageQueryPoolDTO pageQueryPoolDTO = new PageQueryPoolDTO(diskPoolId, UserQueryHelper.DISK_POOL_ID, request, true);
        pageQueryPoolDTO.setNotIn(true);
        Page<RcoViewUserEntity> page = userListService.pageUserInOrNotInPool(pageQueryPoolDTO);
        return page.getTotalElements();
    }
}
