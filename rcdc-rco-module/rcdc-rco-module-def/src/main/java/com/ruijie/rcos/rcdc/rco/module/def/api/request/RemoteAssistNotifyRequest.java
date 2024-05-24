package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RemoteAssistNotifyContentDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年1月7日
 * 
 * @author wjp
 */
public class RemoteAssistNotifyRequest implements Request {

    @NotNull
    private RemoteAssistNotifyContentDTO[] remoteAssistNotifyContentDTOArr;

    public RemoteAssistNotifyContentDTO[] getRemoteAssistNotifyContentDTOArr() {
        return remoteAssistNotifyContentDTOArr;
    }

    public void setRemoteAssistNotifyContentDTOArr(RemoteAssistNotifyContentDTO[] remoteAssistNotifyContentDTOArr) {
        this.remoteAssistNotifyContentDTOArr = remoteAssistNotifyContentDTOArr;
    }
}
