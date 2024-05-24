package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.ProtocolTransferConfigDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: 配置全局策略请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月1日
 * 
 * @author Ghang
 */
public class ConfigureGlobalStrategyWebRequest implements WebRequest {

    @NotNull
    private ProtocolTransferConfigDTO protocolTransferConfig;

    public ProtocolTransferConfigDTO getProtocolTransferConfig() {
        return protocolTransferConfig;
    }

    public void setProtocolTransferConfig(ProtocolTransferConfigDTO protocolTransferConfig) {
        this.protocolTransferConfig = protocolTransferConfig;
    }

}
