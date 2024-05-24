package com.ruijie.rcos.rcdc.rco.module.impl.service;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.impl.dto.CheckTerminalWhiteDTO;

/**
 * Description: 白名单授权终端相关服务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/31
 *
 * @author zhk
 */
public interface AuthorizationTerminalWhiteSnService {


    /**
     * 记录非法白名单终端接入告警日志
     * 
     * @param terminalId 终端id
     * @param terminalMac 终端mac
     * @param productType 终端型号
     * @param sn 终端sn
     */
    void saveIllegalTerminalWarnLog(String terminalId, String terminalMac, String productType, @Nullable String sn);
}
