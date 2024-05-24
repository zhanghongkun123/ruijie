package com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto;

import java.util.UUID;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbNetworkModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;

/**
 * Description: 远程终端编辑镜像-终端DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/11
 *
 * @author ypp
 */
public class ViewTerminalWithImageDTO {

    private String id;

    private String ip;

    private String macAddr;

    private String productType;

    private String terminalId;

    private String terminalName;

    /**
     * 终端状态
     */
    private String terminalState;

    /**
     * 镜像下载状态
     */
    private String downloadState;

    /**
     * 是否安装过驱动
     */
    private String installDriveState;

    /**
     * 终端所属教室id
     */
    private String terminalGroupId;

    /**
     * 终端所属教室名称
     */
    private String terminalGroupName;


    /**
     * 镜像id
     */
    private String imageId;

    /**
     * 驱动id
     */
    private UUID driveId;

    /**
     * 开机模式
     */
    private String startMode;

    /**
     * 产品id
     */
    private String productId;

    private Boolean enableProxy;

    private CbbNetworkModeEnums networkAccessMode;

    /**
     * 绑定用户名称
     */
    private String bindUserName;

    /**
     * 绑定桌面id
     */
    private UUID bindDeskId;

    private IdvTerminalModeEnums idvTerminalMode;

    private String deskIp;

    /**
     * CPU架构
     */
    @Enumerated(EnumType.STRING)
    private CbbCpuArchType cpuArchType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
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

    public String getTerminalState() {
        return terminalState;
    }

    public void setTerminalState(String terminalState) {
        this.terminalState = terminalState;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public UUID getDriveId() {
        return driveId;
    }

    public void setDriveId(UUID driveId) {
        this.driveId = driveId;
    }

    public String getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(String downloadState) {
        this.downloadState = downloadState;
    }

    public String getInstallDriveState() {
        return installDriveState;
    }

    public void setInstallDriveState(String installDriveState) {
        this.installDriveState = installDriveState;
    }

    public String getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(String terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }

    public String getTerminalGroupName() {
        return terminalGroupName;
    }

    public void setTerminalGroupName(String terminalGroupName) {
        this.terminalGroupName = terminalGroupName;
    }

    public String getStartMode() {
        return startMode;
    }

    public void setStartMode(String startMode) {
        this.startMode = startMode;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Boolean getEnableProxy() {
        return enableProxy;
    }

    public void setEnableProxy(Boolean enableProxy) {
        this.enableProxy = enableProxy;
    }

    public CbbNetworkModeEnums getNetworkAccessMode() {
        return networkAccessMode;
    }

    public void setNetworkAccessMode(CbbNetworkModeEnums networkAccessMode) {
        this.networkAccessMode = networkAccessMode;
    }

    public String getBindUserName() {
        return bindUserName;
    }

    public void setBindUserName(String bindUserName) {
        this.bindUserName = bindUserName;
    }

    public UUID getBindDeskId() {
        return bindDeskId;
    }

    public void setBindDeskId(UUID bindDeskId) {
        this.bindDeskId = bindDeskId;
    }

    public IdvTerminalModeEnums getIdvTerminalMode() {
        return idvTerminalMode;
    }

    public void setIdvTerminalMode(IdvTerminalModeEnums idvTerminalMode) {
        this.idvTerminalMode = idvTerminalMode;
    }

    public String getDeskIp() {
        return deskIp;
    }

    public void setDeskIp(String deskIp) {
        this.deskIp = deskIp;
    }

    public CbbCpuArchType getCpuArchType() {
        return cpuArchType;
    }

    public void setCpuArchType(CbbCpuArchType cpuArchType) {
        this.cpuArchType = cpuArchType;
    }
}
