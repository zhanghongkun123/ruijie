package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import org.springframework.lang.Nullable;

/**
 * Description: 分级分权-查询用户列表
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/17
 *
 * @author WuShengQiang
 */
public class ListUserWebRequest extends PageQueryServerRequest {

    @Nullable
    private String adminName;

    @Nullable
    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(@Nullable String adminName) {
        this.adminName = adminName;
    }
}
