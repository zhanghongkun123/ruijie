package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.*;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalWakeUpStatus;

/**
 * 
 * Description: 终端信息DTO
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月18日
 * 
 * @author nt
 */
public class TerminalDTO {

    /**
     * 终端id
     */
    private String id;

    /**
     * 终端名称
     */
    private String terminalName;

    /**
     * 分组id
     */
    private UUID terminalGroupId;

    /**
     * 分组名称
     */
    private String terminalGroupName;

    private String[] terminalGroupNameArr;

    /**
     * 终端状态
     */
    private CbbTerminalStateEnums terminalState;

    /**
     * 终端系统版本号
     */
    private String rainOsVersion;

    /**
     * 硬件版本号
     */
    private String hardwareVersion;

    /**
     * 软件版本号，指组件升级包的版本号
     */
    private String rainUpgradeVersion;

    /**
     * 终端系统类型
     */
    private String terminalOsType;

    /**
     * 终端最近一次离线的时间点
     */
    private Date lastOfflineTime;

    /**
     * 终端最近接入的时间点
     */
    private Date lastOnlineTime;

    /**
     * 终端最近接入的时间点
     */
    private String macAddr;

    /**
     * 磁盘大小，单位kb
     */
    private Long diskSize;

    /**
     * 磁盘sn
     */
    private String sysDiskSn;

    /**
     * cup型号
     */
    private String cpuMode;

    /**
     * 终端ip地址
     */
    private String ip;


    /**
     * 内存大小，单位kb
     */
    private Long memorySize;

    /**
     * ip是否冲突
     */
    private Integer ipConflict;

    /**
     * 外网访问是否正常
     */
    private Integer accessInternet;

    /**
     * 丢包率
     */
    private Double packetLossRate;

    /**
     * 网络时延
     */
    private Double delay;

    /**
     * 带宽大小
     */
    private Double bandwidth;

    /**
     * 检测时间
     */
    private Date detectTime;

    private String productType;

    private String productId;

    /**
     * 带宽阈值
     */
    private Double bandwidthThreshold;

    /**
     * 丢包率阈值
     */
    private Double packetLossRateThreshold;

    /**
     * 时延阈值
     */
    private Integer delayThreshold;

    /**
     * 终端检测状态
     */
    private String detectState;

    /**
     * 终端在用用户ID
     */
    private UUID userId;

    /**
     * 绑定用户id
     */
    private UUID bindUserId;

    /**
     * 绑定用户名称
     */
    private String bindUserName;

    /**
     * 绑定桌面id
     */
    private UUID bindDeskId;

    /**
     * 网络接入方式
     */
    @Enumerated(EnumType.STRING)
    private CbbNetworkModeEnums networkAccessMode;

    private String wirelessIp;

    private String wirelessMacAddr;

    private String ssid;

    @JSONField(serializeUsing = CbbTerminalWirelessAuthModeEnumsSerializer.class, deserializeUsing = CbbTerminalWirelessAuthModeEnumsSerializer.class)
    private CbbTerminalWirelessAuthModeEnums wirelessAuthMode;

    private IdvTerminalModeEnums idvTerminalMode;

    private Boolean enableVisitorLogin;

    private Boolean enableAutoLogin;

    private Long onlineTime;

    private String desktopName;

    private Date createTime;

    @Enumerated(EnumType.STRING)
    private CbbTerminalPlatformEnums platform;

    /**
     * 终端ip获取方式
     */
    private CbbGetNetworkModeEnums getIpMode;

    /**
     * 终端ip获取方式
     */
    private CbbGetNetworkModeEnums getWirelessIpMode;

    /**
     * SN序列号
     */
    private String serialNumber;

    /**
     * 无线网卡数量
     */
    private Integer wirelessNetCardNum;

    /**
     * 以太网卡数量
     */
    private Integer ethernetNetCardNum;

    /**
     * 所有磁盘信息
     */
    private String allDiskInfo;

    /**
     * 终端授权信息
     */
    private Boolean authed;

    /**
     * 终端授权状态码
     */
    private Integer authorization;

    /**
     * 真实的终端ID，t_cbb_terminal中的ID字段
     */
    private UUID realTerminalId;

    /**
     * 终端管理密码是否锁定
     */
    private Boolean lock;

    /**
     * 用户终端动态口令开启开关
     */
    private Boolean openOtpCertification;

    /**
     * 用户动态口令绑定开关
     */
    private Boolean hasBindOtp;

    /**
     * TCI开机模式
     */
    private String bootType;

    /**
     * 是否支持TC引导
     */
    private Boolean supportTcStart;

    private Boolean enableProxy;

    /**
     * 是否支持远程唤醒
     */
    private Boolean supportRemoteWake;

    /**
     * 是否开启远程唤醒功能
     */
    private CbbTerminalWakeUpStatus wakeUpStatus;

    /**
     * 下载状态
     */
    private DownloadStateEnum downloadState;

    /**
     * 下载结果提示语
     */
    private String downloadPromptMessage;

    /**
     * 错误码
     */
    private Integer failCode;

    /**
     * 下载完成时间
     */
    private Date downloadFinishTime;

    /**
     * 网卡工作模式
     */
    private CbbNicWorkModeEnums nicWorkMode;

    /**
     * 是否开启系统盘自动扩容
     */
    private Boolean enableFullSystemDisk;

    /**
     * 云桌面使用的镜像模板系统类型
     */
    private CbbOsType imageTemplateOsType;

    /**
     * 具体的授权类型
     */
    private String authType;

    /**
     * 支持的授权模式
     */
    private String authMode;


    List<CbbImageDiskInfoDTO> cbbImageDiskInfoDTOList;

    @Nullable
    private String userName;

    /**
     * 客户端版本号
     */
    @Nullable
    private String clientVersion;

    /**
     * @return 是否部署为IDV模式
     */
    public boolean isIDVModel() {
        return platform == CbbTerminalPlatformEnums.IDV;
    }

    /**
     * @return 是否部署为TCI模式
     */
    public boolean isVOIModel() {
        return platform == CbbTerminalPlatformEnums.VOI;
    }

    /**
     * @return 终端是否在线状态
     */
    public boolean isTerminalOnline() {
        return terminalState == CbbTerminalStateEnums.ONLINE;
    }

    /**
     * 是否支持终端初始化，仅瘦终端使用
     */
    private Boolean canTerminalInit;

    public Integer getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Integer authorization) {
        this.authorization = authorization;
    }

    private TerminalCloudDesktopDTO terminalCloudDesktop;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }

    public String getTerminalGroupName() {
        return terminalGroupName;
    }

    public void setTerminalGroupName(String terminalGroupName) {
        this.terminalGroupName = terminalGroupName;
    }

    public CbbTerminalStateEnums getTerminalState() {
        return terminalState;
    }

    public void setTerminalState(CbbTerminalStateEnums terminalState) {
        this.terminalState = terminalState;
    }

    public String getRainOsVersion() {
        return rainOsVersion;
    }

    public void setRainOsVersion(String rainOsVersion) {
        this.rainOsVersion = rainOsVersion;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public String getRainUpgradeVersion() {
        return rainUpgradeVersion;
    }

    public void setRainUpgradeVersion(String rainUpgradeVersion) {
        this.rainUpgradeVersion = rainUpgradeVersion;
    }

    public Date getLastOfflineTime() {
        return lastOfflineTime;
    }

    public void setLastOfflineTime(Date lastOfflineTime) {
        this.lastOfflineTime = lastOfflineTime;
    }

    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public Long getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(Long diskSize) {
        this.diskSize = diskSize;
    }

    public String getSysDiskSn() {
        return sysDiskSn;
    }

    public void setSysDiskSn(String sysDiskSn) {
        this.sysDiskSn = sysDiskSn;
    }

    public Long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(Long memorySize) {
        this.memorySize = memorySize;
    }

    public String getCpuMode() {
        return cpuMode;
    }

    public void setCpuMode(String cpuMode) {
        this.cpuMode = cpuMode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public Integer getIpConflict() {
        return ipConflict;
    }

    public void setIpConflict(Integer ipConflict) {
        this.ipConflict = ipConflict;
    }

    public Integer getAccessInternet() {
        return accessInternet;
    }

    public void setAccessInternet(Integer accessInternet) {
        this.accessInternet = accessInternet;
    }

    public Double getPacketLossRate() {
        return packetLossRate;
    }

    public void setPacketLossRate(Double packetLossRate) {
        this.packetLossRate = packetLossRate;
    }

    public Double getDelay() {
        return delay;
    }

    public void setDelay(Double delay) {
        this.delay = delay;
    }

    public Double getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Double bandwidth) {
        this.bandwidth = bandwidth;
    }

    public Date getDetectTime() {
        return detectTime;
    }

    public void setDetectTime(Date detectTime) {
        this.detectTime = detectTime;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Double getBandwidthThreshold() {
        return bandwidthThreshold;
    }

    public void setBandwidthThreshold(Double bandwidthThreshold) {
        this.bandwidthThreshold = bandwidthThreshold;
    }

    public Double getPacketLossRateThreshold() {
        return packetLossRateThreshold;
    }

    public void setPacketLossRateThreshold(Double packetLossRateThreshold) {
        this.packetLossRateThreshold = packetLossRateThreshold;
    }

    public Integer getDelayThreshold() {
        return delayThreshold;
    }

    public void setDelayThreshold(Integer delayThreshold) {
        this.delayThreshold = delayThreshold;
    }

    public String getDetectState() {
        return detectState;
    }

    public void setDetectState(String detectState) {
        this.detectState = detectState;
    }

    public String[] getTerminalGroupNameArr() {
        return terminalGroupNameArr;
    }

    public void setTerminalGroupNameArr(String[] terminalGroupNameArr) {
        this.terminalGroupNameArr = terminalGroupNameArr;
    }

    public String getTerminalOsType() {
        return terminalOsType;
    }

    public void setTerminalOsType(String terminalOsType) {
        this.terminalOsType = terminalOsType;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getBindUserId() {
        return bindUserId;
    }

    public void setBindUserId(UUID bindUserId) {
        this.bindUserId = bindUserId;
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

    public CbbNetworkModeEnums getNetworkAccessMode() {
        return networkAccessMode;
    }

    public void setNetworkAccessMode(CbbNetworkModeEnums networkAccessMode) {
        this.networkAccessMode = networkAccessMode;
    }

    public String getWirelessIp() {
        return wirelessIp;
    }

    public void setWirelessIp(String wirelessIp) {
        this.wirelessIp = wirelessIp;
    }

    public String getWirelessMacAddr() {
        return wirelessMacAddr;
    }

    public void setWirelessMacAddr(String wirelessMacAddr) {
        this.wirelessMacAddr = wirelessMacAddr;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public CbbTerminalWirelessAuthModeEnums getWirelessAuthMode() {
        return wirelessAuthMode;
    }

    public void setWirelessAuthMode(CbbTerminalWirelessAuthModeEnums wirelessAuthMode) {
        this.wirelessAuthMode = wirelessAuthMode;
    }

    public IdvTerminalModeEnums getIdvTerminalMode() {
        return idvTerminalMode;
    }

    public void setIdvTerminalMode(IdvTerminalModeEnums idvTerminalMode) {
        this.idvTerminalMode = idvTerminalMode;
    }

    public Boolean getEnableVisitorLogin() {
        return enableVisitorLogin;
    }

    public void setEnableVisitorLogin(Boolean enableVisitorLogin) {
        this.enableVisitorLogin = enableVisitorLogin;
    }

    public Boolean getEnableAutoLogin() {
        return enableAutoLogin;
    }

    public void setEnableAutoLogin(Boolean enableAutoLogin) {
        this.enableAutoLogin = enableAutoLogin;
    }

    public Long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Long onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public CbbTerminalPlatformEnums getPlatform() {
        return platform;
    }

    public void setPlatform(CbbTerminalPlatformEnums platform) {
        this.platform = platform;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public TerminalCloudDesktopDTO getTerminalCloudDesktop() {
        return terminalCloudDesktop;
    }

    public void setTerminalCloudDesktop(TerminalCloudDesktopDTO terminalCloudDesktop) {
        this.terminalCloudDesktop = terminalCloudDesktop;
    }

    public CbbGetNetworkModeEnums getGetIpMode() {
        return getIpMode;
    }

    public void setGetIpMode(CbbGetNetworkModeEnums getIpMode) {
        this.getIpMode = getIpMode;
    }

    public Integer getWirelessNetCardNum() {
        return wirelessNetCardNum;
    }

    public void setWirelessNetCardNum(Integer wirelessNetCardNum) {
        this.wirelessNetCardNum = wirelessNetCardNum;
    }

    public Integer getEthernetNetCardNum() {
        return ethernetNetCardNum;
    }

    public void setEthernetNetCardNum(Integer ethernetNetCardNum) {
        this.ethernetNetCardNum = ethernetNetCardNum;
    }

    public String getAllDiskInfo() {
        return allDiskInfo;
    }

    public void setAllDiskInfo(String allDiskInfo) {
        this.allDiskInfo = allDiskInfo;
    }

    public CbbGetNetworkModeEnums getGetWirelessIpMode() {
        return getWirelessIpMode;
    }

    public void setGetWirelessIpMode(CbbGetNetworkModeEnums getWirelessIpMode) {
        this.getWirelessIpMode = getWirelessIpMode;
    }

    public Boolean getAuthed() {
        return authed;
    }

    public void setAuthed(Boolean authed) {
        this.authed = authed;
    }

    public UUID getRealTerminalId() {
        return realTerminalId;
    }

    public void setRealTerminalId(UUID realTerminalId) {
        this.realTerminalId = realTerminalId;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public Boolean getOpenOtpCertification() {
        return openOtpCertification;
    }

    public void setOpenOtpCertification(Boolean openOtpCertification) {
        this.openOtpCertification = openOtpCertification;
    }

    public Boolean getHasBindOtp() {
        return hasBindOtp;
    }

    public void setHasBindOtp(Boolean hasBindOtp) {
        this.hasBindOtp = hasBindOtp;
    }

    public String getBootType() {
        return bootType;
    }

    public void setBootType(String bootType) {
        this.bootType = bootType;
    }

    public Boolean getSupportTcStart() {
        return supportTcStart;
    }

    public void setSupportTcStart(Boolean supportTcStart) {
        this.supportTcStart = supportTcStart;
    }

    public Boolean getEnableProxy() {
        return enableProxy;
    }

    public void setEnableProxy(Boolean enableProxy) {
        this.enableProxy = enableProxy;
    }

    public Boolean getSupportRemoteWake() {
        return supportRemoteWake;
    }

    public void setSupportRemoteWake(Boolean supportRemoteWake) {
        this.supportRemoteWake = supportRemoteWake;
    }

    public DownloadStateEnum getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(DownloadStateEnum downloadState) {
        this.downloadState = downloadState;
    }

    public String getDownloadPromptMessage() {
        return downloadPromptMessage;
    }

    public void setDownloadPromptMessage(String downloadPromptMessage) {
        this.downloadPromptMessage = downloadPromptMessage;
    }

    public Date getDownloadFinishTime() {
        return downloadFinishTime;
    }

    public void setDownloadFinishTime(Date downloadFinishTime) {
        this.downloadFinishTime = downloadFinishTime;
    }

    public Integer getFailCode() {
        return failCode;
    }

    public void setFailCode(Integer failCode) {
        this.failCode = failCode;
    }

    public CbbNicWorkModeEnums getNicWorkMode() {
        return nicWorkMode;
    }

    public void setNicWorkMode(CbbNicWorkModeEnums nicWorkMode) {
        this.nicWorkMode = nicWorkMode;
    }

    public Boolean getEnableFullSystemDisk() {
        return enableFullSystemDisk;
    }

    public void setEnableFullSystemDisk(Boolean enableFullSystemDisk) {
        this.enableFullSystemDisk = enableFullSystemDisk;
    }

    public CbbOsType getImageTemplateOsType() {
        return imageTemplateOsType;
    }

    public void setImageTemplateOsType(CbbOsType imageTemplateOsType) {
        this.imageTemplateOsType = imageTemplateOsType;
    }

    public String getUpperMacAddr() {
        return StringUtils.isEmpty(this.macAddr) ? this.macAddr : this.macAddr.toUpperCase();
    }

    public String getUpperMacAddrOrTerminalId() {
        return StringUtils.isEmpty(this.getUpperMacAddr()) ? this.id : this.getUpperMacAddr();
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getAuthMode() {
        return authMode;
    }

    public void setAuthMode(String authMode) {
        this.authMode = authMode;
    }

    public List<CbbImageDiskInfoDTO> getCbbImageDiskInfoDTOList() {
        return cbbImageDiskInfoDTOList;
    }

    public void setCbbImageDiskInfoDTOList(List<CbbImageDiskInfoDTO> cbbImageDiskInfoDTOList) {
        this.cbbImageDiskInfoDTOList = cbbImageDiskInfoDTOList;
    }

    public CbbTerminalWakeUpStatus getWakeUpStatus() {
        return wakeUpStatus;
    }

    public void setWakeUpStatus(CbbTerminalWakeUpStatus wakeUpStatus) {
        this.wakeUpStatus = wakeUpStatus;
    }


    public Boolean getCanTerminalInit() {
        return canTerminalInit;
    }

    public void setCanTerminalInit(Boolean canTerminalInit) {
        this.canTerminalInit = canTerminalInit;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Nullable
    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(@Nullable String clientVersion) {
        this.clientVersion = clientVersion;
    }
}
