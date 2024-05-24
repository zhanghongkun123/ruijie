package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.impl.dto.VncConnectionInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: VNC代理管理API
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/10/16 17:08
 *
 * @author zhangyichi
 */
public interface VncProxyService {

    /**
     * 添加VNC配置文件，目标虚机在云平台上
     * @param businessId 业务ID
     * @return 目标虚机的VNC连接参数
     * @throws BusinessException 业务异常
     */
    VncConnectionInfoDTO addVncConfig(UUID businessId) throws BusinessException;

    /**
     * 删除VNC配置文件
     * @param businessId 业务ID
     */
    void removeVncConfig(UUID businessId);
}
