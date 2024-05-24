package com.ruijie.rcos.rcdc.rco.module.impl.diskpool.quartz;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DiskPoolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.DiskStatus;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.constants.DiskPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.DiskPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.ViewUserDiskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.ViewUserDiskEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.service.UserDiskPoolService;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Description: 磁盘异常定时恢复
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/26
 *
 * @author TD
 */
@Service
@Quartz(scheduleTypeCode = DiskPoolConstants.DISK_FAULT_RECOVERY_TASK, scheduleName = DiskPoolBusinessKey.RCDC_RCO_DISK_FAULT_AUTO_RECOVERY_TASK,
        cron = "0 0/2 * * * ?")
public class DiskFaultScheduledRecoveryQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiskFaultScheduledRecoveryQuartzTask.class);

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private ViewUserDiskDAO viewUserDiskDAO;

    @Autowired
    private UserDiskPoolService diskPoolService;

    /**
     * 待卸载的磁盘
     */
    private static final Map<UUID, ViewUserDiskEntity> NEED_RECOVER_DISK_MAP = new ConcurrentHashMap<>();

    /**
     * 标记磁盘状态集合
     */
    private static final Set<DiskStatus> SIGN_DISK_SET = Sets.newHashSet(DiskStatus.ACTIVE, DiskStatus.IN_USE);

    /**
     * 分配20个线程，队列一万个处理磁盘异常恢复
     */
    private static final ExecutorService THREAD_POOL =
            ThreadExecutors.newBuilder(DiskPoolConstants.DISK_FAULT_RECOVERY_THREAD).maxThreadNum(20).queueSize(10000).build();

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can be not null");
        List<ViewUserDiskEntity> diskEntityList = viewUserDiskDAO.findByDiskPoolTypeAndDesktopState(DiskPoolType.POOL, CbbCloudDeskState.CLOSE);
        LOGGER.info("开始执行DiskFaultScheduledRecoveryQuartzTask定时任务--查询出来磁盘数量：[{}]", diskEntityList.size());
        // 清理缓存数据
        this.cleanNeedRecoverDisk(diskEntityList);
        diskEntityList.stream().filter(userDiskEntity -> Objects.nonNull(userDiskEntity.getDeskId()))
                // 磁盘卸载中不需要处理
                .filter(userDiskEntity -> userDiskEntity.getDiskState() != DiskStatus.DETACHING)
                // 磁盘可用-已绑定桌面首次不需要进行处理，防止出现动态池桌面锁定磁盘，就立即被卸载的情况
                .filter(this::isActiveDiskRecovery)
                .forEach(userDiskEntity -> THREAD_POOL.execute(() -> {
                    LOGGER.info("卸载磁盘信息：{}", JSON.toJSONString(userDiskEntity));
                    UUID deskId = userDiskEntity.getDeskId();
                    UUID diskId = userDiskEntity.getDiskId();
                    try {
                        diskPoolService.deactivateDisk(deskId, diskId);
                        auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_QUARTZ_TASK_DISK_FAULT_RECOVERY_SUCCESS, userDiskEntity.getDiskName());
                        // 删除成功，清理对应缓存
                        NEED_RECOVER_DISK_MAP.remove(diskId);
                    } catch (Exception e) {
                        LOGGER.error("定时任务卸载[{}]磁盘[{}]出现异常", deskId, diskId, e);
                    }
                }));
    }

    private boolean isActiveDiskRecovery(ViewUserDiskEntity userDiskEntity) {
        if (!SIGN_DISK_SET.contains(userDiskEntity.getDiskState())) {
            return true;
        }
        UUID diskId = userDiskEntity.getDiskId();
        if (NEED_RECOVER_DISK_MAP.containsKey(diskId)) {
            return true;
        }
        LOGGER.info("磁盘信息：[{}]，被定时卸载磁盘任务标记", JSON.toJSONString(userDiskEntity));
        NEED_RECOVER_DISK_MAP.put(diskId, userDiskEntity);
        return false;
    }

    private void cleanNeedRecoverDisk(List<ViewUserDiskEntity> diskEntityList) {
        // 查询出来的磁盘集合为空，直接清空缓存
        if (CollectionUtils.isEmpty(diskEntityList)) {
            NEED_RECOVER_DISK_MAP.clear();
        }
        Set<UUID> diskIdSet = diskEntityList.stream().filter(userDiskEntity -> SIGN_DISK_SET.contains(userDiskEntity.getDiskState()))
                .map(ViewUserDiskEntity::getDiskId).collect(Collectors.toSet());
        NEED_RECOVER_DISK_MAP.keySet().stream().filter(needDiskId -> !diskIdSet.contains(needDiskId)).forEach(NEED_RECOVER_DISK_MAP::remove);
    }
}
