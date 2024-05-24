package com.ruijie.rcos.rcdc.rco.module.impl.disksnapshot.quartz;


import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.request.systemlog.BaseCreateSystemLogRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDiskSnapshotMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDiskSnapshotDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.disk.CbbDiskCreateSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOperateDiskSnapshotMethod;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.DiskStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskSnapshotAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDiskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.UserDiskDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.disksnapshot.DiskSnapshotBusinessKey;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.base.quartz.TaskGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.rco.module.impl.disksnapshot.DiskSnapshotBusinessKey.*;

/**
 * Description: 创建磁盘快照定时任务
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年07月25日
 *
 * @author lyb
 */
@Service
@Quartz(taskGroup = TaskGroup.ADMIN_CONFIG, scheduleName = DiskSnapshotBusinessKey.RCDC_RCO_QUARTZ_DISK_CREATE_SNAPSHOT,
        scheduleTypeCode = ScheduleTypeCodeConstants.DISK_CREATE_SNAPSHOT_TYPE_CODE, blockInMaintenanceMode = true)
public class CloudDiskCreateSnapshotQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudDiskCreateSnapshotQuartzTask.class);

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder("create-disk-snapshot").maxThreadNum(10).queueSize(10000).build();

    @Autowired
    private DiskPoolMgmtAPI diskPoolMgmtAPI;

    @Autowired
    private CbbVDIDiskSnapshotMgmtAPI cbbVDIDiskSnapshotMgmtAPI;

    @Autowired
    private DiskSnapshotAPI diskSnapshotAPI;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Autowired
    private UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    private CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        DiskSnapshotQuartzData diskSnapshotQuartzData = quartzTaskContext.getByType(DiskSnapshotQuartzData.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("创建磁盘快照定时任务开始，diskSnapshotQuartzData:[{}]", JSON.toJSONString(diskSnapshotQuartzData));
        }
        Set<DiskInfo> diskInfoSet = Sets.newHashSet();
        addDiskPoolArr(diskSnapshotQuartzData.getDiskPoolArr(), diskInfoSet);
        addDiskArr(diskSnapshotQuartzData.getDiskArr(), diskInfoSet);

        diskInfoSet.forEach(diskInfo -> THREAD_EXECUTOR.execute(() -> execute(diskInfo)));
    }

    @Override
    public void validate(@Nullable String bizData) throws BusinessException {
        Assert.hasText(bizData, "bizData must has text");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("创建磁盘快照定时任务校验参数：{}", bizData);
        }
        DiskSnapshotQuartzData diskSnapshotQuartzData = JSON.parseObject(bizData, DiskSnapshotQuartzData.class);
        Assert.notNull(diskSnapshotQuartzData, "diskSnapshotQuartzData can not be null");
        boolean isDiskPoolArrEmpty = Objects.isNull(diskSnapshotQuartzData.getDiskPoolArr());
        boolean isDiskArrEmpty = Objects.isNull(diskSnapshotQuartzData.getDiskArr());
        if (isDiskPoolArrEmpty && isDiskArrEmpty) {
            throw new BusinessException(RCDC_RCO_QUARTZ_DISK_CREATE_SNAPSHOT_DISK_POOL_AND_DISK_ID_BOTH_BE_NULL);
        }
        if (!isDiskArrEmpty) {
            for (UUID diskId : diskSnapshotQuartzData.getDiskArr()) {
                cbbVDIDeskDiskAPI.getDiskDetail(diskId);
            }
        }

        if (!isDiskPoolArrEmpty) {
            for (UUID poolId : diskSnapshotQuartzData.getDiskPoolArr()) {
                cbbDiskPoolMgmtAPI.getDiskPoolDetail(poolId);
            }
        }
    }

    private void addDiskPoolArr(UUID[] diskPoolArr, Set<DiskInfo> diskInfoSet) {
        if (Objects.isNull(diskPoolArr)) {
            return;
        }
        Arrays.stream(diskPoolArr).forEach(diskPoolId -> {
            List<UserDiskDetailDTO> userDiskDetailDTOList = diskPoolMgmtAPI.querySnapshotCapableDiskByDiskPoolId(diskPoolId);
            diskInfoSet.addAll(
                    userDiskDetailDTOList.stream().filter(this::isNeedToSnapshot)
                            .map(dto -> new DiskInfo(dto.getDiskId(), dto.getDiskName())).collect(Collectors.toList()));
        });
    }

    private void addDiskArr(UUID[] diskArr, Set<DiskInfo> diskInfoSet) throws BusinessException {
        if (Objects.isNull(diskArr)) {
            return;
        }
        for (UUID singleDiskId : diskArr) {
            CbbDeskDiskDTO cbbDeskDiskDTO = cbbVDIDeskDiskAPI.getDiskDetail(singleDiskId);
            if (Objects.isNull(cbbDeskDiskDTO)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("创建磁盘快照定时任务异常，磁盘[{}}]不存在", singleDiskId);
                }
                continue;
            }
            UserDiskDetailDTO userDiskDTO = userDiskMgmtAPI.userDiskDetail(singleDiskId);
            if (isNeedToSnapshot(userDiskDTO)) {
                diskInfoSet.add(new DiskInfo(singleDiskId, cbbDeskDiskDTO.getName()));
            }
        }
    }

    private boolean isNeedToSnapshot(UserDiskDetailDTO userDiskDetailDTO) {
        Optional<CbbDiskSnapshotDetailDTO> cbbDiskSnapshotDetailDTO =
                cbbVDIDiskSnapshotMgmtAPI.getLatestDiskSnapshotByDiskId(userDiskDetailDTO.getDiskId());

        // 对刚创建完成没有数据的个人盘做快照，支持手动打快照，定时任务不支持
        if (Objects.isNull(userDiskDetailDTO.getLatestUseTime())) {
            return false;
        }

        // 磁盘最后在线时间早于最后快照时间，不做快照(使用中状态的磁盘需要打快照)
        if (userDiskDetailDTO.getDiskState() != DiskStatus.IN_USE && cbbDiskSnapshotDetailDTO.isPresent()) {
            boolean isLastUseTimeAfterSnapshotTime = userDiskDetailDTO.getLatestUseTime().after(cbbDiskSnapshotDetailDTO.get().getCreateTime());
            if (!isLastUseTimeAfterSnapshotTime) {
                LOGGER.info("磁盘[{}]最后在线时间早于最后快照时间，不做快照", userDiskDetailDTO.getDiskId());
            }
            return isLastUseTimeAfterSnapshotTime;
        }
        return true;
    }

    private void execute(DiskInfo diskInfo) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("执行创建磁盘[{}]快照异步任务开始。", diskInfo.getDiskId());
        }
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            Boolean isOver = diskSnapshotAPI.checkSnapshotNumberOverByDiskId(diskInfo.getDiskId());
            if (Boolean.TRUE.equals(isOver)) {
                diskSnapshotAPI.deleteBeforeOverDiskSnapshotByDiskId(diskInfo.getDiskId());
            }

            UUID taskId = UUID.randomUUID();
            UUID diskSnapshotId = UUID.randomUUID();
            String diskSnapshotName = diskSnapshotAPI.generateSnapshotNameByDiskName(diskInfo.getDiskName());
            CbbDiskCreateSnapshotDTO cbbDiskCreateSnapshotDTO = new CbbDiskCreateSnapshotDTO();
            cbbDiskCreateSnapshotDTO.setTaskId(taskId);
            cbbDiskCreateSnapshotDTO.setDiskSnapshotId(diskSnapshotId);
            cbbDiskCreateSnapshotDTO.setSnapshotName(diskSnapshotName);
            cbbDiskCreateSnapshotDTO.setDiskId(diskInfo.getDiskId());
            cbbDiskCreateSnapshotDTO.setCreateMethod(CbbOperateDiskSnapshotMethod.POOL);
            cbbVDIDiskSnapshotMgmtAPI.createDiskSnapshot(cbbDiskCreateSnapshotDTO);

            writeLog(true, diskInfo.getDiskName(), stopwatch, "");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("执行创建磁盘[{}]快照异步任务结束。", diskInfo.getDiskId());
            }
        } catch (BusinessException e) {
            LOGGER.error("执行创建磁盘[" + diskInfo.getDiskId() + "]快照异步任务失败。", e);
            writeLog(false, diskInfo.getDiskName(), stopwatch, e.getI18nMessage());
        } catch (Exception e) {
            LOGGER.error("执行创建磁盘[" + diskInfo.getDiskId() + "]快照异步任务失败。", e);
            writeLog(false, diskInfo.getDiskName(), stopwatch, LocaleI18nResolver.resolve(RCDC_RCO_DISK_SNAPSHOT_NUKNOWN_ERROR));
        }
    }

    private void writeLog(boolean isSuccess, String diskName, Stopwatch stopwatch, String message) {
        String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
        if (isSuccess) {
            addSystemLog(RCDC_RCO_QUARTZ_DISK_CREATE_SNAPSHOT_SUCCESS_SYSTEM_LOG, new String[] {diskName, timeMillis});
        } else {
            addSystemLog(RCDC_RCO_QUARTZ_DISK_CREATE_SNAPSHOT_FAIL_SYSTEM_LOG, new String[] {diskName, message, timeMillis});
        }
    }

    private void addSystemLog(String businessKey, String[] args) {
        BaseCreateSystemLogRequest createRequest = new BaseCreateSystemLogRequest(businessKey, args);
        baseSystemLogMgmtAPI.createSystemLog(createRequest);
    }
}
