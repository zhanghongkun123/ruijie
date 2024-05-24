package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.license;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: 检查命名唯一性
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月19日
 * 
 * @author zouqi
 */
public class CheckDuplicationWebRequest implements WebRequest {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
