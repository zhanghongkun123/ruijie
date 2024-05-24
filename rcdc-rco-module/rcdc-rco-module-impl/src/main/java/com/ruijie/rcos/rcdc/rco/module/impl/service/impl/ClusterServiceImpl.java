package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ComputerClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.StoragePoolServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformComputerClusterDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ClusterService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.vo.DefaultClusterInfo;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/26
 *
 * @author zhiweihong
 */
@Service
public class ClusterServiceImpl implements ClusterService {


    @Autowired
    private ComputerClusterServerMgmtAPI computerClusterAPI;


    @Autowired
    private StoragePoolServerMgmtAPI storagePoolMgmtAPI;


    /**
     * 默认存储池Id
     */
    private static final UUID DEFAULT_STORAGE_POOL_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Override
    public DefaultClusterInfo getDefaultClusterInfo() throws BusinessException {
        PlatformComputerClusterDTO computeCluster = computerClusterAPI.getDefaultComputeCluster();
        UUID storagePoolId = storagePoolMgmtAPI.getStoragePoolByComputeClusterId(computeCluster.getId())
                .stream().findFirst().map(PlatformStoragePoolDTO::getId).orElse(DEFAULT_STORAGE_POOL_ID);
        return new DefaultClusterInfo(computeCluster.getId(), storagePoolId);
    }
}
