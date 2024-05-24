package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.PageQueryNullableRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.cluster.RccpClusterResourceResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.storagepool.StoragePoolClustersSummaryResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.data.base.PageResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/25
 *
 * @author lyb
 */
public interface OpenApiRccpManageAPI {

    /**
     * 获取计算集群资源统计列表
     *
     * @param request 分页参数
     * @return 结果
     * @throws BusinessException 异常
     */
    PageResponse<RccpClusterResourceResponse> computeClustersSummary(PageQueryNullableRequest request) throws BusinessException;

    /**
     * 存储集群容量统计
     *
     * @param request 分页参数
     * @return 结果
     * @throws BusinessException 异常
     */
    PageResponse<StoragePoolClustersSummaryResponse> storageClustersSummary(PageQueryNullableRequest request) throws BusinessException;
}
