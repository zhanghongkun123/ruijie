package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopRemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: 远程协助接口
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/6 11:34
 *
 * @author ketb
 */
public interface ComputerRemoteAssistMgmtAPI {

    /**
     ** 向用户发起远程协助询问
     *
     * @param request 页面请求参数：uuid
     * @throws BusinessException 业务异常
     * @return reposne 执行状态：成功、失败（消息）
     */
    DefaultResponse remoteAssistInquire(RemoteAssistRequest request) throws BusinessException;

    /**
     ** 取消远程协助
     *
     * @param request request
     * @return DefaultResponse
     * @throws BusinessException business exception
     */
    DefaultResponse cancelRemoteAssist(RemoteAssistRequest request) throws BusinessException;

    /**
     * 查询远程协助信息
     *
     * @param request request
     * @return 远程协助状态信息
     * @throws BusinessException business exception
     */
    CloudDesktopRemoteAssistDTO queryRemoteAssistInfo(RemoteAssistRequest request) throws BusinessException;

    /**
     * 创建vnc通道结果通知
     *
     * @param request 请求参数
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    DefaultResponse createVncChannelResult(RemoteAssistRequest request) throws BusinessException;
}
