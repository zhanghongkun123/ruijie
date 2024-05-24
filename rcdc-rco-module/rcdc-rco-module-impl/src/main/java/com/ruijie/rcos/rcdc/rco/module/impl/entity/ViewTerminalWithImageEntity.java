package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbNetworkModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;

/**
 * Description: 终端关于镜像信息视图
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年05月30日
 *
 * @author lifeng
 */
@Entity
@Table(name = "v_rco_terminal_with_image_info")
public class ViewTerminalWithImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    /**
     * 终端ip
     */
    private String ip;

    /**
     * 终端mac
     */
    private String macAddr;

    /**
     * 终端型号
     */
    private String productType;

    /**
     * 终端id
     */
    private String terminalId;

    /**
     * 终端名称
     */
    private String terminalName;

    /**
     * 终端名称-转大写
     */
    private String terminalNameUpper;

    /**
     * 终端状态
     */
    private String terminalState;

    /**
     * 分组id
     */
    private String terminalGroupId;

    /**
     * 分组名称
     */
    private String terminalGroupName;

    /**
     * 镜像id
     */
    private String imageId;

    /**
     * 镜像下载状态
     */
    private String downloadState;

    /**
     * 镜像下载时间
     */
    private Date downloadUpdateTime;

    /**
     * 是否安装过驱动
     */
    private String installDriveState;


    @Version
    private Integer version;

    /**
     * 开机模式
     */
    private String startMode;

    /**
     * 产品id
     */
    private String productId;

    private Boolean enableProxy;

    @Enumerated(EnumType.STRING)
    private CbbNetworkModeEnums networkAccessMode;

    /**
     * 绑定用户名称
     */
    private String bindUserName;

    /**
     * 绑定桌面id
     */
    private UUID bindDeskId;

    @Enumerated(EnumType.STRING)
    private IdvTerminalModeEnums idvTerminalMode;

    private String deskIp;

    /**
     * CPU架构类型
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

    public Date getDownloadUpdateTime() {
        return downloadUpdateTime;
    }

    public void setDownloadUpdateTime(Date downloadUpdateTime) {
        this.downloadUpdateTime = downloadUpdateTime;
    }

    public String getTerminalState() {
        return terminalState;
    }

    public void setTerminalState(String terminalState) {
        this.terminalState = terminalState;
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

    public String getTerminalGroupName() {
        return terminalGroupName;
    }

    public void setTerminalGroupName(String terminalGroupName) {
        this.terminalGroupName = terminalGroupName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(String terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getStartMode() {
        return startMode;
    }

    public void setStartMode(String startMode) {
        this.startMode = startMode;
    }

    public String getTerminalNameUpper() {
        return terminalNameUpper;
    }

    public void setTerminalNameUpper(String terminalNameUpper) {
        this.terminalNameUpper = terminalNameUpper;
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
