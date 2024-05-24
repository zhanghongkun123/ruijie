package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7 13:55
 *
 * @author chenl
 */
public class SoftwareStrategyRelatedWebRequest extends PageWebRequest {

    /**
     * 软件清单
     */
    @NotNull
    private UUID softwareId;

    public UUID getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(UUID softwareId) {
        this.softwareId = softwareId;
    }
}

