package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbadvancedsetting;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年5月17日
 * 
 * @author zhuangchenwu
 */
public class CreateAutoRedirectConfigWebRequest implements WebRequest {

    @NotBlank
    @Size(max = 4)
    private String firmFlag;
    
    @NotBlank
    @Size(max = 4)
    private String productFlag;

    public String getFirmFlag() {
        return firmFlag;
    }

    public void setFirmFlag(String firmFlag) {
        this.firmFlag = firmFlag;
    }

    public String getProductFlag() {
        return productFlag;
    }

    public void setProductFlag(String productFlag) {
        this.productFlag = productFlag;
    }
    
}
