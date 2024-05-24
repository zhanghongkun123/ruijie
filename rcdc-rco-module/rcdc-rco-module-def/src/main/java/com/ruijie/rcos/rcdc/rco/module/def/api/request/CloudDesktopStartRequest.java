package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.enums.CbbDeskBackupStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VmState;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * 启动云桌面请求
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月3日
 * 
 * @author artom
 */
public class CloudDesktopStartRequest implements Request {
    @NotNull
    private UUID id;
    
    @Nullable
    private UUID taskId;

    @Nullable
    private CbbCloudDeskState forbiddenState;

    @NotNull
    private Boolean enableMountOldData = false;

    /**
     * 支持跨CPU厂商启动（异构场景, 仅唤醒桌面需要）
     */
    @Nullable
    private Boolean supportCrossCpuVendor = false;

    @Nullable
    private VmState vmState;

    @Nullable
    private CbbDeskBackupStateEnum deskBackupState;

    @Nullable
    private BatchTaskItem batchTaskItem;

    @Nullable
    private UUID userId;

    public Boolean getEnableMountOldData() {
        return enableMountOldData;
    }

    public void setEnableMountOldData(Boolean enableMountOldData) {
        this.enableMountOldData = enableMountOldData;
    }

    public CloudDesktopStartRequest() {
    }

    public CloudDesktopStartRequest(UUID id, Boolean enableMountOldData) {
        this.id = id;
        this.enableMountOldData = enableMountOldData;
    }

    public CloudDesktopStartRequest(UUID id) {
        this.id = id;
    }

    public CloudDesktopStartRequest(UUID id, UUID taskId) {
        this.id = id;
        this.taskId = taskId;
    }

    public CloudDesktopStartRequest(UUID id, @Nullable UUID taskId, @Nullable CbbCloudDeskState forbiddenState) {
        this.id = id;
        this.taskId = taskId;
        this.forbiddenState = forbiddenState;
    }

    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTaskId() {
        return taskId;
    }
    
    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    @Nullable
    public CbbCloudDeskState getForbiddenState() {
        return forbiddenState;
    }

    public void setForbiddenState(@Nullable CbbCloudDeskState forbiddenState) {
        this.forbiddenState = forbiddenState;
    }

    @Nullable
    public Boolean getSupportCrossCpuVendor() {
        return supportCrossCpuVendor;
    }

    public void setSupportCrossCpuVendor(@Nullable Boolean supportCrossCpuVendor) {
        this.supportCrossCpuVendor = supportCrossCpuVendor;
    }

    @Nullable
    public VmState getVmState() {
        return vmState;
    }

    public void setVmState(@Nullable VmState vmState) {
        this.vmState = vmState;
    }

    @Nullable
    public CbbDeskBackupStateEnum getDeskBackupState() {
        return deskBackupState;
    }

    public void setDeskBackupState(@Nullable CbbDeskBackupStateEnum deskBackupState) {
        this.deskBackupState = deskBackupState;
    }

    @Nullable
    public BatchTaskItem getBatchTaskItem() {
        return batchTaskItem;
    }

    public void setBatchTaskItem(@Nullable BatchTaskItem batchTaskItem) {
        this.batchTaskItem = batchTaskItem;
    }

    @Nullable
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(@Nullable UUID userId) {
        this.userId = userId;
    }
}
