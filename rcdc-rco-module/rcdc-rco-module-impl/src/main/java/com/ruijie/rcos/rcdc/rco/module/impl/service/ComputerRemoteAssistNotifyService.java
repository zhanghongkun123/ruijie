package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistHeartbeatNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistReportStateNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistUserCloseNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistUserConfirmNotifyReqeust;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * 远程协助通知接口定义
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月2日
 * 
 * @author ketb
 */
public interface ComputerRemoteAssistNotifyService {

    /**
     * 用户确认/拒绝远程协助通知
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */
    void remoteAssistUserConfirm(RemoteAssistUserConfirmNotifyReqeust request) throws BusinessException;
    
    /**
     * 用户关闭远程协助通知
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */
    void remoteAssistUserClose(RemoteAssistUserCloseNotifyRequest request) throws BusinessException;
    
    /**
     * 远程协助心跳消息通知
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */
    void remoteAssistHeartbeat(RemoteAssistHeartbeatNotifyRequest request) throws BusinessException;

    /**
     * 上报远程协助状态
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */
    void remoteAssistReportState(RemoteAssistReportStateNotifyRequest request) throws BusinessException;
    
}
