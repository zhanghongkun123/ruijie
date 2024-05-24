package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/6
 *
 * @author songxiang
 */
public interface DeskStrategyTciNotifyAPI {

    /**
     * 向在线的TCI公共终端发送获取启动参数的命令
     * 
     * @param strategyId 变更的策略ID
     */
    void notifyFetchStartParams(UUID strategyId);

    /**
     * 向所有在线的TCI公共终端发送获取启动参数的命令
     */
    void notifyFetchStartParams();


    /**
     * 桌面ID
     * @param deskId 桌面id
     */
    void notifyDeskFetchStartParams(UUID deskId);

    /**
     * 通知TCI公用终端编辑计算机名
     * @param deskId  桌面Id
     * @param computerName 计算机名
     */
    void notifyDeskEditComputerName(UUID deskId, String computerName);
}
