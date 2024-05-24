package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/9/3
 *
 * @author jarman
 */
public class GuestToolWatermarkMessageDTO {

    private Boolean enable;

    private String userName;

    private String multiSessionId;

    private String deskName;

    private String deskIp;

    private String deskMac;

    private Integer transparency;

    private Integer angle;

    private Integer fontSize;

    private String fontColor;

    private String customContent;

    private Boolean enableDarkWatermark = Boolean.FALSE;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getDeskIp() {
        return deskIp;
    }

    public void setDeskIp(String deskIp) {
        this.deskIp = deskIp;
    }

    public String getDeskMac() {
        return deskMac;
    }

    public void setDeskMac(String deskMac) {
        this.deskMac = deskMac;
    }

    public Integer getTransparency() {
        return transparency;
    }

    public void setTransparency(Integer transparency) {
        this.transparency = transparency;
    }

    public Integer getAngle() {
        return angle;
    }

    public void setAngle(Integer angle) {
        this.angle = angle;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getCustomContent() {
        return customContent;
    }

    public void setCustomContent(String customContent) {
        this.customContent = customContent;
    }

    public Boolean getEnableDarkWatermark() {
        return enableDarkWatermark;
    }

    public void setEnableDarkWatermark(Boolean enableDarkWatermark) {
        this.enableDarkWatermark = enableDarkWatermark;
    }

    public String getMultiSessionId() {
        return multiSessionId;
    }

    public void setMultiSessionId(String multiSessionId) {
        this.multiSessionId = multiSessionId;
    }
}
