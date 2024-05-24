package com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.CbbEstDisplayModeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description: EST配置DTO类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/17 9:41
 *
 * @author yxq
 */
public class EstConfigCheckDTO {

    /**
     * 基础模板类型id
     */
    @NotNull
    @JSONField(name = "templateid")
    private Integer templateId;

    /**
     * 启用自定义模板
     */
    @NotNull
    private Boolean enableCustomTemplate;

    /**
     * 显示质量-模板名称
     */
    @Nullable
    private CbbEstDisplayModeEnum name;

    /**
     * 是否自定义，0：非自定义，1：自定义
     */
    @Nullable
    private Integer customize;

    /**
     * 传输协议，0：TCP，1：RUTP
     */
    @NotNull
    private Integer transport;

    /**
     * 显示自适应网络
     */
    @Nullable
    @JSONField(name = "adaptdisplay")
    private Integer adaptDisplay;

    /**
     * 显示总带宽，单位：kpbs
     */
    @NotNull
    private Integer bitrate;

    /**
     * 视频带宽，单位：kpbs
     */
    @Nullable
    @JSONField(name = "videobitrate")
    private Integer videoBitrate;

    /**
     * 目标最大帧率，EST范围：1-45 HEST范围：1-144
     */
    @NotNull
    private Integer framerate;

    /**
     * 目标最小帧率，范围：0-45
     */
    @Nullable
    @JSONField(name = "minframerate")
    private Integer minFramerate;

    /**
     * 画质清晰度，0：低，1：默认(中)，2：高，3：类无损
     */
    @NotNull
    private Integer quality;

    /**
     * 视频流设置，0：不使用视频流，1：自适应主动变化区域，2：针对整个屏幕
     */
    @Nullable
    @JSONField(name = "faststreammode")
    private Integer fastStreamMode;

    /**
     * 视频编码器，0：实时，1：高清
     */
    @Nullable
    @JSONField(name = "videocodec")
    private Integer videoCodec;

    /**
     * 增强静止画面清晰度，0：关闭，1：开启
     */
    @NotNull
    private Integer reencode;

    /**
     * 音频自适应网络，0：关闭，1：开启
     */
    @Nullable
    @JSONField(name = "adaptsound")
    private Integer adaptSound;

    /**
     * 播放音频，0：关闭，1：开启
     */
    @Nullable
    @JSONField(name = "snd_playback")
    private Integer sndPlayback;

    /**
     * 实时音频，0：关闭，1：开启
     */
    @Nullable
    @JSONField(name = "snd_udp")
    private Integer sndUdp;

    /**
     * 音频清晰度，0：低，1：默认(中)，2：高，3：自定义
     */
    @Nullable
    @JSONField(name = "snd_quality")
    private Integer sndQuality;

    /**
     * 高级配置，0：关闭，1：开启
     */
    @NotNull
    @JSONField(name = "enable_web_advance_setting")
    private Integer enableWebAdvanceSetting;

    /**
     * 高级配置信息
     */
    @Nullable
    @JSONField(name = "web_advance_setting_info")
    private JSONObject webAdvanceSettingInfo;

    /**
     * 启用SSL加密
     */
    @NotNull
    private Boolean enableSsl;

    //协议传输配置:显示质量
    @Nullable
    private Integer displayQuality;

    //协议传输配置:采样率
    @Nullable
    private Integer samplingRate;

    //应用显示模式策略:全局操作控制,不开启应用显示模式策略时可为空
    @Nullable
    private String globalOperationControl;

    //应用显示模式策略:是否开启
    @Nullable
    private Boolean enableAppDisplayMode;

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Boolean getEnableCustomTemplate() {
        return enableCustomTemplate;
    }

    public void setEnableCustomTemplate(Boolean enableCustomTemplate) {
        this.enableCustomTemplate = enableCustomTemplate;
    }

    @Nullable
    public CbbEstDisplayModeEnum getName() {
        return name;
    }

    public void setName(@Nullable CbbEstDisplayModeEnum name) {
        this.name = name;
    }

    @Nullable
    public Integer getCustomize() {
        return customize;
    }

    public void setCustomize(@Nullable Integer customize) {
        this.customize = customize;
    }

    public Integer getTransport() {
        return transport;
    }

    public void setTransport(Integer transport) {
        this.transport = transport;
    }

    @Nullable
    public Integer getAdaptDisplay() {
        return adaptDisplay;
    }

    public void setAdaptDisplay(@Nullable Integer adaptDisplay) {
        this.adaptDisplay = adaptDisplay;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    @Nullable
    public Integer getVideoBitrate() {
        return videoBitrate;
    }

    public void setVideoBitrate(@Nullable Integer videoBitrate) {
        this.videoBitrate = videoBitrate;
    }

    public Integer getFramerate() {
        return framerate;
    }

    public void setFramerate(Integer framerate) {
        this.framerate = framerate;
    }

    @Nullable
    public Integer getMinFramerate() {
        return minFramerate;
    }

    public void setMinFramerate(@Nullable Integer minFramerate) {
        this.minFramerate = minFramerate;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    @Nullable
    public Integer getFastStreamMode() {
        return fastStreamMode;
    }

    public void setFastStreamMode(@Nullable Integer fastStreamMode) {
        this.fastStreamMode = fastStreamMode;
    }

    @Nullable
    public Integer getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(@Nullable Integer videoCodec) {
        this.videoCodec = videoCodec;
    }

    public Integer getReencode() {
        return reencode;
    }

    public void setReencode(Integer reencode) {
        this.reencode = reencode;
    }

    @Nullable
    public Integer getAdaptSound() {
        return adaptSound;
    }

    public void setAdaptSound(@Nullable Integer adaptSound) {
        this.adaptSound = adaptSound;
    }

    @Nullable
    public Integer getSndPlayback() {
        return sndPlayback;
    }

    public void setSndPlayback(@Nullable Integer sndPlayback) {
        this.sndPlayback = sndPlayback;
    }

    @Nullable
    public Integer getSndUdp() {
        return sndUdp;
    }

    public void setSndUdp(@Nullable Integer sndUdp) {
        this.sndUdp = sndUdp;
    }

    @Nullable
    public Integer getSndQuality() {
        return sndQuality;
    }

    public void setSndQuality(@Nullable Integer sndQuality) {
        this.sndQuality = sndQuality;
    }

    public Integer getEnableWebAdvanceSetting() {
        return enableWebAdvanceSetting;
    }

    public void setEnableWebAdvanceSetting(Integer enableWebAdvanceSetting) {
        this.enableWebAdvanceSetting = enableWebAdvanceSetting;
    }

    @Nullable
    public JSONObject getWebAdvanceSettingInfo() {
        return webAdvanceSettingInfo;
    }

    public void setWebAdvanceSettingInfo(@Nullable JSONObject webAdvanceSettingInfo) {
        this.webAdvanceSettingInfo = webAdvanceSettingInfo;
    }

    public Boolean getEnableSsl() {
        return enableSsl;
    }

    public void setEnableSsl(Boolean enableSsl) {
        this.enableSsl = enableSsl;
    }

    @Nullable
    public Integer getDisplayQuality() {
        return displayQuality;
    }

    public void setDisplayQuality(@Nullable Integer displayQuality) {
        this.displayQuality = displayQuality;
    }

    @Nullable
    public Integer getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(@Nullable Integer samplingRate) {
        this.samplingRate = samplingRate;
    }

    @Nullable
    public String getGlobalOperationControl() {
        return globalOperationControl;
    }

    public void setGlobalOperationControl(@Nullable String globalOperationControl) {
        this.globalOperationControl = globalOperationControl;
    }

    @Nullable
    public Boolean getEnableAppDisplayMode() {
        return enableAppDisplayMode;
    }

    public void setEnableAppDisplayMode(@Nullable Boolean enableAppDisplayMode) {
        this.enableAppDisplayMode = enableAppDisplayMode;
    }
}
