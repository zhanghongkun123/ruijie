package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;

/**
 * 
 * Description: 传输协议配置DTO
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月29日
 * 
 * @author Ghang
 */
public class ProtocolTransferConfigDTO {

    //协议传输配置:显示质量
    @NotNull
    @Range(min = "1" , max = "100")
    private Integer displayQuality; 
    
    //协议传输配置:采样率
    @NotNull
    @Range(min = "1",max = "2")
    private Integer samplingRate; 
    
    //协议传输配置:帧率
    @NotNull
    @Range(min = "1" , max = "100")
    private Integer frameRate;
    
    //协议传输配置:是否启用SSL加密
    @NotNull
    private Boolean enableEncryption;
    
    //协议传输配置:是否启用音频压缩
    @NotNull
    private Boolean enableAudioCompression;

    //协议传输配置:是否启用增强静止画面清晰度
    @NotNull
    private Boolean enableQualityEnhance;

    public Integer getDisplayQuality() {
        return displayQuality;
    }

    public void setDisplayQuality(Integer displayQuality) {
        this.displayQuality = displayQuality;
    }

    public Integer getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(Integer samplingRate) {
        this.samplingRate = samplingRate;
    }

    public Integer getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(Integer frameRate) {
        this.frameRate = frameRate;
    }

    public Boolean getEnableEncryption() {
        return enableEncryption;
    }

    public void setEnableEncryption(Boolean enableEncryption) {
        this.enableEncryption = enableEncryption;
    }

    public Boolean getEnableAudioCompression() {
        return enableAudioCompression;
    }

    public void setEnableAudioCompression(Boolean enableAudioCompression) {
        this.enableAudioCompression = enableAudioCompression;
    }

    public Boolean getEnableQualityEnhance() {
        return enableQualityEnhance;
    }

    public void setEnableQualityEnhance(Boolean enableQualityEnhance) {
        this.enableQualityEnhance = enableQualityEnhance;
    }
}
