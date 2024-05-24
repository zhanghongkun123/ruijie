package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.response;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PlatformBaseInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskSnapshotState;

import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月06日
 *
 * @author 徐国祥
 */
public class DiskSnapshotDetailVO extends PlatformBaseInfoDTO {
    private UUID id;

    private String name;

    private UUID diskId;

    private CbbDiskSnapshotState state;

    private Date createTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getDiskId() {
        return diskId;
    }

    public void setDiskId(UUID diskId) {
        this.diskId = diskId;
    }

    public CbbDiskSnapshotState getState() {
        return state;
    }

    public void setState(CbbDiskSnapshotState state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
