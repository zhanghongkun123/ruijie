package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AssistantMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AssistantRemoteResponseDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: GT消息服务
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/30 15:35
 *
 * @author ketb
 */
public interface AssistantMessageService {

    /**
     * 发送同步消息
     * @param messageDTO 消息内容
     * @return 响应消息
     * @throws BusinessException 业务异常
     */
    AssistantRemoteResponseDTO syncRequest(AssistantMessageDTO messageDTO) throws BusinessException;

    /**
     * 发送异步消息
     * @param messageDTO 消息内容
     * @throws BusinessException 业务异常
     */
    void asyncRequest(AssistantMessageDTO messageDTO) throws BusinessException;

    /**
     * 广播消息
     * @param messageDTO 消息内容
     * @throws BusinessException 业务异常
     */
    void broadcastRequest(AssistantMessageDTO messageDTO) throws BusinessException;
}
