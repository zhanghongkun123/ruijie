package com.ruijie.rcos.rcdc.rco.module.impl.disksnapshot.quartz;

import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import java.util.UUID;

/**
 * Description: 磁盘基本信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年07月26日
 *
 * @author lyb
 */
public class DiskInfo extends EqualsHashcodeSupport {

    /**
     * 磁盘id
     */
    private UUID diskId;

    /**
     * 磁盘名称
     */
    private String diskName;

    public DiskInfo(UUID diskId, String disName) {
        this.diskId = diskId;
        this.diskName = disName;
    }

    public UUID getDiskId() {
        return diskId;
    }

    public void setDiskId(UUID diskId) {
        this.diskId = diskId;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }
}
