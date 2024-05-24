package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 处理云应用策略水印通知
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年9月2日
 *
 * @author XiaoJiaXin
 */
public interface RcaMainStrategyWatermarkAPI {

    /**
     * 处理云应用策略水印通知
     * @param rcaMainStrategyDTO 云应用策略
     * @throws BusinessException 业务异常
     */
    void handleNotifyWatermarkConfig(RcaMainStrategyDTO rcaMainStrategyDTO) throws BusinessException;

    /**
     * 处理云应用策略水印通知
     * @param rcaHostDTO 云应用主机信息
     * @param multiSessionId 云应用主机多会话id
     * @param rcaMainStrategyDTO 云应用策略
     * @throws BusinessException 业务异常
     */
    void handleNotifyWatermarkConfig(RcaHostDTO rcaHostDTO, @Nullable UUID multiSessionId, RcaMainStrategyDTO rcaMainStrategyDTO) throws BusinessException;
}
