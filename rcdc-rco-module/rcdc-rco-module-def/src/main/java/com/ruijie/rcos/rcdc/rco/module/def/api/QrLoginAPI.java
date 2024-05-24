package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.UUID;

/**
 * Description: QrLoginComponentAPI
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年5月6日
 *
 * @author zhang.zhiwen
 */
public interface QrLoginAPI {

    /**
     * 缓存代理生成的UUID
     * 
     * @param id UUID
     * @param phone 号码（扫码用户账号）
     */
    void saveQrLoginId(UUID id, String phone);
    
}
