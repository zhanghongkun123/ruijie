package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.license;

import java.util.UUID;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.DownloadWebRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月9日
 * 
 * @author zouqi
 */
public class BaseDownLoadLicenseFileWebRequest implements DownloadWebRequest {

    @NotNull
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    
    
}
