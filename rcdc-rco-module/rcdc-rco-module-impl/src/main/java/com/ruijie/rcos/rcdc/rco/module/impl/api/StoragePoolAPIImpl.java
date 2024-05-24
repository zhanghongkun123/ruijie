package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.StoragePoolServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.StoragePoolAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Description: 存储池操作实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/19
 *
 * @author TD
 */
public class StoragePoolAPIImpl implements StoragePoolAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoragePoolAPIImpl.class);

    /** 单次处理记录数 */
    private static final int SINGLE_TIME_HANDLE_NUMBER = 1000;
    
    @Autowired
    private StoragePoolServerMgmtAPI storagePoolServerMgmtAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Override
    public List<PlatformStoragePoolDTO> queryAllStoragePool() throws BusinessException {
        List<PlatformStoragePoolDTO> storagePoolList = Lists.newArrayList();
        int page = 0;
        while (true) {
            PageQueryRequest request = pageQueryBuilderFactory.newRequestBuilder().setPageLimit(page++, SINGLE_TIME_HANDLE_NUMBER).build();
            PageQueryResponse<PlatformStoragePoolDTO> response = storagePoolServerMgmtAPI.pageQuery(request);
            PlatformStoragePoolDTO[] storagePoolItemArr = response.getItemArr();
            if (ArrayUtils.isEmpty(storagePoolItemArr)) {
                LOGGER.debug("从CCP查询第[{}]页数据为空，无需继续查询", page);
                break;
            }
            LOGGER.debug("从CCP查询第[{}]页数据数量为[{}]", page, storagePoolItemArr.length);
            storagePoolList.addAll(new ArrayList<>(Arrays.asList(storagePoolItemArr)));
        }
        return storagePoolList;
    }

    @Override
    public PlatformStoragePoolDTO getStoragePoolDetail(UUID storagePoolId) {
        Assert.notNull(storagePoolId, "storagePoolId can be not null");
        PlatformStoragePoolDTO storagePoolDTO = new PlatformStoragePoolDTO();
        try {
            BeanUtils.copyProperties(storagePoolServerMgmtAPI.getPlatformStoragePoolDetail(storagePoolId), storagePoolDTO);
        } catch (BusinessException e) {
            LOGGER.error("查询存储池[{}]出错", storagePoolId, e);
        }
        return storagePoolDTO;
    }

    @Override
    public UUID queryStoragePoolIdByName(UUID platformId, String storagePoolName) {
        Assert.hasText(storagePoolName, "storagePoolName is not empty");
        Assert.notNull(platformId, "platformId can be not null");

        PlatformStoragePoolDTO cbbStoragePoolDTO = storagePoolServerMgmtAPI.getStoragePoolByName(platformId, storagePoolName);
        //获取该存储池Id
        return Objects.isNull(cbbStoragePoolDTO) ? null : cbbStoragePoolDTO.getId();
    }
}
