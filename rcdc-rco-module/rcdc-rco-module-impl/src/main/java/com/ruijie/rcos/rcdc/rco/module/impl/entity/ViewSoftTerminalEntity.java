package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoTerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalCloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalNetworkInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.*;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbNetworkCardEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;

/**
 * Description: 分组终端列表详情视图
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月21日
 *
 * @author nt
 */
@Entity
@Table(name = "v_cbb_user_soft_terminal")
public class ViewSoftTerminalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

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

    private String ip;

    private Long memorySize;

    private String productType;

    private String productId;

    private Date createTime;

    private Boolean hasLogin;

    private Boolean hasOnline;



    private Boolean authed;

    private String ocsSn;

    @Version
    private int version;

    @Enumerated(EnumType.STRING)
    private CbbTerminalPlatformEnums platform;

    private String terminalOsType;

    @Enumerated(EnumType.STRING)
    private IdvTerminalModeEnums idvTerminalMode;

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



    private String serialNumber;


    private Integer wirelessNetCardNum;

    private Integer ethernetNetCardNum;

    private String allDiskInfo;


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
     * 网卡工作模式
     */
    @Enumerated(EnumType.STRING)
    private CbbNicWorkModeEnums nicWorkMode;


    private UUID userId;

    /**
     * 具体授权类类型
     */
    private String authType;

    private String authMode;

    /**
     * 用户名
     */
    private String userName;

    private String clientVersion;

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
        dto.setTerminalOsType(terminalOsType);
        dto.setIdvTerminalMode(idvTerminalMode);
        dto.setBindUserId(bindUserId);
        dto.setBindUserName(bindUserName);
        dto.setBindDeskId(bindDeskId);
        dto.setEnableVisitorLogin(enableVisitorLogin);
        dto.setEnableAutoLogin(enableAutoLogin);
        dto.setAuthed(authed);
        dto.setPlatform(platform);
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
        dto.setNicWorkMode(nicWorkMode);
        dto.setAuthType(authType);
        dto.setAuthMode(authMode);
        dto.setUserId(userId);
        dto.setUserName(userName);
        dto.setClientVersion(clientVersion);
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
        terminalDTO.setBindUserName(bindUserName);
        terminalDTO.setBindUserId(bindUserId);
        terminalDTO.setBindDeskId(bindDeskId);
        terminalDTO.setWirelessNetCardNum(wirelessNetCardNum);
        terminalDTO.setEthernetNetCardNum(ethernetNetCardNum);
        terminalDTO.setAllDiskInfo(allDiskInfo);
        terminalDTO.setLock(lock);
        terminalDTO.setRealTerminalId(realTerminalId);
        terminalDTO.setTerminalCloudDesktop(terminalCloudDesktopDTO);
        setNetworkInfo(terminalDTO);
        setNetworkModel(terminalDTO);
        terminalDTO.setOpenOtpCertification(openOtpCertification);
        terminalDTO.setHasBindOtp(hasBindOtp);
        terminalDTO.setBootType(bootType);
        terminalDTO.setSupportTcStart(supportTcStart);
        terminalDTO.setEnableProxy(enableProxy);
        terminalDTO.setSupportRemoteWake(supportRemoteWake);
        terminalDTO.setNicWorkMode(nicWorkMode);
        terminalDTO.setAuthMode(authMode);
        terminalDTO.setUserId(userId);
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



    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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




    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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


    public CbbNicWorkModeEnums getNicWorkMode() {
        return nicWorkMode;
    }

    public void setNicWorkMode(CbbNicWorkModeEnums nicWorkMode) {
        this.nicWorkMode = nicWorkMode;
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

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }
}
