package com.ruijie.rcos.rcdc.rco.module.impl.disksnapshot.quartz;

import java.util.UUID;

/**
 * Description: 创建磁盘快照定时任务数据
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年07月25日
 *
 * @author lyb
 */
public class DiskSnapshotQuartzData {

    /**
     * 磁盘池id列表
     */
    private UUID[] diskPoolArr;

    /**
     * 磁盘id
     */
    private UUID[] diskArr;

    public UUID[] getDiskPoolArr() {
        return diskPoolArr;
    }

    public void setDiskPoolArr(UUID[] diskPoolArr) {
        this.diskPoolArr = diskPoolArr;
    }

    public UUID[] getDiskArr() {
        return diskArr;
    }

    public void setDiskArr(UUID[] diskArr) {
        this.diskArr = diskArr;
    }
}
