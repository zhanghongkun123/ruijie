package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desksoft;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.DownloadWebRequest;

import java.util.UUID;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/05/08 22:48
 *
 * @author liuwang1
 */

public class DownloadDeskSoftRequest implements DownloadWebRequest {

    @NotNull
    private UUID id;



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
