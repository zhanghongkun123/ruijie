package com.ruijie.rcos.rcdc.rco.module.impl.service;

import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月27日
 *
 * @author zdc
 */
public interface RcoIacPermissionService {

    /**
     * 通知IAC全局开关配置
     * @param key key
     */
    void notifyAllMenuGlobalEnable(@Nullable String key);
}
