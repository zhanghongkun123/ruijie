package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.SystemTimeResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;


/**
 * Description: 大屏监控云平台信息API
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月10日
 *
 * @author BaiGuoliang
 */
public interface PlatformAPI {

    /**
     * 获取系统时间信息
     *
     * @return 响应
     * @throws BusinessException 异常
     */
    
    SystemTimeResponse getSystemTime() throws BusinessException;

}
