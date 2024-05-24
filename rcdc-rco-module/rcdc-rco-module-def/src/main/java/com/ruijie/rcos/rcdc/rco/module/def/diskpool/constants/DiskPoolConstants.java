package com.ruijie.rcos.rcdc.rco.module.def.diskpool.constants;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.DiskStatus;
import org.apache.commons.compress.utils.Sets;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

/**
 * Description: 磁盘池常量类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/29
 *
 * @author TD
 */
public interface DiskPoolConstants {
    /**
     * 磁盘名称分割下划线
     */
    String DISK_NAME_SEPARATOR = "_";

    /**
     * 线程名称
     */
    String CREATE_POOL_DISK = "create_pool_disk";

    /**
     * 磁盘名称格式化
     */
    String DISK_NAME_FORMAT = "%s_%05d";

    /**
     * 线程TASK标识
     */
    UUID CREATE_DISK_POOL_TASK_ID = UUID.nameUUIDFromBytes(CREATE_POOL_DISK.getBytes(StandardCharsets.UTF_8));

    /**
     * 可删除的磁盘状态
     */
    Set<CbbDiskState> REMOVABLE_DISK_STATUS = Sets.newHashSet(CbbDiskState.ACTIVE, CbbDiskState.DISABLE, CbbDiskState.ERROR);

    /**
     * 可扩容的磁盘状态
     */
    Set<CbbDiskState> EXPAND_DISK_STATUS = Sets.newHashSet(CbbDiskState.ACTIVE, CbbDiskState.DISABLE, CbbDiskState.IN_USE);

    /**
     * 可绑定-可解绑的磁盘状态
     */
    Set<DiskStatus> BIND_DISK_STATUS = Sets.newHashSet(DiskStatus.ACTIVE, DiskStatus.DISABLE, DiskStatus.ERROR);

    /**
     * 磁盘故障恢复任务
     */
    String DISK_FAULT_RECOVERY_TASK = "disk_fault_recovery_task";

    /**
     * 磁盘故障恢复线程名称
     */
    String DISK_FAULT_RECOVERY_THREAD = "disk_fault_recovery_thread";

    Integer SUCCESS = 0;

    Integer FAIL = 1;

    String DEFAULT_FAIL_MESSAGE_WITHOUT_DISK = "not exist personal disk";

    /**
     * 默认盘符
     */
    String DEFAULT_LETTER = "DEFAULT";

    /**
     * 支持启动云桌面的磁盘状态
     */
    Set<DiskStatus> START_DISK_STATUS = Sets.newHashSet(DiskStatus.IN_USE, DiskStatus.SNAPSHOT_CREATING, DiskStatus.BACKUP_CREATING);

    /**
     * 支持锁定磁盘的云桌面状态
     */
    Set<CbbCloudDeskState> LOCK_DISK_DESKTOP_STATUS = Sets.newHashSet(CbbCloudDeskState.CLOSE, CbbCloudDeskState.SLEEP, CbbCloudDeskState.RUNNING);
}
