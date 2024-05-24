package com.ruijie.rcos.rcdc.rco.module.impl.openapi.api;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ClusterMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.StoragePoolMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.PageQueryNullableRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.cluster.RccpClusterResourceResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.storagepool.StoragePoolClustersSummaryResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiRccpManageAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.data.base.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/25
 *
 * @author lyb
 */
public class OpenApiRccpManageAPIImpl implements OpenApiRccpManageAPI {

    @Autowired
    private ClusterMgmtAPI clusterMgmtAPI;

    @Autowired
    private StoragePoolMgmtAPI storagePoolMgmtAPI;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Override
    public PageResponse<RccpClusterResourceResponse> computeClustersSummary(PageQueryNullableRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        return clusterMgmtAPI.getClusterSummaryResource(request);
    }

    @Override
    public PageResponse<StoragePoolClustersSummaryResponse> storageClustersSummary(PageQueryNullableRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        if (Objects.isNull(request.getPlatformId())) {
            CloudPlatformDTO cloudPlatform = cloudPlatformManageAPI.getDefaultCloudPlatform();
            request.setPlatformId(cloudPlatform.getId());
            request.setPlatformType(cloudPlatform.getType());
        }
        return storagePoolMgmtAPI.storageClustersSummary(request);
    }
}
