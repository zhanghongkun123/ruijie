package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto;

import com.ruijie.rcos.rcdc.task.ext.module.def.dto.StateMachineConfigAndBatchTaskItemDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.DiskCtrlType;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年04月13日
 *
 * @author xgx
 */
public class EditImageTemplateDTO extends StateMachineConfigAndBatchTaskItemDTO {
    @Nullable
    private DiskCtrlType systemDiskCtrlType;

    @NotNull
    private ImageTemplateAdvancedConfigDTO advancedConfig;

    @Nullable
    public DiskCtrlType getSystemDiskCtrlType() {
        return systemDiskCtrlType;
    }

    public void setSystemDiskCtrlType(@Nullable DiskCtrlType systemDiskCtrlType) {
        this.systemDiskCtrlType = systemDiskCtrlType;
    }

    public ImageTemplateAdvancedConfigDTO getAdvancedConfig() {
        return advancedConfig;
    }

    public void setAdvancedConfig(ImageTemplateAdvancedConfigDTO advancedConfig) {
        this.advancedConfig = advancedConfig;
    }
}
