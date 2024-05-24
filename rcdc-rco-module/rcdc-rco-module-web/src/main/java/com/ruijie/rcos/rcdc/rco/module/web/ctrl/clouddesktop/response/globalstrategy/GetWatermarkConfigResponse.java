package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.WatermarkDisplayConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.WatermarkDisplayContentDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月26日
 *
 * @author XiaoJiaXin
 */
public class GetWatermarkConfigResponse extends DefaultResponse {

    private Boolean enable;

    private Boolean enableDarkWatermark;

    private WatermarkDisplayContentDTO displayContent;

    private WatermarkDisplayConfigDTO displayConfig;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public WatermarkDisplayContentDTO getDisplayContent() {
        return displayContent;
    }

    public void setDisplayContent(WatermarkDisplayContentDTO displayContent) {
        this.displayContent = displayContent;
    }

    public WatermarkDisplayConfigDTO getDisplayConfig() {
        return displayConfig;
    }

    public void setDisplayConfig(WatermarkDisplayConfigDTO displayConfig) {
        this.displayConfig = displayConfig;
    }

    public Boolean getEnableDarkWatermark() {
        return enableDarkWatermark;
    }

    public void setEnableDarkWatermark(Boolean enableDarkWatermark) {
        this.enableDarkWatermark = enableDarkWatermark;
    }
}
