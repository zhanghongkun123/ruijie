package com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.QueryDesktopItemDTO;

import java.util.UUID;

/**
 * Description: 统一查询r-center返回的VDI云桌面信息DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27
 *
 * @author linke
 */
public class UnifiedUserDesktopResultDTO extends QueryDesktopItemDTO {

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

    @Override
    public String getDesktopPoolType() {
        return desktopPoolType;
    }

    @Override
    public void setDesktopPoolType(String desktopPoolType) {
        this.desktopPoolType = desktopPoolType;
    }

    @Override
    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    @Override
    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    @Override
    public Boolean getIsPool() {
        return isPool;
    }

    @Override
    public void setIsPool(Boolean isPool) {
        this.isPool = isPool;
    }

    @Override
    public Boolean getIsOpenMaintenance() {
        return isOpenMaintenance;
    }

    @Override
    public void setIsOpenMaintenance(Boolean isOpenMaintenance) {
        this.isOpenMaintenance = isOpenMaintenance;
    }
}