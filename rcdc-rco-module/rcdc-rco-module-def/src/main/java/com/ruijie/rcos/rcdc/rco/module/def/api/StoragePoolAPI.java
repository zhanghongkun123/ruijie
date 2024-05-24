package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description: 存储池操作API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/19
 *
 * @author TD
 */
public interface StoragePoolAPI {
    
    /**
     * 查询全部的存储池
     * @return 存储池集合
     * @throws BusinessException 业务异常
     */
    List<PlatformStoragePoolDTO> queryAllStoragePool() throws BusinessException;

    /**
     *  查询指定存储池信息
     * @param storagePoolId 存储池ID
     * @return 存储池信息
     */
    PlatformStoragePoolDTO getStoragePoolDetail(UUID storagePoolId);

    /**
     * 获取存储池Id
     * 
     * @param platformId 云平台ID
     * @param storagePoolName 存储池名称
     * @return 存储池Id
     */
    UUID queryStoragePoolIdByName(UUID platformId, String storagePoolName);
}
