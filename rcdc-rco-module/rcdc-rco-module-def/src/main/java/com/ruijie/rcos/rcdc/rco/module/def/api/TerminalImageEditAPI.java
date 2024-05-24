package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 终端镜像编辑API
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/28
 *
 * @author songxiang
 * @reviser zhiweiHong
 */
public interface TerminalImageEditAPI {

    /**
     * 检测是否在线
     * 
     * @param terminalId 终端ID
     * @throws BusinessException 业务异常
     */
    void checkTerminalOnline(String terminalId) throws BusinessException;

    /**
     * 检测是否为有线连接，且检测终端是否在线
     * 
     * @param terminalId 终端ID
     * @throws BusinessException <p>无法查询到对应终端时</p>
     *         <p>非在线终端抛出异常</p>
     *         <p>非有线连接时抛出异常</p>
     */
    void checkTerminalOnlineAndWired(String terminalId) throws BusinessException;
}
