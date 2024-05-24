package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.EvaluationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.EvaluationStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 评测功能API实现
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/3 14:20
 *
 * @author yxq
 */
public class EvaluationAPIImpl implements EvaluationAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationAPIImpl.class);

    @Autowired
    private GlobalParameterService globalParameterService;

    @Override
    public void modifyEvaluationAndSyncTerminal(EvaluationStrategyDTO evaluationStrategyDTO) {
        Assert.notNull(evaluationStrategyDTO, "evaluationStrategyDTO must not null");

        Boolean enableEvaluation = evaluationStrategyDTO.getEnableEvaluation();
        if (enableEvaluation == null) {
            LOGGER.info("前端未传递是否开启评测功能，无需修改全局配置表和通知在线终端");
            return;
        }

        // 修改策略
        String evaluationStrategy = JSON.toJSONString(enableEvaluation);
        LOGGER.info("要修改的评测功能策略为：[{}]", evaluationStrategy);
        globalParameterService.updateParameter(Constants.EVALUATION_STRATEGY, evaluationStrategy);
    }

    @Override
    public Boolean getEvaluationStrategy() {
        String parameter = globalParameterService.findParameter(Constants.EVALUATION_STRATEGY);
        LOGGER.info("查询全局表中评测策略为：{}", parameter);

        return Boolean.valueOf(parameter);
    }
}
