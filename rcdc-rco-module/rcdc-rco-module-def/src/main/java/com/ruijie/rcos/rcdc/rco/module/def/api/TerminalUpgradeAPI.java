package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbUpgradeableTerminalListDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/15 11:43
 *
 * @author ketb
 */
public interface TerminalUpgradeAPI {

    /**
     * 查询符合升级条件的终端（加入了3120终端的定制）
     * @param request 查询参数
     * @return 查询结果
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<CbbUpgradeableTerminalListDTO> findUpgradeableTerminals(PageSearchRequest request) throws BusinessException;
}
