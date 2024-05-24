package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;

import javax.persistence.*;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/05/01 23:33
 *
 * @author coderLee23
 */
@Table(name = "v_rco_uam_user_desktop")
@Entity
public class ViewUamUserDesktopEntity {

    /**
     * 云桌面id
     */
    @Id
    private UUID cloudDesktopId;

    /**
     * 云桌面名称
     */
    private String cloudDesktopName;

    /**
     * ip地址
     */
    private String deskIp;

    /**
     * 用户id
     */
    private UUID userId;

    /**
     * 0
     * 用户名
     */
    private String userName;

    /**
     * 镜像模板名称
     */
    private String imageTemplateName;

    /**
     * 规格：镜像类型
     */
    @Enumerated(EnumType.STRING)
    private CbbImageType cbbImageType;

    /**
     * 云桌面类型
     */
    @Enumerated(EnumType.STRING)
    private CbbCloudDeskPattern desktopType;

    @Version
    private Integer version;

    public UUID getCloudDesktopId() {
        return cloudDesktopId;
    }

    public void setCloudDesktopId(UUID cloudDesktopId) {
        this.cloudDesktopId = cloudDesktopId;
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
