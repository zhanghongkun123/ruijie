package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseCategoryEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoTerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalCloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalNetworkInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.*;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbNetworkCardEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalWakeUpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Description: 分组终端列表详情视图
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月21日
 *
 * @author nt
 */
@Entity
@Table(name = "v_cbb_user_terminal")
public class ViewTerminalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    //possible null if not bind image
    private UUID imageId;

    private String terminalId;

    private String terminalName;

    private UUID terminalGroupId;

    private String terminalGroupName;

    @Enumerated(EnumType.STRING)
    private CbbTerminalStateEnums terminalState;

    private String rainOsVersion;

    private String hardwareVersion;

    private String rainUpgradeVersion;

    private Date lastOfflineTime;

    private Date lastOnlineTime;

    private String macAddr;

    private Long diskSize;

    private String cpuType;

    private String ip;

    private Long memorySize;

    private String productType;

    private String productId;

    private Date createTime;

    private Boolean hasLogin;

    private Boolean hasOnline;

    private String desktopName;

    private UUID desktopId;

    private Boolean authed;

    private String ocsSn;

    @Version
    private int version;

    @Enumerated(EnumType.STRING)
    private CbbTerminalPlatformEnums platform;

    private String terminalOsType;

    @Enumerated(EnumType.STRING)
    private IdvTerminalModeEnums idvTerminalMode;

    private UUID userId;

    private UUID bindUserId;

    private String bindUserName;

    private UUID bindDeskId;

    @Enumerated(EnumType.STRING)
    private CbbNetworkModeEnums networkAccessMode;

    private String subnetMask;

    private String gateway;

    private String mainDns;

    private String secondDns;

    @Enumerated(EnumType.STRING)
    private CbbGetNetworkModeEnums getIpMode;

    @Enumerated(EnumType.STRING)
    private CbbGetNetworkModeEnums getDnsMode;

    private String ssid;

    @Enumerated(EnumType.STRING)
    private CbbTerminalWirelessAuthModeEnums wirelessAuthMode;

    private Boolean enableVisitorLogin;

    private Boolean enableAutoLogin;

    private String imageTemplateName;

    @Enumerated(EnumType.STRING)
    private CbbCloudDeskPattern pattern;

    private String deskIp;

    private Integer systemSize;

    private Integer personSize;

    private Boolean isAllowLocalDisk;

    private String serialNumber;

    private String deskMac;

    private Integer wirelessNetCardNum;

    private Integer ethernetNetCardNum;

    private String allDiskInfo;

    private String sysDiskSn;

    private String networkInfos;

    private Boolean enableProxy;

    private UUID realTerminalId;

    private Boolean lock;

    private Date lockTime;

    private Date unlockTime;

    private Integer pwdErrorTimes;

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

    /**
     * 是否支持远程唤醒
     */
    private Boolean supportRemoteWake;

    /**
     * 是否开启远程唤醒功能
     */
    @Enumerated(EnumType.STRING)
    private CbbTerminalWakeUpStatus wakeUpStatus;

    /**
     * 镜像下载状态
     */
    @Enumerated(EnumType.STRING)
    private DownloadStateEnum downloadState;

    private Integer failCode;

    /**
     * 镜像下载完成时间
     */
    private Date downloadFinishTime;

    /**
     * 网卡工作模式
     */
    @Enumerated(EnumType.STRING)
    private CbbNicWorkModeEnums nicWorkMode;

    /**
     * 是否开启系统盘自动扩容
     */
    private Boolean enableFullSystemDisk;

    @Enumerated(EnumType.STRING)
    private CbbOsType imageTemplateOsType;


    /**
     * 具体授权类类型
     */
    private String authType;

    private String authMode;

    /**
     * 对象转换
     *
     * @return 返回dto
     */
    public TerminalDTO convertFor() {
        TerminalDTO dto = new TerminalDTO();
        TerminalCloudDesktopDTO terminalCloudDesktopDTO = new TerminalCloudDesktopDTO();
        dto.setId(terminalId);
        dto.setIp(ip);
        dto.setMacAddr(macAddr);
        dto.setProductType(productType);
        dto.setProductId(productId);
        dto.setTerminalName(terminalName);
        dto.setTerminalState(terminalState);
        dto.setRainUpgradeVersion(rainUpgradeVersion);
        dto.setTerminalGroupId(terminalGroupId);
        dto.setTerminalGroupName(terminalGroupName);
        dto.setCreateTime(createTime);
        dto.setLastOnlineTime(lastOnlineTime);
        dto.setDesktopName(desktopName);
        dto.setTerminalOsType(terminalOsType);
        dto.setIdvTerminalMode(idvTerminalMode);
        dto.setUserId(userId);
        dto.setBindUserId(bindUserId);
        dto.setBindUserName(bindUserName);
        dto.setBindDeskId(bindDeskId);
        dto.setEnableVisitorLogin(enableVisitorLogin);
        dto.setEnableAutoLogin(enableAutoLogin);
        dto.setAuthed(authed);
        dto.setPlatform(platform);
        dto.setSysDiskSn(sysDiskSn);
        terminalCloudDesktopDTO.setImageTemplateName(imageTemplateName);
        terminalCloudDesktopDTO.setPattern(pattern);
        terminalCloudDesktopDTO.setDeskIp(deskIp);
        terminalCloudDesktopDTO.setSystemSize(systemSize);
        terminalCloudDesktopDTO.setPersonSize(personSize);
        terminalCloudDesktopDTO.setAllowLocalDisk(isAllowLocalDisk);
        terminalCloudDesktopDTO.setDesktopId(desktopId);
        dto.setTerminalCloudDesktop(terminalCloudDesktopDTO);
        dto.setLock(lock);
        dto.setRealTerminalId(realTerminalId);
        setNetworkInfo(dto);
        setNetworkModel(dto);
        dto.setOpenOtpCertification(openOtpCertification);
        dto.setHasBindOtp(hasBindOtp);
        dto.setBootType(bootType);
        dto.setSupportTcStart(supportTcStart);
        dto.setEnableProxy(enableProxy);
        dto.setSupportRemoteWake(supportRemoteWake);
        dto.setDownloadState(downloadState);
        dto.setFailCode(failCode);
        dto.setDownloadFinishTime(downloadFinishTime);
        dto.setNicWorkMode(nicWorkMode);
        dto.setEnableFullSystemDisk(enableFullSystemDisk);
        dto.setImageTemplateOsType(imageTemplateOsType);
        dto.setAuthType(authType);
        dto.setAuthMode(authMode);
        dto.setWakeUpStatus(wakeUpStatus);
        return dto;
    }


    /**
     * 将对象转换为终端信息对象
     *
     * @param terminalDTO 终端信息对象
     */
    public void convertTo(TerminalDTO terminalDTO) {
        Assert.notNull(terminalDTO, "terminal dto can not be null");
        TerminalCloudDesktopDTO terminalCloudDesktopDTO = new TerminalCloudDesktopDTO();
        terminalDTO.setId(terminalId);
        terminalDTO.setTerminalName(terminalName);
        terminalDTO.setTerminalState(terminalState);
        terminalDTO.setTerminalGroupId(terminalGroupId);
        terminalDTO.setTerminalGroupName(terminalGroupName);
        terminalDTO.setRainOsVersion(rainOsVersion);
        terminalDTO.setHardwareVersion(hardwareVersion);
        terminalDTO.setRainUpgradeVersion(rainUpgradeVersion);
        terminalDTO.setLastOfflineTime(lastOfflineTime);
        terminalDTO.setLastOnlineTime(lastOnlineTime);
        terminalDTO.setMacAddr(macAddr);
        terminalDTO.setDiskSize(diskSize);
        terminalDTO.setCpuMode(cpuType);
        terminalDTO.setIp(ip);
        terminalDTO.setMemorySize(memorySize);
        terminalDTO.setProductType(productType);
        terminalDTO.setProductId(productId);
        terminalDTO.setTerminalOsType(terminalOsType);
        terminalDTO.setNetworkAccessMode(networkAccessMode);
        terminalDTO.setSsid(ssid);
        terminalDTO.setWirelessAuthMode(wirelessAuthMode);
        terminalDTO.setIdvTerminalMode(idvTerminalMode);
        terminalDTO.setEnableVisitorLogin(enableVisitorLogin);
        terminalDTO.setEnableAutoLogin(enableAutoLogin);
        terminalDTO.setPlatform(platform);
        terminalDTO.setSerialNumber(serialNumber);
        terminalDTO.setGetIpMode(getIpMode);
        terminalDTO.setUserId(userId);
        terminalDTO.setBindUserName(bindUserName);
        terminalDTO.setBindUserId(bindUserId);
        terminalDTO.setBindDeskId(bindDeskId);
        terminalDTO.setWirelessNetCardNum(wirelessNetCardNum);
        terminalDTO.setEthernetNetCardNum(ethernetNetCardNum);
        terminalDTO.setAllDiskInfo(allDiskInfo);
        terminalDTO.setLock(lock);
        terminalDTO.setRealTerminalId(realTerminalId);
        terminalCloudDesktopDTO.setAllowLocalDisk(isAllowLocalDisk);
        terminalCloudDesktopDTO.setImageTemplateName(imageTemplateName);
        terminalCloudDesktopDTO.setPattern(pattern);
        terminalCloudDesktopDTO.setDeskIp(deskIp);
        terminalCloudDesktopDTO.setSystemSize(systemSize);
        terminalCloudDesktopDTO.setDeskMac(deskMac);
        terminalCloudDesktopDTO.setDesktopId(desktopId);
        terminalDTO.setTerminalCloudDesktop(terminalCloudDesktopDTO);
        setNetworkInfo(terminalDTO);
        setNetworkModel(terminalDTO);
        terminalDTO.setOpenOtpCertification(openOtpCertification);
        terminalDTO.setHasBindOtp(hasBindOtp);
        terminalDTO.setBootType(bootType);
        terminalDTO.setSupportTcStart(supportTcStart);
        terminalDTO.setDownloadState(downloadState);
        terminalDTO.setFailCode(failCode);
        terminalDTO.setDownloadFinishTime(downloadFinishTime);
        terminalDTO.setEnableProxy(enableProxy);
        terminalDTO.setSupportRemoteWake(supportRemoteWake);
        terminalDTO.setNicWorkMode(nicWorkMode);
        terminalDTO.setEnableFullSystemDisk(enableFullSystemDisk);
        terminalDTO.setImageTemplateOsType(imageTemplateOsType);
        terminalDTO.setAuthMode(authMode);
        terminalDTO.setWakeUpStatus(wakeUpStatus);
    }

    private void setNetworkModel(TerminalDTO dto) {
        if (StringUtils.isNotEmpty(dto.getIp())) {
            dto.setNetworkAccessMode(CbbNetworkModeEnums.WIRED);
        }
        if (StringUtils.isEmpty(dto.getIp()) && StringUtils.isNotEmpty(dto.getWirelessIp())) {
            dto.setNetworkAccessMode(CbbNetworkModeEnums.WIRELESS);
        }
    }

    /**
     *
     * 设置无线信息
     *
     * @param dto terminalDTO
     */
    private void setNetworkInfo(TerminalDTO dto) {
        if (Objects.isNull(this.networkInfos)) {
            return;
        }
        List<CbbTerminalNetworkInfoDTO> networkInfoDTOList = JSON.parseArray(this.networkInfos, CbbTerminalNetworkInfoDTO.class);
        // 默认不是有线连接
        AtomicReference<Boolean> hasWired = new AtomicReference<>(false);
        // 是否是新版连接
        AtomicReference<Boolean> hasNewVersion = new AtomicReference<>(false);
        // 在SHINE 未升级的时候 使用旧的数据处理
        networkInfoDTOList.forEach(item -> {
            if (CbbNetworkModeEnums.WIRELESS == item.getNetworkAccessMode()) {
                dto.setWirelessIp(item.getIp());
                dto.setWirelessMacAddr(item.getMacAddr());
                dto.setGetWirelessIpMode(item.getGetIpMode());
            }
            if (CbbNetworkModeEnums.WIRED == item.getNetworkAccessMode() && CbbNetworkCardEnums.MAIN_NETCARD == item.getBusinessCard()) {
                hasWired.set(true);
                dto.setIp(item.getIp());
                dto.setMacAddr(item.getMacAddr());
                dto.setGetIpMode(item.getGetIpMode());
            }
        });
    }

    /**
     * 将视图对象转换成组件终端dto
     *
     * @param rcoTerminalDTO 组件终端dto
     */
    public void convertTo(RcoTerminalDTO rcoTerminalDTO) {
        Assert.notNull(rcoTerminalDTO, "rcoTerminalDTO dto can not be null");

        rcoTerminalDTO.setId(id);
        rcoTerminalDTO.setTerminalId(terminalId);
        rcoTerminalDTO.setTerminalName(terminalName);
    }



    public String getOcsSn() {
        return ocsSn;
    }

    public void setOcsSn(String ocsSn) {
        this.ocsSn = ocsSn;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getCpuType() {
        return cpuType;
    }

    public void setCpuType(String cpuType) {
        this.cpuType = cpuType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(Long diskSize) {
        this.diskSize = diskSize;
    }

    public Long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(Long memorySize) {
        this.memorySize = memorySize;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Boolean getHasLogin() {
        return hasLogin;
    }

    public void setHasLogin(Boolean hasLogin) {
        this.hasLogin = hasLogin;
    }

    public Boolean getHasOnline() {
        return hasOnline;
    }

    public void setHasOnline(Boolean hasOnline) {
        this.hasOnline = hasOnline;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public CbbTerminalPlatformEnums getPlatform() {
        return platform;
    }

    public void setPlatform(CbbTerminalPlatformEnums platform) {
        this.platform = platform;
    }

    public String getTerminalOsType() {
        return terminalOsType;
    }

    public void setTerminalOsType(String terminalOsType) {
        this.terminalOsType = terminalOsType;
    }

    public IdvTerminalModeEnums getIdvTerminalMode() {
        return idvTerminalMode;
    }

    public void setIdvTerminalMode(IdvTerminalModeEnums idvTerminalMode) {
        this.idvTerminalMode = idvTerminalMode;
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

    public String getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getMainDns() {
        return mainDns;
    }

    public void setMainDns(String mainDns) {
        this.mainDns = mainDns;
    }

    public String getSecondDns() {
        return secondDns;
    }

    public void setSecondDns(String secondDns) {
        this.secondDns = secondDns;
    }

    public CbbGetNetworkModeEnums getGetIpMode() {
        return getIpMode;
    }

    public void setGetIpMode(CbbGetNetworkModeEnums getIpMode) {
        this.getIpMode = getIpMode;
    }

    public CbbGetNetworkModeEnums getGetDnsMode() {
        return getDnsMode;
    }

    public void setGetDnsMode(CbbGetNetworkModeEnums getDnsMode) {
        this.getDnsMode = getDnsMode;
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

    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
    }

    public CbbCloudDeskPattern getPattern() {
        return pattern;
    }

    public void setPattern(CbbCloudDeskPattern pattern) {
        this.pattern = pattern;
    }

    public String getDeskIp() {
        return deskIp;
    }

    public void setDeskIp(String deskIp) {
        this.deskIp = deskIp;
    }

    public Integer getSystemSize() {
        return systemSize;
    }

    public void setSystemSize(Integer systemSize) {
        this.systemSize = systemSize;
    }

    public Integer getPersonSize() {
        return personSize;
    }

    public void setPersonSize(Integer personSize) {
        this.personSize = personSize;
    }

    public Boolean getAllowLocalDisk() {
        return isAllowLocalDisk;
    }

    public void setAllowLocalDisk(Boolean allowLocalDisk) {
        isAllowLocalDisk = allowLocalDisk;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDeskMac() {
        return deskMac;
    }

    public void setDeskMac(String deskMac) {
        this.deskMac = deskMac;
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


    public String getNetworkInfos() {
        return networkInfos;
    }

    public void setNetworkInfos(String networkInfos) {
        this.networkInfos = networkInfos;
    }

    public Boolean getAuthed() {
        return authed;
    }

    public void setAuthed(Boolean authed) {
        this.authed = authed;
    }

    public boolean getEnableProxy() {
        return enableProxy;
    }

    public void setEnableProxy(boolean enableProxy) {
        this.enableProxy = enableProxy;
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

    public void setLock(Boolean isLock) {
        this.lock = isLock;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public Date getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(Date unlockTime) {
        this.unlockTime = unlockTime;
    }

    public Integer getPwdErrorTimes() {
        return pwdErrorTimes;
    }

    public void setPwdErrorTimes(Integer pwdErrorTimes) {
        this.pwdErrorTimes = pwdErrorTimes;
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

    public Date getDownloadFinishTime() {
        return downloadFinishTime;
    }

    public void setDownloadFinishTime(Date downloadTime) {
        this.downloadFinishTime = downloadTime;
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

    public @Nullable UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    public CbbTerminalWakeUpStatus getWakeUpStatus() {
        return wakeUpStatus;
    }

    public void setWakeUpStatus(CbbTerminalWakeUpStatus wakeUpStatus) {
        this.wakeUpStatus = wakeUpStatus;
    }

}
