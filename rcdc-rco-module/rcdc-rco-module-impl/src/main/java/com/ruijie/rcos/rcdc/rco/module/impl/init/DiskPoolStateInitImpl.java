package com.ruijie.rcos.rcdc.rco.module.impl.init;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DiskPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.constants.DiskPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.DiskPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.ViewUserDiskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.ViewUserDiskEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.service.UserDiskPoolService;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Description: 系统异常恢复后，磁盘池中间状态恢复初始化
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/28
 *
 * @author TD
 */
@Service
public class DiskPoolStateInitImpl implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiskPoolStateInitImpl.class);

    @Autowired
    private CbbDiskPoolMgmtAPI diskPoolMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private ViewUserDiskDAO viewUserDiskDAO;

    @Autowired
    private UserDiskPoolService diskPoolService;

    private static final List<CbbDiskPoolState> MIDDLE_STATE_LIST =
            Arrays.asList(CbbDiskPoolState.CREATING, CbbDiskPoolState.DELETING, CbbDiskPoolState.UPDATING);

    @Override
    public void safeInit() {
        // 磁盘池中间状态恢复
        diskPoolMgmtAPI.listAllDiskPool().stream().filter(diskPoolDTO -> MIDDLE_STATE_LIST.contains(diskPoolDTO.getPoolState()))
                .forEach(diskPoolDTO -> {
                    try {
                        diskPoolMgmtAPI.updateState(diskPoolDTO.getId(), CbbDiskPoolState.AVAILABLE);
                    } catch (Exception e) {
                        LOGGER.info("修改磁盘池[{}]状态出现异常", diskPoolDTO.getName(), e);
                    }
                });
        // 磁盘定时检测恢复，开机自启执行一次
        diskFaultRecovery();
    }

    private void diskFaultRecovery() {
        List<ViewUserDiskEntity> diskEntityList = viewUserDiskDAO.findByDiskPoolTypeAndDesktopState(DiskPoolType.POOL, CbbCloudDeskState.CLOSE);
        LOGGER.info("初始化进行磁盘恢复信息为:{}", JSON.toJSONString(diskEntityList));
        diskEntityList.stream().filter(userDiskEntity -> Objects.nonNull(userDiskEntity.getDeskId()))
                .forEach(userDiskEntity -> ThreadExecutors.execute(DiskPoolConstants.DISK_FAULT_RECOVERY_THREAD, () -> {
                    LOGGER.info("准备开始卸载磁盘[{}], 磁盘信息：{}", userDiskEntity.getDiskId(), userDiskEntity.toString());
                    try {
                        diskPoolService.deactivateDisk(userDiskEntity.getDeskId(), userDiskEntity.getDiskId());
                        auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_QUARTZ_TASK_DISK_FAULT_RECOVERY_SUCCESS, userDiskEntity.getDiskName());
                    } catch (Exception e) {
                        LOGGER.error("定时任务桌面[{}]卸载故障磁盘[{}]出现异常", userDiskEntity.getDeskId(), userDiskEntity.getDiskId(), e);
                    }
                }));
    }
}
