package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import org.springframework.lang.Nullable;

/**
 * Description: 主题策略配置信息DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-06-22
 *
 * @author lihengjing
 */
public class ThemeConfigInfoDTO {

    @Nullable
    private ThemeFileInfoDTO backgroundInfo;

    @Nullable
    private ThemeFileInfoDTO logoInfo;

    public ThemeConfigInfoDTO(ThemeFileInfoDTO backgroundInfo, ThemeFileInfoDTO logoInfo) {
        this.backgroundInfo = backgroundInfo;
        this.logoInfo = logoInfo;
    }

    public ThemeConfigInfoDTO() {

    }

    public ThemeFileInfoDTO getBackgroundInfo() {
        return backgroundInfo;
    }

    public void setBackgroundInfo(ThemeFileInfoDTO backgroundInfo) {
        this.backgroundInfo = backgroundInfo;
    }

    public ThemeFileInfoDTO getLogoInfo() {
        return logoInfo;
    }

    public void setLogoInfo(ThemeFileInfoDTO logoInfo) {
        this.logoInfo = logoInfo;
    }

    @Override
    public String toString() {
        return "ThemeConfigInfoDTO{" +
                "backgroundInfo=" + backgroundInfo +
                ", logoInfo=" + logoInfo +
                '}';
    }
}
