package com.ruijie.rcos.rcdc.rco.module.impl.timedtasks.handler;

import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.dto.CronConvertDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;

/**
 * Description: Cron表达式转换 处理器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年11月01日
 *
 * @author luojianmo
 * @param <T>
 */
public interface CronExpressionConvert<T> {


    /**
     * 生成Cron表达式
     *
     * @param entity 实体
     * @return Cron表达式
     */
    String generateExpression(T entity);

    /**
     * Cron表达式转 RcoScheduleTaskDTO
     *
     * @param cronList 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    CronConvertDTO parseCronExpression(List<String> cronList) throws BusinessException;

}
