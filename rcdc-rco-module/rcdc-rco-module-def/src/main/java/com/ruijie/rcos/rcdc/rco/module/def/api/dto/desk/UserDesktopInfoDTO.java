package com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskRegisterState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;

import java.util.UUID;

/**
 * Description: 桌面信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-04-22
 *
 * @author zqj
 */
public class UserDesktopInfoDTO {

    /**
     *  云桌面所在集群id
     */
    private UUID clusterId;

    /**
     * 云桌面所在集群名称
     */
    private String clusterName;

    /**
     * 云桌面所在集群ip
     */
    private String clusterIp;

    /**
     * 云桌面所在集群RCDC通信端口
     */
    private String clusterPort;

    /**
     * 云桌面所在集群代理服务器ip
     */
    private String proxyIp;

    /**
     * 云桌面所在集群代理服务器端口
     */
    private String proxyPort;

    /**
     * 云桌面所在集群 客户端版本号
     */
    private String clusterVersion;

    /**
     * 请求节点的客户端版本
     */
    private String currentVersion;

    private String desktopId;

    private String imageName;

    private String osName;

    private String desktopState;

    private String desktopName;

    /**
     * 云桌面标签
     */
    private String remark;

    /**
     * 根据策略获取代理协议信息（true 情况下只允许通过代理访问）
     */
    private Boolean enableAgreementAgency;

    /**
     * 桌面模式: IDV/VDI
     */
    private String desktopCategory;

    /**
     * 是否开启网页客户端接入
     **/
    private Boolean enableWebClient;

    private UUID userId;

    private String userName;

    private Boolean isPool = false;

    private Boolean isOpenMaintenance = false;

    /**
     * 是否离线
     */
    private Boolean isOffline = false;

    /**
     * 云平台ID
     */
    private UUID platformId;

    /**
     * 云平台名称
     */
    private String platformName;

    /**
     * 注册状态
     */
    private CbbDeskRegisterState registerState;

    /**
     * 注册消息
     */
    private String registerMessage;

    private CbbDesktopSessionType sessionType;

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getClusterIp() {
        return clusterIp;
    }

    public void setClusterIp(String clusterIp) {
        this.clusterIp = clusterIp;
    }

    public String getClusterPort() {
        return clusterPort;
    }

    public void setClusterPort(String clusterPort) {
        this.clusterPort = clusterPort;
    }

    public String getProxyIp() {
        return proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getClusterVersion() {
        return clusterVersion;
    }

    public void setClusterVersion(String clusterVersion) {
        this.clusterVersion = clusterVersion;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(String desktopId) {
        this.desktopId = desktopId;
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

    public String getDesktopCategory() {
        return desktopCategory;
    }

    public void setDesktopCategory(String desktopCategory) {
        this.desktopCategory = desktopCategory;
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

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public CbbDeskRegisterState getRegisterState() {
        return registerState;
    }

    public void setRegisterState(CbbDeskRegisterState registerState) {
        this.registerState = registerState;
    }

    public String getRegisterMessage() {
        return registerMessage;
    }

    public void setRegisterMessage(String registerMessage) {
        this.registerMessage = registerMessage;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }
}