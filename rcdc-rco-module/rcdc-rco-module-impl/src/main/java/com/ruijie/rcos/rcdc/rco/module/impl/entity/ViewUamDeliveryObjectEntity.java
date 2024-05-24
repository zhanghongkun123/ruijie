package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/05 12:02
 *
 * @author coderLee23
 */
@Table(name = "v_rco_uam_delivery_object")
@Entity
public class ViewUamDeliveryObjectEntity {

    @Id
    private UUID id;

    /**
     * 交付组id
     */
    private UUID deliveryGroupId;

    /**
     * 交付名称
     */
    private String deliveryGroupName;

    /**
     * 云桌面id
     */
    private UUID cloudDesktopId;

    /**
     * 应用交付类型
     */
    @Enumerated(value = EnumType.STRING)
    private AppDeliveryTypeEnum appDeliveryType;

    /**
     * 交付状态
     */
    @Enumerated(value = EnumType.STRING)
    private DeliveryStatusEnum deliveryStatus;

    /**
     * 云桌面名称
     */
    private String cloudDesktopName;

    /**
     * 云桌面状态
     */
    @Enumerated(value = EnumType.STRING)
    private CbbCloudDeskState deskState;

    /**
     * 规格：操作系统类型
     */
    @Enumerated(value = EnumType.STRING)
    private CbbOsType osType;

    /**
     * 操作系统版本号
     */
    private String osVersion;

    /**
     * 规格：镜像类型
     */
    @Enumerated(EnumType.STRING)
    private CbbImageType cbbImageType;

    /**
     * 云桌面ip地址
     */
    private String deskIp;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 终端名
     */
    private String terminalName;

    /**
     * 交付时间
     */
    private Date createTime;

    private UUID platformId;

    @Enumerated(EnumType.STRING)
    private CloudPlatformStatus platformStatus;

    private String platformName;

    @Enumerated(EnumType.STRING)
    private CloudPlatformType platformType;

    @Version
    private Integer version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDeliveryGroupId() {
        return deliveryGroupId;
    }

    public void setDeliveryGroupId(UUID deliveryGroupId) {
        this.deliveryGroupId = deliveryGroupId;
    }

    public String getDeliveryGroupName() {
        return deliveryGroupName;
    }

    public void setDeliveryGroupName(String deliveryGroupName) {
        this.deliveryGroupName = deliveryGroupName;
    }

    public UUID getCloudDesktopId() {
        return cloudDesktopId;
    }

    public void setCloudDesktopId(UUID cloudDesktopId) {
        this.cloudDesktopId = cloudDesktopId;
    }

    public AppDeliveryTypeEnum getAppDeliveryType() {
        return appDeliveryType;
    }

    public void setAppDeliveryType(AppDeliveryTypeEnum appDeliveryType) {
        this.appDeliveryType = appDeliveryType;
    }

    public DeliveryStatusEnum getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatusEnum deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getCloudDesktopName() {
        return cloudDesktopName;
    }

    public void setCloudDesktopName(String cloudDesktopName) {
        this.cloudDesktopName = cloudDesktopName;
    }

    public CbbCloudDeskState getDeskState() {
        return deskState;
    }

    public void setDeskState(CbbCloudDeskState deskState) {
        this.deskState = deskState;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public String getDeskIp() {
        return deskIp;
    }

    public void setDeskIp(String deskIp) {
        this.deskIp = deskIp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }

    public CloudPlatformStatus getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(CloudPlatformStatus platformStatus) {
        this.platformStatus = platformStatus;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public CloudPlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(CloudPlatformType platformType) {
        this.platformType = platformType;
    }
}
