package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rca.module.def.api.request.RcaCreateCloudDesktopRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: 创建派生云主机桌面信息
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月17日
 *
 * @author liuwc
 */
public interface CreateRcaHostDesktopAPI {

    /**
     ** 创建云桌面
     *
     * @param desktopId 桌面id
     * @param rcaCreateCloudDesktopRequest 新增云桌面 request
     * @throws BusinessException 业务异常
     */
    void create(UUID desktopId, RcaCreateCloudDesktopRequest rcaCreateCloudDesktopRequest) throws BusinessException;
}
