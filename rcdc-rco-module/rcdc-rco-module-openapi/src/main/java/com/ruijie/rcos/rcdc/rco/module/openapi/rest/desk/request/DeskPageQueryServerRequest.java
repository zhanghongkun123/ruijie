package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.QueryPlatformTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import org.springframework.lang.Nullable;

/**
 * Description: 分级分权-搜索云桌面列表
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/17
 *
 * @author WuShengQiang
 */
public class DeskPageQueryServerRequest extends PageQueryServerRequest {

    @Nullable
    private String adminName;

    @Nullable
    private QueryPlatformTypeEnum platformType;

    @Nullable
    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(@Nullable String adminName) {
        this.adminName = adminName;
    }

    @Nullable
    public QueryPlatformTypeEnum getPlatformType() {
        return platformType;
    }

    public void setPlatformType(@Nullable QueryPlatformTypeEnum platformType) {
        this.platformType = platformType;
    }
}
