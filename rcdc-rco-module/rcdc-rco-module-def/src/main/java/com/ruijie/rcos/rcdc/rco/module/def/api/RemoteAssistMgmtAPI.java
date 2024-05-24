package com.ruijie.rcos.rcdc.rco.module.def.api;


import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopRemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;


/**
 * Description: 远程协助接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/12
 *
 * @author Jarman
 */
public interface RemoteAssistMgmtAPI {

    /**
     ** 向用户发起远程协助询问
     *
     * @param request 页面请求参数：uuid
     * @throws BusinessException 业务异常
     */
    void applyRemoteAssist(RemoteAssistRequest request) throws BusinessException;

    /**
     ** 取消远程协助
     *
     * @param request request
     * @throws BusinessException business exception
     */
    
    void cancelRemoteAssist(RemoteAssistRequest request) throws BusinessException;

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
     * @throws BusinessException 业务异常
     */
    
    void createVncChannelResult(RemoteAssistRequest request) throws BusinessException;

    /**
     * 云桌面关闭后远程协助的处理
     * @param deskId 云桌面ID
     */
    void deskShutdownHandle(UUID deskId);

}
