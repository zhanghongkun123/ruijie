package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskRemoteAssistResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ComputerRemoteAssistRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ObtainDeskRemoteAssistConnectionInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.AssistantRemoteResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;


/**
 * 云桌面远程协助API接口定义
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月27日
 *
 * @author ketb
 */
public interface ComputerDeskRemoteAssistAPI {

	/**
     * 请求远程协助云桌面
     * 
     * @param request 请求参数
     * @return 协助结果
     * @throws BusinessException 异常
     */
    AssistantRemoteResponse remoteAssistDesk(ComputerRemoteAssistRequest request) throws BusinessException;

    /**
     * 取消远程协助云桌面
     * 
     * @param deskId 请求参数
     * @throws BusinessException 异常
     */
    void cancelRemoteAssistDesk(UUID deskId) throws BusinessException;

    /**
     * 获取云桌面远程协助连接信息
     *
     * @param request 请求参数
     * @return 云桌面远程协助连接信息
     * @throws BusinessException 异常
     */

    CbbDeskRemoteAssistResultDTO obtainDeskRemoteAssistInfo(ObtainDeskRemoteAssistConnectionInfoRequest request) throws BusinessException;

    /**
     * 创建vnc通道结果通知
     *
     * @param deskId 请求参数
     * @throws BusinessException 异常
     */

    void createVncChannelResult(UUID deskId) throws BusinessException;
}
