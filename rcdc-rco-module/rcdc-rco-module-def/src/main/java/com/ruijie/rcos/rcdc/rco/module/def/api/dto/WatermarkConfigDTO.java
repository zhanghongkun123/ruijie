package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkDisplayConfigDTO;
import org.springframework.lang.Nullable;

/**
 * Description: 云桌面水印配置
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/7 10:48
 *
 * @author zjy
 */
public class WatermarkConfigDTO {

    // 水印开关
    @Nullable
    private Boolean enable;

    // 水印字体配置
    @Nullable
    private CbbWatermarkDisplayConfigDTO displayConfig;

    // 水印展示内容
    @Nullable
    private WatermarkDisplayContent displayContent;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public CbbWatermarkDisplayConfigDTO getDisplayConfig() {
        return displayConfig;
    }

    public void setDisplayConfig(CbbWatermarkDisplayConfigDTO displayConfig) {
        this.displayConfig = displayConfig;
    }

    public WatermarkDisplayContent getDisplayContent() {
        return displayContent;
    }

    public void setDisplayContent(WatermarkDisplayContent displayContent) {
        this.displayContent = displayContent;
    }

    /**
     * Description: 水印展示内容
     * Copyright: Copyright (c) 2019
     * Company: Ruijie Co., Ltd.
     * Create Time: 2022/3/7 10:48
     *
     * @author zjy
     */
    public static class WatermarkDisplayContent {
        // 展示字段名
        @Nullable
        private String[] fieldArr;

        // 自定义展示内容
        @Nullable
        private String customContent;

        public String[] getFieldArr() {
            return fieldArr;
        }

        public void setFieldArr(String[] fieldArr) {
            this.fieldArr = fieldArr;
        }

        public String getCustomContent() {
            return customContent;
        }

        public void setCustomContent(String customContent) {
            this.customContent = customContent;
        }
    }
}
