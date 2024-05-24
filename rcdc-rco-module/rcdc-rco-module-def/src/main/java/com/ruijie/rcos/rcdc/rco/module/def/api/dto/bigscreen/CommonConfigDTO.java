package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ConfigKeyEnum;
import java.util.UUID;

/**
 *
 * Description: 配置
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author BaiGuoliang
 */
public class CommonConfigDTO {

    private UUID id;

    private ConfigKeyEnum configKey;

    private String configValue;

    private String description;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ConfigKeyEnum getConfigKey() {
        return configKey;
    }

    public void setConfigKey(ConfigKeyEnum configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
