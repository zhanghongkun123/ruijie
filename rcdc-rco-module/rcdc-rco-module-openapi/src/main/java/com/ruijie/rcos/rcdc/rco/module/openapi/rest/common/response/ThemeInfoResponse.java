package com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ThemeFileInfoDTO;

/**
 * Description: 主题策略配置信息RCDC响应对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-06-22
 *
 * @author lihengjing
 */
public class ThemeInfoResponse {
    private ThemeFileInfoDTO backgroundInfo;

    private ThemeFileInfoDTO logoInfo;

    public ThemeInfoResponse(ThemeFileInfoDTO backgroundInfo, ThemeFileInfoDTO logoInfo) {
        this.backgroundInfo = backgroundInfo;
        this.logoInfo = logoInfo;
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
}
