package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.cloudplatform;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月15日
 * 
 * @author zhuangchenwu
 */
public class UpdateCloudPlatformIpWebRequest implements WebRequest {
    
    @NotBlank
    private String cloudPlatformIp;

    public String getCloudPlatformIp() {
        return cloudPlatformIp;
    }

    public void setCloudPlatformIp(String cloudPlatformIp) {
        this.cloudPlatformIp = cloudPlatformIp;
    }

}
