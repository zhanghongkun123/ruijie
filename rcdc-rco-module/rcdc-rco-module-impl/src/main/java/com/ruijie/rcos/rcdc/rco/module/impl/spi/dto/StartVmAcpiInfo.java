package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;
import org.springframework.lang.Nullable;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/22
 *
 * @author Jarman
 */
public class StartVmAcpiInfo {

    private String product = "RCO";

    private String run = "USER";

    private String platform = "RCDC";

    private String username;

    private String password;

    private String usertype;

    @JSONField(name = "sn")
    private String terminalSerialNumber;

    @JSONField(name = "term_name")
    private String terminalName;

    @JSONField(name = "term_ip")
    private String terminalIp;

    @JSONField(name = "mac")
    private String terminalMac;

    @JSONField(name = "idvmode")
    private String terminalMode;

    @JSONField(name = "term_password")
    private String termPassword;

    @JSONField(name = "personal_config_dir")
    private String personalConfigDir;

    private String printerManager;

    @JSONField(name = "image_id")
    private String imageTemplateId;

    @JSONField(name = "auto_logon")
    private String autoLogon;

    /**
     * 自动登录密码
     */
    @JSONField(name = "sys_password")
    private String winPassword;

    @JSONField(name = "ad_auto_logon")
    private String adAutoLogon;

    private IacUserTypeEnum domainUserType;

    /**
     * 显卡参数  参考镜像
     */
    private String gpu;

    @JSONField(name = "image_ad")
    private String imageAd;

    @JSONField(name = "upm_policy_enable")
    private Integer upmPolicyEnable = 0;

    private DesktopPoolType desktopPoolType;

    @JSONField(name = "uam_vm_mode")
    private String uamVmMode;

    @JSONField(name = "app_disk_id_arr")
    private String appDiskIdArr;

    @Nullable
    @JSONField(name = "appdisk_restore")
    private UUID appDiskRestore;

    @Nullable
    @JSONField(name = "sys_username")
    private String winUsername;

    @Nullable
    @JSONField(name = "set_winname")
    private String setWinName;

    @Nullable
    @JSONField(name = "sys_usergroup")
    private String winUserGroup;

    @JSONField(name = "IsRCA")
    private String isRca;

    @JSONField(name = "vsocket_cid")
    private String vsocketDeviceID;

    /**
     * vsock监听端口
     * @see "VSOCKET_CONTROL_LISTEN_PORT"
     */
    @JSONField(name = "vsocket_control_listen_port")
    private String vsocketListenPort;

    private String hostBusinessType;

    private String sessionType;

    /**
     * 云应用策略，VDI应用主机，还原模式下，个性数据保存
     */
    @JSONField(name = Constants.ACPI_PERSONAL_DATA_RETENTION)
    private Boolean rcaHostPersonalDataRetention;

    private CbbEstProtocolType connectType;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getTerminalSerialNumber() {
        return terminalSerialNumber;
    }

    public void setTerminalSerialNumber(String terminalSerialNumber) {
        this.terminalSerialNumber = terminalSerialNumber;
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

    public String getTerminalMac() {
        return terminalMac;
    }

    public void setTerminalMac(String terminalMac) {
        this.terminalMac = terminalMac;
    }

    public String getTerminalMode() {
        return terminalMode;
    }

    public void setTerminalMode(String terminalMode) {
        this.terminalMode = terminalMode;
    }

    public String getTermPassword() {
        return termPassword;
    }

    public void setTermPassword(String termPassword) {
        this.termPassword = termPassword;
    }

    public String getPersonalConfigDir() {
        return personalConfigDir;
    }

    public void setPersonalConfigDir(String personalConfigDir) {
        this.personalConfigDir = personalConfigDir;
    }

    public String getPrinterManager() {
        return printerManager;
    }

    public void setPrinterManager(String printerManager) {
        this.printerManager = printerManager;
    }

    public String getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(String imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public String getAutoLogon() {
        return autoLogon;
    }

    public void setAutoLogon(String autoLogon) {
        this.autoLogon = autoLogon;
    }

    public String getWinPassword() {
        return winPassword;
    }

    public void setWinPassword(String winPassword) {
        this.winPassword = winPassword;
    }

    public String getAdAutoLogon() {
        return adAutoLogon;
    }

    public void setAdAutoLogon(String adAutoLogon) {
        this.adAutoLogon = adAutoLogon;
    }

    public IacUserTypeEnum getDomainUserType() {
        return domainUserType;
    }

    public void setDomainUserType(IacUserTypeEnum domainUserType) {
        this.domainUserType = domainUserType;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public String getImageAd() {
        return imageAd;
    }

    public void setImageAd(String imageAd) {
        this.imageAd = imageAd;
    }

    public Integer getUpmPolicyEnable() {
        return upmPolicyEnable;
    }

    public void setUpmPolicyEnable(Integer upmPolicyEnable) {
        this.upmPolicyEnable = upmPolicyEnable;
    }

    public DesktopPoolType getDesktopPoolType() {
        return desktopPoolType;
    }

    public void setDesktopPoolType(DesktopPoolType desktopPoolType) {
        this.desktopPoolType = desktopPoolType;
    }

    public String getUamVmMode() {
        return uamVmMode;
    }

    public void setUamVmMode(String uamVmMode) {
        this.uamVmMode = uamVmMode;
    }

    public String getAppDiskIdArr() {
        return appDiskIdArr;
    }

    public void setAppDiskIdArr(String appDiskIdArr) {
        this.appDiskIdArr = appDiskIdArr;
    }

    public UUID getAppDiskRestore() {
        return appDiskRestore;
    }

    public void setAppDiskRestore(UUID appDiskRestore) {
        this.appDiskRestore = appDiskRestore;
    }

    @Nullable
    public String getWinUsername() {
        return winUsername;
    }

    public void setWinUsername(@Nullable String winUsername) {
        this.winUsername = winUsername;
    }

    @Nullable
    public String getSetWinName() {
        return setWinName;
    }

    public void setSetWinName(@Nullable String setWinName) {
        this.setWinName = setWinName;
    }

    @Nullable
    public String getWinUserGroup() {
        return winUserGroup;
    }

    public void setWinUserGroup(@Nullable String winUserGroup) {
        this.winUserGroup = winUserGroup;
    }

    public String getVsocketDeviceID() {
        return vsocketDeviceID;
    }

    public void setVsocketDeviceID(String vsocketDeviceID) {
        this.vsocketDeviceID = vsocketDeviceID;
    }

    public String getVsocketListenPort() {
        return vsocketListenPort;
    }

    public void setVsocketListenPort(String vsocketListenPort) {
        this.vsocketListenPort = vsocketListenPort;
    }

    public String getIsRca() {
        return isRca;
    }

    public void setIsRca(String isRca) {
        this.isRca = isRca;
    }

    public String getHostBusinessType() {
        return hostBusinessType;
    }

    public void setHostBusinessType(String hostBusinessType) {
        this.hostBusinessType = hostBusinessType;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public CbbEstProtocolType getConnectType() {
        return connectType;
    }

    public void setConnectType(CbbEstProtocolType connectType) {
        this.connectType = connectType;
    }

    public Boolean getRcaHostPersonalDataRetention() {
        return rcaHostPersonalDataRetention;
    }

    public void setRcaHostPersonalDataRetention(Boolean rcaHostPersonalDataRetention) {
        this.rcaHostPersonalDataRetention = rcaHostPersonalDataRetention;
    }
}

