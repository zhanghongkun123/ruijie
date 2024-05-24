package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: 创建局部显示策略
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月1日
 * 
 * @author Ghang
 */
public class CreatePartDisplayStrategyWebRequest implements WebRequest {
    
    @NotBlank
    @TextShort
    @TextName
    private String appName;
    
    @NotBlank
    private String displayStrategy;
    
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDisplayStrategy() {
        return displayStrategy;
    }

    public void setDisplayStrategy(String displayStrategy) {
        this.displayStrategy = displayStrategy;
    }
    
}
