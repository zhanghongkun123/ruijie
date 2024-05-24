package com.ruijie.rcos.rcdc.rco.module.impl.diskpool.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbDiskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbUpdateDiskBaseInfoNotifySPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDiskUpdateBaseInfoNotifyRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.StoragePoolServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.VMMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.vm.VmIdRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.vm.VmDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.DiskStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskPoolUserAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDiskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.constants.DiskPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DesktopLockDiskResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolUserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.UserDiskDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.UserGroupBindDiskNumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.enums.DiskApplySupportEnum;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.enums.DiskBusinessKeyEnums;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileStrategyStorageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.DiskPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.UserPoolDiskHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.UserDiskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.ViewUserDiskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.ViewUserDiskEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.UserDiskServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.util.*;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants.UPDATE_DISK_BASE_INFO_KEY;

/**
 * Description: 用户磁盘管理类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/14
 *
 * @author TD
 */
public class UserDiskMgmtAPIImpl implements UserDiskMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDiskMgmtAPIImpl.class);

    private static final String USER_DISK_POOL_ASSIGN_LOCK = "USER_DISK_POOL_ASSIGN_LOCK_";

    @Autowired
    private ViewUserDiskDAO viewUserDiskDAO;

    @Autowired
    private UserDiskServiceTx userDiskServiceTx;

    @Autowired
    private UserPoolDiskHelper userPoolDiskHelper;

    @Autowired
    private UserDiskDAO userDiskDAO;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private QueryCloudDesktopService cloudDesktopService;

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private DiskPoolUserAPI diskPoolUserAPI;

    @Autowired
    private CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private VMMgmtAPI vmMgmtAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private StoragePoolServerMgmtAPI storagePoolServerMgmtAPI;

    @Autowired
    private CbbUpdateDiskBaseInfoNotifySPI notifySPI;

    @Override
    public UserDiskDetailDTO userDiskDetail(UUID diskId) throws BusinessException {
        Assert.notNull(diskId, "userDiskDetail diskId can not be null");
        ViewUserDiskEntity viewUserDiskEntity = viewUserDiskDAO.findByDiskId(diskId);
        if (Objects.isNull(viewUserDiskEntity)) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_NO_EXIST, diskId.toString());
        }
        UserDiskDetailDTO userDiskDetailDTO = new UserDiskDetailDTO();
        BeanUtils.copyProperties(viewUserDiskEntity, userDiskDetailDTO);
        return userDiskDetailDTO;
    }

    @Override
    public Optional<UserDiskDetailDTO> diskDetailByUserId(UUID userId) {
        Assert.notNull(userId, "diskDetailByUserId userId can not be null");
        ViewUserDiskEntity viewUserDiskEntity = viewUserDiskDAO.findByUserId(userId);
        if (Objects.isNull(viewUserDiskEntity)) {
            return Optional.empty();
        }
        UserDiskDetailDTO userDiskDetailDTO = new UserDiskDetailDTO();
        BeanUtils.copyProperties(viewUserDiskEntity, userDiskDetailDTO);
        return Optional.of(userDiskDetailDTO);
    }

    @Override
    public List<UUID> diskDetailByDiskPoolIdAndUserIdList(UUID diskPoolId, IacConfigRelatedType relatedType, List<UUID> idList) {
        Assert.notNull(diskPoolId, "diskPoolId can not be null");
        Assert.notNull(relatedType, "relatedType can not be null");
        Assert.notNull(idList, "idList can not be null");
        List<ViewUserDiskEntity> entityList =
                relatedType == IacConfigRelatedType.USER ? viewUserDiskDAO.findByDiskPoolIdAndUserIdIn(diskPoolId, idList)
                        : viewUserDiskDAO.findByDiskPoolIdAndGroupIdIn(diskPoolId, idList);
        return entityList.stream().filter(entity -> DiskPoolConstants.EXPAND_DISK_STATUS.contains(CbbDiskState.valueOf(entity.getDiskState().name())))
                .map(ViewUserDiskEntity::getDiskId).collect(Collectors.toList());
    }

    @Override
    public void bindUserOrOn(UUID diskId, @Nullable UUID userId) throws BusinessException {
        Assert.notNull(diskId, "bindUserOrOn diskId can not be null");
        if (Objects.isNull(userId)) {
            userDiskServiceTx.unBindUserDisk(diskId);
        } else {
            userDiskServiceTx.bindUserDisk(diskId, userId);
        }

        notifyBindUserChange(diskId);
    }

    private void notifyBindUserChange(UUID diskId) {
        ViewUserDiskEntity viewUserDiskEntity = viewUserDiskDAO.findByDiskId(diskId);
        final CbbDiskUpdateBaseInfoNotifyRequest request = new CbbDiskUpdateBaseInfoNotifyRequest(diskId);
        request.setPlatformId(viewUserDiskEntity.getPlatformId());
        request.setDiskBindUserDesc(viewUserDiskEntity.getUserName());
        notifySPI.updateDiskBindUserDesc(UPDATE_DISK_BASE_INFO_KEY, request);
    }

    @Override
    public void attachDesktopDisk(UUID deskId) {
        Assert.notNull(deskId, "attachUserDisk deskId can not be null");
        ViewUserDesktopEntity userDesktopEntity = viewDesktopDetailDAO.findByCbbDesktopId(deskId);
        // 桌面信息为空/绑定的用户不存在/云桌面非RUNNING状态-则不进行磁盘挂载
        if (Objects.isNull(userDesktopEntity) || Objects.isNull(userDesktopEntity.getUserId())
                || !CbbCloudDeskType.VDI.name().equals(userDesktopEntity.getDesktopType())
                || CbbCloudDeskState.RUNNING != CbbCloudDeskState.valueOf(userDesktopEntity.getDeskState())) {
            LOGGER.info("云桌面信息不符合预期，不进行磁盘动态挂载，云桌面信息：{}", JSON.toJSONString(userDesktopEntity));
            return;
        }
        StopWatch watch = new StopWatch();
        watch.start();
        // 同步加锁挂载磁盘
        try {
            LockableExecutor.executeWithTryLock(USER_DISK_POOL_ASSIGN_LOCK + userDesktopEntity.getUserId(),
                () -> userPoolDiskHelper.attachUserDisk(deskId, userDesktopEntity), 10);
        } catch (Exception e) {
            LOGGER.error("异步挂载桌面[{}]用户个人盘失败：", deskId, e);
        }
        watch.stop();
        LOGGER.info("桌面[{}]挂载磁盘执行耗时：{}", deskId, watch.getLastTaskTimeNanos());
    }

    @Override
    public void deleteUserDisk(UUID diskId) {
        Assert.notNull(diskId, "detachUserDisk deskId can not be null");
        userDiskDAO.deleteByDiskId(diskId);
    }

    @Override
    public void unbindUserAllDiskPool(UUID relatedId, IacConfigRelatedType relatedType) throws BusinessException {
        Assert.notNull(relatedId, "unbindUserAllDiskPool relatedId can not be null");
        Assert.notNull(relatedType, "relatedType can not be null");
        userDiskServiceTx.unbindUserAllDiskPool(relatedId, relatedType);
    }

    @Override
    public List<DiskApplySupportEnum> getDiskUseApplySupport(UUID diskId) throws BusinessException {
        Assert.notNull(diskId, "getDiskUseApplySupport diskId can not be null");
        List<DiskApplySupportEnum> supportEnumList = new ArrayList<>();
        ViewUserDiskEntity userDiskEntity = viewUserDiskDAO.findByDiskId(diskId);
        if (Objects.isNull(userDiskEntity) || Objects.isNull(userDiskEntity.getDeskId()) || userDiskEntity.getDiskPoolType() != DiskPoolType.POOL) {
            return supportEnumList;
        }
        UUID deskId = userDiskEntity.getDeskId();
        // 动态池支持UPM
        if (cbbDeskMgmtAPI.getDeskById(deskId).getDesktopPoolType() == DesktopPoolType.DYNAMIC) {
            supportEnumList.add(DiskApplySupportEnum.UPM);
        }
        return supportEnumList;
    }

    @Override
    public Set<UUID> getFaultDiskDesktopSet(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "getFaultDiskDesktopSet desktopPoolId can not be null");
        return cbbVDIDeskDiskAPI.getDesktopIdsByPoolIdAndType(desktopPoolId, DiskPoolType.POOL).stream()
                .map(CbbDeskDiskDTO::getDeskId).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @Override
    public List<UserGroupBindDiskNumDTO> countGroupBindDiskNumByDiskPoolId(UUID diskPoolId) {
        Assert.notNull(diskPoolId, "countGroupBindDiskNumByDiskPoolId diskPoolId can not be null");
        return viewUserDiskDAO.countGroupBindDiskNumByDiskPoolId(diskPoolId)
                .stream()
                .map(item -> JSON.parseObject(JSON.toJSONString(item), UserGroupBindDiskNumDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkDesktopUserProFile(UUID desktopId) throws BusinessException {
        Assert.notNull(desktopId, "desktopId can not be null");
        ViewUserDesktopEntity viewUserDesktopEntity = cloudDesktopService.checkDesktopExistInDeskViewById(desktopId);
        // 桌面用户信息或者UPM信息为null，非VDI还原桌面不限制启动
        if (Objects.isNull(viewUserDesktopEntity.getUserProfileStrategyId())
                || Objects.isNull(viewUserDesktopEntity.getUserId())
                || !Objects.equals(viewUserDesktopEntity.getDesktopType(), CbbCloudDeskType.VDI.name())
                || !Objects.equals(viewUserDesktopEntity.getPattern(), CbbCloudDeskPattern.RECOVERABLE.name())) {
            return false;
        }
        UUID userProfileStrategyId = viewUserDesktopEntity.getUserProfileStrategyId();
        UserProfileStrategyDTO userProfileStrategyDTO = userProfileMgmtAPI.findUserProfileStrategyById(userProfileStrategyId);
        // UPM不在个人盘上则不限制开机
        return userProfileStrategyDTO.getStorageType() == UserProfileStrategyStorageTypeEnum.PERSONAL;
    }

    @Override
    public DesktopLockDiskResultDTO desktopLockDisk(UUID desktopId) {
        Assert.notNull(desktopId, "desktopLockDisk desktopId can not be null");
        final DesktopLockDiskResultDTO desktopLockDiskResultDTO = new DesktopLockDiskResultDTO();
        desktopLockDiskResultDTO.setCode(DiskPoolConstants.FAIL);
        ViewUserDesktopEntity userDesktopEntity = viewDesktopDetailDAO.findByCbbDesktopId(desktopId);
        // 桌面信息为空/绑定的用户不存在/非VDI云桌面-不进行磁盘锁定
        if (Objects.isNull(userDesktopEntity) || Objects.isNull(userDesktopEntity.getUserId())
                || !CbbCloudDeskType.VDI.name().equals(userDesktopEntity.getDesktopType())) {
            LOGGER.info("云桌面信息不符合预期，不进行磁盘锁定，云桌面信息：{}", JSON.toJSONString(userDesktopEntity));
            return desktopLockDiskResultDTO;
        }
        // 非CLOSE，SLEEP，RUNNING状态的云桌面禁止锁定磁盘
        String deskState = userDesktopEntity.getDeskState();
        if (!DiskPoolConstants.LOCK_DISK_DESKTOP_STATUS.contains(CbbCloudDeskState.valueOf(deskState))) {
            String errMsg = LocaleI18nResolver.resolve(BusinessKey.RCD_RCO_SHINE_START_VM_VM_BUSY,
                    LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_CLOUDDESKTOP_STATE_PRE + deskState.toLowerCase()));
            desktopLockDiskResultDTO.setMessage(errMsg);
            return desktopLockDiskResultDTO;
        }
        try {
            UUID userId = userDesktopEntity.getUserId();
            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
            // 访客不支持使用磁盘池
            if (userDetail.getUserType() == IacUserTypeEnum.VISITOR) {
                return desktopLockDiskResultDTO;
            }
            // 当前版本只支持一个用户绑定一个磁盘池（挂载的个人磁盘目前只来源于磁盘池）
            Optional<DiskPoolUserDTO> optional = diskPoolUserAPI.listPoolIdByRelatedId(userId);
            if (!optional.isPresent()) {
                desktopLockDiskResultDTO.setMessage(LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_USER_NOT_ASSIGN_DISK_POOL));
                LOGGER.info("用户[{}]没有分配任何磁盘池，因此没有绑定磁盘，不进行磁盘锁定", userId);
                return desktopLockDiskResultDTO;
            }
            UUID diskPoolId = optional.get().getDiskPoolId();
            CbbDiskPoolDTO diskPoolDetail = cbbDiskPoolMgmtAPI.getDiskPoolDetail(diskPoolId);
            String diskPoolName = diskPoolDetail.getName();
            if (diskPoolDetail.getPoolState() == CbbDiskPoolState.DELETING) {
                desktopLockDiskResultDTO.setMessage(LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_STATUS_ERROR, diskPoolName));
                LOGGER.info("用户[{}]绑定磁盘池[{}]处于删除中状态，不进行磁盘锁定", userId, diskPoolName);
                return desktopLockDiskResultDTO;
            }
            // 锁定磁盘操作，同步加锁用户
            LockableExecutor.executeWithTryLock(USER_DISK_POOL_ASSIGN_LOCK + userId, () -> {
                Optional<UserDiskDetailDTO> optionalDisk = this.diskDetailByUserId(userId);
                UserDiskDetailDTO userDiskDetailDTO;
                if (optionalDisk.isPresent()) {
                    userDiskDetailDTO = optionalDisk.get();
                } else {
                    // 请求分配磁盘
                    userDiskDetailDTO = userPoolDiskHelper.assignDisk(userDetail, diskPoolDetail);
                }
                // 磁盘信息为空
                if (Objects.isNull(userDiskDetailDTO)) {
                    desktopLockDiskResultDTO.setMessage(LocaleI18nResolver.resolve(
                            DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_ASSIGN_DISK_ERROR, diskPoolName));
                    return;
                }
                // 检查磁盘状态是否可锁定
                checkUserDiskStatus(userDiskDetailDTO, desktopId, desktopLockDiskResultDTO);
            }, 10);
        } catch (Exception e) {
            LOGGER.error("分配锁定桌面[{}]用户个人盘失败：", desktopId, e);
            desktopLockDiskResultDTO.setMessage(LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DESKTOP_LOCK_USER_DISK_ERROR));
        }
        return desktopLockDiskResultDTO;
    }

    @Override
    public void desktopUnlockDisk(UUID desktopId) throws BusinessException {
        Assert.notNull(desktopId, "desktopId can not be null");
        Optional<ViewUserDiskEntity> optional =
                viewUserDiskDAO.findByDeskId(desktopId).stream()
                        .filter(entity -> entity.getDiskPoolType() == DiskPoolType.POOL)
                        .findFirst();
        if (optional.isPresent()) {
            ViewUserDiskEntity userDiskEntity = optional.get();
            UUID diskId = userDiskEntity.getDiskId();
            // CPP查询磁盘是否在该桌面上，不在该桌面上CDC直接进行关系解绑即可
            if (!isDiskAttach(desktopId, diskId)) {
                cbbVDIDeskDiskAPI.updateDiskState(diskId, CbbDiskState.ACTIVE, null);
            }
        }

    }

    @Override
    public void unbindUserAndDesktopAndDiskRelation(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId can not be null");
        try {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = queryCloudDesktopService.queryDeskDetail(desktopId);
            if (Objects.isNull(cloudDesktopDetailDTO.getDesktopPoolId())) {
                return;
            }
            // 单会话动态池需要数据回滚一下
            CbbDesktopPoolDTO desktopPool = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(cloudDesktopDetailDTO.getDesktopPoolId());
            if (!Objects.equals(desktopPool.getPoolModel(), CbbDesktopPoolModel.DYNAMIC)) {
                return;
            }
            LOGGER.info("启动虚机异常，动态池已分配虚机进行回滚，deskId: {}", desktopId);
            userDesktopService.unbindUserAndDesktopRelation(cloudDesktopDetailDTO.getId());
            // 解绑磁盘
            this.desktopUnlockDisk(cloudDesktopDetailDTO.getId());
        } catch (Exception e) {
            LOGGER.error("启动虚机[{}]失败，回滚用户-桌面-磁盘数据异常", desktopId, e);
        }
    }

    private void checkUserDiskStatus(UserDiskDetailDTO userDiskDetailDTO, UUID desktopId,
                                     DesktopLockDiskResultDTO desktopLockDiskResultDTO) throws BusinessException {
        // 锁定磁盘
        DiskStatus diskState = userDiskDetailDTO.getDiskState();
        UUID deskId = userDiskDetailDTO.getDeskId();
        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(desktopId);
        // 判断桌面与个人磁盘是否同属一个计算集群
        if (storagePoolServerMgmtAPI.getStoragePoolByComputeClusterId(cbbDeskDTO.getClusterId()).stream()
                .noneMatch(dto -> Objects.equals(userDiskDetailDTO.getAssignStoragePoolIds(), dto.getId().toString()))) {
            LOGGER.warn("磁盘[{}]与桌面[{}]所处的计算集群不一致，不支持桌面锁定磁盘挂载", userDiskDetailDTO.getDiskId(), cbbDeskDTO.getName());
            desktopLockDiskResultDTO.setMessage(LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DISK_DESKTOP_COMPUTE_CLUSTER_ERROR,
                    userDiskDetailDTO.getDiskName(), cbbDeskDTO.getName()));
            return;
        }
        // 磁盘状态可用，(未被其它桌面锁定||被当前桌面锁定)
        if (diskState == DiskStatus.ACTIVE && (Objects.isNull(deskId) || Objects.equals(desktopId, deskId))) {
            cbbVDIDeskDiskAPI.updateDiskState(userDiskDetailDTO.getDiskId(), CbbDiskState.ACTIVE, desktopId);
            desktopLockDiskResultDTO.setCode(DiskPoolConstants.SUCCESS);
            return;
        }
        // 磁盘状态使用中-快照创建中-备份创建中且挂载在当前桌面
        if (DiskPoolConstants.START_DISK_STATUS.contains(diskState) && Objects.equals(desktopId, deskId)) {
            desktopLockDiskResultDTO.setCode(DiskPoolConstants.SUCCESS);
            return;
        }
        // 磁盘状态不允许
        if (Objects.isNull(deskId)) {
            desktopLockDiskResultDTO.setMessage(LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DESKTOP_USER_DISK_NOT_READY_ERROR,
                    DiskBusinessKeyEnums.obtainResolve(diskState.name())));
            return;
        }
        // 已被其它桌面锁定
        desktopLockDiskResultDTO.setMessage(LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DISK_OTHER_DESKTOP_USE_ERROR,
                userDiskDetailDTO.getDeskName()));
    }

    private boolean isDiskAttach(UUID deskId, UUID diskId) throws BusinessException {
        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
        final VmDTO vmDTO = vmMgmtAPI.queryById(new VmIdRequest(cbbDeskDTO.getPlatformId(), deskId)).getDto();
        Assert.notNull(vmDTO, "vmDto is null");
        Assert.notNull(vmDTO.getDiskDTOArr(), "vmDto.diskDTOArr is null");
        return Arrays.stream(vmDTO.getDiskDTOArr()).anyMatch(diskDTO -> Objects.equals(diskDTO.getDiskId(), diskId));
    }
}
