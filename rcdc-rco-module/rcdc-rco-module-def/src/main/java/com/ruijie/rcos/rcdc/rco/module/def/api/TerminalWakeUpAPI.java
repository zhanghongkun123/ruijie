package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * 终端唤醒API
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年4月17日
 *
 * @author zhengjingyong
 */
public interface TerminalWakeUpAPI {

    /**
     * 唤醒终端
     *
     * @param terminalId 终端id
     * @throws BusinessException 业务异常
     */
    void wakeUpTerminal(String terminalId) throws BusinessException;

    /**
     * 唤醒终端
     *
     * @param terminalId 终端id
     * @param enableParallelWake 是否支持同时唤醒
     * @throws BusinessException 业务异常
     */
    void wakeUpTerminal(String terminalId, Boolean enableParallelWake) throws BusinessException;
}
