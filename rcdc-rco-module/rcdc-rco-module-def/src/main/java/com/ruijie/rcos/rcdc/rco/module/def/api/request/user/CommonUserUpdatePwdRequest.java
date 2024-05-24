package com.ruijie.rcos.rcdc.rco.module.def.api.request.user;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ApiCallerTypeEnum;
import org.springframework.lang.Nullable;

/**
 * Description: 通用用户修改密码请求，以区分内部调用还是外部调用（使用密钥不同）
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/4 14:07
 *
 * @author zdc
 */
public class CommonUserUpdatePwdRequest {

    /**
     * 是否外部调用
     */
    @Nullable
    private ApiCallerTypeEnum apiCallerTypeEnum = ApiCallerTypeEnum.INNER;

    @Nullable
    public ApiCallerTypeEnum getApiCallerTypeEnum() {
        return apiCallerTypeEnum;
    }

    public void setApiCallerTypeEnum(@Nullable ApiCallerTypeEnum apiCallerTypeEnum) {
        this.apiCallerTypeEnum = apiCallerTypeEnum;
    }
}
