package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.enums.CbbDeskBackupStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VmState;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/15
 *
 * @author Jarman
 */
public class StartVmDispatcherDTO {

    private CbbDispatcherRequest dispatcherRequest;

    private UUID desktopId;

    private CbbCloudDeskState deskState;

    private VmState vmState;

    private Boolean supportCrossCpuVendor = false;

    private CbbDeskBackupStateEnum deskBackupState;

    public CbbDispatcherRequest getDispatcherRequest() {
        return dispatcherRequest;
    }

    public void setDispatcherRequest(CbbDispatcherRequest dispatcherRequest) {
        this.dispatcherRequest = dispatcherRequest;
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public CbbCloudDeskState getDeskState() {
        return deskState;
    }

    public void setDeskState(CbbCloudDeskState deskState) {
        this.deskState = deskState;
    }

    public Boolean getSupportCrossCpuVendor() {
        return supportCrossCpuVendor;
    }

    public void setSupportCrossCpuVendor(Boolean supportCrossCpuVendor) {
        this.supportCrossCpuVendor = supportCrossCpuVendor;
    }

    public VmState getVmState() {
        return vmState;
    }

    public void setVmState(VmState vmState) {
        this.vmState = vmState;
    }

    public CbbDeskBackupStateEnum getDeskBackupState() {
        return deskBackupState;
    }

    public void setDeskBackupState(CbbDeskBackupStateEnum deskBackupState) {
        this.deskBackupState = deskBackupState;
    }
}
