package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月26日
 *
 * @author XiaoJiaXin
 */
public class WatermarkDisplayConfigDTO {

    @NotNull
    @Range(
            min = "0",
            max = "100"
    )
    private Integer transparency;

    @NotNull
    @Range(
            min = "0",
            max = "359"
    )
    private Integer angle;

    @NotNull
    @Range(
            min = "12",
            max = "50"
    )
    private Integer fontSize;

    @NotBlank
    private String fontColor;

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
}
