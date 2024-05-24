package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbHestConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;

import java.util.Date;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/17 10:42
 *
 * @author yxq
 */
public class SyncEstConfigDTO {

    /**
     * 连接协议
     */
    private CbbEstProtocolType estProtocolType;

    /**
     * 广域网配置
     */
    @JSONField(name = "wan_est_config")
    private CbbHestConfigDTO wanEstConfig;

    /**
     * 局域网配置
     */
    @JSONField(name = "lan_est_config")
    private CbbHestConfigDTO lanEstConfig;

    /**
     * 时间戳
     */
    private Date timestamp;

    public CbbEstProtocolType getEstProtocolType() {
        return estProtocolType;
    }

    public void setEstProtocolType(CbbEstProtocolType estProtocolType) {
        this.estProtocolType = estProtocolType;
    }

    public CbbHestConfigDTO getWanEstConfig() {
        return wanEstConfig;
    }

    public void setWanEstConfig(CbbHestConfigDTO wanEstConfig) {
        this.wanEstConfig = wanEstConfig;
    }

    public CbbHestConfigDTO getLanEstConfig() {
        return lanEstConfig;
    }

    public void setLanEstConfig(CbbHestConfigDTO lanEstConfig) {
        this.lanEstConfig = lanEstConfig;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
