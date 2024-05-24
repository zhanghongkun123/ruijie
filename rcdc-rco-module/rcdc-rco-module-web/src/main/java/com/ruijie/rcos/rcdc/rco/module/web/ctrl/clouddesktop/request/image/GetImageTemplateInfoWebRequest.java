package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import java.util.UUID;

/**
 * Description: GetImageTemplateInfoWebRequest Description
 * Copyright: Copyright (c) 2017
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/05/06
 *
 * @author chixin
 */
public class GetImageTemplateInfoWebRequest implements WebRequest {

    @NotNull
    private UUID id;

    public GetImageTemplateInfoWebRequest() {
    }

    public GetImageTemplateInfoWebRequest(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
