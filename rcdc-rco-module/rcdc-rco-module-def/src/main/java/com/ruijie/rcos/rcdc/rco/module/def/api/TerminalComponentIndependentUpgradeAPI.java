package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbIndependentUpgradeableTerminalListDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description: 组件独立升级业务API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/1
 *
 * @author lyb
 */
public interface TerminalComponentIndependentUpgradeAPI {

    /**
     * 查询符合升级条件的终端（加入了3120终端的定制）
     * 
     * @param request 查询参数
     * @return 查询结果
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<CbbIndependentUpgradeableTerminalListDTO> findUpgradeableTerminals(PageSearchRequest request) throws BusinessException;
}
