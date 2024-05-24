package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

/**
 * Description: 返回给SHINE评测策略DTO类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/4 23:16
 *
 * @author yxq
 */
public class ShineEvaluationDTO {
    /**
     * 是否开启评测功能
     */
    private Boolean enableEvaluation;

    /**
     * 时间戳
     */
    private Long timestamp;

    public ShineEvaluationDTO() {
    }

    public ShineEvaluationDTO(Boolean enableEvaluation, Long timestamp) {
        this.enableEvaluation = enableEvaluation;
        this.timestamp = timestamp;
    }

    public Boolean getEnableEvaluation() {
        return enableEvaluation;
    }

    public void setEnableEvaluation(Boolean enableEvaluation) {
        this.enableEvaluation = enableEvaluation;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}