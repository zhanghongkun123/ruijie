package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * query desktop item info
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月7日
 *
 * @author artom
 */
public class QueryDesktopItemDTO {

    private String id;

    private String imageName;

    private String osName;

    private String desktopState;

    private String desktopName;

    /**
     * 云桌面标签
     */
    private String remark;

    /**
     * 根据策略获取代理协议信息（当用户处于代理环境下时 进行该字段校验 false时不允许接入）
     */
    private Boolean enableAgreementAgency;

    /**
     * 是否开启网页客户端接入
     **/
    private Boolean enableWebClient;

    /**
     *  桌面池类型
     **/
    private String desktopPoolType;

    /**
     * 桌面池id
     **/
    private UUID desktopPoolId;

    private Boolean isPool = false;

    private Boolean isOpenMaintenance = false;

    /**
     * 是否离线
     */
    private Boolean isOffline = false;


    /**
     ** 云桌面镜像类型（win7、 win10...）
     */
    private CbbOsType desktopImageType;

    private CbbDesktopSessionType sessionType;

    private String desktopCategory;

    private ImageUsageTypeEnum imageUsage;

    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getDesktopState() {
        return desktopState;
    }

    public void setDesktopState(String desktopState) {
        this.desktopState = desktopState;
    }


    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getEnableAgreementAgency() {
        return enableAgreementAgency;
    }

    public void setEnableAgreementAgency(Boolean enableAgreementAgency) {
        this.enableAgreementAgency = enableAgreementAgency;
    }

    public Boolean getEnableWebClient() {
        return enableWebClient;
    }

    public void setEnableWebClient(Boolean enableWebClient) {
        this.enableWebClient = enableWebClient;
    }

    public String getDesktopPoolType() {
        return desktopPoolType;
    }

    public void setDesktopPoolType(String desktopPoolType) {
        this.desktopPoolType = desktopPoolType;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public Boolean getIsPool() {
        return isPool;
    }

    public void setIsPool(Boolean isPool) {
        this.isPool = isPool;
    }

    public Boolean getIsOpenMaintenance() {
        return isOpenMaintenance;
    }

    public void setIsOpenMaintenance(Boolean isOpenMaintenance) {
        this.isOpenMaintenance = isOpenMaintenance;
    }

    public Boolean getIsOffline() {
        return isOffline;
    }

    public void setIsOffline(Boolean offline) {
        isOffline = offline;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }

    public CbbOsType getDesktopImageType() {
        return desktopImageType;
    }

    public void setDesktopImageType(CbbOsType desktopImageType) {
        this.desktopImageType = desktopImageType;
    }

    public String getDesktopCategory() {
        return desktopCategory;
    }

    public void setDesktopCategory(String desktopCategory) {
        this.desktopCategory = desktopCategory;
    }

    public ImageUsageTypeEnum getImageUsage() {
        return imageUsage;
    }

    public void setImageUsage(ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "QueryDesktopItemDTO{" +
                "id='" + id + '\'' +
                ", imageName='" + imageName + '\'' +
                ", osName='" + osName + '\'' +
                ", desktopState='" + desktopState + '\'' +
                ", desktopName='" + desktopName + '\'' +
                ", remark='" + remark + '\'' +
                ", enableAgreementAgency=" + enableAgreementAgency +
                ", enableWebClient=" + enableWebClient +
                ", desktopPoolType='" + desktopPoolType + '\'' +
                ", desktopPoolId=" + desktopPoolId +
                ", isPool=" + isPool +
                ", isOpenMaintenance=" + isOpenMaintenance +
                ", desktopImageType=" + desktopImageType +
                ", imageUsage=" + imageUsage +
                ", imageUsage=" + createTime +
                '}';
    }

    /**
     *
     * @param desktopDTO desktop CloudDesktopDTO
     * @return item
     */
    public static QueryDesktopItemDTO convertToItem(CloudDesktopDTO desktopDTO) {
        Assert.notNull(desktopDTO, "desktopDTO must not be null.");
        QueryDesktopItemDTO item = new QueryDesktopItemDTO();
        item.setId(String.valueOf(desktopDTO.getCbbId()));
        item.setImageName(desktopDTO.getImageName());
        item.setOsName(desktopDTO.getOsName());
        item.setDesktopState(desktopDTO.getDesktopState());
        item.setDesktopName(desktopDTO.getDesktopName());
        // 云桌面标签
        item.setRemark(desktopDTO.getRemark());
        // 是否只允许代理接入访问
        item.setEnableAgreementAgency(desktopDTO.getEnableAgreementAgency());
        // 是否开启网页客户端接入
        item.setEnableWebClient(desktopDTO.getEnableWebClient());
        item.setDesktopPoolId(desktopDTO.getDesktopPoolId());
        item.setDesktopPoolType(desktopDTO.getDesktopPoolType());
        if (Objects.isNull(desktopDTO.getDesktopPoolType())) {
            item.setIsPool(false);
        } else {
            item.setIsPool(DesktopPoolType.isPoolDesktop(DesktopPoolType.valueOf(desktopDTO.getDesktopPoolType())));
        }
        item.setIsOpenMaintenance(desktopDTO.getIsOpenMaintenance());
        if (!Objects.equals(desktopDTO.getDesktopType(), CbbCloudDeskType.THIRD.name()) && desktopDTO.getPlatformStatus() != null) {
            item.setIsOffline(CloudPlatformStatus.isAvailable(desktopDTO.getPlatformStatus()));
        }
        item.setDesktopImageType(desktopDTO.getDesktopImageType());
        item.setSessionType(desktopDTO.getSessionType());
        item.setDesktopCategory(desktopDTO.getDesktopCategory());
        item.setImageUsage(desktopDTO.getImageUsage());
        item.setCreateTime(desktopDTO.getCreateTime());
        return item;
    }
}
