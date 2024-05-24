package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalNetworkInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbNetworkModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbSystemUpgradeStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import org.springframework.util.Assert;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/16 14:26
 *
 * @author zhangyichi
 */
public class UpgradeTerminalDetailVO {
    private String terminalId;

    private String terminalName;

    private String ip;

    private String mac;

    private Date startTime;

    /**
     * 终端运行平台
     */
    @Enumerated(EnumType.STRING)
    private CbbTerminalPlatformEnums terminalPlatform;

    /**
     * 网络接入方式
     */
    @Enumerated(EnumType.STRING)
    private CbbNetworkModeEnums networkAccessMode;

    private CbbSystemUpgradeStateEnums terminalUpgradeState;

    /**
     * 终端型号
     */
    private String productType;

    /**
     * 终端系统版本
     */
    private String rainOsVersion;

    /**
     * 终端运行平台
     */
    private String terminalOsType;

    /**
     * 解析网络信息数组
     * @param networkInfoArr 网络信息数组
     */
    public void parseNetworkInfoArr(CbbTerminalNetworkInfoDTO[] networkInfoArr) {
        Assert.notNull(networkInfoArr, "networkInfoArr");
        // 有线网络信息
        CbbTerminalNetworkInfoDTO wiredNetworkInfo =
                Stream.of(networkInfoArr).filter(item -> item.getNetworkAccessMode() == CbbNetworkModeEnums.WIRED).findFirst().orElse(null);
        // 无线网络信息
        CbbTerminalNetworkInfoDTO wirelessNetworkInfo =
                Stream.of(networkInfoArr).filter(item -> item.getNetworkAccessMode() == CbbNetworkModeEnums.WIRELESS).findFirst().orElse(null);
        if (Objects.nonNull(wiredNetworkInfo)) {
            setNetworkInfo(wiredNetworkInfo);
            return;
        }
        if (Objects.nonNull(wirelessNetworkInfo)) {
            setNetworkInfo(wirelessNetworkInfo);
            return;
        }
        // 如果都不存在，默认是有线
        this.networkAccessMode = CbbNetworkModeEnums.WIRED;
    }

    private void setNetworkInfo(CbbTerminalNetworkInfoDTO networkInfo) {
        this.ip = networkInfo.getIp();
        this.mac = networkInfo.getMacAddr();
        this.networkAccessMode = networkInfo.getNetworkAccessMode();
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public CbbSystemUpgradeStateEnums getTerminalUpgradeState() {
        return terminalUpgradeState;
    }

    public void setTerminalUpgradeState(CbbSystemUpgradeStateEnums terminalUpgradeState) {
        this.terminalUpgradeState = terminalUpgradeState;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getRainOsVersion() {
        return rainOsVersion;
    }

    public void setRainOsVersion(String rainOsVersion) {
        this.rainOsVersion = rainOsVersion;
    }

    public String getTerminalOsType() {
        return terminalOsType;
    }

    public void setTerminalOsType(String terminalOsType) {
        this.terminalOsType = terminalOsType;
    }

    public CbbNetworkModeEnums getNetworkAccessMode() {
        return networkAccessMode;
    }

    public void setNetworkAccessMode(CbbNetworkModeEnums networkAccessMode) {
        this.networkAccessMode = networkAccessMode;
    }

    public CbbTerminalPlatformEnums getTerminalPlatform() {
        return terminalPlatform;
    }

    public void setTerminalPlatform(CbbTerminalPlatformEnums terminalPlatform) {
        this.terminalPlatform = terminalPlatform;
    }
}
