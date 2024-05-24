package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy;


import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.WatermarkDisplayConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.vo.WatermarkDisplayContentVO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月26日
 *
 * @author XiaoJiaXin
 */
public class EditWatermarkWebRequest implements WebRequest {

    @NotNull
    private Boolean enable;

    @Nullable
    private Boolean enableDarkWatermark = Boolean.FALSE;

    @NotNull
    private WatermarkDisplayContentVO displayContent;

    @NotNull
    private WatermarkDisplayConfigDTO displayConfig;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public WatermarkDisplayContentVO getDisplayContent() {
        return displayContent;
    }

    public void setDisplayContent(WatermarkDisplayContentVO displayContent) {
        this.displayContent = displayContent;
    }

    public WatermarkDisplayConfigDTO getDisplayConfig() {
        return displayConfig;
    }

    public void setDisplayConfig(WatermarkDisplayConfigDTO displayConfig) {
        this.displayConfig = displayConfig;
    }

    @Nullable
    public Boolean getEnableDarkWatermark() {
        return enableDarkWatermark;
    }

    public void setEnableDarkWatermark(@Nullable Boolean enableDarkWatermark) {
        this.enableDarkWatermark = enableDarkWatermark;
    }
}
