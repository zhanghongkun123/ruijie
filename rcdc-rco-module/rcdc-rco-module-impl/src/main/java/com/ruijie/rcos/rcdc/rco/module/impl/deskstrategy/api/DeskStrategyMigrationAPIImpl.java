package com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyMigrationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DeskStrategyMigrationDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年4月22日
 *
 * @author chenl
 */
public class DeskStrategyMigrationAPIImpl implements DeskStrategyMigrationAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskStrategyMigrationAPIImpl.class);

    @Override
    public PageQueryResponse<DeskStrategyMigrationDTO> pageQuery(PageQueryRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "Param [pageQueryRequest] must not be null");

        PageQueryResponse<DeskStrategyMigrationDTO> pageQueryResponse = DeskStrategyMigrationAPI.super.pageQuery(pageQueryRequest);
        return pageQueryResponse;
    }

    @Override
    public PageQueryResponse<DeskStrategyMigrationDTO> pageDeskStrategyQuery(PageQueryRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "Param [pageQueryRequest] must not be null");
        PageQueryResponse<DeskStrategyMigrationDTO> pageQueryResponse = DeskStrategyMigrationAPI.super.pageQuery(pageQueryRequest);

        PageQueryResponse<DeskStrategyMigrationDTO> deskStrategyResponse = new PageQueryResponse<>();
        deskStrategyResponse.setTotal(pageQueryResponse.getTotal());

        if (ArrayUtils.isEmpty(pageQueryResponse.getItemArr())) {
            deskStrategyResponse.setItemArr(new DeskStrategyMigrationDTO[] {});
            return deskStrategyResponse;
        }
        deskStrategyResponse.setItemArr(pageQueryResponse.getItemArr());
        return deskStrategyResponse;
    }

}
