package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalSimplifyDeploymentConfigDTO;

/**
 * Description: 终端极简部署API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/3 14:08
 *
 * @author linrenjian
 */
public interface TerminalSimplifyDeploymentConfigAPI {
    /**
     * 修改策略，并且将新的策略同步给在线终端
     *
     * @param terminalSimplifyDeploymentConfigDTO 修改终端极简部署DTO
     */
    void modifyTerminalSimplifyDeploymentConfig(TerminalSimplifyDeploymentConfigDTO terminalSimplifyDeploymentConfigDTO);

    /**
     * 获取终端极简部署开关
     * 
     * @return 是否开启终端极简部署开关
     */
    TerminalSimplifyDeploymentConfigDTO getTerminalSimplifyDeploymentConfig();
}
