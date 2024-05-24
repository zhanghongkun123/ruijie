package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desksoft;

import java.util.UUID;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: 获取安装包详情
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月25日
 * 
 * @author Ghang
 */
public class GetDeskSoftWebRequest implements WebRequest {

    @NotNull
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
