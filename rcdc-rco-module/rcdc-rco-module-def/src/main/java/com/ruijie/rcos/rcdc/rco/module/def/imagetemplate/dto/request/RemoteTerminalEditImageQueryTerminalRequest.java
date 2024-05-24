package com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.request;

import java.util.UUID;

import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

/**
 * <br>
 * Description: 终端列表查询请求 <br>
 * Copyright: Copyright (c) 2022 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2022/07/13 <br>
 *
 * @author ypp
 */

public class RemoteTerminalEditImageQueryTerminalRequest extends PageWebRequest {

    /**
     * 是否下载过镜像
     */
    private Boolean hasDownloadImage;

    /**
     * 是否安装过驱动
     */
    private Boolean hasInstallDrive;

    /**
     * 终端mac
     */
    private String terminalId;

    /**
     * 终端名称
     */
    private String terminalName;

    /**
     * 终端ip
     */
    private String terminalIp;

    /**
     * 终端状态
     */
    private CbbTerminalStateEnums state;

    /**
     * 管理员id
     */
    private UUID adminId;

    /**
     * 是否拥有所有权限
     */
    private Boolean hasAllTerminalGroupPermission;

    /**
     * 镜像id
     */
    private UUID imageId;

    public Boolean getHasDownloadImage() {
        return hasDownloadImage;
    }

    public void setHasDownloadImage(Boolean hasDownloadImage) {
        this.hasDownloadImage = hasDownloadImage;
    }

    public Boolean getHasInstallDrive() {
        return hasInstallDrive;
    }

    public void setHasInstallDrive(Boolean hasInstallDrive) {
        this.hasInstallDrive = hasInstallDrive;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public CbbTerminalStateEnums getState() {
        return state;
    }

    public void setState(CbbTerminalStateEnums state) {
        this.state = state;
    }

    public Boolean getHasAllTerminalGroupPermission() {
        return hasAllTerminalGroupPermission;
    }

    public void setHasAllTerminalGroupPermission(Boolean hasAllTerminalGroupPermission) {
        this.hasAllTerminalGroupPermission = hasAllTerminalGroupPermission;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }
}
