package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.CloudPlatformBaseRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.NtpResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-04-26 17:30
 *
 * @author wanglianyun
 */

public interface NtpMgmtAPI {

    /**
     * 获取ntp信息
     * @return 结果返回
     * @throws BusinessException 业务异常
     */
    NtpResponse getNtpServer() throws BusinessException;

}
