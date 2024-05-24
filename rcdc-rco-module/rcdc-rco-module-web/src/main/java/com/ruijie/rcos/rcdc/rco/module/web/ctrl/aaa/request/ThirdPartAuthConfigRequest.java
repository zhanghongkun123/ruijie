package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request;

import com.ruijie.rcos.gss.base.iac.module.enums.AssistAuthTypeEnum;
import com.ruijie.rcos.gss.base.iac.module.enums.MainAuthTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.enums.AuthType;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 获取认证方式详细配置请求
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-09 15:35
 *
 * @author wanglianyun
 */
public class ThirdPartAuthConfigRequest {
    /**
     * 授权类型
     */
    @NotNull
    private MainAuthTypeEnum authType;

    public MainAuthTypeEnum getAuthType() {
        return authType;
    }

    public void setAuthType(MainAuthTypeEnum authType) {
        this.authType = authType;
    }
}
