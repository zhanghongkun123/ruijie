package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.EvaluationStrategyDTO;

/**
 * Description: 评测功能API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/3 14:08
 *
 * @author yxq
 */
public interface EvaluationAPI {
    /**
     * 修改策略，并且将新的策略同步给在线终端
     *
     * @param evaluationStrategyDTO 修改评测功能策略DTO
     */
    void modifyEvaluationAndSyncTerminal(EvaluationStrategyDTO evaluationStrategyDTO);

    /**
     * 获取评测策略信息
     * 
     * @return 是否开启评测策略
     */
    Boolean getEvaluationStrategy();
}
