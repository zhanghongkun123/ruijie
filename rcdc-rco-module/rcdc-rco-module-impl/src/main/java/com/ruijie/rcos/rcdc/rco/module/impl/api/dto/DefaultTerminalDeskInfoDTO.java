package com.ruijie.rcos.rcdc.rco.module.impl.api.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * Description: 默认终端磁盘信息，用于接收底层终端返回信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/7
 *
 * @author zhiweiHong
 */
public class DefaultTerminalDeskInfoDTO {

    @JSONField(name = "devName")
    private String devName;

    @JSONField(name = "devType")
    private String devType;

    @JSONField(name = "devForm")
    private String devForm;

    @JSONField(name = "devTotalsize")
    private String devTotalSize;

    @JSONField(name = "devMedia")
    private String devMedia;

    @JSONField(name = "devState")
    private String devState;

    @JSONField(name = "devSn")
    private String devSn;

    @JSONField(name = "devFirmwareVersion")
    private String devFirmwareVersion;

    @JSONField(name = "devHealth")
    private String devHealth;

    @JSONField(name = "devPowerOnhour")
    private String devPowerOnhour;

    @JSONField(name = "devTotalWritten")
    private String devTotalWritten;

    @JSONField(name = "devReadIops")
    private String devReadIops;

    @JSONField(name = "devWriteIops")
    private String devWriteIops;

    @JSONField(name = "devModel")
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
