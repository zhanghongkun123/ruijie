package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

/**
 * 
 * Description: 终端磁盘信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/7
 *
 * @author zhiweiHong
 */
public class TerminalDeskInfoDTO {

    private String devName;

    private String devType;

    private String devForm;

    private String devTotalSize;

    private String devMedia;

    private String devState;

    private String devSn;

    private String devFirmwareVersion;

    private String devHealth;

    private String devPowerOnhour;

    private String devTotalWritten;

    private String devReadIops;

    private String devWriteIops;

    private String devModel;


    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getDevForm() {
        return devForm;
    }

    public void setDevForm(String devForm) {
        this.devForm = devForm;
    }

    public String getDevTotalSize() {
        return devTotalSize;
    }

    public void setDevTotalSize(String devTotalSize) {
        this.devTotalSize = devTotalSize;
    }

    public String getDevMedia() {
        return devMedia;
    }

    public void setDevMedia(String devMedia) {
        this.devMedia = devMedia;
    }

    public String getDevState() {
        return devState;
    }

    public void setDevState(String devState) {
        this.devState = devState;
    }

    public String getDevSn() {
        return devSn;
    }

    public void setDevSn(String devSn) {
        this.devSn = devSn;
    }

    public String getDevFirmwareVersion() {
        return devFirmwareVersion;
    }

    public void setDevFirmwareVersion(String devFirmwareVersion) {
        this.devFirmwareVersion = devFirmwareVersion;
    }

    public String getDevHealth() {
        return devHealth;
    }

    public void setDevHealth(String devHealth) {
        this.devHealth = devHealth;
    }

    public String getDevPowerOnhour() {
        return devPowerOnhour;
    }

    public void setDevPowerOnhour(String devPowerOnhour) {
        this.devPowerOnhour = devPowerOnhour;
    }

    public String getDevTotalWritten() {
        return devTotalWritten;
    }

    public void setDevTotalWritten(String devTotalWritten) {
        this.devTotalWritten = devTotalWritten;
    }

    public String getDevReadIops() {
        return devReadIops;
    }

    public void setDevReadIops(String devReadIops) {
        this.devReadIops = devReadIops;
    }

    public String getDevWriteIops() {
        return devWriteIops;
    }

    public void setDevWriteIops(String devWriteIops) {
        this.devWriteIops = devWriteIops;
    }

    public String getDevModel() {
        return devModel;
    }

    public void setDevModel(String devModel) {
        this.devModel = devModel;
    }
}
