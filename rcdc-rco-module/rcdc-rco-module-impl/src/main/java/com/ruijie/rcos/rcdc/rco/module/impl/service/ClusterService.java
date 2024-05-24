package com.ruijie.rcos.rcdc.rco.module.impl.service;


import com.ruijie.rcos.rcdc.rco.module.impl.service.vo.DefaultClusterInfo;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: 集群管理接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
public interface ClusterService {

    /**
     * 获取默认集群信息
     * @return 默认集群信息
     * @throws BusinessException BusinessException
     */
    DefaultClusterInfo getDefaultClusterInfo() throws BusinessException;
}
