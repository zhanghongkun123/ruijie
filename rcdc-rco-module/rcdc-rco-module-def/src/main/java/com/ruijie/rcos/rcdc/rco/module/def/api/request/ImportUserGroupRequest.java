package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

/**
 * 
 * Description: 用户组导入请求参数
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/23
 * 
 * @author zhangyichi
 */
public class ImportUserGroupRequest implements Request {

    @NotBlank
    private String groupNames;

    @Nullable
    private String imageTemplateName;

    @Nullable
    private String strategyName;

    @Nullable
    private String networkName;

    @Nullable
    private String idvImageTemplateName;

    @Nullable
    private String idvStrategyName;

    public String getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String groupNames) {
        this.groupNames = groupNames;
    }

    @Nullable
    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(@Nullable String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
    }

    @Nullable
    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(@Nullable String strategyName) {
        this.strategyName = strategyName;
    }

    @Nullable
    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(@Nullable String networkName) {
        this.networkName = networkName;
    }

    @Nullable
    public String getIdvImageTemplateName() {
        return idvImageTemplateName;
    }

    public void setIdvImageTemplateName(@Nullable String idvImageTemplateName) {
        this.idvImageTemplateName = idvImageTemplateName;
    }

    @Nullable
    public String getIdvStrategyName() {
        return idvStrategyName;
    }

    public void setIdvStrategyName(@Nullable String idvStrategyName) {
        this.idvStrategyName = idvStrategyName;
    }
}

