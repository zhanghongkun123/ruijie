package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.ProgressStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/22 16:43
 *
 * @author coderLee23
 */
public class UamDeliveryRecordDTO {

    private UUID id;

    /**
     * 云桌面名称
     */
    private String cloudDesktopName;

    /**
     * ip地址
     */
    private String deskIp;

    /**
     * 应用名称
     */
    private String appName;


    /**
     * 软件安装包名称
     */
    private String fileName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * /**
     * 规格：镜像类型
     */
    private CbbImageType cbbImageType;

    /**
     * 云桌面类型
     */
    private CbbCloudDeskPattern desktopType;

    /**
     * 交付状态
     */
    private ProgressStatusEnum progressStatus;

    /**
     * 交付描述
     */
    private String progressDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCloudDesktopName() {
        return cloudDesktopName;
    }

    public void setCloudDesktopName(String cloudDesktopName) {
        this.cloudDesktopName = cloudDesktopName;
    }

    public String getDeskIp() {
        return deskIp;
    }

    public void setDeskIp(String deskIp) {
        this.deskIp = deskIp;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public CbbCloudDeskPattern getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(CbbCloudDeskPattern desktopType) {
        this.desktopType = desktopType;
    }

    public ProgressStatusEnum getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(ProgressStatusEnum progressStatus) {
        this.progressStatus = progressStatus;
    }

    public String getProgressDesc() {
        return progressDesc;
    }

    public void setProgressDesc(String progressDesc) {
        this.progressDesc = progressDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
