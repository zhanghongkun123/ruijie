package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;

import com.ruijie.rcos.rcdc.rco.module.def.terminaldriver.response.TerminalDriverConfigResponse;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年02月02日
 *
 * @author luojianmo
 */

public interface TerminalDriverConfigAPI {

    /**
     * 根据终端产品ID获取终端驱动配置信息
     *
     * @param productId 产品ID
     * @return 终端驱动配置信息
     * 
     */
    TerminalDriverConfigResponse findTerminalDriverConfigByProductId(String productId);

    /**
     * 获取所有终端驱动配置信息
     * 
     * @return 响应
     */
    List<TerminalDriverConfigResponse> findAllTerminalDriverConfig();
}
