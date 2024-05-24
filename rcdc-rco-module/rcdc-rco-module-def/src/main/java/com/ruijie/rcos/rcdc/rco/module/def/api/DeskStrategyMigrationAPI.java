package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DeskStrategyMigrationDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryAPI;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年4月22日
 *
 * @author chenl
 */
public interface DeskStrategyMigrationAPI extends PageQueryAPI<DeskStrategyMigrationDTO> {

    /**
     * 分页查询云桌面策略
     *
     * @param pageQueryRequest 分页查询参数
     * @return 分页查询结果PageQueryResponse<DeskStrategyDTO>
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<DeskStrategyMigrationDTO> pageDeskStrategyQuery(PageQueryRequest pageQueryRequest) throws BusinessException;

}
