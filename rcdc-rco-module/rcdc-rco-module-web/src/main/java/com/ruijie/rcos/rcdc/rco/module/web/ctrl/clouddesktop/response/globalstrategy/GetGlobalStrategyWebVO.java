package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.ProtocolTransferConfigDTO;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月29日
 * 
 * @author Ghang
 */
public class GetGlobalStrategyWebVO {

    private ProtocolTransferConfigDTO protocolTransferConfig;

    /**
     * 隐藏应用分层
     */
    private Boolean appLayerHidden;

    public ProtocolTransferConfigDTO getProtocolTransferConfig() {
        return protocolTransferConfig;
    }

    public void setProtocolTransferConfig(ProtocolTransferConfigDTO protocolTransferConfig) {
        this.protocolTransferConfig = protocolTransferConfig;
    }

    public Boolean getAppLayerHidden() {
        return appLayerHidden;
    }

    public void setAppLayerHidden(Boolean appLayerHidden) {
        this.appLayerHidden = appLayerHidden;
    }
}
