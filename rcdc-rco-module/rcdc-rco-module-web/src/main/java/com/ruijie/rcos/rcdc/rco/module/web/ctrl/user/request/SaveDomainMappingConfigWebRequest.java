package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;


import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdDataTreeNodeDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 保存AD域映射配置
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-04-09 15:35
 *
 * @author zql
 */
public class SaveDomainMappingConfigWebRequest extends SaveDomainConfigWebRequest {

    public SaveDomainMappingConfigWebRequest() {

    }

    public SaveDomainMappingConfigWebRequest(@Nullable IacAdDataTreeNodeDTO[] adDataTreeNodeArr) {
        this.adDataTreeNodeArr = adDataTreeNodeArr;
    }

    @Nullable
    private IacAdDataTreeNodeDTO[] adDataTreeNodeArr;

    @NotNull
    private Boolean enableSyncUser;

    @Nullable
    public IacAdDataTreeNodeDTO[] getAdDataTreeNodeArr() {
        return adDataTreeNodeArr;
    }

    public void setAdDataTreeNodeArr(@Nullable IacAdDataTreeNodeDTO[] adDataTreeNodeArr) {
        this.adDataTreeNodeArr = adDataTreeNodeArr;
    }

    public Boolean getEnableSyncUser() {
        return enableSyncUser;
    }

    public void setEnableSyncUser(Boolean enableSyncUser) {
        this.enableSyncUser = enableSyncUser;
    }
}
