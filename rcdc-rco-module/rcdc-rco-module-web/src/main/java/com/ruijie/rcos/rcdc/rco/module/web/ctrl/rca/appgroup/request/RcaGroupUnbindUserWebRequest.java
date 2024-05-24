package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Description: 应用池分组移除绑定用户
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月22日
 *
 * @author zhengjingyong
 */
public class RcaGroupUnbindUserWebRequest {

    /**
     * 应用分组Id
     */
    @NotNull
    private UUID appGroupId;

    /**
     * 用户ID列表
     */
    @NotEmpty
    private List<UUID> userIdList;

    public UUID getAppGroupId() {
        return appGroupId;
    }

    public void setAppGroupId(UUID appGroupId) {
        this.appGroupId = appGroupId;
    }

    public List<UUID> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<UUID> userIdList) {
        this.userIdList = userIdList;
    }
}
