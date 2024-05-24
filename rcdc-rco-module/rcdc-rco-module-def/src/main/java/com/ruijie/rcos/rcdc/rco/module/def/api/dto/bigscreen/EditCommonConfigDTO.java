package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import java.util.UUID;

/**
 *
 * Description: 编辑通用配置
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author BaiGuoliang
 */
public class EditCommonConfigDTO {

    @NotNull
    private UUID id;

    @NotBlank
    private String configValue;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
}
