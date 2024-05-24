package com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.dto;


import java.util.UUID;

/**
 * Description: 远程协助运行状态
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/16
 *
 * @author chenl
 */
public class RemoteAssistBaseRequestDTO {

    private UUID deskId;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }
}
