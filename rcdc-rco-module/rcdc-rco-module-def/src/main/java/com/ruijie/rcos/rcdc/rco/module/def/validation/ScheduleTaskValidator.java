package com.ruijie.rcos.rcdc.rco.module.def.validation;

import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年08月26日
 * 
 * @param <T> 校验参数类型
 * @author xgx
 */
public interface ScheduleTaskValidator<T> {
    /**
     * 校验定时任务配置参数
     * 
     * @param entity 实体
     * @throws BusinessException 业务异常
     * @throws AnnotationValidationException 参数检验异常
     */
    void validate(T entity) throws BusinessException, AnnotationValidationException;


}
