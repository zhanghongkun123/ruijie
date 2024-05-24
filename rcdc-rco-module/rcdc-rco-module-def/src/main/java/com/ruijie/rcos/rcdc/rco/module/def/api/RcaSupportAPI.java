package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.AppTerminalDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: RCA应用虚拟化支持API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/22 10:00
 *
 * @author zhangyichi
 */
public interface RcaSupportAPI {

    /**
     * 获取应用客户端详情
     * @param appTerminalId 应用客户端ID
     * @return 应用客户端详情
     * @throws BusinessException 业务异常
     */
    AppTerminalDTO getAppTerminalDetail(Integer appTerminalId) throws BusinessException;

    /**
     * 更新虚拟应用的启用状态
     *
     * @param enable 虚拟应用的启动状态
     * @throws BusinessException 业务异常
     */
    void modifyVirtualApplicationState(Boolean enable) throws BusinessException;

    /**
     * 获取虚拟应用的启用状态
     *
     * @return 虚拟应用的启动状态
     * @throws BusinessException 业务异常
     */
    Boolean getVirtualApplicationState();
}
