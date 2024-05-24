package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.vo;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTaskStatus;

import java.util.UUID;

/**
 * Description: 云桌面类型分发任务（子任务）VO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/21 10:29
 *
 * @author zhangyichi
 */
public class DesktopSubTaskVO {

    private String desktopName;

    private String userName;

    /**
     * CbbCloudDeskState
     */
    private String desktopState;

    /**
     * CbbCloudDeskType: VDI/IDV
     */
    private String desktopCategory;

    /**
     * CbbCloudDeskPattern: PERSONAL/RECOVERABLE/APP_LAYER
     */
    private String desktopType;

    private ImageUsageTypeEnum imageUsage;

    private String desktopIp;

    private String terminalIp;

    private String physicalServerIp;

    private UUID subTaskId;

    private FileDistributionTaskStatus status;

    private String message;

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDesktopState() {
        return desktopState;
    }

    public void setDesktopState(String desktopState) {
        this.desktopState = desktopState;
    }

    public String getDesktopCategory() {
        return desktopCategory;
    }

    public void setDesktopCategory(String desktopCategory) {
        this.desktopCategory = desktopCategory;
    }

    public String getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }

    public ImageUsageTypeEnum getImageUsage() {
        return imageUsage;
    }

    public void setImageUsage(ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }

    public String getDesktopIp() {
        return desktopIp;
    }

    public void setDesktopIp(String desktopIp) {
        this.desktopIp = desktopIp;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public String getPhysicalServerIp() {
        return physicalServerIp;
    }

    public void setPhysicalServerIp(String physicalServerIp) {
        this.physicalServerIp = physicalServerIp;
    }

    public UUID getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(UUID subTaskId) {
        this.subTaskId = subTaskId;
    }

    public FileDistributionTaskStatus getStatus() {
        return status;
    }

    public void setStatus(FileDistributionTaskStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
