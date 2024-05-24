package com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit;

import java.util.UUID;

/**
 * Description: VDI终端编辑镜像，带会话ID的请求消息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/20 16:34
 *
 * @author zhangyichi
 */
public class VDIEditImageSessionRequestDTO {

    private UUID adminSessionId;

    public UUID getAdminSessionId() {
        return adminSessionId;
    }

    public void setAdminSessionId(UUID adminSessionId) {
        this.adminSessionId = adminSessionId;
    }
}
