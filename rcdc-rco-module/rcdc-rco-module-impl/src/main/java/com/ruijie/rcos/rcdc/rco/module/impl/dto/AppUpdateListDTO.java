package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import java.io.Serializable;

/**
 * 
 * Description: windows软终端组件升级版本信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月14日
 * 
 * @author nt
 */
public class AppUpdateListDTO extends EqualsHashcodeSupport implements Serializable {

    private String name;

    private String completePackageName;

    private String platform;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompletePackageName() {
        return completePackageName;
    }

    public void setCompletePackageName(String completePackageName) {
        this.completePackageName = completePackageName;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
