package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 创建用户提交的身份验证数据对象
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/6
 *
 * @author lintingling
 */
public class SyncUserIdentityConfigRequest extends IacUserIdentityConfigRequest {


    /**
     * rccm 集群id
     */
    @Nullable
    private UUID clusterId;

    /**
     * 用户名
     */
    @Nullable
    private String userName;


    @Nullable
    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(@Nullable UUID clusterId) {
        this.clusterId = clusterId;
    }

    @Nullable
    public String getUserName() {
        return userName;
    }

    public void setUserName(@Nullable String userName) {
        this.userName = userName;
    }
}
