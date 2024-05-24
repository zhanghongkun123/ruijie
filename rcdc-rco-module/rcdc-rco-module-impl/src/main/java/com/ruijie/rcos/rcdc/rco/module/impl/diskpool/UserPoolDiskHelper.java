package com.ruijie.rcos.rcdc.rco.module.impl.diskpool;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.disk.CbbVDICreatePoolDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.disk.CbbVDIExpandDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbDiskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DiskPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDetachDesktopUserDiskNotifyRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.StoragePoolServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.DiskStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskPoolUserAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDiskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.constants.DiskPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.AttachDiskMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolUserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.UserDiskDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.enums.DiskBusinessKeyEnums;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.GuestToolForDiskStateTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.UserDiskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.UserDiskEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.service.UserDiskPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.enums.GuesttoolMessageResultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Description: 用户磁盘挂载帮助类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/19
 *
 * @author TD
 */
@Service
public class UserPoolDiskHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserPoolDiskHelper.class);

    @Autowired
    private UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    private DiskPoolUserAPI diskPoolUserAPI;

    @Autowired
    private DiskPoolMgmtAPI diskPoolMgmtAPI;

    @Autowired
    private CbbVDIDeskDiskAPI deskDiskAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbGuestToolMessageAPI guestToolMessageAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UserDiskDAO userDiskDAO;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private UserDiskPoolService userDiskPoolService;

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder("notify_upm_disk_info").maxThreadNum(10).queueSize(100).build();

    /**
     * 磁盘名称缓存，防止重复
     */
    private static final Set<String> DISK_NAME_CACHE = Sets.newHashSet();

    /**
     * 磁盘ID缓存，防止重复
     */
    private static final Set<UUID> DISK_ID_CACHE = Sets.newConcurrentHashSet();

    /**
     * 挂载磁盘重试最大次数
     */
    private static final int RETRY_MAX_COUNT = 3;

    /**
     * 分配磁盘池锁KEY
     */
    private static final String DISK_POOL_ASSIGN_LOCK = "DISK_POOL_ASSIGN_LOCK_";

    @Autowired
    private StoragePoolServerMgmtAPI storagePoolServerMgmtAPI;

    /**
     * 挂载桌面磁盘
     *
     * @param deskId 桌面ID
     * @param userDesktopEntity 用户与桌面关系
     * @throws BusinessException 业务异常
     */
    public void attachUserDisk(UUID deskId, ViewUserDesktopEntity userDesktopEntity) throws BusinessException {
        Assert.notNull(deskId, "deskId can not be null");
        Assert.notNull(userDesktopEntity, "userDesktopEntity can not be null");
        UUID userId = userDesktopEntity.getUserId();
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
        // 访客不支持磁盘挂载
        if (userDetail.getUserType() == IacUserTypeEnum.VISITOR) {
            sendNoneAttachDiskToUpmAgent(deskId, GuestToolForDiskStateTypeEnum.WITHOUT.getCode());
            return;
        }
        // 当前版本只支持一个用户绑定一个磁盘池（挂载的个人磁盘目前只来源于磁盘池）
        Optional<DiskPoolUserDTO> optional = diskPoolUserAPI.listPoolIdByRelatedId(userId);
        if (!optional.isPresent()) {
            LOGGER.info("用户[{}]没有分配任何磁盘池，因此没有绑定磁盘，不进行磁盘挂载", userId);
            sendNoneAttachDiskToUpmAgent(deskId, GuestToolForDiskStateTypeEnum.WITHOUT.getCode());
            return;
        }
        UUID diskPoolId = optional.get().getDiskPoolId();
        CbbDiskPoolDTO diskPoolDetail = cbbDiskPoolMgmtAPI.getDiskPoolDetail(diskPoolId);
        if (diskPoolDetail.getPoolState() == CbbDiskPoolState.DELETING) {
            LOGGER.info("用户[{}]绑定磁盘池[{}]处于删除中状态，不进行磁盘挂载", userDetail.getUserName(), diskPoolDetail.getName());
            sendNoneAttachDiskToUpmAgent(deskId, GuestToolForDiskStateTypeEnum.WITHOUT.getCode());
            return;
        }
        Optional<UserDiskDetailDTO> userDiskOptional = userDiskMgmtAPI.diskDetailByUserId(userDetail.getId());
        UserDiskDetailDTO userDiskDTO;
        // 用户已绑定磁盘
        if (userDiskOptional.isPresent()) {
            userDiskDTO = userDiskOptional.get();
        } else {
            userDiskDTO = assignDisk(userDetail, diskPoolDetail);
        }
        // 分配磁盘为空
        if (Objects.isNull(userDiskDTO)) {
            // 不存在个人盘信息
            AttachDiskMessageDTO diskMessageDTO = new AttachDiskMessageDTO();
            diskMessageDTO.setCode(GuesttoolMessageResultTypeEnum.LOSS.getCode());
            // 通知GT
            sendAttachDiskMessageToGuestTool(deskId, diskMessageDTO);
            // 通知UPM
            sendNoneAttachDiskToUpmAgent(deskId, GuestToolForDiskStateTypeEnum.WITHOUT.getCode());
            return;
        }
        // 判断当前磁盘是否可挂载
        if (!isAttachUserDisk(userDiskDTO, userDesktopEntity, userDetail)) {
            return;
        }
        // 挂载磁盘
        attachDisk(userDiskDTO, userDesktopEntity, userDetail);
    }

    /**
     * 分配磁盘
     * @param userDetail 用户信息
     * @param diskPoolDetail 磁盘池信息
     * @return UserDiskDetailDTO
     * @throws BusinessException 业务异常
     */
    public UserDiskDetailDTO assignDisk(IacUserDetailDTO userDetail, CbbDiskPoolDTO diskPoolDetail) throws BusinessException {
        Assert.notNull(userDetail, "userDetail can not be null");
        Assert.notNull(diskPoolDetail, "diskPoolDetail can not be null");
        UUID diskPoolId = diskPoolDetail.getId();
        UUID userId = userDetail.getId();
        AtomicReference<UserDiskDetailDTO> assignSuccess = new AtomicReference<>();
        LockableExecutor.executeWithTryLock(DISK_POOL_ASSIGN_LOCK + diskPoolId, () -> {

            List<UserDiskDetailDTO> assignableDiskList = diskPoolMgmtAPI.queryAssignableDiskByDiskPoolId(diskPoolId).stream().filter
                    (userDiskDetailDTO -> !DISK_ID_CACHE.contains(userDiskDetailDTO.getDiskId())).collect(Collectors.toList());
            // 随机打散
            if (CollectionUtils.isNotEmpty(assignableDiskList)) {
                Collections.shuffle(assignableDiskList);
            }
            // 存在可分配磁盘，随机取一块磁盘分配
            while (CollectionUtils.isNotEmpty(assignableDiskList)) {
                // 取最新磁盘状态
                UserDiskDetailDTO userDiskDetailDTO = userDiskMgmtAPI.userDiskDetail(assignableDiskList.get(0).getDiskId());
                // 当前分配的磁盘已被使用，重新获取
                if (userDiskDetailDTO.getDiskState() != DiskStatus.ACTIVE || Objects.nonNull(userDiskDetailDTO.getUserId())) {
                    assignableDiskList.remove(0);
                    LOGGER.warn("磁盘[{}]信息不符合预期，不进行该磁盘分配，磁盘信息：{}", userDiskDetailDTO.getDiskName(), userDiskDetailDTO.toString());
                    continue;
                }
                // 绑定磁盘
                userDiskMgmtAPI.bindUserOrOn(userDiskDetailDTO.getDiskId(), userDetail.getId());
                // 记录审计日志
                auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_SYSTEM_AUTO_ASSIGN_DISK_USER_SUCCESS, userDetail.getUserName(),
                        userDiskDetailDTO.getDiskName());
                // 写入分配成功标识
                assignSuccess.set(userDiskDetailDTO);
                return;
            }
        }, 10);
        UserDiskDetailDTO userDiskDetailDTO = assignSuccess.get();
        if (Objects.nonNull(userDiskDetailDTO)) {
            return userDiskDetailDTO;
        }
        // 未打开自动创建磁盘功能
        if (!BooleanUtils.toBoolean(diskPoolDetail.getEnableCreateDisk())) {
            LOGGER.info("用户[{}]绑定磁盘池[{}]，但磁盘池没有可用分配磁盘，且没有开启创建磁盘功能，直接结束流程", userId, diskPoolId);
            // 磁盘池资源不足，生成告警
            diskPoolUserAPI.saveDiskPoolAssignFailWarnLog(userDetail.getUserName(), diskPoolDetail);
            // 未分配磁盘，返回空
            return null;
        }
        UUID diskId = null;
        // 创建磁盘
        try {
            diskId = createDisk(diskPoolDetail, userDetail.getUserName());
            // 绑定磁盘
            userDiskMgmtAPI.bindUserOrOn(diskId, userDetail.getId());
            userDiskDetailDTO = userDiskMgmtAPI.userDiskDetail(diskId);
            // 记录审计日志
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_SYSTEM_AUTO_ASSIGN_DISK_USER_SUCCESS,
                    userDetail.getUserName(), userDiskDetailDTO.getDiskName());
            return userDiskDetailDTO;
        } catch (BusinessException e) {
            LOGGER.error("自动为磁盘池{}创建磁盘失败", diskPoolDetail.getName(), e);
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_AUTO_CREATE_DISK_FAIL, e, diskPoolDetail.getName(), e.getI18nMessage());
        } finally {
            if (Objects.nonNull(diskId)) {
                DISK_ID_CACHE.remove(diskId);
            }
        }
        // 未分配磁盘，返回空
        return null;
    }

    private boolean isAttachUserDisk(UserDiskDetailDTO userDiskDTO, ViewUserDesktopEntity userDesktopEntity,
                                     IacUserDetailDTO userDetail) throws BusinessException {
        UUID deskId = userDesktopEntity.getCbbDesktopId();
        UUID userId = userDetail.getId();
        UUID clusterId = userDesktopEntity.getClusterId();
        String desktopName = userDesktopEntity.getDesktopName();

        // 多会话桌面池桌面不支持挂载磁盘池中磁盘
        if (CbbDesktopSessionType.MULTIPLE == userDesktopEntity.getSessionType() &&
                !DesktopPoolType.COMMON.name().equals(userDesktopEntity.getDesktopPoolType())) {
            LOGGER.info("多会话桌面池桌面[{}]不支持挂载磁盘池中磁盘", desktopName);
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_USER_DISK_DESKTOP_MULTI_SESSION, desktopName);
            sendNoneAttachDiskToUpmAgent(deskId, GuestToolForDiskStateTypeEnum.FAIL.getCode());
            return false;
        }

        // 判断桌面与个人磁盘是否同属一个计算集群
        if (storagePoolServerMgmtAPI.getStoragePoolByComputeClusterId(clusterId).stream()
                .noneMatch(dto -> Objects.equals(userDiskDTO.getAssignStoragePoolIds(), dto.getId().toString()))) {
            LOGGER.warn("用户[{}]绑定的磁盘[{}]与桌面[{}]所处的计算集群不一致，不支持挂载", userId, userDiskDTO.getDiskId(), desktopName);
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_USER_DISK_DESKTOP_COMPUTE_CLUSTER_ERROR, 
                    userDetail.getUserName(), userDiskDTO.getDiskName(), desktopName);
            sendNoneAttachDiskToUpmAgent(deskId, GuestToolForDiskStateTypeEnum.FAIL.getCode());
            return false;
        }
        // 已绑定桌面，不进行再次挂载
        if (Objects.nonNull(userDiskDTO.getDeskId()) && !Objects.equals(deskId, userDiskDTO.getDeskId())) {
            LOGGER.info("用户[{}]绑定的磁盘信息:[{}]，不支持再次挂载", userId, userDiskDTO.toString());
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_USER_DISK_IS_RELATION_DESK, userDetail.getUserName(),
                    userDiskDTO.getDiskName(), userDiskDTO.getDeskName(), desktopName);
            sendNoneAttachDiskToUpmAgent(deskId, GuestToolForDiskStateTypeEnum.WITHOUT.getCode());
            return false;
        }
        DiskStatus diskState = userDiskDTO.getDiskState();
        // 磁盘状态不是可用状态且不绑定桌面，不挂载直接通知GT
        if (diskState != DiskStatus.ACTIVE) {
            LOGGER.info("用户[{}]绑定的磁盘[{}]状态为[{}]，不支持挂载", userId, userDiskDTO.getDiskId(),
                    DiskBusinessKeyEnums.obtainResolve(diskState.name()));
            // 磁盘绑定的桌面为空/磁盘故障，通知GT
            if (Objects.isNull(userDiskDTO.getDeskId()) || diskState == DiskStatus.ERROR) {
                auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_USER_DISK_UNAVAILABLE_DESK_ATTACH, userDetail.getUserName(),
                        userDiskDTO.getDiskName(), DiskBusinessKeyEnums.obtainResolve(diskState.name()), desktopName);
                sendAttachDiskMessageToGuestTool(deskId, AttachDiskMessageDTO.fail(LocaleI18nResolver
                        .resolve(DiskPoolBusinessKey.RCDC_RCO_USER_DISK_STATE_ERROR, DiskBusinessKeyEnums.obtainResolve(diskState.name()))));
                sendNoneAttachDiskToUpmAgent(deskId, GuestToolForDiskStateTypeEnum.BAN.getCode());
            }
            return false;
        }
        return true;
    }

    /**
     * 卸载磁盘后续业务处理
     * 
     * @param request 请求信息
     * @throws BusinessException 业务异常
     */
    public void detachDiskFollowUpHandler(CbbDetachDesktopUserDiskNotifyRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        UUID diskId = request.getDiskId();
        CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(request.getDesktopId());
        UserDiskDetailDTO userDiskDetail = userDiskMgmtAPI.userDiskDetail(diskId);
        // 判断桌面卸载磁盘是否成功，记录审计日志
        if (BooleanUtils.toBoolean(request.getSuccess())) {
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_USER_DESK_DETACH_DISK_SUCCESS, userDiskDetail.getUserName(), deskDTO.getName(),
                    userDiskDetail.getDiskName());
        } else {
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_USER_DESK_DETACH_DISK_FAIL, userDiskDetail.getUserName(), deskDTO.getName(),
                    userDiskDetail.getDiskName(), request.getErrorMsg());
        }
        Integer delayCapacity = userDiskDetail.getDelayCapacity();
        // 更新磁盘最近使用时间
        updateUserDiskLatestUseTime(diskId);
        // 出现异常不进行磁盘扩容
        if (userDiskDetail.getDiskState() == DiskStatus.ERROR || Objects.isNull(delayCapacity) || delayCapacity <= userDiskDetail.getCapacity()) {
            return;
        }
        // 异步扩容磁盘
        ThreadExecutors.execute("expandDisk", () -> {
            try {
                expandDisk(userDiskDetail, delayCapacity);
            } catch (BusinessException e) {
                LOGGER.error("延迟异步扩容磁盘[{}]失败", diskId, e);
            }
        });
    }

    private void attachDisk(UserDiskDetailDTO userDiskDetail,
                            ViewUserDesktopEntity userDesktopEntity, IacUserDetailDTO userDetail) throws BusinessException {
        AttachDiskMessageDTO attachDiskMessageDTO;
        UUID deskId = userDesktopEntity.getCbbDesktopId();
        String desktopName = userDesktopEntity.getDesktopName();
        try {
            UUID diskId = userDiskDetail.getDiskId();
            // 挂载磁盘
            attachUserDisk(deskId, diskId);
            // 更新磁盘使用时间
            updateUserDiskLatestUseTime(diskId);
            // 通知GT，挂载成功
            CbbDiskPoolDTO diskPoolDetail = cbbDiskPoolMgmtAPI.getDiskPoolDetail(userDiskDetail.getDiskPoolId());
            attachDiskMessageDTO =
                    AttachDiskMessageDTO.success(userDiskDetail, diskPoolDetail.getDiskLetter(), userDiskMgmtAPI.getDiskUseApplySupport(diskId));
            userDiskPoolService.setUserInfoForAttachDiskMessage(attachDiskMessageDTO, userDetail.getId());
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_USER_DESK_ATTACH_DISK_SUCCESS,
                    userDetail.getUserName(), desktopName, userDiskDetail.getDiskName());
        } catch (Exception e) {
            LOGGER.error("用户[{}]桌面：[{}]，挂载-磁盘[{}]出错", userDetail.getUserName(), desktopName, userDiskDetail.getDiskName(), e);
            String message = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() :
                    LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_SYSTEM_ERROR);
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_USER_DESK_ATTACH_DISK_FAIL, e,
                    userDetail.getUserName(), desktopName, userDiskDetail.getDiskName(), message);
            attachDiskMessageDTO = AttachDiskMessageDTO.fail(message);
        }

        //通知UPM
        if (StringUtils.hasText(userDesktopEntity.getDesktopPoolType())) {
            AttachDiskMessageDTO messageDTO = attachDiskMessageDTO;
            THREAD_EXECUTOR.execute(() -> sendAttachDiskToUpmAgent(deskId,
                    messageDTO, DesktopPoolType.valueOf(userDesktopEntity.getDesktopPoolType())));
        }
        // 发送消息给GT
        sendAttachDiskMessageToGuestTool(deskId, attachDiskMessageDTO);
    }

    private void attachUserDisk(UUID desktopId, UUID diskId) throws BusinessException {
        int tryNum = 0;
        while (tryNum <= RETRY_MAX_COUNT) {
            try {
                // 挂载磁盘
                deskDiskAPI.dynamicAttachDisk(desktopId, diskId);
                break;
            } catch (Exception e) {
                LOGGER.error("桌面[{}]挂载磁盘[{}]出错，次数：{}", desktopId, diskId, tryNum, e);
                // 达到执行次数，抛出异常
                if (tryNum == RETRY_MAX_COUNT) {
                    LOGGER.warn("桌面[{}]挂载磁盘[{}]出错，已重试{}次，抛出对应的异常", desktopId, diskId, tryNum);
                    throw e;
                }
                try {
                    tryNum++;
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void updateUserDiskLatestUseTime(UUID diskId) {
        UserDiskEntity diskEntity = userDiskDAO.findByDiskId(diskId);
        if (Objects.nonNull(diskEntity)) {
            diskEntity.setLatestUseTime(new Date());
            userDiskDAO.save(diskEntity);
        }
    }

    private UUID createDisk(CbbDiskPoolDTO diskPoolDetail, String userName) throws BusinessException {
        UUID diskId = UUID.randomUUID();
        String diskName = null;
        try {
            UUID diskPoolId = diskPoolDetail.getId();
            diskName = buildDiskName(diskPoolDetail.getDiskNamePrefix(), diskPoolId);
            CbbVDICreatePoolDiskDTO createDiskDTO = new CbbVDICreatePoolDiskDTO();
            createDiskDTO.setDiskId(diskId);
            createDiskDTO.setTaskId(UUID.randomUUID());
            createDiskDTO.setDiskName(diskName);
            createDiskDTO.setDiskPoolId(diskPoolId);
            createDiskDTO.setCapacity(diskPoolDetail.getDiskSize());
            createDiskDTO.setDiskType(CbbDiskType.DATA);
            createDiskDTO.setDiskPoolType(DiskPoolType.POOL);
            createDiskDTO.setClusterId(diskPoolDetail.getClusterId());
            createDiskDTO.setAssignedStoragePoolId(diskPoolDetail.getStoragePoolId());
            createDiskDTO.setPlatformId(diskPoolDetail.getPlatformId());
            createDiskDTO.setDiskBindUserDesc(userName);
            DISK_ID_CACHE.add(diskId);
            deskDiskAPI.createPoolDisk(createDiskDTO);
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_AUTO_CREATE_DISK_SUCCESS, diskPoolDetail.getName(), diskName);
        } finally {
            DISK_NAME_CACHE.remove(diskName);
        }

        return diskId;
    }

    private void expandDisk(UserDiskDetailDTO diskDetail, Integer expandCapacity) throws BusinessException {
        CbbVDIExpandDiskDTO expandDiskDTO = new CbbVDIExpandDiskDTO();
        UUID taskId = UUID.randomUUID();
        expandDiskDTO.setTaskId(taskId);
        expandDiskDTO.setDeskId(diskDetail.getDeskId());
        expandDiskDTO.setDiskId(diskDetail.getDiskId());
        expandDiskDTO.setTargetCapacity(expandCapacity);
        LOGGER.info("关机完成--准备开始扩容磁盘，磁盘信息：{}，目标大小:{}", expandCapacity, JSON.toJSONString(diskDetail));
        deskDiskAPI.expandDisk(expandDiskDTO);
        // 等待任务结束
        StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
        stateMachineMgmtAgent.waitForAllProcessFinish();
        auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_EXPAND_DELAY_SUCCESS, diskDetail.getDiskName());
    }

    private void sendAttachDiskMessageToGuestTool(UUID deskId, AttachDiskMessageDTO attachDiskMessageDTO) throws BusinessException {
        GuesttoolMessageContent guesttoolMessageContent = new GuesttoolMessageContent();
        Integer code = attachDiskMessageDTO.getCode();
        if (Objects.equals(code, DiskPoolConstants.SUCCESS)) {
            attachDiskMessageDTO.setDiskNum(deskDiskAPI.listDeskDisk(deskId).size());
            attachDiskMessageDTO.setDiskName(LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DISK_DEFAULT_NAME));
            guesttoolMessageContent.setCode(GuesttoolMessageResultTypeEnum.SUCCESS.getCode());
            guesttoolMessageContent.setMessage(GuesttoolMessageResultTypeEnum.SUCCESS.getMessage());
            guesttoolMessageContent.setContent(attachDiskMessageDTO);
        } else if (Objects.equals(code, DiskPoolConstants.FAIL)) {
            guesttoolMessageContent.setCode(GuesttoolMessageResultTypeEnum.FAIL.getCode());
            guesttoolMessageContent.setMessage(LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DISK_TEMPORARY_UNAVAILABLE));
        } else {
            guesttoolMessageContent.setCode(GuesttoolMessageResultTypeEnum.LOSS.getCode());
            guesttoolMessageContent.setMessage(LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_NOT_EXIST_PERSONAL_DISK,
                    String.valueOf(deskId)));
        }

        final CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setBody(JSON.toJSONString(guesttoolMessageContent));
        dto.setCmdId(Integer.valueOf(GuestToolCmdId.NOTIFY_GT_CMD_ID_DISK_INFO));
        dto.setPortId(GuestToolCmdId.NOTIFY_GT_PORT_ID);
        dto.setDeskId(deskId);
        // 异步发送消息给GT
        guestToolMessageAPI.asyncRequest(dto);
    }

    private void sendNoneAttachDiskToUpmAgent(UUID deskId, Integer code) {
        String requestBody = JSON.toJSONString(userDiskPoolService.buildFailMessage(deskId, code));
        CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setBody(requestBody);
        dto.setCmdId(Integer.valueOf(GuestToolCmdId.NOTIFY_GT_CMD_ID_DISK_INFO));
        dto.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_USER_PROFILE_STRATEGY);
        dto.setDeskId(deskId);

        LOGGER.info("向云桌面[{}]发送个人盘不可用的消息,消息为[{}]", deskId, JSON.toJSONString(dto));
        try {
            guestToolMessageAPI.asyncRequest(dto);
        } catch (BusinessException e) {
            LOGGER.error("给云桌面[{}]的UPMAgent发送挂载磁盘信息失败，失败原因：", deskId, e);
        }
    }

    private void sendAttachDiskToUpmAgent(UUID deskId, AttachDiskMessageDTO attachDiskMessageDTO,
                                          DesktopPoolType desktopPoolType) {
        GuesttoolMessageContent guesttoolMessageContent = new GuesttoolMessageContent();
        if (DesktopPoolType.isPoolDesktop(desktopPoolType)) {
            guesttoolMessageContent.setCode(GuesttoolMessageResultTypeEnum.SUCCESS.getCode());
            guesttoolMessageContent.setMessage(GuesttoolMessageResultTypeEnum.SUCCESS.getMessage());
            guesttoolMessageContent.setContent(attachDiskMessageDTO);
            String requestBody = JSON.toJSONString(guesttoolMessageContent);

            CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
            dto.setBody(requestBody);
            dto.setCmdId(Integer.valueOf(GuestToolCmdId.NOTIFY_GT_CMD_ID_DISK_INFO));
            dto.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_USER_PROFILE_STRATEGY);
            dto.setDeskId(deskId);
            LOGGER.info("给云桌面[{}]的UPMAgent发送挂载磁盘信息，消息内容:{}", deskId, requestBody);

            try {
                guestToolMessageAPI.asyncRequest(dto);
            } catch (BusinessException e) {
                LOGGER.error("给云桌面[{}]的UPMAgent发送挂载磁盘信息失败，失败原因：", deskId, e);
            }
        }
    }

    private synchronized String buildDiskName(String diskNamePrefix, UUID diskPoolId) {
        AtomicInteger startIndex = new AtomicInteger(diskPoolMgmtAPI.getMaxIndexNumWhenAddDisk(diskPoolId));
        String name = String.format(DiskPoolConstants.DISK_NAME_FORMAT, diskNamePrefix, startIndex.incrementAndGet());
        while (DISK_NAME_CACHE.contains(name) || BooleanUtils.toBoolean(deskDiskAPI.checkDiskNameExist(name))) {
            LOGGER.warn("已存在磁盘名称[{}]，重新生成", name);
            name = String.format(DiskPoolConstants.DISK_NAME_FORMAT, diskNamePrefix, startIndex.incrementAndGet());
        }
        DISK_NAME_CACHE.add(name);
        return name;
    }
}
