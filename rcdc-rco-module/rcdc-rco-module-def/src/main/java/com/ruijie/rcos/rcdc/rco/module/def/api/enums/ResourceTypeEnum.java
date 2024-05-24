package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

/**
 * Description: ResourceTypeEnum
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月25日
 *
 * @author wanmulin
 */
public enum ResourceTypeEnum {

    CPU(ConfigKeyEnum.BIGSCREEN_SERVER_FORECAST_LIMIT_CPU, ConfigKeyEnum.BIGSCREEN_SERVER_FORECAST_TIME_CPU),
    MEMORY(ConfigKeyEnum.BIGSCREEN_SERVER_FORECAST_LIMIT_MEMORY, ConfigKeyEnum.BIGSCREEN_SERVER_FORECAST_TIME_MEMORY),
    DISK(ConfigKeyEnum.BIGSCREEN_SERVER_FORECAST_LIMIT_DISK, ConfigKeyEnum.BIGSCREEN_SERVER_FORECAST_TIME_DISK);

    private ConfigKeyEnum limitKey;

    private ConfigKeyEnum timeKey;


    ResourceTypeEnum(ConfigKeyEnum limitKey, ConfigKeyEnum timeKey) {
        this.limitKey = limitKey;
        this.timeKey = timeKey;
    }

    public ConfigKeyEnum getLimitKey() {
        return limitKey;
    }

    public ConfigKeyEnum getTimeKey() {
        return timeKey;
    }
}

