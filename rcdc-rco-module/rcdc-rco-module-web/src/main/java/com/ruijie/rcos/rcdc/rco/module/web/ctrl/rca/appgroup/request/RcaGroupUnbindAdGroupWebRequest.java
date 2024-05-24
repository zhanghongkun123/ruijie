package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Description: 应用池分组移除绑定安全组
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月22日
 *
 * @author zhengjingyong
 */
public class RcaGroupUnbindAdGroupWebRequest {

    /**
     * 应用分组Id
     */
    @NotNull
    private UUID appGroupId;

    /**
     * 用户ID列表
     */
    @NotEmpty
    private List<UUID> adGroupIdList;

    public UUID getAppGroupId() {
        return appGroupId;
    }

    public void setAppGroupId(UUID appGroupId) {
        this.appGroupId = appGroupId;
    }

    public List<UUID> getAdGroupIdList() {
        return adGroupIdList;
    }

    public void setAdGroupIdList(List<UUID> adGroupIdList) {
        this.adGroupIdList = adGroupIdList;
    }
}
