package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

/**
 * Description: 评测功能DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/6 19:13
 *
 * @author yxq
 */
public class EvaluationStrategyDTO {
    /**
     * 是否开启评测功能
     */
    private Boolean enableEvaluation;

    public Boolean getEnableEvaluation() {
        return enableEvaluation;
    }

    public void setEnableEvaluation(Boolean enableEvaluation) {
        this.enableEvaluation = enableEvaluation;
    }

    public EvaluationStrategyDTO() {
    }

    public EvaluationStrategyDTO(Boolean enableEvaluation) {
        this.enableEvaluation = enableEvaluation;
    }
}
