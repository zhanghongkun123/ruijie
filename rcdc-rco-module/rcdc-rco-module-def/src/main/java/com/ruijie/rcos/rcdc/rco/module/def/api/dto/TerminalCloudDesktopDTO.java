package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 终端绑定的云桌面信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/10 16:08
 *
 * @author conghaifeng
 */
public class TerminalCloudDesktopDTO {

    /**
     * 桌面ID
     */
    private UUID desktopId;

    /**
     * 镜像名称
     */
    private String imageTemplateName;

    /**
     * 云桌面运行模式
     */
    private CbbCloudDeskPattern pattern;

    /**
     * 云桌面Ip
     */
    private String deskIp;

    /**
     * 系统盘大小
     */
    private Integer systemSize;

    /**
     * 个人盘大小
     */
    private Integer personSize;

    /**
     * 是否开启本地盘
     */
    private Boolean isAllowLocalDisk;

    /**
     * 云桌面mac
     */
    private String deskMac;

    /**
     * 云桌面ip获取方式
     */
    private Boolean autoDhcp;

    /**
     * 云桌面网关
     */
    private String gateWay;

    /**
     * 云桌面掩码
     */
    private String mask;

    /**
     * 云桌面dns获取方式
     */
    private Boolean autoDns;

    /**
     * 云桌面首选DNS
     */
    private String dnsPrimary;

    /**
     * 云桌面备选DNS
     */
    private String dnsSecondary;

    private String wirelessIp;

    private String wirelessMacAddr;

    private Boolean autoWirelessDhcp;

    private String wirelessGateway;

    private String wirelessMask;

    private Boolean autoWirelessDns;

    private String wirelessDnsPrimary;

    private String wirelessSecondDnsPrimary;

    private String desktopType;

    public TerminalCloudDesktopDTO() {
        // Do nothing because of X and Y.
    }

    /**
     * 由CloudDesktopDTO转换到TerminalCloudDesktopDTO
     * @param deskDTO CloudDesktopDTO
     */
    public void convertFor(CloudDesktopDTO deskDTO) {
        Assert.notNull(deskDTO,"deskDTO can not be null");

        this.wirelessIp = deskDTO.getWirelessIp();
        this.wirelessDnsPrimary = deskDTO.getWirelessDnsPrimary();
        this.wirelessGateway = deskDTO.getWirelessGateway();
        this.wirelessMacAddr = deskDTO.getWirelessMacAddr();
        this.wirelessMask = deskDTO.getWirelessMask();
        this.wirelessSecondDnsPrimary = deskDTO.getWirelessSecondDnsPrimary();
        this.autoWirelessDhcp = deskDTO.getAutoWirelessDhcp();
        this.autoWirelessDns = deskDTO.getAutoWirelessDns();
        this.deskIp = deskDTO.getDesktopIp();
        this.deskMac = deskDTO.getDesktopMac();
        this.dnsPrimary = deskDTO.getDeskDnsPrimary();
        this.dnsSecondary = deskDTO.getDeskSecondDnsPrimary();
        this.gateWay = deskDTO.getDeskGateway();
        this.autoDns = deskDTO.getAutoDeskDns();
        this.autoDhcp = deskDTO.getAutoDeskDhcp();
        this.mask = deskDTO.getDeskMask();
        this.desktopType = deskDTO.getDesktopType();
        this.systemSize = deskDTO.getSystemDisk();
        this.personSize = deskDTO.getPersonDisk();
        this.imageTemplateName = deskDTO.getImageName();
        this.desktopId = deskDTO.getCbbId();
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
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

    public String getDeskMac() {
        return deskMac;
    }

    public void setDeskMac(String deskMac) {
        this.deskMac = deskMac;
    }

    public Boolean getAutoDhcp() {
        return autoDhcp;
    }

    public void setAutoDhcp(Boolean autoDhcp) {
        this.autoDhcp = autoDhcp;
    }

    public String getGateWay() {
        return gateWay;
    }

    public void setGateWay(String gateWay) {
        this.gateWay = gateWay;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public Boolean getAutoDns() {
        return autoDns;
    }

    public void setAutoDns(Boolean autoDns) {
        this.autoDns = autoDns;
    }

    public String getDnsPrimary() {
        return dnsPrimary;
    }

    public void setDnsPrimary(String dnsPrimary) {
        this.dnsPrimary = dnsPrimary;
    }

    public String getDnsSecondary() {
        return dnsSecondary;
    }

    public void setDnsSecondary(String dnsSecondary) {
        this.dnsSecondary = dnsSecondary;
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

    public Boolean getAutoWirelessDhcp() {
        return autoWirelessDhcp;
    }

    public void setAutoWirelessDhcp(Boolean autoWirelessDhcp) {
        this.autoWirelessDhcp = autoWirelessDhcp;
    }

    public String getWirelessGateway() {
        return wirelessGateway;
    }

    public void setWirelessGateway(String wirelessGateway) {
        this.wirelessGateway = wirelessGateway;
    }

    public String getWirelessMask() {
        return wirelessMask;
    }

    public void setWirelessMask(String wirelessMask) {
        this.wirelessMask = wirelessMask;
    }

    public Boolean getAutoWirelessDns() {
        return autoWirelessDns;
    }

    public void setAutoWirelessDns(Boolean autoWirelessDns) {
        this.autoWirelessDns = autoWirelessDns;
    }

    public String getWirelessDnsPrimary() {
        return wirelessDnsPrimary;
    }

    public void setWirelessDnsPrimary(String wirelessDnsPrimary) {
        this.wirelessDnsPrimary = wirelessDnsPrimary;
    }

    public String getWirelessSecondDnsPrimary() {
        return wirelessSecondDnsPrimary;
    }

    public void setWirelessSecondDnsPrimary(String wirelessSecondDnsPrimary) {
        this.wirelessSecondDnsPrimary = wirelessSecondDnsPrimary;
    }

    public String getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }
}
