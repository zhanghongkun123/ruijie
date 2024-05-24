package com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.CbbEstEncodingFormat;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description: HEST配置DTO类
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/30
 *
 * @author WuShengQiang
 */
public class HestConfigCheckDTO extends EstConfigCheckDTO {

    /**
     * 硬件加速编码
     */
    @NotNull
    private Boolean hardware;

    /**
     * 色彩模式:"禁止，标准色彩444，双流色彩444 = 界面值 { 0, 1, 2}"
     */
    @NotNull
    @JSONField(name = "coloraccuracy")
    private Integer colorAccuracy;

    /**
     * 始终捕获屏幕
     */
    @NotNull
    @JSONField(name = "fullfps")
    private Boolean fullFps;

    /**
     * 可容忍的最低延迟 "禁止、低、中、高: { 0, 1, 2, 3}"
     */
    @NotNull
    @JSONField(name = "playoutdelay")
    private Integer playOutDelay;

    /**
     * 编码格式 "锐捷智能编码、H264、H265、VP9、AV1:{""cache3"", ""h264"", ""h265"", ""vp9"", ""av1""}"
     */
    @NotNull
    private CbbEstEncodingFormat payload;

    /**
     * 前置条件:传输模式RUTP 启用音视频优化
     */
    @Nullable
    private Boolean enableAudioVideoOptimize;

    /**
     * 前置条件:传输模式RUTP 前向纠错  0表示关闭，2表示开启，这边用2主要是有fec类型，目前只使用2这个类型
     */
    @Nullable
    @JSONField(name = "fec_type")
    private Integer fecType;

    /**
     * 音频清晰度 "低、中、高、自定义: { 0, 1, 2，3}"
     */
    @NotNull
    @JSONField(name = "audioquality")
    private Integer audioQuality;

    /**
     * 前置条件:音频清晰度选自定义  码率
     */
    @Nullable
    @JSONField(name = "audiobitrate")
    private Integer audioBitrate;

    public Boolean getHardware() {
        return hardware;
    }

    public void setHardware(Boolean hardware) {
        this.hardware = hardware;
    }

    public Integer getColorAccuracy() {
        return colorAccuracy;
    }

    public void setColorAccuracy(Integer colorAccuracy) {
        this.colorAccuracy = colorAccuracy;
    }

    public Boolean getFullFps() {
        return fullFps;
    }

    public void setFullFps(Boolean fullFps) {
        this.fullFps = fullFps;
    }

    public Integer getPlayOutDelay() {
        return playOutDelay;
    }

    public void setPlayOutDelay(Integer playOutDelay) {
        this.playOutDelay = playOutDelay;
    }

    public CbbEstEncodingFormat getPayload() {
        return payload;
    }

    public void setPayload(CbbEstEncodingFormat payload) {
        this.payload = payload;
    }

    @Nullable
    public Boolean getEnableAudioVideoOptimize() {
        return enableAudioVideoOptimize;
    }

    public void setEnableAudioVideoOptimize(@Nullable Boolean enableAudioVideoOptimize) {
        this.enableAudioVideoOptimize = enableAudioVideoOptimize;
    }

    @Nullable
    public Integer getFecType() {
        return fecType;
    }

    public void setFecType(@Nullable Integer fecType) {
        this.fecType = fecType;
    }

    public Integer getAudioQuality() {
        return audioQuality;
    }

    public void setAudioQuality(Integer audioQuality) {
        this.audioQuality = audioQuality;
    }

    @Nullable
    public Integer getAudioBitrate() {
        return audioBitrate;
    }

    public void setAudioBitrate(@Nullable Integer audioBitrate) {
        this.audioBitrate = audioBitrate;
    }
}
