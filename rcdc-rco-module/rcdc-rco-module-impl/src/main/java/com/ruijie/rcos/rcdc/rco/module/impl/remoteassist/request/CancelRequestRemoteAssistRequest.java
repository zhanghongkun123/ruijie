package com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.request;

import com.ruijie.rcos.rcdc.rco.module.impl.enums.CancelRequestRemoteAssistSrcTypeEnum;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/4 20:25
 *
 * @author ketb
 */
public class CancelRequestRemoteAssistRequest {

    private CancelRequestRemoteAssistSrcTypeEnum srcType;

    public CancelRequestRemoteAssistRequest(CancelRequestRemoteAssistSrcTypeEnum srcType) {
        this.srcType = srcType;
    }

    public CancelRequestRemoteAssistSrcTypeEnum getSrcType() {
        return srcType;
    }

    public void setSrcType(CancelRequestRemoteAssistSrcTypeEnum srcType) {
        this.srcType = srcType;
    }
}
